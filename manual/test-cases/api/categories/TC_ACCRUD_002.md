# TC-ACCRUD-002: GET /api/part/category/{id}/ retrieves a single category with path detail

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/category/{id}/`

**Preconditions:**

- Category with `pk=1` ("Electronics") exists on the server
- Valid HTTP Basic credentials are available

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/part/category/1/` with the authorization header
3. Verify response status code is `200`
4. Verify response body field `pk` equals `1`
5. Verify response body field `name` equals `"Electronics"`
6. Verify response body field `level` equals `0`
7. Verify response body field `parent` equals `null`
8. Verify response body does NOT contain field `path` (absent when `path_detail` not requested)
9. Send `GET /api/part/category/1/?path_detail=true` with the authorization header
10. Verify response status code is `200`
11. Verify response body contains field `path` as an array
12. Verify the first element of `path` is `{"pk": 1, "name": "Electronics"}`

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/category/1/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Request (step 9):**

- Method: `GET`
- URL: `/api/part/category/1/?path_detail=true`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Without `path_detail`, the response contains the base category fields. With `path_detail=true`, the response additionally contains a `path` array of `{pk, name}` objects representing the full ancestry chain from root to the category.

**Observed** (probed 2026-04-14):

- Status (both): `200`
- Without `path_detail`:
  ```json
  {
    "pk": 1,
    "name": "Electronics",
    "description": "Electronic components and systems",
    "level": 0,
    "parent": null,
    "part_count": 144,
    "subcategories": 12,
    "pathstring": "Electronics",
    "starred": false,
    "structural": false
  }
  ```
- With `path_detail=true`: adds `"path": [{"pk": 1, "name": "Electronics"}]`
- Matches spec: Yes

**Notes:** `path_detail=true` is the only way to obtain the structured ancestry array. The flat `pathstring` field (e.g., `"Electronics/Connectors/Pin Headers"`) is always present.
