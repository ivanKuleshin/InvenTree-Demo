# TC-ACCMP-011: POST /api/company/ without required name field returns 400

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/company/`

**Preconditions:**

- Valid HTTP Basic credentials with add permissions are available

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with an empty JSON body `{}`
3. Verify response status code is `400`
4. Verify response body contains key `"name"`
5. Verify the value at `"name"` is an array containing a string that includes `"required"` or `"blank"` (field is required)
6. Send `POST /api/company/` with body `{"name": ""}` (empty string)
7. Verify response status code is `400`
8. Verify response body contains key `"name"` with a validation error message

**Request (step 2):**

- Method: `POST`
- URL: `/api/company/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {}
  ```

**Request (step 6):**

- Method: `POST`
- URL: `/api/company/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "" }
  ```

**Expected Result:** Both requests return `400 Bad Request`. The response body contains field-level error detail under the `name` key. No company is created.

**Observed** (probed 2026-04-17):

- Empty body POST status: `400`
- Response: `{"name": ["This field is required."]}`
- Empty string name POST status: `400`
- Response: `{"name": ["This field may not be blank."]}`
- Matches spec: Yes

**Notes:** The error message distinguishes between "missing" (`"This field is required."`) and "blank" (`"This field may not be blank."`) — automation assertions must check for the correct message per scenario. No partial record is created on a `400` response.
