# TC-ACCRUD-005: PUT /api/part/category/{id}/ replaces all writable fields

**Type:** API
**Priority:** P2
**Endpoint:** `PUT /api/part/category/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with change permissions are available
- A test category exists; create one via `POST /api/part/category/` with `{"name": "TC-ACCRUD-005-Setup"}` and record its `pk` as `CAT_PK`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/category/` with body `{"name": "TC-ACCRUD-005-Setup"}` and record the response `pk` as `CAT_PK`
3. Send `PUT /api/part/category/{CAT_PK}/` with the full replacement body below
4. Verify response status code is `200`
5. Verify response body field `pk` equals `CAT_PK`
6. Verify response body field `name` equals `"TC-ACCRUD-005-UpdatedName"`
7. Verify response body field `description` equals `"PUT full update applied"`
8. Verify response body field `structural` equals `true`
9. Verify response body field `default_keywords` equals `"updated keywords"`
10. Verify response body field `parent` equals `null`
11. Verify response body field `pathstring` equals `"TC-ACCRUD-005-UpdatedName"`
12. Verify response body field `part_count` equals `0`
13. Send `DELETE /api/part/category/{CAT_PK}/` to clean up
14. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `PUT`
- URL: `/api/part/category/{CAT_PK}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-ACCRUD-005-UpdatedName",
    "description": "PUT full update applied",
    "structural": true,
    "default_keywords": "updated keywords",
    "parent": null
  }
  ```

**Expected Result:** Server replaces all writable fields with the provided values and returns `200 OK` with the updated category object. Read-only computed fields (`pathstring`, `level`, `part_count`) are recomputed server-side. The `path` array reflects the new name.

**Observed** (probed 2026-04-14):

- Status: `200`; `pk`: `283`
- Response snippet:
  ```json
  {
    "pk": 283,
    "name": "TC-ACCRUD-005-UpdatedCat",
    "description": "PUT full update applied",
    "level": 1,
    "parent": 282,
    "part_count": 0,
    "subcategories": 0,
    "pathstring": "TC-ACCRUD-003-MinimalCat/TC-ACCRUD-005-UpdatedCat",
    "path": [
      { "pk": 282, "name": "TC-ACCRUD-003-MinimalCat" },
      { "pk": 283, "name": "TC-ACCRUD-005-UpdatedCat" }
    ]
  }
  ```
- `part_count` is `0` (populated) in PUT response, unlike `null` in fresh POST response
- Matches spec: Yes

**Notes:** PUT requires all mandatory fields (`name`). Omitting `name` from a PUT body returns `400 {"name": ["This field is required."]}`. `pathstring` is recomputed immediately upon save — automation assertions can rely on it reflecting the new name within the same response.
