# TC-APVAL-005: POST /api/part/ with IPN exceeding 100 characters returns 400

**Type:** API
**Priority:** P3
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No specific data state required (request will be rejected before persistence)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Construct a string of exactly 101 characters (the letter `"C"` repeated 101 times)
3. Send `POST /api/part/` with body `{"name": "ValidPartName", "IPN": "<101-char string>"}`
4. Verify response status code is `400`
5. Verify response body contains key `"IPN"`
6. Verify the value of `"IPN"` is an array containing the string `"Ensure this field has no more than 100 characters."`
7. Verify response body does NOT contain a `pk` field

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "ValidPartName",
    "IPN": "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC"
  }
  ```
  _(IPN is 101 "C" characters)_

**Expected Result:** Server rejects the request with `400 Bad Request`. The `IPN` field error identifies the max-length constraint. The `name` field is valid and does not appear in the error response.

**Observed** (probed 2026-04-14):

- Status: `400`
- Response body:
  ```json
  { "IPN": ["Ensure this field has no more than 100 characters."] }
  ```
- Only `IPN` key in error — `name` was valid and not mentioned
- Matches spec: Yes

**Notes:** The error response only lists fields that failed validation. When multiple fields are invalid simultaneously, all failing field keys appear in the same response object. This allows a single request to surface multiple validation errors at once.
