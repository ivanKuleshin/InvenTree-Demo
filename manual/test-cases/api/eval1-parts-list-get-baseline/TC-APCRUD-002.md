# TC-APCRUD-002 — GET /api/part/{id}/ Returns Single Part Detail

## Metadata
- **Endpoint**: GET /api/part/{id}/
- **Priority**: P1
- **Auth**: Required (Basic or Token)
- **Observed on**: 2026-04-18 against https://demo.inventree.org (API v479, InvenTree v1.4.0 dev)

## Objective
Verify that the single part detail endpoint returns a complete part object including the `notes` field that is absent from the list endpoint.

## Preconditions
- Authenticated as `reader` role
- A part with a known PK exists (e.g., pk=82 "1551ABK")

## Request
```
GET /api/part/82/?format=json
Authorization: Basic cmVhZGVyOnJlYWRvbmx5
```

## Expected Response

**Status**: 200 OK
**Content-Type**: application/json

**Body**: Single Part JSON object (not wrapped in an envelope).

**All fields from the list endpoint are present, PLUS**:
- `notes` — string or null (this field is absent from the list endpoint)

## Assertions
1. Status code is 200.
2. Response body is a JSON object (not a list or envelope).
3. `pk` value matches the requested ID.
4. All baseline fields from TC-APCRUD-001 are present.
5. `notes` field is present (value may be null or a string).
6. `name`, `description`, `category`, `active` values are consistent with list endpoint data for the same part.

## Observed Response (pk=82)
```json
{
  "active": true,
  "assembly": false,
  "barcode_hash": "",
  "category": 15,
  "category_name": "Enclosures",
  "component": true,
  "creation_date": "2021-11-15",
  "creation_user": 1,
  "default_expiry": 0,
  "default_location": null,
  "description": "Small plastic enclosure, black",
  "full_name": "1551ABK",
  "image": "/media/part_images/1551mini-photo.jpg",
  "IPN": "",
  "is_template": false,
  "keywords": "",
  "link": "",
  "locked": false,
  "minimum_stock": 0.0,
  "name": "1551ABK",
  "notes": null,
  "pk": 82,
  "purchaseable": true,
  "revision": "",
  "revision_of": null,
  "revision_count": 0,
  "salable": false,
  "starred": false,
  "thumbnail": "/media/part_images/1551mini-photo.thumbnail.jpg",
  "testable": false,
  "trackable": false,
  "units": "",
  "variant_of": null,
  "virtual": false,
  "pricing_min": null,
  "pricing_max": null,
  "pricing_updated": "2026-04-18 13:04",
  "responsible": null,
  "allocated_to_build_orders": 0.0,
  "allocated_to_sales_orders": 0.0,
  "building": 0.0,
  "scheduled_to_build": 0.0,
  "category_default_location": null,
  "in_stock": 166.0,
  "ordering": 0.0,
  "required_for_build_orders": 0,
  "required_for_sales_orders": 0,
  "stock_item_count": 3,
  "total_in_stock": 166.0,
  "external_stock": 0.0,
  "unallocated_stock": 166.0,
  "variant_stock": 0.0
}
```
