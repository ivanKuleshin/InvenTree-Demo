# TC-APFLT-015: GET /api/part/?trackable=true filters to trackable parts only

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Parts with `trackable=true` exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?trackable=true&limit=5` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Verify `count` is greater than `0`
4. Verify every part in `results` has `trackable` equal to `true`
5. Send `GET /api/part/?trackable=false&limit=5`
6. Verify response status code is `200`
7. Verify every part in `results` has `trackable` equal to `false`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?trackable=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `trackable=true` returns only parts that use serial number tracking. Every returned part has `trackable: true`.

**Observed** (probed 2026-04-14):

- `trackable=true`: status `200`, count `58`, first 3 results: `pk=1934 "AllFlagsPart-TC004-1776159658932-dk4zc" trackable: true`, `pk=2046 "AllFlagsPart-TC004-..." trackable: true`, `pk=1217 "AUTO_QA_TRACKABLE_PART" trackable: true`
- All returned parts have `trackable: true`
- Matches spec: Yes

**Notes:** Trackable parts require serial numbers for individual stock items. This flag enables serialized inventory tracking.
