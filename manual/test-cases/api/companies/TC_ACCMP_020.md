# TC-ACCMP-020: PATCH /api/company/address/{id}/ partially updates an address

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/company/address/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with change permissions are available
- Create a test company and address via setup steps below; record `COMPANY_ID` and `ADDRESS_ID`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-020-PatchCo"}` and record the response `id` as `COMPANY_ID`
3. Send `POST /api/company/address/` with body `{"company": COMPANY_ID, "line1": "Patch Start Lane", "city": "StartCity", "is_default": false}` and record the response `id` as `ADDRESS_ID`
4. Send `PATCH /api/company/address/{ADDRESS_ID}/` with body `{"city": "PatchedCity", "is_default": true}`
5. Verify response status code is `200`
6. Verify response body field `city` equals `"PatchedCity"` (updated)
7. Verify response body field `is_default` equals `true` (updated)
8. Verify response body field `line1` equals `"Patch Start Lane"` (unchanged)
9. Verify response body field `company` equals `COMPANY_ID` (unchanged)
10. Verify response body field `id` equals `ADDRESS_ID`
11. Send `DELETE /api/company/address/{ADDRESS_ID}/` to clean up
12. Verify DELETE response status code is `204`
13. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
14. Verify DELETE response status code is `204`

**Request (step 4):**

- Method: `PATCH`
- URL: `/api/company/address/{ADDRESS_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "city": "PatchedCity", "is_default": true }
  ```

**Expected Result:** Server applies only the two specified changes (`city` and `is_default`) and returns `200 OK`. All other address fields retain their current values. No fields are required in a PATCH body.

**Observed** (probed 2026-04-17):

- Status: `200`
- `city`: `"PatchedCity"` (updated)
- `is_default`: `true` (updated)
- `line1`, `company` unchanged
- Matches spec: Yes

**Notes:** PATCH on addresses is idempotent for the same input values. The `is_default` field is a simple boolean; the server does not automatically unset `is_default` on other addresses belonging to the same company when a new default is set — that constraint, if any, must be verified separately.
