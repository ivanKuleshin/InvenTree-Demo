# TC-ACCMP-012: POST /api/company/ with name exceeding 100 characters returns 400

**Type:** API
**Priority:** P3
**Endpoint:** `POST /api/company/`

**Preconditions:**

- Valid HTTP Basic credentials with add permissions are available

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Construct a string of exactly 100 characters: `"TC-ACCMP-012-" + "A" * 87` (13 + 87 = 100 chars); record as `NAME_100`
3. Send `POST /api/company/` with body `{"name": NAME_100}`
4. Verify response status code is `201` (exactly 100 characters is accepted — boundary value)
5. Record the response `id` as `COMPANY_ID`
6. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
7. Verify DELETE response status code is `204`
8. Construct a string of exactly 101 characters: `"TC-ACCMP-012-" + "A" * 88` (13 + 88 = 101 chars); record as `NAME_101`
9. Send `POST /api/company/` with body `{"name": NAME_101}`
10. Verify response status code is `400`
11. Verify response body contains key `"name"` with a message indicating the value is too long (e.g., contains `"Ensure this field has no more than 100 characters"`)

**Request (step 3):**

- Method: `POST`
- URL: `/api/company/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-ACCMP-012-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" }
  ```
  _(exactly 100 characters)_

**Request (step 9):**

- Method: `POST`
- URL: `/api/company/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-ACCMP-012-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" }
  ```
  _(exactly 101 characters)_

**Expected Result:** A name of exactly 100 characters is accepted (`201`). A name of 101 characters is rejected (`400`) with a field-level `"name"` error citing the maximum length constraint.

**Observed** (probed 2026-04-17):

- 100-char name: status `201` — accepted at boundary
- 101-char name: status `400` — `{"name": ["Ensure this field has no more than 100 characters."]}`
- Matches spec: Yes

**Notes:** The schema documents `name` max length as 100. The boundary test confirms the inclusive upper limit. The same max-length constraint applies to the `contact` field (max 100). The `description` field has a higher limit of 500 characters and the `address` field allows up to 250.
