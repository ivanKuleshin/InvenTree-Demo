# TC-APVAL-002: POST /api/part/category/ with duplicate name under the same parent returns 400

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/category/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No category named "TC-APVAL-002-DupeCat" exists at the top level (parent=null)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/category/` with body `{"name": "TC-APVAL-002-DupeCat"}`
3. Verify response status code is `201` and record `pk` as `FIRST_PK`
4. Send a second `POST /api/part/category/` with the identical body `{"name": "TC-APVAL-002-DupeCat"}`
5. Verify response status code is `400`
6. Verify response body contains key `"non_field_errors"`
7. Verify the value of `"non_field_errors"` is an array containing the string `"Duplicate names cannot exist under the same parent"`
8. Verify response body does NOT contain a `pk` field
9. Send `DELETE /api/part/category/{FIRST_PK}/` to clean up
10. Verify DELETE response status code is `204`

**Request (step 4):**

- Method: `POST`
- URL: `/api/part/category/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-APVAL-002-DupeCat" }
  ```

**Expected Result:** The second POST with an identical name under the same parent (null = top-level) is rejected with `400`. The error appears under `non_field_errors`.

**Observed** (probed 2026-04-14):

- First POST status: `201`; `pk`: `285`
- Second POST status: `400`
- Response body:
  ```json
  { "non_field_errors": ["Duplicate names cannot exist under the same parent"] }
  ```
- Matches spec: Partial divergence — documentation shows `"The fields name, parent must make a unique set."` but live server returns `"Duplicate names cannot exist under the same parent"`

**Notes:** The actual error message differs from the documentation example. Automation assertions must use the live-observed string `"Duplicate names cannot exist under the same parent"` rather than the generic DRF unique-together message. The same-name category is allowed under a different parent — uniqueness is scoped to `(name, parent)` pair.
