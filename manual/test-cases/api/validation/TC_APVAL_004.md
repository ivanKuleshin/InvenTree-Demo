# TC-APVAL-004: POST /api/part/ with name exceeding 100 characters returns 400

**Type:** API
**Priority:** P3
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No specific data state required (request will be rejected before persistence)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Construct a string of exactly 101 characters (the letter `"B"` repeated 101 times)
3. Send `POST /api/part/` with body `{"name": "<101-char string>"}`
4. Verify response status code is `400`
5. Verify response body contains key `"name"`
6. Verify the value of `"name"` is an array containing the string `"Ensure this field has no more than 100 characters."`
7. Verify response body does NOT contain a `pk` field (no part was created)

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"
  }
  ```
  _(101 "B" characters)_

**Expected Result:** Server rejects the request with `400 Bad Request`. The `name` field error message identifies the max-length constraint violation.

**Observed** (probed 2026-04-14):

- Status: `400`
- Response body:
  ```json
  { "name": ["Ensure this field has no more than 100 characters."] }
  ```
- Matches spec: Yes

**Notes:** The `name` field has `max_length: 100` per the schema. The same error format applies to all string fields with max-length constraints. The `IPN` field also has `max_length: 100` (verified in TC-APVAL-005). The `description` field has `max_length: 250`.
