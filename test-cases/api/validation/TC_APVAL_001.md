# TC-APVAL-001: POST /api/part/category/ with missing required name field returns 400

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/category/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No specific data state required (request will be rejected before persistence)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/category/` with an empty JSON body `{}`
3. Verify response status code is `400`
4. Verify response body contains key `"name"`
5. Verify the value of `"name"` is an array containing the string `"This field is required."`
6. Verify response body does NOT contain a `pk` field (no category was created)

**Request:**

- Method: `POST`
- URL: `/api/part/category/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {}
  ```

**Expected Result:** Server rejects the request with `400 Bad Request`. Response body identifies `name` as a required field using DRF's standard error structure.

**Observed** (probed 2026-04-14):

- Status: `400`
- Response body:
  ```json
  { "name": ["This field is required."] }
  ```
- Matches spec: Yes

**Notes:** The `name` field is the only required field for category creation. All other fields (`description`, `parent`, `default_location`, `default_keywords`, `structural`, `icon`) are optional with server defaults.
