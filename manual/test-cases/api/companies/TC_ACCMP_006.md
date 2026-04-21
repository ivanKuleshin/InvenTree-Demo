# TC-ACCMP-006: PATCH /api/company/{id}/ partially updates selected fields on an existing company

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/company/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with change permissions are available
- Create a test company via `POST /api/company/` with `{"name": "TC-ACCMP-006-Setup", "is_supplier": false, "description": "original"}` and record its `id` as `COMPANY_ID`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-006-Setup", "is_supplier": false, "description": "original"}` and record the response `id` as `COMPANY_ID`
3. Send `PATCH /api/company/{COMPANY_ID}/` with body `{"is_supplier": true, "description": "patched description"}`
4. Verify response status code is `200`
5. Verify response body field `is_supplier` equals `true`
6. Verify response body field `description` equals `"patched description"`
7. Verify response body field `name` equals `"TC-ACCMP-006-Setup"` (unchanged)
8. Verify response body field `is_manufacturer` equals `false` (unchanged default)
9. Verify response body field `is_customer` equals `false` (unchanged default)
10. Verify response body field `active` equals `true` (unchanged default)
11. Verify response body field `id` equals `COMPANY_ID`
12. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
13. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `PATCH`
- URL: `/api/company/{COMPANY_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "is_supplier": true, "description": "patched description" }
  ```

**Expected Result:** Server applies only the two specified field changes (`is_supplier` and `description`) and returns `200 OK`. All other fields retain their current values. No fields are required in a PATCH body.

**Observed** (probed 2026-04-17):

- Status: `200`
- `is_supplier`: `true` (updated)
- `description`: `"patched description"` (updated)
- `name`, `is_manufacturer`, `is_customer`, `active` all unchanged
- Matches spec: Yes

**Notes:** PATCH is idempotent for the same input values. Sending an empty body `{}` is valid and returns `200` with no field changes applied.
