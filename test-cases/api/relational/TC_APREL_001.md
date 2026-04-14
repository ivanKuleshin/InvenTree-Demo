# TC-APREL-001: POST /api/part/ with non-existent category FK returns 400

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- Category PK `99999999` does not exist (verify via `GET /api/part/category/99999999/` returning `404`)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/part/category/99999999/` and confirm response status is `404`
3. Send `POST /api/part/` with body `{"name": "TC-APREL-001-BadCategory", "category": 99999999}`
4. Verify response status code is `400`
5. Verify response body contains key `"category"`
6. Verify the value of `"category"` is an array containing a string that includes `"Invalid pk"` and `"object does not exist"`
7. Verify response body does NOT contain a `pk` field

**Request (step 3):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-APREL-001-BadCategory", "category": 99999999 }
  ```

**Expected Result:** An integer PK that does not reference an existing category object is rejected with `400`. The error is on the `category` field, not a `404` — DRF validates FK existence at the serializer level and returns `400`.

**Observed** (probed 2026-04-14):

- `GET /api/part/category/99999999/` status: `404`, body: `{"detail": "No PartCategory matches the given query."}`
- POST status: `400`
- Response body:
  ```json
  { "category": ["Invalid pk \"99999999\" - object does not exist."] }
  ```
- Matches spec: Yes

**Notes:** DRF FK validation always returns `400` (not `404`) when a referenced object does not exist. The error message embeds the submitted PK value — automation assertions should use substring matching on `"Invalid pk"` and `"object does not exist"` rather than the exact formatted string to avoid hardcoding PK values.
