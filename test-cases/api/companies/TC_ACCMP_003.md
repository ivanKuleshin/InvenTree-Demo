# TC-ACCMP-003: POST /api/company/ creates a company with required fields only

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/company/`

**Preconditions:**

- Valid HTTP Basic credentials with add permissions are available

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with the body below
3. Verify response status code is `201`
4. Verify response body field `name` equals `"TC-ACCMP-003-MinimalCo"`
5. Verify response body field `id` is a positive integer; record it as `COMPANY_ID`
6. Verify response body field `active` equals `true` (server default)
7. Verify response body field `is_supplier` equals `false` (server default)
8. Verify response body field `is_manufacturer` equals `false` (server default)
9. Verify response body field `is_customer` equals `false` (server default)
10. Verify response body field `description` is `null` or empty string (not provided)
11. Verify response body field `created` is a non-null ISO 8601 datetime string
12. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
13. Verify DELETE response status code is `204`

**Request (step 2):**

- Method: `POST`
- URL: `/api/company/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-ACCMP-003-MinimalCo" }
  ```

**Expected Result:** Server creates the company using only the required `name` field and returns `201 Created`. All optional fields default to `null` or `false`. The `id` is auto-assigned. The `active` flag defaults to `true`.

**Observed** (probed 2026-04-17):

- Status: `201`
- Response snippet:
  ```json
  {
    "id": 98,
    "name": "TC-ACCMP-003-MinimalCo",
    "active": true,
    "is_supplier": false,
    "is_manufacturer": false,
    "is_customer": false,
    "description": null,
    "created": "2026-04-17T10:15:00Z"
  }
  ```
- Matches spec: Yes

**Notes:** `name` is the only required field for company creation. All role booleans (`is_supplier`, `is_manufacturer`, `is_customer`) default to `false` — a newly created company has no roles until explicitly set. Cleanup via DELETE is straightforward; unlike parts, companies do not require deactivation before deletion.
