# TC-APFLT-010: GET /api/part/ with offset=-1 is treated as offset=0

**Type:** API
**Priority:** P3
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least 5 parts exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?limit=5&offset=0` and record the `pk` values of all results as `PAGE_0_PKS`
2. Send `GET /api/part/?limit=5&offset=-1` with header `Authorization: Token <token>`
3. Verify response status code is `200`
4. Verify response body is a JSON object (paginated envelope)
5. Verify `previous` is `null`
6. Record `pk` values of all results as `NEG_OFFSET_PKS`
7. Verify `NEG_OFFSET_PKS` equals `PAGE_0_PKS` (same first page returned)

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?limit=5&offset=-1`
- Headers: `Authorization: Token <token>`

**Expected Result:** Negative `offset` is silently normalized to `0`. The response returns the first page of results identical to `offset=0`. No HTTP error is raised.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response type: paginated envelope
- `previous`: `null`
- First result `pk`: `82` — same as `offset=0` response
- `count`: `1442`
- Matches spec: Yes

**Notes:** The server treats any negative offset as zero without raising a validation error. This is a lenient input normalization behavior.
