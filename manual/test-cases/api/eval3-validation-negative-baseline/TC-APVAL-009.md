# TC-APVAL-009: POST /api/part/ with non-integer value for default_expiry returns 400

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No specific data state required (request will be rejected before persistence)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "ValidPartName", "default_expiry": "seven"}`
3. Verify response status code is `400`
4. Verify response body contains key `"default_expiry"`
5. Verify the value of `"default_expiry"` is an array containing the string `"A valid integer is required."`
6. Verify response body does NOT contain a `pk` field (no part was created)
7. Send `POST /api/part/` with body `{"name": "ValidPartName2", "default_expiry": -1}`
8. Verify response status code is `400`
9. Verify response body contains key `"default_expiry"`
10. Verify the value of `"default_expiry"` is an array containing a string describing the minimum value constraint (e.g., `"Ensure this value is greater than or equal to 0."`)

**Request (step 2):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "ValidPartName", "default_expiry": "seven" }
  ```

**Request (step 7):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "ValidPartName2", "default_expiry": -1 }
  ```

**Expected Result:**
- Step 2–6: Server rejects string value with `400`. The `default_expiry` error identifies the type mismatch.
- Step 7–10: Server rejects negative integer with `400`. The `default_expiry` error identifies the minimum value constraint (schema specifies `minimum: 0`).

**Observed** (probed 2026-04-18):

- String value (`"seven"`): status `400`
  - Response body:
    ```json
    { "default_expiry": ["A valid integer is required."] }
    ```
- Negative value (`-1`): status `400`
  - Response body:
    ```json
    { "default_expiry": ["Ensure this value is greater than or equal to 0."] }
    ```
- No `pk` field present in either response
- Matches pattern: Yes — DRF integer fields return `"A valid integer is required."` for strings; range validators produce min/max messages

**Notes:** The `default_expiry` field is typed as `integer` in the schema with `minimum: 0` and `maximum: 2147483647`. Two distinct validation paths exist: type checking (string → integer error) and range checking (negative number → min constraint error). The schema's `minimum` constraint is enforced server-side and produces a localized DRF error message.
