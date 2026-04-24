# TC-APTT-003: POST /api/part/test-template/ creates a new test template

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/test-template/`

**Preconditions:**

- A part (e.g., pk `74`) exists and is active
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `POST /api/part/test-template/` with body below
3. Verify response status code is `201`
4. Verify `pk` is a new integer
5. Verify `key` is auto-generated as lowercase version of `test_name` with spaces/hyphens stripped
6. Verify `enabled` defaults to `true` when not provided
7. Verify `results` field is **absent** from the response
8. Clean up: `DELETE /api/part/test-template/{new_pk}/`

**Request:**

- Method: `POST`
- URL: `/api/part/test-template/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "part": 74,
    "test_name": "TC-APTT-Create-Test",
    "description": "Probe test template creation",
    "required": true,
    "requires_value": true,
    "requires_attachment": false
  }
  ```

**Expected Result:** `201 Created` with the new template object. `key` is auto-derived. `enabled` defaults to `true`.

**Observed** (probed 2026-04-14):

- Status: `201`
- pk: `16`
- Response snippet:
  ```json
  {
    "pk": 16,
    "key": "tcapttcreatetest",
    "part": 74,
    "test_name": "TC-APTT-Create-Test",
    "description": "Probe test template creation",
    "enabled": true,
    "required": true,
    "requires_value": true,
    "requires_attachment": false,
    "choices": ""
  }
  ```
- `results` field **absent** from POST response (present in GET/PATCH responses)
- `key` computed as `"tcapttcreatetest"` (hyphen stripped, lowercased)
- Matches spec: Partial — `results` field missing from create response is undocumented behavior

**Notes:** The `results` read-only count field is absent from the POST response body but appears in subsequent GET and PATCH calls. Automation assertions should not assert `results` on POST responses.
