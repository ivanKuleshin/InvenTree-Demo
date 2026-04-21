# TC-ACCMP-010: PATCH /api/company/{id}/ sets active=false to deactivate a company

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/company/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with change permissions are available
- Create a test company via `POST /api/company/` with `{"name": "TC-ACCMP-010-Active", "active": true}` and record its `id` as `COMPANY_ID`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-010-Active", "active": true}` and record the response `id` as `COMPANY_ID`
3. Verify the POST response body field `active` equals `true`
4. Send `PATCH /api/company/{COMPANY_ID}/` with body `{"active": false}`
5. Verify response status code is `200`
6. Verify response body field `active` equals `false`
7. Verify response body field `name` equals `"TC-ACCMP-010-Active"` (unchanged)
8. Send `GET /api/company/{COMPANY_ID}/` with the authorization header
9. Verify response status code is `200`
10. Verify response body field `active` equals `false` (persisted)
11. Send `GET /api/company/?active=false&limit=50` with the authorization header
12. Verify the response contains a result with `id` equal to `COMPANY_ID`
13. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
14. Verify DELETE response status code is `204`

**Request (step 4):**

- Method: `PATCH`
- URL: `/api/company/{COMPANY_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "active": false }
  ```

**Expected Result:** Setting `active=false` via PATCH marks the company as inactive without deleting it. The change persists when retrieved via GET. An `active=false` filter on the list endpoint includes the deactivated company in its results.

**Observed** (probed 2026-04-17):

- PATCH status: `200`; `active: false`
- Subsequent GET confirms `active: false` persisted
- `active=false` filter returns the deactivated company
- Matches spec: Yes

**Notes:** Deactivating a company via `active=false` is the recommended approach for preserving referential integrity when the company is referenced by purchase orders or other records. The company remains visible via the API but should be excluded from active supplier/customer selection UIs.
