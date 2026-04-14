# TC-APFLT-011: GET /api/part/ with limit exceeding total count returns all parts in one page

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Valid auth token available

**Steps:**

1. Send `GET /api/part/?limit=1` and record `count` as `TOTAL_COUNT`
2. Send `GET /api/part/?limit=5000` with header `Authorization: Token <token>`
3. Verify response status code is `200`
4. Verify response body is a JSON object (paginated envelope)
5. Verify `count` equals `TOTAL_COUNT`
6. Verify `results` length equals `TOTAL_COUNT`
7. Verify `next` is `null`

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?limit=5000`
- Headers: `Authorization: Token <token>`

**Expected Result:** When `limit` exceeds the total number of matching parts, the server returns a single-page envelope containing all parts. `next` is `null`. `results` length equals `count`.

**Observed** (probed 2026-04-14):

- Status: `200`
- `count`: `1453`
- `results` length: `1453`
- `next`: `null`
- All parts returned in a single response
- Matches spec: Yes

**Notes:** The demo dataset had `1453` parts at time of probing. The counts drift as other users create parts. Assertions should verify `results.length == count` rather than asserting a specific count value.
