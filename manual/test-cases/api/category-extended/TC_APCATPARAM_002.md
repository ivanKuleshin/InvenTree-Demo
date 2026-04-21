# TC-APCATPARAM-002: GET /api/part/category/parameters/{id}/ retrieves a single assignment

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/category/parameters/{id}/`

**Preconditions:**

- Category parameter assignment with pk `1` exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/category/parameters/1/`
3. Verify response status code is `200`
4. Verify `pk` equals `1`, `category` equals `5`, `template` equals `233`
5. Verify `default_value` is a string

**Request:**

- Method: `GET`
- URL: `/api/part/category/parameters/1/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with single `CategoryParameterTemplate` including nested detail objects.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response: `{"pk": 1, "category": 5, "template": 233, "default_value": ""}` (plus detail objects)
- Matches spec: Yes

**Notes:** None.
