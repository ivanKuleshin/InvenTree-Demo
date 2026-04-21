# TC-APFLT-012: GET /api/part/?search= with empty string returns full unfiltered result set

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least one part exists on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?limit=3` and record `count` as `UNFILTERED_COUNT`
2. Send `GET /api/part/?search=&limit=3` with header `Authorization: Token <token>`
3. Verify response status code is `200`
4. Verify `count` equals `UNFILTERED_COUNT`

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?search=&limit=3`
- Headers: `Authorization: Token <token>`

**Expected Result:** An empty search string is treated as no search constraint. The response returns the same total count as an unfiltered request.

**Observed** (probed 2026-04-14):

- `limit=3` (no search): `count: 1453`
- `search=&limit=3`: `count: 1453`
- Counts are equal — empty search string is a no-op
- Matches spec: Yes

**Notes:** The search parameter is optional. Passing an empty string has the same effect as omitting it. Automation should verify that `search=` does not reduce the result set.
