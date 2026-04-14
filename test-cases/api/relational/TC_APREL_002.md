# TC-APREL-002: POST /api/part/category/ with valid default_location FK persists the relationship

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/category/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- A stock location exists; obtain its PK via `GET /api/stock/location/?limit=5` (use `pk=1`, "Factory")
- An invalid location PK `99999999` does not exist

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/stock/location/?limit=5` and record a valid location PK as `LOC_PK` (e.g., `1`)
3. Send `POST /api/part/category/` with body `{"name": "TC-APREL-002-CatWithLoc", "default_location": LOC_PK}`
4. Verify response status code is `201`
5. Verify response body field `default_location` equals `LOC_PK`
6. Record the new category `pk` as `CAT_PK`
7. Send `POST /api/part/category/` with body `{"name": "TC-APREL-002-BadLoc", "default_location": 99999999}`
8. Verify response status code is `400`
9. Verify response body contains key `"default_location"`
10. Verify the value contains a string including `"Invalid pk"` and `"object does not exist"`
11. Send `DELETE /api/part/category/{CAT_PK}/` to clean up
12. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `POST`
- URL: `/api/part/category/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-APREL-002-CatWithLoc", "default_location": 1 }
  ```

**Expected Result:** A valid `default_location` PK is accepted and persisted. An invalid `default_location` PK returns `400` with a field-level error, identical in structure to invalid `category` FK errors.

**Observed** (probed 2026-04-14):

- Valid location POST status: `201`; `pk`: `284`; `default_location: 1`
- Invalid location POST status: `400`
- Response body:
  ```json
  { "default_location": ["Invalid pk \"99999999\" - object does not exist."] }
  ```
- Matches spec: Yes

**Notes:** The `default_location` on a category is inherited by parts assigned to that category via the read-only `category_default_location` field on the part. Setting `default_location` on the category does not retroactively update the `default_location` field on existing parts — only new parts picking up the category default are affected.
