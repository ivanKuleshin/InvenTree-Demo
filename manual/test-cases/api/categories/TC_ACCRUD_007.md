# TC-ACCRUD-007: DELETE /api/part/category/{id}/ removes a category

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/part/category/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with delete permissions are available
- A test category with no parts assigned exists; create one via `POST /api/part/category/` with `{"name": "TC-ACCRUD-007-ToDelete"}` and record its `pk` as `CAT_PK`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/category/` with body `{"name": "TC-ACCRUD-007-ToDelete"}` and record the response `pk` as `CAT_PK`
3. Send `DELETE /api/part/category/{CAT_PK}/`
4. Verify response status code is `204`
5. Verify response body is empty
6. Send `GET /api/part/category/{CAT_PK}/` to confirm deletion
7. Verify response status code is `404`
8. Verify response body contains `"detail"` with value `"No PartCategory matches the given query."`

**Request (step 3):**

- Method: `DELETE`
- URL: `/api/part/category/{CAT_PK}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server deletes the category and returns `204 No Content` with an empty body. A subsequent GET on the same PK returns `404`.

**Observed** (probed 2026-04-14):

- DELETE status: `204`; body: `""`
- Subsequent GET status: `404`
- GET body: `{"detail": "No PartCategory matches the given query."}`
- Matches spec: Yes

**Notes:** Deleting a category that still has parts assigned is allowed by the API — parts are orphaned (their `category` field is set to `null`). Deleting a category that has child subcategories also succeeds; children become top-level (their `parent` is set to `null`). These cascade behaviors should be verified in relational integrity tests.
