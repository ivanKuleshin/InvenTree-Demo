# TC-APTT-006: GET /api/part/test-template/?part={id} filters templates by part

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/test-template/`

**Preconditions:**

- Part with pk `74` has at least one test template
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/test-template/?part=74&limit=10`
3. Verify response status code is `200`
4. Verify all items in `results` array have `part` equal to `74`
5. Send `GET /api/part/test-template/?part=99999999&limit=5` with a non-existent part pk
6. Verify response status is `200` and `count` equals `0`

**Request:**

- Method: `GET`
- URL: `/api/part/test-template/?part=74&limit=10`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK`. Results scoped to the specified `part`. Filter with non-existent part returns empty list (not 404).

**Observed** (probed 2026-04-14):

- Status: `200`
- All results had `"part": 74`
- Non-existent part filter: `200` with `"count": 0, "results": []`
- Matches spec: Yes

**Notes:** Part filter returns empty paginated list for unknown part pk — does not 404.
