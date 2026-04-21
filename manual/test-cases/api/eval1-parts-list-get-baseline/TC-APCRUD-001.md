# TC-APCRUD-001 — GET /api/part/ Returns Paginated Part List

## Metadata
- **Endpoint**: GET /api/part/
- **Priority**: P1
- **Auth**: Required (Basic or Token)
- **Observed on**: 2026-04-18 against https://demo.inventree.org (API v479, InvenTree v1.4.0 dev)

## Objective
Verify that the parts list endpoint returns a valid paginated response with the expected envelope structure and a baseline set of fields per part object.

## Preconditions
- Authenticated as `reader` role (username: `reader`, password: `readonly`)
- At least one part exists in the system

## Request
```
GET /api/part/?limit=3&format=json
Authorization: Basic cmVhZGVyOnJlYWRvbmx5
```

## Expected Response

**Status**: 200 OK
**Content-Type**: application/json

**Body structure**:
```json
{
  "count": <integer>,
  "next": "<url or null>",
  "previous": "<url or null>",
  "results": [ <Part objects> ]
}
```

**Observed count**: 435

**Each Part object in `results` must contain these fields** (baseline without expansion flags):
- `pk` — integer, primary key
- `name` — string
- `full_name` — string
- `description` — string
- `IPN` — string (may be empty)
- `revision` — string (may be empty)
- `revision_of` — integer or null
- `revision_count` — integer
- `category` — integer (category PK) or null
- `category_name` — string or null
- `is_template` — boolean
- `variant_of` — integer or null
- `keywords` — string
- `units` — string (may be empty)
- `link` — string (may be empty)
- `active` — boolean
- `locked` — boolean
- `virtual` — boolean
- `assembly` — boolean
- `component` — boolean
- `trackable` — boolean
- `purchaseable` — boolean
- `salable` — boolean
- `testable` — boolean
- `starred` — boolean
- `image` — string (relative URL) or null
- `thumbnail` — string (relative URL) or null
- `creation_date` — string, format YYYY-MM-DD
- `creation_user` — integer or null
- `default_expiry` — integer
- `default_location` — integer or null
- `category_default_location` — integer or null
- `minimum_stock` — number
- `responsible` — integer or null
- `barcode_hash` — string (may be empty)
- `pricing_min` — string or null
- `pricing_max` — string or null
- `pricing_updated` — string or null
- `in_stock` — number
- `total_in_stock` — number
- `unallocated_stock` — number
- `external_stock` — number
- `variant_stock` — number
- `stock_item_count` — integer
- `ordering` — number
- `building` — number
- `scheduled_to_build` — number
- `allocated_to_build_orders` — number
- `allocated_to_sales_orders` — number
- `required_for_build_orders` — integer
- `required_for_sales_orders` — integer

**Field NOT present in list response** (only in detail endpoint):
- `notes`

## Assertions
1. Status code is 200.
2. Response `Content-Type` header includes `application/json`.
3. Response body is a JSON object (not array) with keys `count`, `next`, `previous`, `results`.
4. `count` is an integer greater than 0.
5. `results` is a non-empty array when `count > 0`.
6. `results` length equals `limit` parameter (or `count` if fewer items remain).
7. Each result object contains all baseline fields listed above.
8. `notes` field is absent from each list result object.
9. `next` is a non-null URL when there are more pages; `previous` is null on the first page.

## Observed Sample (pk=82)
```json
{
  "pk": 82,
  "name": "1551ABK",
  "full_name": "1551ABK",
  "description": "Small plastic enclosure, black",
  "IPN": "",
  "category": 15,
  "category_name": "Enclosures",
  "active": true,
  "assembly": false,
  "component": true,
  "purchaseable": true,
  "salable": false,
  "trackable": false,
  "virtual": false,
  "in_stock": 166.0,
  "stock_item_count": 3
}
```
