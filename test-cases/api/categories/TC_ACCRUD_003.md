# TC-ACCRUD-003: POST /api/part/category/ creates a minimal top-level category

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/category/`

**Preconditions:**

- Valid HTTP Basic credentials with create permissions are available
- A category named "TC-ACCRUD-003-MinimalCat" does not already exist at the top level

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/category/` with body `{"name": "TC-ACCRUD-003-MinimalCat"}`
3. Verify response status code is `201`
4. Verify response body field `pk` is a positive integer (record as `NEW_CAT_PK`)
5. Verify response body field `name` equals `"TC-ACCRUD-003-MinimalCat"`
6. Verify response body field `parent` equals `null`
7. Verify response body field `level` equals `0`
8. Verify response body field `pathstring` equals `"TC-ACCRUD-003-MinimalCat"`
9. Verify response body field `description` equals `""`
10. Verify response body field `structural` equals `false`
11. Verify response body field `default_location` equals `null`
12. Verify response body field `path` is an array containing one element: `{"pk": NEW_CAT_PK, "name": "TC-ACCRUD-003-MinimalCat"}`
13. Send `DELETE /api/part/category/{NEW_CAT_PK}/` to clean up the created category
14. Verify DELETE response status code is `204`

**Request:**

- Method: `POST`
- URL: `/api/part/category/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-ACCRUD-003-MinimalCat" }
  ```

**Expected Result:** Server creates a new top-level category and returns `201 Created` with the full category object. All optional fields use server defaults. The `path` array is returned in the POST response even without `?path_detail=true`.

**Observed** (probed 2026-04-14):

- Status: `201`; `pk`: `282`
- Response snippet:
  ```json
  {
    "pk": 282,
    "name": "TC-ACCRUD-003-MinimalCat",
    "description": "",
    "default_location": null,
    "default_keywords": null,
    "level": 0,
    "parent": null,
    "part_count": null,
    "subcategories": null,
    "pathstring": "TC-ACCRUD-003-MinimalCat",
    "starred": false,
    "structural": false,
    "icon": "",
    "parent_default_location": null,
    "path": [{ "pk": 282, "name": "TC-ACCRUD-003-MinimalCat" }]
  }
  ```
- Matches spec: Yes

**Notes:** `part_count` and `subcategories` are `null` immediately after creation (not yet computed). They become `0` after the first GET or after a PUT/PATCH response. The `path` array is always included in create/update responses — unlike GET responses which require `?path_detail=true`.
