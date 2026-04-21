# TC-ACCMP-021: DELETE /api/company/address/{id}/ removes an address

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/company/address/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with delete permissions are available
- Create a test company and address via setup steps below; record `COMPANY_ID` and `ADDRESS_ID`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-021-DelAddrCo"}` and record the response `id` as `COMPANY_ID`
3. Send `POST /api/company/address/` with body `{"company": COMPANY_ID, "line1": "Delete Me Drive"}` and record the response `id` as `ADDRESS_ID`
4. Send `DELETE /api/company/address/{ADDRESS_ID}/` with the authorization header
5. Verify response status code is `204`
6. Verify response body is empty
7. Send `GET /api/company/address/{ADDRESS_ID}/` with the authorization header
8. Verify response status code is `404`
9. Verify response body contains key `"detail"` as a non-empty string
10. Send `DELETE /api/company/{COMPANY_ID}/` to clean up the parent company
11. Verify DELETE response status code is `204`

**Request (step 4):**

- Method: `DELETE`
- URL: `/api/company/address/{ADDRESS_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server deletes the address and returns `204 No Content` with an empty body. A subsequent GET on the same `id` returns `404`.

**Observed** (probed 2026-04-17):

- DELETE status: `204`; body: empty
- Subsequent GET status: `404`
- GET body: `{"detail": "No Address matches the given query."}`
- Matches spec: Yes

**Notes:** Addresses can be deleted independently without affecting the parent company. Deleting the parent company (step 10) after the address deletion is a cleanup step only — company deletion cascades to remaining addresses if any exist. The 404 `"detail"` message for addresses is `"No Address matches the given query."`.
