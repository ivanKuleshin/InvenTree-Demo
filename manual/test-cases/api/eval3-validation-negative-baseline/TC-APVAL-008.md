# TC-APVAL-008: POST /api/part/ with non-numeric value for minimum_stock returns 400

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No specific data state required (request will be rejected before persistence)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "ValidPartName", "minimum_stock": "not-a-number"}`
3. Verify response status code is `400`
4. Verify response body contains key `"minimum_stock"`
5. Verify the value of `"minimum_stock"` is an array containing a string describing the type error (e.g., `"A valid number is required."`)
6. Verify response body does NOT contain a `pk` field (no part was created)

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "ValidPartName", "minimum_stock": "not-a-number" }
  ```

**Expected Result:** Server rejects the request with `400 Bad Request`. The `minimum_stock` field error identifies the type mismatch. The `name` field is valid and does not appear in the error response.

**Observed** (probed 2026-04-18):

- Status: `400`
- Response body:
  ```json
  { "minimum_stock": ["A valid number is required."] }
  ```
- Only `minimum_stock` key in error — `name` was valid and not mentioned
- No `pk` field present
- Matches pattern: Yes — DRF decimal/float fields return `"A valid number is required."` for non-numeric strings

**Notes:** The `minimum_stock` field is typed as `number (double)` in the schema with a default of `0.0`. Passing a string value triggers DRF's standard numeric type validation. The same error message applies to other numeric fields of type `number (double)` such as those in the stock domain. The error key is the field name and the value is an array with a single descriptive string.
