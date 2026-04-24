---
source: https://demo.inventree.org/api/schema/
component: api
topic: GET /api/part/ — Filtering, Pagination, and Search
fetched: 2026-04-14
---

# `GET /api/part/` — Filtering, Pagination, and Search

> **Source**: OpenAPI 3.0.3 schema at `https://demo.inventree.org/api/schema/` (API version 479) and live API
> observations against `https://demo.inventree.org` (demo environment, 2026-04-14).

## Table of Contents

- [Endpoint Overview](#endpoint-overview)
- [Authentication](#authentication)
- [Pagination Parameters](#pagination-parameters)
- [Pagination Behavior — Observed](#pagination-behavior--observed)
- [Search Parameter](#search-parameter)
- [Ordering Parameter](#ordering-parameter)
- [Filter Parameters — Boolean Flags](#filter-parameters--boolean-flags)
- [Filter Parameters — Identifiers and Text](#filter-parameters--identifiers-and-text)
- [Filter Parameters — Dates](#filter-parameters--dates)
- [Filter Parameters — Relational / Detail Expansion](#filter-parameters--relational--detail-expansion)
- [Response Schema](#response-schema)
- [Response Fields (Per Part Object)](#response-fields-per-part-object)
- [Error Responses](#error-responses)
- [Observed Edge Case Behaviors](#observed-edge-case-behaviors)
- [Combined Filter Examples](#combined-filter-examples)

---

## Endpoint Overview

- **Method:** `GET`
- **Path:** `/api/part/`
- **Operation ID:** `part_list`
- **Tags:** `part`
- **Description:** Returns a list of Part objects. Supports filtering, searching, ordering, and pagination. When the
  `limit` query parameter is omitted or set to `0`, the API bypasses pagination and returns a raw JSON array instead of
  the standard paginated envelope object.

---

## Authentication

Required. Any of the following schemes is accepted:

| Scheme       | Mechanism                                    |
| ------------ | -------------------------------------------- |
| `tokenAuth`  | Header: `Authorization: Token <token>`       |
| `basicAuth`  | HTTP Basic (username:password, base64)       |
| `cookieAuth` | Cookie: `sessionid`                          |
| `oauth2`     | OAuth2 scopes: `r:view:part`, `r:view:build` |

**Demo environment credentials (token flow):**

```
POST https://demo.inventree.org/api/user/token/
Authorization: Basic allaccess:nolimits
→ { "token": "inv-<hash>-<date>", "expiry": "<date>" }
```

Then use: `Authorization: Token inv-<hash>-<date>` on subsequent requests.

**401 Unauthenticated response:**

```json
{ "detail": "Authentication credentials were not provided." }
```

---

## Pagination Parameters

| Parameter | In    | Type    | Required | Description                                                                                                     |
| --------- | ----- | ------- | -------- | --------------------------------------------------------------------------------------------------------------- |
| `limit`   | query | integer | no       | Number of results to return per page. When omitted or `0`, returns an unpaginated raw JSON array (not envelope). |
| `offset`  | query | integer | no       | Zero-based index of the first result to return. Defaults to `0`.                                                |

---

## Pagination Behavior — Observed

| Scenario                                 | Observed Behavior                                                                                                        |
| ---------------------------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| `limit=N` (positive integer)             | Returns paginated envelope: `{ count, next, previous, results[] }`. `next`/`previous` are absolute URLs or `null`.      |
| `limit` omitted                          | Returns raw JSON array (unpaginated). No `count`, `next`, or `previous` fields. All matching records returned.           |
| `limit=0`                                | Returns raw JSON array (same as omitted). Bypasses pagination entirely.                                                  |
| `limit=-1` (negative integer)            | Returns raw JSON array (treated same as `0` / omitted).                                                                  |
| `limit=5000` (exceeds total count)       | Returns paginated envelope with all matching results. `next` is `null`. `results` length equals `count`.                 |
| `offset=0`                               | Returns first page. `previous` is `null`.                                                                                |
| `offset` beyond total count (e.g. 99999) | Returns paginated envelope: `count` reflects actual total, `results` is empty `[]`, `next` is `null`.                   |
| `offset=-1` (negative integer)           | Server treats as `offset=0` — returns first page without error.                                                          |

**Paginated envelope structure (when `limit` > 0):**

```json
{
  "count": 1429,
  "next": "https://demo.inventree.org/api/part/?limit=2&offset=2",
  "previous": null,
  "results": [ /* array of Part objects */ ]
}
```

**Unpaginated response (when `limit` omitted or `0`):**

```json
[ /* raw array of all matching Part objects */ ]
```

---

## Search Parameter

| Parameter | In    | Type   | Required | Description                          |
| --------- | ----- | ------ | -------- | ------------------------------------ |
| `search`  | query | string | no       | A free-text search term. Case-insensitive substring/prefix match applied across multiple fields. |

**Searched fields (from OpenAPI schema):**

- `IPN` (Internal Part Number)
- `category__name`
- `description`
- `keywords`
- `manufacturer_parts__MPN`
- `name`
- `revision`
- `supplier_parts__SKU`
- `tags__name`
- `tags__slug`

**Observed behavior:**

- `search=resistor` against ~1,429 parts returned `count: 69` — matches parts whose `name`, `description`, or
  `keywords` contain "resistor".
- `search=xyznonexistent12345` returned `count: 0`, `results: []`.
- The search is a broad substring match spanning all listed fields simultaneously; it is not a per-field exact match.
- Search can be combined with any filter parameter. Filters narrow the dataset first; search applies within that subset.

**Example:**

```
GET /api/part/?search=resistor&limit=25&offset=0
Authorization: Token <token>
```

---

## Ordering Parameter

| Parameter  | In    | Type | Required | Description                                                |
| ---------- | ----- | ---- | -------- | ---------------------------------------------------------- |
| `ordering` | query | enum | no       | Field name (prefix with `-` for descending). Default order is unspecified (database order). |

**Allowed values:**

| Value               | Direction  | Field                         |
| ------------------- | ---------- | ----------------------------- |
| `name`              | ascending  | Part name                     |
| `-name`             | descending | Part name                     |
| `creation_date`     | ascending  | Date part was created         |
| `-creation_date`    | descending | Date part was created         |
| `IPN`               | ascending  | Internal Part Number          |
| `-IPN`              | descending | Internal Part Number          |
| `in_stock`          | ascending  | Quantity currently in stock   |
| `-in_stock`         | descending | Quantity currently in stock   |
| `total_in_stock`    | ascending  | Total stock including variants|
| `-total_in_stock`   | descending | Total stock including variants|
| `unallocated_stock` | ascending  | Stock not allocated           |
| `-unallocated_stock`| descending | Stock not allocated           |
| `category`          | ascending  | Category ID                   |
| `-category`         | descending | Category ID                   |
| `default_location`  | ascending  | Default location ID           |
| `-default_location` | descending | Default location ID           |
| `units`             | ascending  | Unit of measure               |
| `-units`            | descending | Unit of measure               |
| `pricing_min`       | ascending  | Minimum pricing               |
| `-pricing_min`      | descending | Minimum pricing               |
| `pricing_max`       | ascending  | Maximum pricing               |
| `-pricing_max`      | descending | Maximum pricing               |
| `pricing_updated`   | ascending  | Date pricing was last updated |
| `-pricing_updated`  | descending | Date pricing was last updated |
| `revision`          | ascending  | Revision string               |
| `-revision`         | descending | Revision string               |
| `revision_count`    | ascending  | Number of revisions           |
| `-revision_count`   | descending | Number of revisions           |

**Observed behavior:**

- `ordering=name` returns parts alphabetically (e.g., `1551ABK`, `1551ACLR`, `1551AGY`, ...).
- `ordering=-name` returns parts reverse-alphabetically (e.g., `Zero Qty Comp`, ...).
- `ordering=-in_stock` returns highest-stock parts first (e.g., 19625.0, 18090.0, 15803.0).
- Supplying an unrecognized ordering value (e.g., `ordering=invalid_field`) is silently ignored — the API returns HTTP
  200 with results in default order.

---

## Filter Parameters — Boolean Flags

All boolean parameters accept `true` or `false` (case-insensitive). Multiple boolean filters combine with logical AND.

| Parameter        | Description                                                                              | Observed count (demo, 2026-04-14) |
| ---------------- | ---------------------------------------------------------------------------------------- | --------------------------------- |
| `active`         | Filter by active status. `active=true` → published/active parts. `active=false` → inactive. | true: 1400 / false: 29           |
| `assembly`       | Part is an assembly (has a BOM).                                                         | true: 135                         |
| `component`      | Part can be used as a component in a BOM.                                                | —                                 |
| `virtual`        | Part is virtual (no physical stock).                                                     | true: 32                          |
| `trackable`      | Part uses serial number tracking.                                                        | true: 56                          |
| `purchasable`   | Part can be purchased from a supplier.                                                   | true: 1358                        |
| `salable`        | Part can be sold to a customer.                                                          | true: 72                          |
| `is_template`    | Part is a template (parent of variants).                                                 | true: 71                          |
| `is_variant`     | Part is a variant of a template.                                                         | —                                 |
| `is_revision`    | Part is a revision of another part.                                                      | —                                 |
| `locked`         | Part record is locked from edits.                                                        | —                                 |
| `testable`       | Part has test templates associated.                                                      | —                                 |
| `bom_valid`      | Part's BOM has been validated.                                                           | —                                 |
| `starred`        | Part is starred/favourited by the requesting user.                                       | —                                 |
| `has_stock`      | Part has at least one stock item.                                                        | true: 404 / false: 1024           |
| `has_units`      | Part has a unit of measure defined.                                                      | —                                 |
| `has_ipn`        | Part has an IPN set (non-empty).                                                         | —                                 |
| `has_pricing`    | Part has pricing data.                                                                   | true: 361                         |
| `has_revisions`  | Part has one or more revisions.                                                          | —                                 |
| `low_stock`      | Stock quantity is below the minimum stock threshold.                                     | —                                 |
| `depleted_stock` | Stock quantity is zero or negative.                                                      | —                                 |
| `unallocated_stock` | Part has unallocated stock available.                                                 | —                                 |
| `stock_to_build` | Part is required for one or more active build orders.                                    | —                                 |
| `cascade`        | When used with `category`, also include parts in child categories. `cascade=true`.       | —                                 |

---

## Filter Parameters — Identifiers and Text

| Parameter       | Type             | Description                                                                                                 |
| --------------- | ---------------- | ----------------------------------------------------------------------------------------------------------- |
| `IPN`           | string           | Exact match on the Internal Part Number (IPN) field.                                                        |
| `IPN_regex`     | string           | Regex match on IPN. Example: `IPN_regex=^RES` matches all IPNs starting with "RES".                        |
| `name_regex`    | string           | Regex match on the `name` field. Example: `name_regex=^R` returns parts whose name starts with "R".        |
| `category`      | integer or `null`| Filter by category primary key. Use `category=null` to return parts with no category assigned.              |
| `ancestor`      | integer          | Filter to parts that are descendants of the given category ID (by ancestry, not direct parent).             |
| `variant_of`    | integer          | Return variants of the given template part ID.                                                              |
| `revision_of`   | integer          | Return revisions of the given part ID.                                                                      |
| `in_bom_for`    | integer          | Return parts that appear in the BOM of the given part ID.                                                   |
| `convert_from`  | integer          | Return parts that can be substituted for the given part ID.                                                 |
| `related`       | number           | Return parts that have a "related parts" relationship with the given part ID.                               |
| `exclude_related`| number          | Exclude parts related to the given part ID.                                                                 |
| `exclude_tree`  | integer          | Exclude parts that are in the same variant/template tree as the given part ID.                              |
| `exclude_id`    | array of integer | Exclude parts with the specified PKs. Pass as comma-separated values: `exclude_id=1,2,3`.                   |
| `default_location`| integer        | Filter by the default stock location ID.                                                                    |
| `tags_name`     | string           | Filter by exact tag name.                                                                                   |
| `tags_slug`     | string           | Filter by exact tag slug.                                                                                   |

**Observed IPN behavior:**

- `IPN=RES-001` → `count: 1` (exact match, case-sensitive).
- `IPN_regex=^RES` → regex filter works correctly and returns all parts whose IPN matches the pattern. The specific count depends on the dataset; the demo instance had 7 such parts at the time of original observation, but this may change. Always use a dynamically known IPN prefix when writing tests against this filter.

> Updated 2026-04-23: Removed the hardcoded count claim of "7 parts" for `IPN_regex=^RES`. The filter functions correctly but requires parts with matching IPNs to exist in the dataset. Tests should use a dynamically created part with a known IPN and build the regex from that known value rather than relying on pre-existing demo data.

**Observed category behavior:**

- `category=24` → `count: 2` (only parts directly in category 24).
- `category=24&cascade=true` → `count: 2` (same as without cascade when no child categories exist; increases if
  category has children).
- `category=null` → on this demo instance, passing the literal string `'null'` as the category value does NOT filter to uncategorized parts. The parameter appears to be ignored and the full unfiltered part list is returned (count matches total parts, e.g. ~467). The literal string `'null'` is not recognized as a null sentinel by this InvenTree version.

> Updated 2026-04-23: Corrected `category=null` observed behavior. Previously documented as returning ~1427 uncategorized parts. Actual behavior on this demo instance: the `null` string is not treated as a filter sentinel — the request returns all parts unfiltered. Tests should not assert that results are restricted to uncategorized parts when using `category=null`.

**Observed name_regex behavior:**

- `name_regex=^R` → `count: 93` (parts whose name starts with "R").

---

## Filter Parameters — Dates

| Parameter        | Type        | Description                                                    |
| ---------------- | ----------- | -------------------------------------------------------------- |
| `created_after`  | string (date, format `YYYY-MM-DD`) | Return parts created on or after this date.  |
| `created_before` | string (date, format `YYYY-MM-DD`) | Return parts created on or before this date. |

> **Note:** The OpenAPI schema labels both as "Updated after" / "Updated before" in the description text, but the
> parameter names are `created_after` / `created_before` — they filter on `creation_date`.

---

## Filter Parameters — Relational / Detail Expansion

These parameters do not filter the result set; they expand the data returned per part object.

| Parameter         | Type    | Default | Description                                                                                              |
| ----------------- | ------- | ------- | -------------------------------------------------------------------------------------------------------- |
| `category_detail` | boolean | `false` | Include full category object in each part's `category` field instead of just the ID.                    |
| `location_detail` | boolean | `false` | Include full stock location object in `default_location` field instead of just the ID.                  |
| `path_detail`     | boolean | `false` | Include full category path hierarchy.                                                                    |
| `parameters`      | boolean | `false` | Include part parameter values in each part object.                                                       |
| `tags`            | boolean | `false` | Include tag objects in each part object.                                                                 |
| `price_breaks`    | boolean | `false` | Include pricing break data in each part object.                                                          |

---

## Response Schema

### Paginated Response (when `limit` > 0)

```json
{
  "count": 1429,
  "next": "https://demo.inventree.org/api/part/?limit=2&offset=2",
  "previous": null,
  "results": [
    { /* Part object */ },
    { /* Part object */ }
  ]
}
```

| Field      | Type              | Nullable | Description                          |
| ---------- | ----------------- | -------- | ------------------------------------ |
| `count`    | integer           | no       | Total number of matching records     |
| `next`     | string (URI)      | yes      | URL to fetch the next page           |
| `previous` | string (URI)      | yes      | URL to fetch the previous page       |
| `results`  | array of Part     | no       | Part objects for the current page    |

### Unpaginated Response (when `limit` omitted or `0`)

```json
[
  { /* Part object */ },
  { /* Part object */ }
]
```

Raw JSON array of Part objects. No envelope. No `count`, `next`, or `previous`.

---

## Response Fields (Per Part Object)

Fields observed on each Part object in the default (no expansion) response:

| Field                        | Type             | Nullable | Notes                                                                 |
| ---------------------------- | ---------------- | -------- | --------------------------------------------------------------------- |
| `pk`                         | integer          | no       | Primary key / unique ID                                               |
| `name`                       | string           | no       | Part name                                                             |
| `full_name`                  | string           | no       | Fully qualified name (includes revision if present)                   |
| `IPN`                        | string           | no       | Internal Part Number (empty string if not set)                        |
| `description`                | string           | no       | Part description                                                      |
| `keywords`                   | string           | no       | Search keywords                                                       |
| `revision`                   | string           | no       | Revision string (empty string if not set)                             |
| `revision_of`                | integer          | yes      | PK of the parent part this is a revision of                           |
| `revision_count`             | integer          | no       | Number of revisions                                                   |
| `category`                   | integer          | yes      | Category PK (null if uncategorised)                                   |
| `variant_of`                 | integer          | yes      | PK of the template part                                               |
| `active`                     | boolean          | no       |                                                                       |
| `assembly`                   | boolean          | no       |                                                                       |
| `component`                  | boolean          | no       |                                                                       |
| `is_template`                | boolean          | no       |                                                                       |
| `locked`                     | boolean          | no       |                                                                       |
| `purchasable`               | boolean          | no       |                                                                       |
| `salable`                    | boolean          | no       |                                                                       |
| `starred`                    | boolean          | no       | Whether the authenticated user has starred this part                  |
| `testable`                   | boolean          | no       |                                                                       |
| `trackable`                  | boolean          | no       |                                                                       |
| `virtual`                    | boolean          | no       |                                                                       |
| `units`                      | string           | no       | Unit of measure (empty string if not set)                             |
| `link`                       | string           | no       | External URL (empty string if not set)                                |
| `default_expiry`             | integer          | no       | Default expiry in days (0 = no expiry)                                |
| `default_location`           | integer          | yes      | Default stock location PK                                             |
| `minimum_stock`              | number (decimal) | no       | Minimum stock level                                                   |
| `creation_date`              | string (date)    | no       | ISO 8601 date, e.g. `"2021-11-15"`                                    |
| `creation_user`              | integer          | yes      | PK of the user who created this part                                  |
| `responsible`                | integer          | yes      | PK of the user/group responsible                                      |
| `image`                      | string           | yes      | URL path to part image                                                |
| `thumbnail`                  | string           | yes      | URL path to thumbnail                                                 |
| `barcode_hash`               | string           | no       | Hash of attached barcode (empty string if none)                       |
| `pricing_min`                | string (decimal) | yes      | Minimum price (null if no pricing)                                    |
| `pricing_max`                | string (decimal) | yes      | Maximum price (null if no pricing)                                    |
| `pricing_updated`            | string (datetime)| yes      | Last pricing update timestamp, e.g. `"2026-04-14 09:57"`             |
| `in_stock`                   | number           | no       | Current stock quantity (direct stock items only)                      |
| `total_in_stock`             | number           | no       | Total stock including variant stock                                   |
| `external_stock`             | number           | no       | External / consigned stock quantity                                   |
| `unallocated_stock`          | number           | no       | Stock not allocated to builds or sales orders                         |
| `variant_stock`              | number           | no       | Stock held under variant parts                                        |
| `stock_item_count`           | integer          | no       | Number of distinct stock items                                        |
| `ordering`                   | number           | no       | Quantity currently on order from suppliers                            |
| `building`                   | number           | no       | Quantity currently being built                                        |
| `scheduled_to_build`         | number           | no       | Quantity scheduled in future build orders                             |
| `allocated_to_build_orders`  | number           | no       | Quantity allocated to active build orders                             |
| `allocated_to_sales_orders`  | number           | no       | Quantity allocated to active sales orders                             |
| `required_for_build_orders`  | integer          | no       | Quantity required for active build orders                             |
| `required_for_sales_orders`  | integer          | no       | Quantity required for active sales orders                             |
| `category_default_location`  | integer          | yes      | Default location inherited from the part's category                   |

---

## Error Responses

| HTTP Status | Scenario                                              | Response Body                                                          |
| ----------- | ----------------------------------------------------- | ---------------------------------------------------------------------- |
| 401         | No authentication credentials provided               | `{"detail": "Authentication credentials were not provided."}`          |
| 401         | Invalid or expired token                             | `{"detail": "Invalid token."}`                                         |
| 200         | Invalid `ordering` value                             | Request succeeds; ordering is silently ignored, default order applied  |
| 200         | `offset` beyond total count                          | Envelope returned; `results` is `[]`; `count` reflects actual total   |
| 200         | `offset=-1` (negative)                               | Treated as `offset=0`; first page returned without error               |
| 200         | `limit=-1` (negative)                                | Treated as `limit=0`; unpaginated array returned without error         |

---

## Observed Edge Case Behaviors

| Case                                    | Behavior                                                                                                           |
| --------------------------------------- | ------------------------------------------------------------------------------------------------------------------ |
| `limit` omitted                         | Returns raw JSON array (no pagination envelope). Full unfiltered dataset returned.                                 |
| `limit=0`                               | Same as omitted — returns raw JSON array.                                                                          |
| `limit=-1`                              | Same as `limit=0` — returns raw JSON array.                                                                        |
| `offset=-1`                             | Treated as `offset=0`; returns first page without HTTP error.                                                      |
| `offset=99999` (beyond count)           | Returns envelope with `count` = actual total, empty `results`, `next=null`, `previous` pointing to near-last page. |
| `ordering=invalid_field`                | Silent no-op; HTTP 200 returned with results in default database order.                                            |
| Multiple boolean filters                | Combined with AND logic: `assembly=true&active=true` → 133 parts (vs. assembly alone: 135).                        |
| `category=null`                         | On this demo instance, the literal string `'null'` is not recognized as a null sentinel. The filter is ignored and all parts are returned unfiltered. Do not assert uncategorized-only results when using this parameter value. |
| `search` with no matches                | HTTP 200, `count: 0`, `results: []`.                                                                               |
| `IPN` exact match                       | Returns exactly 1 part when IPN is unique. Case-sensitive.                                                         |

---

## Combined Filter Examples

```
# Active assemblies only
GET /api/part/?assembly=true&active=true&limit=25

# Parts in a specific category including subcategories
GET /api/part/?category=7&cascade=true&limit=25

# Search for resistors with IPN, sorted by name
GET /api/part/?search=resistor&ordering=name&limit=25&offset=0

# Inactive parts with no stock
GET /api/part/?active=false&has_stock=false&limit=50

# Parts matching an IPN prefix regex
GET /api/part/?IPN_regex=^RES&limit=25

# Parts created in a date range, paginated
GET /api/part/?created_after=2024-01-01&created_before=2024-12-31&limit=25&offset=0

# Trackable salable parts with pricing, ordered by price descending
GET /api/part/?trackable=true&salable=true&has_pricing=true&ordering=-pricing_min&limit=25

# Uncategorised virtual parts (NOTE: category=null is not reliably filtered on this demo instance — the literal
# string 'null' is not recognized as a null sentinel; the parameter is ignored and all parts are returned)
GET /api/part/?category=null&virtual=true&limit=25

# Parts 26-50 sorted by creation date descending
GET /api/part/?ordering=-creation_date&limit=25&offset=25
```
