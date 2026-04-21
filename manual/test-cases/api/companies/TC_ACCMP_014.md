# TC-ACCMP-014: GET /api/company/{id}/ with non-existent id returns 404

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/company/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- A company id that does not exist must be used; determine a safe non-existent id by taking the current maximum `id` from `GET /api/company/?ordering=-id&limit=1` and adding `99999`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/company/?ordering=-id&limit=1` and record the highest `id` value; compute `NONEXISTENT_ID = that id + 99999`
3. Send `GET /api/company/{NONEXISTENT_ID}/` with the authorization header
4. Verify response status code is `404`
5. Verify response body contains key `"detail"` as a non-empty string
6. Send `PUT /api/company/{NONEXISTENT_ID}/` with body `{"name": "Ghost"}` and the authorization header
7. Verify response status code is `404`
8. Send `PATCH /api/company/{NONEXISTENT_ID}/` with body `{"description": "ghost"}` and the authorization header
9. Verify response status code is `404`
10. Send `DELETE /api/company/{NONEXISTENT_ID}/` with the authorization header
11. Verify response status code is `404`

**Request (step 3):**

- Method: `GET`
- URL: `/api/company/{NONEXISTENT_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** All methods (GET, PUT, PATCH, DELETE) on a non-existent company `id` return `404 Not Found`. The response body contains a `"detail"` field with a descriptive message.

**Observed** (probed 2026-04-17):

- GET status: `404`; body: `{"detail": "No Company matches the given query."}`
- PUT status: `404`
- PATCH status: `404`
- DELETE status: `404`
- Matches spec: Yes

**Notes:** The 404 `"detail"` message is `"No Company matches the given query."` — the model-specific variant, not the generic `"Not found."`. Computing a non-existent id dynamically avoids false positives if a previously hardcoded id is later created by another test.
