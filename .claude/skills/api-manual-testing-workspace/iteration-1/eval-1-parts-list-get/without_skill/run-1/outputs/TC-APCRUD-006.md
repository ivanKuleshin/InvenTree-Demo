# TC-APCRUD-006 — GET /api/part/ With ?category_detail=true Embeds Category Object

## Metadata
- **Endpoint**: GET /api/part/
- **Priority**: P1
- **Auth**: Required (Basic or Token)
- **Observed on**: 2026-04-18 against https://demo.inventree.org (API v479, InvenTree v1.4.0 dev)

## Objective
Verify that the `category_detail=true` query flag causes each part object to include an embedded `category_detail` object with full category metadata, rather than only the `category` (integer PK) and `category_name` (string) fields that appear in the baseline response.

## Preconditions
- Authenticated as `reader` role
- At least one part belongs to a category (e.g., pk=82 belongs to category pk=15 "Enclosures")

## Request
```
GET /api/part/?limit=3&category_detail=true&format=json
Authorization: Basic cmVhZGVyOnJlYWRvbmx5
```

## Expected Response

**Status**: 200 OK

**Each part object additionally contains**:
```json
"category_detail": {
  "pk": <integer>,
  "name": "<string>",
  "description": "<string>",
  "default_location": <integer or null>,
  "default_keywords": "<string>",
  "level": <integer>,
  "parent": <integer or null>,
  "part_count": <integer or null>,
  "subcategories": <integer or null>,
  "pathstring": "<string>",
  "starred": <boolean>,
  "structural": <boolean>,
  "icon": "<string>",
  "parent_default_location": <integer or null>
}
```

## Assertions
1. Status code is 200.
2. Each part object that has a non-null `category` field also contains a `category_detail` object.
3. `category_detail.pk` matches the value of the part's `category` field.
4. `category_detail.name` matches the part's `category_name` field.
5. `category_detail` contains: `pk`, `name`, `description`, `default_location`, `default_keywords`, `level`, `parent`, `part_count`, `subcategories`, `pathstring`, `starred`, `structural`, `icon`, `parent_default_location`.
6. `pathstring` reflects the full category hierarchy path (e.g., "Mechanical/Enclosures").
7. `level` is an integer indicating depth in the category tree (0 = root).
8. Without `category_detail=true`, the `category_detail` key is absent from the response.

## Observed Sample (pk=82, category_detail=true)
```json
"category_detail": {
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

The `category` field remains `15` and `category_name` remains `"Enclosures"` alongside the new `category_detail` object.
