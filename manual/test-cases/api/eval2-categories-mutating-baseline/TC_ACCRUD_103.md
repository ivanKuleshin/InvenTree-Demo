# TC-ACCRUD-103: POST /api/part/category/ creates a new top-level category

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/category/`

**Preconditions:**

- Valid HTTP Basic credentials with create permissions are available
- No category named "TC-ACCRUD-103-NewCategory" exists at the top level

**Steps:**

1. Send `POST /api/part/category/` with body `{"name": "TC-ACCRUD-103-NewCategory", "description": "Test category for ACCRUD eval", "structural": false}`
2. Verify response status code is `201`
3. Verify response body field `pk` is a positive integer (record as `NEW_CAT_PK`)
4. Verify response body field `name` equals `"TC-ACCRUD-103-NewCategory"`
5. Verify response body field `description` equals `"Test category for ACCRUD eval"`
6. Verify response body field `structural` equals `false`
7. Verify response body field `parent` equals `null`
8. Verify response body field `level` equals `0`
9. Verify response body field `pathstring` equals `"TC-ACCRUD-103-NewCategory"`
10. Verify response body field `part_count` is `null` (not yet computed)
11. Verify response body field `subcategories` is `null` (not yet computed)
12. Verify response body field `path` is an array containing one element: `{"pk": NEW_CAT_PK, "name": "TC-ACCRUD-103-NewCategory"}`
13. Send `DELETE /api/part/category/{NEW_CAT_PK}/` to clean up
14. Verify DELETE response status is `204`

**Request:**

- Method: `POST`
- URL: `/api/part/category/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-ACCRUD-103-NewCategory",
    "description": "Test category for ACCRUD eval",
    "structural": false
  }
  ```

**Expected Result:** Server creates a new root-level part category and returns `201 Created` with the full category object including a `path` array. `part_count` and `subcategories` are `null` immediately after creation.

**Observed** (probed 2026-04-18):

- Status: `201`; `pk`: `29`
- Response:
  ```json
  {
    "pk": 29,
    "name": "TC-ACCRUD Test Category",
    "description": "Test category for ACCRUD eval",
    "default_location": null,
    "default_keywords": null,
    "level": 0,
    "parent": null,
    "part_count": null,
    "subcategories": null,
    "pathstring": "TC-ACCRUD Test Category",
    "starred": false,
    "structural": false,
    "icon": "",
    "parent_default_location": null,
    "path": [{"pk": 29, "name": "TC-ACCRUD Test Category"}]
  }
  ```
- Matches spec: Yes

**Notes:** `part_count` and `subcategories` are `null` immediately after creation. The `path` array is always present in POST responses regardless of query parameters.
