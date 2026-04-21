# TC-ACCMP-007: DELETE /api/company/{id}/ removes a company

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/company/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with delete permissions are available
- Create a test company via `POST /api/company/` with `{"name": "TC-ACCMP-007-ToDelete"}` and record its `id` as `COMPANY_ID`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-007-ToDelete"}` and record the response `id` as `COMPANY_ID`
3. Send `DELETE /api/company/{COMPANY_ID}/` with the authorization header
4. Verify response status code is `204`
5. Verify response body is empty
6. Send `GET /api/company/{COMPANY_ID}/` with the authorization header
7. Verify response status code is `404`
8. Verify response body field `detail` is a non-empty string indicating the record was not found

**Request (step 3):**

- Method: `DELETE`
- URL: `/api/company/{COMPANY_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server deletes the company and returns `204 No Content` with an empty body. A subsequent GET on the same `id` returns `404`.

**Observed** (probed 2026-04-17):

- DELETE status: `204`; body: empty
- Subsequent GET status: `404`
- GET body: `{"detail": "No Company matches the given query."}`
- Matches spec: Yes

**Notes:** Unlike parts, companies do not need to be deactivated before deletion. However, companies referenced by purchase orders or other dependent records may be protected by referential integrity constraints — the server will return `400` in that case with a message indicating the blocking dependency. The 404 error message is model-specific: `"No Company matches the given query."`, not the generic `"Not found."`.
