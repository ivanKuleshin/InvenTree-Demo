# TC-ACCRUD-104: PATCH /api/part/category/{pk}/ renames an existing category

**Type:** API
**Priority:** P1
**Endpoint:** `PATCH /api/part/category/{pk}/`

**Preconditions:**

- Valid HTTP Basic credentials with update permissions are available
- A test category can be created via `POST /api/part/category/` before this test

**Steps:**

1. Send `POST /api/part/category/` with body `{"name": "TC-ACCRUD-104-Original", "description": "Before rename"}` and record `pk` as `CAT_PK`
2. Verify POST response status is `201`
3. Send `PATCH /api/part/category/{CAT_PK}/` with body `{"name": "TC-ACCRUD-104-Renamed"}`
4. Verify PATCH response status code is `200`
5. Verify response body field `pk` equals `CAT_PK`
6. Verify response body field `name` equals `"TC-ACCRUD-104-Renamed"`
7. Verify response body field `description` equals `"Before rename"` (unchanged)
8. Verify response body field `pathstring` equals `"TC-ACCRUD-104-Renamed"` (updated to match new name)
9. Verify response body field `path` is an array containing `{"pk": CAT_PK, "name": "TC-ACCRUD-104-Renamed"}`
10. Verify response body field `parent` is unchanged (`null`)
11. Verify response body field `level` is unchanged (`0`)
12. Send `DELETE /api/part/category/{CAT_PK}/` to clean up
13. Verify DELETE response status is `204`

**Request:**

- Method: `PATCH`
- URL: `/api/part/category/{CAT_PK}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-ACCRUD-104-Renamed" }
  ```

**Expected Result:** Server applies the partial update and returns `200 OK` with the full updated category object. Only the `name` (and derived `pathstring`) changes; all other fields remain as before.

**Observed** (probed 2026-04-18):

- Created category with `pk=29`, name `"TC-ACCRUD Test Category"`
- Sent PATCH with `{"name": "TC-ACCRUD Test Category Renamed"}`
- Status: `200`
- Response:
  ```json
  {
    "pk": 29,
    "name": "TC-ACCRUD Test Category Renamed",
    "description": "Test category for ACCRUD eval",
    "default_location": null,
    "default_keywords": null,
    "level": 0,
    "parent": null,
    "part_count": 0,
    "subcategories": 0,
    "pathstring": "TC-ACCRUD Test Category Renamed",
    "starred": false,
    "structural": false,
    "icon": "",
    "parent_default_location": null,
    "path": [{"pk": 29, "name": "TC-ACCRUD Test Category Renamed"}]
  }
  ```
- Matches spec: Yes

**Notes:** After PATCH, `part_count` and `subcategories` are `0` (not `null` as they were after POST). The `pathstring` updates automatically to reflect the new name. PATCH is partial: unspecified fields remain unchanged.
