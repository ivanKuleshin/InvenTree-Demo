# TC-APFLT-007: GET /api/part/ with limit=0 returns unpaginated raw array

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least one part exists on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?limit=0` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Verify response body is a JSON array (not an object)
4. Verify the array length is greater than `0`
5. Verify the response does not contain fields `count`, `next`, or `previous`
6. Verify each element in the array contains at least the fields `pk`, `name`, and `active`

**Request:**

- Method: `GET`
- URL: `/api/part/?limit=0`
- Headers: `Authorization: Token <token>`

**Expected Result:** Response is a raw JSON array of all matching Part objects with no pagination envelope. Fields `count`, `next`, and `previous` are absent.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response type: raw JSON array
- Array length: `1442`
- No `count`, `next`, or `previous` fields present
- First element: `{ "pk": 82, "name": "1551ABK", "active": true, ... }`
- Matches spec: Yes

**Notes:** `limit=0` explicitly bypasses pagination. This is distinct from a paginated response with zero results. The full dataset is returned in one response body.
