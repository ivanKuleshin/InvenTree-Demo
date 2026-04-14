# TC-APFLT-008: GET /api/part/ with limit=-1 returns unpaginated raw array

**Type:** API
**Priority:** P3
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least one part exists on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?limit=-1` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Verify response body is a JSON array (not an object)
4. Verify the array length is greater than `0`
5. Verify the response does not contain fields `count`, `next`, or `previous`
6. Send `GET /api/part/?limit=0` and record array length as `LIMIT_ZERO_LENGTH`
7. Verify the array length from step 4 equals `LIMIT_ZERO_LENGTH`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?limit=-1`
- Headers: `Authorization: Token <token>`

**Request (step 6):**

- Method: `GET`
- URL: `/api/part/?limit=0`
- Headers: `Authorization: Token <token>`

**Expected Result:** `limit=-1` is treated identically to `limit=0`: response is a raw JSON array with no envelope, and no HTTP error is raised.

**Observed** (probed 2026-04-14):

- `limit=-1`: status `200`, raw JSON array, length `1442`
- `limit=0`: status `200`, raw JSON array, length `1442`
- Lengths are equal — behavior is identical
- Matches spec: Yes

**Notes:** The server silently normalizes negative `limit` to the unpaginated behavior. No validation error is returned.
