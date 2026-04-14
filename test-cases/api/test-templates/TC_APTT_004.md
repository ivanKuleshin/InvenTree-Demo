# TC-APTT-004: PATCH /api/part/test-template/{id}/ updates description and boolean flags

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/part/test-template/{id}/`

**Preconditions:**

- A test template created via `POST /api/part/test-template/` (precondition setup step)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Create test template: `POST /api/part/test-template/` with `{"part": 74, "test_name": "PATCH-Target-Test", "requires_attachment": false}`; record `pk`
3. Send `PATCH /api/part/test-template/{pk}/` with body below
4. Verify response status code is `200`
5. Verify `description` equals `"Updated probe description"`
6. Verify `requires_attachment` equals `true`
7. Verify unchanged fields (`test_name`, `required`, `requires_value`) retain their original values
8. Verify `results` field is present in the PATCH response (unlike POST)
9. Clean up: `DELETE /api/part/test-template/{pk}/`

**Request:**

- Method: `PATCH`
- URL: `/api/part/test-template/{pk}/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "description": "Updated probe description",
    "requires_attachment": true
  }
  ```

**Expected Result:** `200 OK` with updated template. Only specified fields change; others remain intact. `results` field present in response.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 16,
    "key": "tcapttcreatetest",
    "part": 74,
    "test_name": "TC-APTT-Create-Test",
    "description": "Updated probe description",
    "enabled": true,
    "required": true,
    "requires_value": true,
    "requires_attachment": true,
    "results": 0,
    "choices": ""
  }
  ```
- `results` field present (unlike POST response)
- Unchanged fields retained their values
- Matches spec: Yes

**Notes:** `results` appears in PATCH response but not POST response — automation should not assert its absence in PATCH.
