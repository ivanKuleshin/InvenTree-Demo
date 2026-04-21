# TC-APVAL-006: POST /api/part/ with non-integer category value returns 400

**Type:** API
**Priority:** P3
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No specific data state required (request will be rejected before persistence)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "ValidName", "category": "not-a-number"}`
3. Verify response status code is `400`
4. Verify response body contains key `"category"`
5. Verify the value of `"category"` is an array containing the string `"Incorrect type. Expected pk value, received str."`
6. Verify response body does NOT contain a `pk` field
7. Send `POST /api/part/` with body `{"name": "ValidName2", "category": 3.14}`
8. Verify response status code is `400`
9. Verify response body contains key `"category"`

**Request (step 2):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "ValidName", "category": "not-a-number" }
  ```

**Expected Result:** Passing a non-integer value for a foreign key field (`category`) is rejected with `400`. The error message identifies the field and states the expected type is a `pk` (integer) value.

**Observed** (probed 2026-04-14):

- Status: `400`
- Response body:
  ```json
  { "category": ["Incorrect type. Expected pk value, received str."] }
  ```
- Matches spec: Yes

**Notes:** This type-check error applies to all foreign key fields (`category`, `default_location`, `revision_of`, `variant_of`, `responsible`). The DRF error message includes the received type (`str`, `float`, `list`, etc.) making it easy to distinguish type errors from existence errors (`"Invalid pk ... - object does not exist."`).
