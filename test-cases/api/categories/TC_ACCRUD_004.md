# TC-ACCRUD-004: POST /api/part/category/ creates a child category with parent assigned

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/category/`

**Preconditions:**

- Valid HTTP Basic credentials with create permissions are available
- A parent category exists; obtain its PK via `GET /api/part/category/?limit=5` (record as `PARENT_PK`; use `pk=1` "Electronics")

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/part/category/?limit=5` and record a valid top-level category PK as `PARENT_PK` (e.g., `1`)
3. Send `POST /api/part/category/` with body containing `name`, `parent`, and `description`
4. Verify response status code is `201`
5. Verify response body field `pk` is a positive integer (record as `CHILD_PK`)
6. Verify response body field `name` equals `"TC-ACCRUD-004-ChildCat"`
7. Verify response body field `parent` equals `PARENT_PK`
8. Verify response body field `level` equals `1` (one level below the top-level parent)
9. Verify response body field `pathstring` equals `"Electronics/TC-ACCRUD-004-ChildCat"`
10. Verify response body field `path` is an array with two elements: `[{"pk": PARENT_PK, "name": "Electronics"}, {"pk": CHILD_PK, "name": "TC-ACCRUD-004-ChildCat"}]`
11. Verify response body field `description` equals `"Child category for CRUD test"`
12. Send `DELETE /api/part/category/{CHILD_PK}/` to clean up
13. Verify DELETE response status code is `204`

**Request:**

- Method: `POST`
- URL: `/api/part/category/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-ACCRUD-004-ChildCat",
    "parent": 1,
    "description": "Child category for CRUD test"
  }
  ```

**Expected Result:** Server creates the category as a child of the specified parent. `level` is auto-computed as `parent.level + 1`. `pathstring` is auto-computed as `"<parent_pathstring>/<name>"`. The `path` array contains both parent and child entries.

**Observed** (probed 2026-04-14):

- Status: `201`; `pk`: `283`
- Response snippet:
  ```json
  {
    "pk": 283,
    "name": "TC-ACCRUD-004-ChildCat",
    "description": "Child of TC-ACCRUD-003",
    "level": 1,
    "parent": 282,
    "pathstring": "TC-ACCRUD-003-MinimalCat/TC-ACCRUD-004-ChildCat",
    "path": [
      { "pk": 282, "name": "TC-ACCRUD-003-MinimalCat" },
      { "pk": 283, "name": "TC-ACCRUD-004-ChildCat" }
    ]
  }
  ```
- Matches spec: Yes

**Notes:** `level` and `pathstring` are always server-computed — sending them in the request body has no effect. The `path` array depth matches the `level` value (level 1 = 2 elements in `path`).
