# TC-APTT-001: GET /api/part/test-template/ returns paginated test template list

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/test-template/`

**Preconditions:**

- At least one part test template exists in the system
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/test-template/?limit=5` with `Authorization: Basic <base64>`
3. Verify response status code is `200`
4. Verify response body contains `count` as an integer > `0`
5. Verify `results` array contains up to `5` items
6. Verify each item contains fields: `pk`, `key`, `part`, `test_name`, `description`, `enabled`, `required`, `requires_value`, `requires_attachment`, `results`, `choices`
7. Verify `key` is a lowercase string with no spaces or hyphens

**Request:**

- Method: `GET`
- URL: `/api/part/test-template/?limit=5`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with paginated `PaginatedPartTestTemplateList`. All template fields present; `key` auto-derived from `test_name`.

**Observed** (probed 2026-04-14):

- Status: `200`
- count: `15`
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
- `choices` returns `""` (empty string), not `null`, when no choices are configured
- Matches spec: Yes

**Notes:** `key` is auto-computed from `test_name` (lowercase, stripped of spaces/hyphens). `choices` is always a string, never `null`.
