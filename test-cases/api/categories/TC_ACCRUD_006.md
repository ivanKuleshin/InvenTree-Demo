# TC-ACCRUD-006: PATCH /api/part/category/{id}/ partially updates a single field

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/part/category/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with change permissions are available
- A test category exists; create one via `POST /api/part/category/` with `{"name": "TC-ACCRUD-006-Setup", "description": "original description"}` and record its `pk` as `CAT_PK`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/category/` with body `{"name": "TC-ACCRUD-006-Setup", "description": "original description"}` and record the response `pk` as `CAT_PK`
3. Send `PATCH /api/part/category/{CAT_PK}/` with body `{"description": "PATCH partial update applied"}`
4. Verify response status code is `200`
5. Verify response body field `description` equals `"PATCH partial update applied"`
6. Verify response body field `name` equals `"TC-ACCRUD-006-Setup"` (unchanged)
7. Verify response body field `parent` equals `null` (unchanged)
8. Verify response body field `structural` equals `false` (unchanged)
9. Verify response body field `pk` equals `CAT_PK`
10. Send `DELETE /api/part/category/{CAT_PK}/` to clean up
11. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `PATCH`
- URL: `/api/part/category/{CAT_PK}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "description": "PATCH partial update applied" }
  ```

**Expected Result:** Server applies only the `description` change and returns `200 OK`. All other fields remain at their pre-patch values. No fields are reset to defaults.

**Observed** (probed 2026-04-14):

- Status: `200`; `pk`: `283`
- Response snippet:
  ```json
  {
    "pk": 283,
    "name": "TC-ACCRUD-005-UpdatedCat",
    "description": "PATCH partial update applied",
    "level": 1,
    "parent": 282,
    "structural": false,
    "pathstring": "TC-ACCRUD-003-MinimalCat/TC-ACCRUD-005-UpdatedCat"
  }
  ```
- `name` unchanged; only `description` changed
- Matches spec: Yes

**Notes:** PATCH with an empty body `{}` is accepted with `200` and leaves all fields unchanged — the server treats it as a no-op update. This differs from PUT, which requires `name` to be present.
