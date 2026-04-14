---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: Parts CRUD API Requirements
fetched: 2026-04-14
---

# Parts API — CRUD Requirements

> **Source**: https://docs.inventree.org/en/stable/api/schema/part/
> API Version: 479

## Table of Contents

- [Authentication](#authentication)
- [Error Response Shapes](#error-response-shapes)
- [Pagination](#pagination)
- [Create Part](#create-part)
- [List Parts](#list-parts)
- [Retrieve Part](#retrieve-part)
- [Update Part (Full)](#update-part-full)
- [Partial Update Part](#partial-update-part)
- [Delete Part](#delete-part)
- [Part Schema — Writable Fields](#part-schema--writable-fields)
- [Part Schema — Read-Only Fields](#part-schema--read-only-fields)

---

## Authentication

All endpoints require authentication. Supported schemes:

| Scheme       | How to use                                       |
| ------------ | ------------------------------------------------ |
| `tokenAuth`  | Header: `Authorization: Token <token>`           |
| `basicAuth`  | HTTP Basic authentication                        |
| `cookieAuth` | Cookie: `sessionid`                              |
| `oauth2`     | Bearer token with appropriate scope (see per-op) |

**Demo credentials** (https://demo.inventree.org):

| Username  | Password  | Access Level                       |
| --------- | --------- | ---------------------------------- |
| allaccess | nolimits  | Full create/edit/view permissions  |
| reader    | readonly  | View-only access                   |
| engineer  | partsonly | Parts management and stock viewing |
| admin     | inventree | Superuser with admin functions     |

---

## Error Response Shapes

The InvenTree API does not define explicit 4xx/5xx schemas in the OpenAPI spec per endpoint. Standard Django REST Framework error shapes apply:

**400 Bad Request** — Validation failure. Body contains a JSON object mapping field names to arrays of error strings:

```json
{
  "name": ["This field is required."],
  "IPN": ["Ensure this field has no more than 100 characters."]
}
```

Non-field errors appear under `"non_field_errors"`:

```json
{
  "non_field_errors": ["Part with this name already exists in this category."]
}
```

**401 Unauthorized** — Missing or invalid credentials:

```json
{ "detail": "Authentication credentials were not provided." }
```

**403 Forbidden** — Authenticated but insufficient permissions:

```json
{ "detail": "You do not have permission to perform this action." }
```

**404 Not Found** — Resource does not exist:

```json
{ "detail": "Not found." }
```

**405 Method Not Allowed**:

```json
{ "detail": "Method \"DELETE\" not allowed." }
```

**500 Internal Server Error** — Unhandled server exception; body varies.

---

## Pagination

List endpoints use limit/offset pagination.

**Request parameters:**

| Parameter | Type    | Required | Description                                             |
| --------- | ------- | -------- | ------------------------------------------------------- |
| `limit`   | integer | **yes**  | Number of results to return per page                    |
| `offset`  | integer | no       | Initial index from which to return results (default: 0) |

**Response shape** (`PaginatedPartList`):

```json
{
  "count": 1234,
  "next": "https://demo.inventree.org/api/part/?limit=25&offset=25",
  "previous": null,
  "results": [
    /* array of Part objects */
  ]
}
```

| Field      | Type          | Required | Description                         |
| ---------- | ------------- | -------- | ----------------------------------- |
| `count`    | integer       | yes      | Total number of matching results    |
| `next`     | string (uri)  | no       | URL of next page; null if last page |
| `previous` | string (uri)  | no       | URL of previous page; null if first |
| `results`  | array of Part | yes      | Page of Part objects                |

---

## Create Part

**`POST /api/part/`**

**Operation ID:** `part_create`

**Required OAuth2 scope:** `r:add:part` or `r:add:build`

### Request Body

Required. Content-Type: `application/json` (also accepts `application/x-www-form-urlencoded`, `multipart/form-data`).

See [Part Schema — Writable Fields](#part-schema--writable-fields) for the full field list.

**Minimum viable payload** (only truly required fields):

```json
{
  "name": "My Part"
}
```

### Responses

| Status | Description      | Body         |
| ------ | ---------------- | ------------ |
| 201    | Created          | Part object  |
| 400    | Validation error | Error object |
| 401    | Unauthorized     | Error object |
| 403    | Forbidden        | Error object |

---

## List Parts

**`GET /api/part/`**

**Operation ID:** `part_list`

**Required OAuth2 scope:** `r:view:part` or `r:view:build`

### Query Parameters

| Name                | Type             | Required | Description                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| ------------------- | ---------------- | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `limit`             | integer          | **yes**  | Number of results to return per page                                                                                                                                                                                                                                                                                                                                                                                                                   |
| `offset`            | integer          | no       | Initial index from which to return results                                                                                                                                                                                                                                                                                                                                                                                                             |
| `search`            | string           | no       | Full-text search term. Searched fields: `IPN`, `category__name`, `description`, `keywords`, `manufacturer_parts__MPN`, `name`, `revision`, `supplier_parts__SKU`, `tags__name`, `tags__slug`                                                                                                                                                                                                                                                           |
| `ordering`          | enum string      | no       | Sort field. Values: `name`, `-name`, `creation_date`, `-creation_date`, `IPN`, `-IPN`, `in_stock`, `-in_stock`, `total_in_stock`, `-total_in_stock`, `unallocated_stock`, `-unallocated_stock`, `category`, `-category`, `default_location`, `-default_location`, `units`, `-units`, `pricing_min`, `-pricing_min`, `pricing_max`, `-pricing_max`, `pricing_updated`, `-pricing_updated`, `revision`, `-revision`, `revision_count`, `-revision_count` |
| `IPN`               | string           | no       | Filter by exact IPN                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| `IPN_regex`         | string           | no       | Filter by regex on IPN                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| `active`            | boolean          | no       | Filter by active status                                                                                                                                                                                                                                                                                                                                                                                                                                |
| `ancestor`          | integer          | no       | Filter by ancestor category ID                                                                                                                                                                                                                                                                                                                                                                                                                         |
| `assembly`          | boolean          | no       | Filter assembly parts                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `bom_valid`         | boolean          | no       | Filter by BOM valid status                                                                                                                                                                                                                                                                                                                                                                                                                             |
| `cascade`           | boolean          | no       | If true, include items in child categories of the given category                                                                                                                                                                                                                                                                                                                                                                                       |
| `category`          | integer          | no       | Filter by numeric category ID or the literal `'null'`                                                                                                                                                                                                                                                                                                                                                                                                  |
| `category_detail`   | boolean          | no       | Include category detail object in response (default: `false`)                                                                                                                                                                                                                                                                                                                                                                                          |
| `component`         | boolean          | no       | Filter component parts                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| `convert_from`      | integer          | no       | Filter by convert_from part ID                                                                                                                                                                                                                                                                                                                                                                                                                         |
| `created_after`     | string (date)    | no       | Filter parts created after this date                                                                                                                                                                                                                                                                                                                                                                                                                   |
| `created_before`    | string (date)    | no       | Filter parts created before this date                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `default_location`  | integer          | no       | Filter by default location ID                                                                                                                                                                                                                                                                                                                                                                                                                          |
| `depleted_stock`    | boolean          | no       | Filter by depleted stock                                                                                                                                                                                                                                                                                                                                                                                                                               |
| `exclude_id`        | array of integer | no       | Exclude parts with these IDs (comma-separated)                                                                                                                                                                                                                                                                                                                                                                                                         |
| `exclude_related`   | number           | no       | Exclude parts related to this part ID                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `exclude_tree`      | integer          | no       | Exclude parts in this tree                                                                                                                                                                                                                                                                                                                                                                                                                             |
| `has_ipn`           | boolean          | no       | Filter parts that have an IPN                                                                                                                                                                                                                                                                                                                                                                                                                          |
| `has_pricing`       | boolean          | no       | Filter parts that have pricing                                                                                                                                                                                                                                                                                                                                                                                                                         |
| `has_revisions`     | boolean          | no       | Filter parts that have revisions                                                                                                                                                                                                                                                                                                                                                                                                                       |
| `has_stock`         | boolean          | no       | Filter parts that have stock                                                                                                                                                                                                                                                                                                                                                                                                                           |
| `has_units`         | boolean          | no       | Filter parts that have units defined                                                                                                                                                                                                                                                                                                                                                                                                                   |
| `in_bom_for`        | integer          | no       | Filter parts that are in the BOM for this part ID                                                                                                                                                                                                                                                                                                                                                                                                      |
| `is_revision`       | boolean          | no       | Filter revision parts                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `is_template`       | boolean          | no       | Filter template parts                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `is_variant`        | boolean          | no       | Filter variant parts                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| `location_detail`   | boolean          | no       | Include location detail in response (default: `false`)                                                                                                                                                                                                                                                                                                                                                                                                 |
| `locked`            | boolean          | no       | Filter locked parts                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| `low_stock`         | boolean          | no       | Filter parts with low stock                                                                                                                                                                                                                                                                                                                                                                                                                            |
| `name_regex`        | string           | no       | Filter by name using regex                                                                                                                                                                                                                                                                                                                                                                                                                             |
| `parameters`        | boolean          | no       | Include part parameters in response (default: `false`)                                                                                                                                                                                                                                                                                                                                                                                                 |
| `path_detail`       | boolean          | no       | Include category path detail in response (default: `false`)                                                                                                                                                                                                                                                                                                                                                                                            |
| `price_breaks`      | boolean          | no       | Include price breaks in response (default: `false`)                                                                                                                                                                                                                                                                                                                                                                                                    |
| `purchaseable`      | boolean          | no       | Filter purchaseable parts                                                                                                                                                                                                                                                                                                                                                                                                                              |
| `related`           | number           | no       | Show parts related to this part ID                                                                                                                                                                                                                                                                                                                                                                                                                     |
| `revision_of`       | integer          | no       | Filter by revision_of part ID                                                                                                                                                                                                                                                                                                                                                                                                                          |
| `salable`           | boolean          | no       | Filter salable parts                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| `starred`           | boolean          | no       | Filter starred parts                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| `stock_to_build`    | boolean          | no       | Filter parts required for build orders                                                                                                                                                                                                                                                                                                                                                                                                                 |
| `tags`              | boolean          | no       | Include tags in response (default: `false`)                                                                                                                                                                                                                                                                                                                                                                                                            |
| `tags_name`         | string           | no       | Filter by tag name                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| `tags_slug`         | string           | no       | Filter by tag slug                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| `testable`          | boolean          | no       | Filter testable parts                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `trackable`         | boolean          | no       | Filter trackable parts                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| `unallocated_stock` | boolean          | no       | Filter parts with unallocated stock                                                                                                                                                                                                                                                                                                                                                                                                                    |
| `variant_of`        | integer          | no       | Filter variants of this part ID                                                                                                                                                                                                                                                                                                                                                                                                                        |
| `virtual`           | boolean          | no       | Filter virtual parts                                                                                                                                                                                                                                                                                                                                                                                                                                   |

### Responses

| Status | Description  | Body              |
| ------ | ------------ | ----------------- |
| 200    | OK           | PaginatedPartList |
| 401    | Unauthorized | Error object      |
| 403    | Forbidden    | Error object      |

---

## Retrieve Part

**`GET /api/part/{id}/`**

**Operation ID:** `part_retrieve`

**Required OAuth2 scope:** `r:view:part` or `r:view:build`

### Path Parameters

| Name | Type    | Required | Description |
| ---- | ------- | -------- | ----------- |
| `id` | integer | **yes**  | Part PK     |

### Query Parameters

| Name              | Type    | Required | Description                                       |
| ----------------- | ------- | -------- | ------------------------------------------------- |
| `category_detail` | boolean | no       | Include category detail object (default: `false`) |
| `location_detail` | boolean | no       | Include stock location detail (default: `false`)  |
| `parameters`      | boolean | no       | Include part parameters (default: `false`)        |
| `path_detail`     | boolean | no       | Include category path detail (default: `false`)   |
| `price_breaks`    | boolean | no       | Include price breaks (default: `false`)           |
| `tags`            | boolean | no       | Include tags (default: `false`)                   |

### Responses

| Status | Description  | Body         |
| ------ | ------------ | ------------ |
| 200    | OK           | Part object  |
| 401    | Unauthorized | Error object |
| 403    | Forbidden    | Error object |
| 404    | Not Found    | Error object |

---

## Update Part (Full)

**`PUT /api/part/{id}/`**

**Operation ID:** `part_update`

**Required OAuth2 scope:** `r:change:part` or `r:change:build`

### Path Parameters

| Name | Type    | Required | Description |
| ---- | ------- | -------- | ----------- |
| `id` | integer | **yes**  | Part PK     |

### Request Body

Required. Same schema as Create (`Part`). All required fields must be present.

See [Part Schema — Writable Fields](#part-schema--writable-fields).

### Responses

| Status | Description      | Body         |
| ------ | ---------------- | ------------ |
| 200    | OK               | Part object  |
| 400    | Validation error | Error object |
| 401    | Unauthorized     | Error object |
| 403    | Forbidden        | Error object |
| 404    | Not Found        | Error object |

---

## Partial Update Part

**`PATCH /api/part/{id}/`**

**Operation ID:** `part_partial_update`

**Required OAuth2 scope:** `r:change:part` or `r:change:build`

### Path Parameters

| Name | Type    | Required | Description |
| ---- | ------- | -------- | ----------- |
| `id` | integer | **yes**  | Part PK     |

### Request Body

Optional. Uses `PatchedPart` schema — identical fields to `Part` but all fields are optional. Send only the fields to be changed.

### Responses

| Status | Description      | Body         |
| ------ | ---------------- | ------------ |
| 200    | OK               | Part object  |
| 400    | Validation error | Error object |
| 401    | Unauthorized     | Error object |
| 403    | Forbidden        | Error object |
| 404    | Not Found        | Error object |

---

## Delete Part

**`DELETE /api/part/{id}/`**

**Operation ID:** `part_destroy`

**Required OAuth2 scope:** `r:delete:part` or `r:delete:build`

### Path Parameters

| Name | Type    | Required | Description |
| ---- | ------- | -------- | ----------- |
| `id` | integer | **yes**  | Part PK     |

### Responses

| Status | Description  | Body         |
| ------ | ------------ | ------------ |
| 204    | No Content   | (empty)      |
| 401    | Unauthorized | Error object |
| 403    | Forbidden    | Error object |
| 404    | Not Found    | Error object |

---

## Part Schema — Writable Fields

Fields that can be sent in POST (create) and PUT/PATCH (update) request bodies.

| Field                      | Type            | Required | Nullable | Constraints                         | Description                                          |
| -------------------------- | --------------- | -------- | -------- | ----------------------------------- | ---------------------------------------------------- |
| `name`                     | string          | **yes**  | no       | max length: 100                     | Part name                                            |
| `description`              | string          | no       | no       | max length: 250                     | Part description (optional)                          |
| `IPN`                      | string          | no       | no       | max length: 100; default: `""`      | Internal part number                                 |
| `revision`                 | string          | no       | yes      | max length: 100; default: `""`      | Revision identifier                                  |
| `revision_of`              | integer         | no       | yes      |                                     | PK of the part this is a revision of                 |
| `category`                 | integer         | no       | yes      |                                     | PK of the part category                              |
| `default_location`         | integer         | no       | yes      |                                     | PK of the default stock location                     |
| `default_expiry`           | integer         | no       | no       | min: 0; max: 2147483647             | Expiry time in days for stock items                  |
| `minimum_stock`            | number (double) | no       | no       | default: `0.0`                      | Minimum stock level                                  |
| `units`                    | string          | no       | yes      | max length: 20                      | Units of measure                                     |
| `keywords`                 | string          | no       | yes      | max length: 250                     | Keywords for search visibility                       |
| `link`                     | string (uri)    | no       | yes      | max length: 2000                    | Link to external URL                                 |
| `image`                    | string (uri)    | no       | yes      |                                     | Part image URL                                       |
| `remote_image`             | string (uri)    | no       | —        | write-only                          | URL of remote image to import                        |
| `existing_image`           | string          | no       | —        | write-only                          | Filename of an existing part image                   |
| `active`                   | boolean         | no       | no       |                                     | Is this part active?                                 |
| `assembly`                 | boolean         | no       | no       |                                     | Can this part be built from other parts?             |
| `component`                | boolean         | no       | no       |                                     | Can this part be used to build other parts?          |
| `is_template`              | boolean         | no       | no       |                                     | Is this part a template part?                        |
| `locked`                   | boolean         | no       | no       |                                     | Locked parts cannot be edited                        |
| `purchaseable`             | boolean         | no       | no       |                                     | Can this part be purchased from external suppliers?  |
| `salable`                  | boolean         | no       | no       |                                     | Can this part be sold to customers?                  |
| `testable`                 | boolean         | no       | no       |                                     | Can this part have test results recorded against it? |
| `trackable`                | boolean         | no       | no       |                                     | Does this part have tracking for unique items?       |
| `virtual`                  | boolean         | no       | no       |                                     | Is this a virtual part (e.g. software/license)?      |
| `variant_of`               | integer         | no       | yes      |                                     | PK of the part this is a variant of                  |
| `responsible`              | integer         | no       | yes      |                                     | PK of the responsible user/group                     |
| `creation_user`            | integer         | no       | yes      |                                     | PK of the creating user                              |
| `duplicate`                | object          | no       | —        | write-only; `$ref: DuplicatePart`   | Copy initial data from another Part                  |
| `initial_stock`            | object          | no       | —        | write-only; `$ref: InitialStock`    | Create Part with initial stock quantity              |
| `initial_supplier`         | object          | no       | —        | write-only; `$ref: InitialSupplier` | Add initial supplier information                     |
| `copy_category_parameters` | boolean         | no       | —        | write-only; default: `true`         | Copy parameter templates from selected category      |

---

## Part Schema — Read-Only Fields

These fields are returned in responses but ignored in request bodies.

| Field                       | Type               | Nullable | Description                                      |
| --------------------------- | ------------------ | -------- | ------------------------------------------------ |
| `pk`                        | integer            | no       | Primary key                                      |
| `full_name`                 | string             | no       | Formatted full name per PART_NAME_FORMAT setting |
| `barcode_hash`              | string             | no       | Unique hash of barcode data                      |
| `category_name`             | string             | no       | Name of the associated category                  |
| `starred`                   | boolean            | no       | True if starred by the current user              |
| `thumbnail`                 | string             | no       | Thumbnail image URL                              |
| `creation_date`             | string (date)      | yes      | Date part was created                            |
| `revision_count`            | integer            | yes      | Number of revisions                              |
| `pricing_min`               | string (decimal)   | yes      | Minimum computed price                           |
| `pricing_max`               | string (decimal)   | yes      | Maximum computed price                           |
| `pricing_updated`           | string (date-time) | yes      | Timestamp of last pricing update                 |
| `allocated_to_build_orders` | number (double)    | yes      | Quantity allocated to build orders               |
| `allocated_to_sales_orders` | number (double)    | yes      | Quantity allocated to sales orders               |
| `building`                  | number (double)    | yes      | Quantity currently in production                 |
| `scheduled_to_build`        | number (double)    | yes      | Outstanding quantity scheduled to be built       |
| `category_default_location` | integer            | yes      | Default location inherited from category         |
| `in_stock`                  | number (double)    | yes      | Total in-stock quantity                          |
| `ordering`                  | number (double)    | yes      | Quantity on order                                |
| `required_for_build_orders` | integer            | yes      | Quantity required for build orders               |
| `required_for_sales_orders` | integer            | yes      | Quantity required for sales orders               |
| `stock_item_count`          | integer            | yes      | Number of distinct stock items                   |
| `total_in_stock`            | number (double)    | yes      | Total stock including variant stock              |
| `external_stock`            | number (double)    | yes      | Quantity held externally                         |
| `unallocated_stock`         | number (double)    | yes      | Unallocated stock quantity                       |
| `variant_stock`             | number (double)    | yes      | Stock quantity across variants                   |

**Additional read-only fields available via PATCH response / expanded queries:**

| Field                     | Type                     | Available When           |
| ------------------------- | ------------------------ | ------------------------ |
| `default_location_detail` | object (DefaultLocation) | Always in PATCH response |
| `parameters`              | array of Parameter       | `?parameters=true`       |
| `category_path`           | array of TreePath        | `?path_detail=true`      |
| `tags`                    | array of string          | `?tags=true`             |
| `price_breaks`            | array of PartSalePrice   | `?price_breaks=true`     |
| `category_detail`         | Category object          | `?category_detail=true`  |
