# TC-ACCMP-022: POST /api/company/address/ with missing required fields returns 400

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/company/address/`

**Preconditions:**

- Valid HTTP Basic credentials with add permissions are available
- Create a test company via `POST /api/company/` with `{"name": "TC-ACCMP-022-ValCo"}` and record its `id` as `COMPANY_ID`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-022-ValCo"}` and record the response `id` as `COMPANY_ID`
3. Send `POST /api/company/address/` with body `{"line1": "No Company Street"}` (missing `company` field)
4. Verify response status code is `400`
5. Verify response body contains key `"company"` with a required field error
6. Send `POST /api/company/address/` with body `{"company": COMPANY_ID}` (missing `line1` field)
7. Verify response status code is `400`
8. Verify response body contains key `"line1"` with a required field error
9. Send `POST /api/company/address/` with body `{}` (both required fields missing)
10. Verify response status code is `400`
11. Verify response body contains key `"company"` with a required field error
12. Verify response body contains key `"line1"` with a required field error
13. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
14. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `POST`
- URL: `/api/company/address/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "line1": "No Company Street" }
  ```

**Request (step 6):**

- Method: `POST`
- URL: `/api/company/address/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "company": COMPANY_ID }
  ```

**Expected Result:** Omitting either `company` or `line1` (or both) returns `400 Bad Request` with field-level errors identifying the missing required fields. No address is created.

**Observed** (probed 2026-04-17):

- Missing `company`: status `400`; `{"company": ["This field is required."]}`
- Missing `line1`: status `400`; `{"line1": ["This field is required."]}`
- Both missing: status `400`; both keys present in error body
- Matches spec: Yes

**Notes:** `company` and `line1` are the only two required fields for address creation per the schema. All other fields (`line2`, `city`, `state`, `postal_code`, `country`, `phone`, `email`, `type`, `title`, `notes`, `is_default`) are optional.
