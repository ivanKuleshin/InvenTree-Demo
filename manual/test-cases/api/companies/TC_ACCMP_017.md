# TC-ACCMP-017: POST /api/company/address/ creates an address linked to a company

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/company/address/`

**Preconditions:**

- Valid HTTP Basic credentials with add permissions are available
- Create a test company first via `POST /api/company/` with `{"name": "TC-ACCMP-017-AddrCo"}` and record its `id` as `COMPANY_ID`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-017-AddrCo"}` and record the response `id` as `COMPANY_ID`
3. Send `POST /api/company/address/` with the body below
4. Verify response status code is `201`
5. Verify response body field `id` is a positive integer; record it as `ADDRESS_ID`
6. Verify response body field `company` equals `COMPANY_ID`
7. Verify response body field `line1` equals `"42 Test Avenue"`
8. Verify response body field `city` equals `"Testville"`
9. Verify response body field `postal_code` equals `"12345"`
10. Verify response body field `country` equals `"US"`
11. Verify response body field `is_default` equals `false` (not explicitly set)
12. Verify response body field `created` is a non-null ISO 8601 datetime string
13. Send `DELETE /api/company/address/{ADDRESS_ID}/` to clean up
14. Verify DELETE response status code is `204`
15. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
16. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `POST`
- URL: `/api/company/address/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "company": COMPANY_ID,
    "line1": "42 Test Avenue",
    "city": "Testville",
    "state": "TS",
    "postal_code": "12345",
    "country": "US"
  }
  ```

**Expected Result:** Server creates the address linked to the specified company and returns `201 Created`. The response includes the auto-assigned `id` and all submitted fields. `is_default` defaults to `false` when not supplied.

**Observed** (probed 2026-04-17):

- Status: `201`
- `company` FK confirmed as `COMPANY_ID`
- `line1`, `city`, `postal_code`, `country` all reflected back
- `is_default`: `false` by default
- `created` timestamp present
- Matches spec: Yes

**Notes:** `company` and `line1` are the two required fields for address creation. All other address fields (`line2`, `city`, `state`, `postal_code`, `country`, `phone`, `email`, `notes`, `type`, `title`) are optional and nullable. Clean up in reverse order: address first, then company.
