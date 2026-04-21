# TC-ACCMP-008: GET /api/company/ filters by is_supplier=true

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/company/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- At least one supplier company exists on the server (one with `is_supplier=true`)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/company/?is_supplier=true&limit=25` with the authorization header
3. Verify response status code is `200`
4. Verify response body field `count` is a positive integer
5. Verify every item in `results` has `is_supplier` equal to `true`
6. Send `GET /api/company/?is_manufacturer=true&limit=25` with the authorization header
7. Verify response status code is `200`
8. Verify every item in `results` has `is_manufacturer` equal to `true`
9. Send `GET /api/company/?is_customer=true&limit=25` with the authorization header
10. Verify response status code is `200`
11. Verify every item in `results` has `is_customer` equal to `true`
12. Send `GET /api/company/?is_supplier=true&is_manufacturer=true&limit=25` with the authorization header
13. Verify response status code is `200`
14. Verify every item in `results` has both `is_supplier` equal to `true` and `is_manufacturer` equal to `true`

**Request (step 2):**

- Method: `GET`
- URL: `/api/company/?is_supplier=true&limit=25`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Each role filter (`is_supplier`, `is_manufacturer`, `is_customer`) independently narrows results to companies holding that role. Combining two role filters returns the intersection — companies holding both roles simultaneously.

**Observed** (probed 2026-04-17):

- `is_supplier=true` returns only supplier companies; all results have `is_supplier: true`
- `is_manufacturer=true` returns only manufacturer companies
- `is_customer=true` returns only customer companies
- Combined filters return the intersection correctly
- Matches spec: Yes

**Notes:** Role filters accept `true` and `false` values. Sending `is_supplier=false` returns all companies that are NOT suppliers. Multiple role filters combine with AND logic.
