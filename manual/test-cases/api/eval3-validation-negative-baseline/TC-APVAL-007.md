# TC-APVAL-007: POST /api/part/ with empty body returns 400 — name is required

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No specific data state required (request will be rejected before persistence)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with an empty JSON body `{}`
3. Verify response status code is `400`
4. Verify response body contains key `"name"`
5. Verify the value of `"name"` is an array containing the string `"This field is required."`
6. Verify response body does NOT contain a `pk` field (no part was created)

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {}
  ```

**Expected Result:** Server rejects the request with `400 Bad Request`. Response body identifies `name` as a required field using DRF's standard error structure.

**Observed** (probed 2026-04-18):

- Status: `400`
- Response body:
  ```json
  { "name": ["This field is required."] }
  ```
- No `pk` field present in the response body
- Matches pattern: Yes — consistent with DRF required-field validation (same pattern as TC-APVAL-001 for categories)

**Notes:** The `name` field is the only required writable field for part creation. All other fields (`category`, `description`, `IPN`, `active`, `assembly`, etc.) have server-side defaults or are nullable. The DRF error key matches the field name exactly, and the error value is always an array of strings.
