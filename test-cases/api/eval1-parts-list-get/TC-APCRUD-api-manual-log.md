# API Manual Probe Log — Parts List & Detail

**Suite**: eval1-parts-list-get
**Date**: 2026-04-18
**Base URL**: https://demo.inventree.org
**Auth**: HTTP Basic (admin credentials)

---

## Probe 1 — GET /api/part/?limit=5 (baseline list)

**Request**: `GET /api/part/?limit=5`
**Status**: 200 OK
**Response shape**:
- Top-level keys: `count`, `next`, `previous`, `results`
- `count`: 435 (total parts)
- `next`: URL with offset
- `previous`: null on first page
- `results`: array of part objects

**Each part object fields observed**: `active`, `assembly`, `barcode_hash`, `category` (integer ID), `category_name` (string), `component`, `creation_date`, `creation_user`, `default_expiry`, `default_location`, `description`, `full_name`, `image`, `IPN`, `is_template`, `keywords`, `link`, `locked`, `minimum_stock`, `name`, `pk`, `purchaseable`, `revision`, `revision_of`, `revision_count`, `salable`, `starred`, `thumbnail`, `testable`, `trackable`, `units`, `variant_of`, `virtual`, `pricing_min`, `pricing_max`, `pricing_updated`, `responsible`, `allocated_to_build_orders`, `allocated_to_sales_orders`, `building`, `scheduled_to_build`, `category_default_location`, `in_stock`, `ordering`, `required_for_build_orders`, `required_for_sales_orders`, `stock_item_count`, `total_in_stock`, `external_stock`, `unallocated_stock`, `variant_stock`

**Notable**: `parameters`, `category_detail`, `tags`, `price_breaks`, `category_path` fields are NOT present without flags.

---

## Probe 2 — GET /api/part/?limit=3&parameters=true

**Request**: `GET /api/part/?limit=3&parameters=true`
**Status**: 200 OK
**Effect**: Each part object gains a `parameters` array field.

**parameters array item shape**:
```json
{
  "pk": 14495,
  "template": 235,
  "model_type": "part.part",
  "model_id": 82,
  "data": "35",
  "data_numeric": 35.0,
  "note": "",
  "updated": null,
  "updated_by": null,
  "template_detail": {
    "pk": 235,
    "name": "Length",
    "units": "mm",
    "description": "Part length",
    "model_type": null,
    "checkbox": false,
    "choices": "",
    "selectionlist": null,
    "enabled": true
  },
  "updated_by_detail": null
}
```
Part pk=82 (1551ABK) has 4 parameters: Length, Width, Height, Color.

---

## Probe 3 — GET /api/part/?limit=3&category_detail=true

**Request**: `GET /api/part/?limit=3&category_detail=true`
**Status**: 200 OK
**Effect**: Each part object gains a `category_detail` object field.

**category_detail shape**:
```json
{
  "pk": 15,
  "name": "Enclosures",
  "description": "Enclosures, boxes, etc",
  "default_location": null,
  "default_keywords": "",
  "level": 1,
  "parent": 2,
  "part_count": null,
  "subcategories": null,
  "pathstring": "Mechanical/Enclosures",
  "starred": false,
  "structural": false,
  "icon": "",
  "parent_default_location": null
}
```
**Notable**: `category_name` still present alongside `category_detail`. `part_count` and `subcategories` are null in the list context.

---

## Probe 4 — GET /api/part/?limit=3&path_detail=true

**Request**: `GET /api/part/?limit=3&path_detail=true`
**Status**: 200 OK
**Effect**: Each part object gains a `category_path` array field (not `path_detail`).

**category_path shape**:
```json
[
  { "pk": 2, "name": "Mechanical" },
  { "pk": 15, "name": "Enclosures" }
]
```
**Notable**: The query parameter is named `path_detail` but the response key is `category_path` (breadcrumb array).

---

## Probe 5 — GET /api/part/ (no limit)

**Request**: `GET /api/part/` (no limit param)
**Status**: 200 OK
**Effect**: Returns a raw JSON array (not paginated envelope) with all 435 parts.
**Notable**: Without `limit`, the response format changes from `{count, next, previous, results}` to a plain `[]` array.

---

## Probe 6 — GET /api/part/82/ (single part detail)

**Request**: `GET /api/part/82/`
**Status**: 200 OK
**Part**: 1551ABK — Small plastic enclosure, black
**Notable fields vs list**: adds `notes` (null) field not present in list results. All other fields identical.

---

## Probe 7 — GET /api/part/82/?parameters=true

**Request**: `GET /api/part/82/?parameters=true`
**Status**: 200 OK
**Effect**: Adds `parameters` array, same structure as in list probe. Part 82 has 4 parameters.

---

## Probe 8 — GET /api/part/82/?category_detail=true

**Request**: `GET /api/part/82/?category_detail=true`
**Status**: 200 OK
**Effect**: Adds `category_detail` object with full category info.

---

## Probe 9 — GET /api/part/82/?parameters=true&category_detail=true

**Request**: `GET /api/part/82/?parameters=true&category_detail=true`
**Status**: 200 OK
**Effect**: Both `parameters` array and `category_detail` object appear together.

---

## Probe 10 — GET /api/part/99999/ (non-existent ID)

**Request**: `GET /api/part/99999/`
**Status**: 404
**Response**: `{"detail": "No Part matches the given query."}`

---

## Probe 11 — GET /api/part/?limit=3 (no auth)

**Request**: `GET /api/part/?limit=3` (no Authorization header)
**Status**: 401 Unauthorized

---

## Probe 12 — GET /api/part/82/ (no auth)

**Request**: `GET /api/part/82/` (no Authorization header)
**Status**: 401 Unauthorized

---

✓ parts-list-get-test-suite.md — 12 TCs written — observed 2026-04-18 — source: demo.inventree.org
