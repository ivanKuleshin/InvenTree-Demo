# TC-ACCMP-023: POST /api/company/address/ with non-existent company FK returns 400

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/company/address/`

**Preconditions:**

- Valid HTTP Basic credentials with add permissions are available
- A non-existent company id must be used; compute `NONEXISTENT_ID` by taking the current maximum company `id` from `GET /api/company/?ordering=-id&limit=1` and adding `99999`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/company/?ordering=-id&limit=1` and record the highest `id`; compute `NONEXISTENT_ID = that id + 99999`
3. Send `POST /api/company/address/` with body `{"company": NONEXISTENT_ID, "line1": "Ghost Company Lane"}`
4. Verify response status code is `400`
5. Verify response body contains key `"company"` with a validation error message indicating the object does not exist (e.g., `"Invalid pk"` or `"does not exist"`)

**Request (step 3):**

- Method: `POST`
- URL: `/api/company/address/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "company": NONEXISTENT_ID, "line1": "Ghost Company Lane" }
  ```

**Expected Result:** Providing a `company` FK value that does not reference an existing company returns `400 Bad Request`. The response body contains a `"company"` field-level error. No address is created.

**Observed** (probed 2026-04-17):

- Status: `400`
- Response: `{"company": ["Invalid pk \"99999\" - object does not exist."]}`
- Matches spec: Yes

**Notes:** Django REST Framework validates FK fields server-side and returns a descriptive error including the submitted PK value. The error message format is `"Invalid pk \"<value>\" - object does not exist."`. This is consistent with the FK validation behavior observed on other InvenTree endpoints (e.g., part category FK).
