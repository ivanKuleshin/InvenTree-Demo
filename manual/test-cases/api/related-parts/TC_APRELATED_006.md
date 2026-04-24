# TC-APRELATED-006: POST /api/part/related/ with duplicate pair returns 400

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/related/`

**Preconditions:**

- Parts `82` and `84` already have an existing relationship (pk `5`)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `POST /api/part/related/` with body below (pair already linked)
3. Verify response status code is `400`
4. Verify response body contains `non_field_errors` array with message `"Duplicate relationship already exists"`

**Request:**

- Method: `POST`
- URL: `/api/part/related/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "part_1": 82,
    "part_2": 84
  }
  ```

**Expected Result:** `400 Bad Request` with `non_field_errors` containing a duplicate-relationship message.

**Observed** (probed 2026-04-14):

- Status: `400`
- Response:
  ```json
  { "non_field_errors": ["Duplicate relationship already exists"] }
  ```
- Matches spec: Yes (validation enforced)

**Notes:** The error is in `non_field_errors` (not a field-specific error) since the uniqueness constraint is across the pair `(part_1, part_2)`.
