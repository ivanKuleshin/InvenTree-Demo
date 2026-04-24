# TC-APCRUD-009 — GET /api/part/ With ?parameters=true and ?category_detail=true Combined

## Metadata
- **Endpoint**: GET /api/part/
- **Priority**: P2
- **Auth**: Required (Basic or Token)
- **Observed on**: 2026-04-18 against https://demo.inventree.org (API v479, InvenTree v1.4.0 dev)

## Objective
Verify that both expansion flags can be combined in a single request, producing a response where each part object contains both the `parameters` array and the `category_detail` object simultaneously.

## Preconditions
- Authenticated as `reader` role
- Part pk=82 exists, belongs to category 15 "Enclosures", and has 4 parameters

## Request
```
GET /api/part/?limit=1&parameters=true&category_detail=true&format=json
Authorization: Basic cmVhZGVyOnJlYWRvbmx5
```

## Expected Response

**Status**: 200 OK

**Each part object contains BOTH**:
- `parameters` — array (same structure as TC-APCRUD-005)
- `category_detail` — object (same structure as TC-APCRUD-006)

**`next` URL** reflects both query flags:
```
.../api/part/?category_detail=true&format=json&limit=1&offset=1&parameters=true
```

## Assertions
1. Status code is 200.
2. Each part object in `results` contains both `parameters` (array) and `category_detail` (object).
3. `category_detail.pk` matches the part's `category` integer field.
4. `parameters` array is present (empty array if part has no parameters, non-empty if it does).
5. The `next` pagination URL preserves both `parameters=true` and `category_detail=true` flags.
6. No server error occurs from combining the two flags.

## Observed
- Request succeeded with status 200.
- Part pk=82 returned with both `parameters` (4 items) and `category_detail` (Enclosures, pk=15) embedded.
- `next` URL: `https://demo.inventree.org/api/part/?category_detail=true&format=json&limit=1&offset=1&parameters=true`
