# TC-APTT-002: GET /api/part/test-template/{id}/ retrieves a single test template

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/test-template/{id}/`

**Preconditions:**

- Test template with pk `8` exists in the system
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/test-template/8/` with `Authorization: Basic <base64>`
3. Verify response status code is `200`
4. Verify `pk` equals `8`
5. Verify all fields present: `pk`, `key`, `part`, `test_name`, `description`, `enabled`, `required`, `requires_value`, `requires_attachment`, `results`, `choices`
6. Verify `results` is an integer representing the count of test results recorded

**Request:**

- Method: `GET`
- URL: `/api/part/test-template/8/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with single `PartTestTemplate` object, all fields populated.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 8,
    "key": "bluepaintapplied",
    "part": 74,
    "test_name": "Blue Paint Applied",
    "description": "Blue paint must be applied to this specific part",
    "enabled": true,
    "required": true,
    "requires_value": false,
    "requires_attachment": false,
    "results": 0,
    "choices": ""
  }
  ```
- Matches spec: Yes

**Notes:** None.
