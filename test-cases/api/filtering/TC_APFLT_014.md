# TC-APFLT-014: GET /api/part/?virtual=true filters to virtual parts only

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Parts with `virtual=true` exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?virtual=true&limit=5` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Verify `count` is greater than `0`
4. Verify every part in `results` has `virtual` equal to `true`
5. Send `GET /api/part/?virtual=false&limit=5`
6. Verify response status code is `200`
7. Verify every part in `results` has `virtual` equal to `false`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?virtual=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `virtual=true` returns only virtual parts. `virtual=false` returns only non-virtual parts. Every returned part's `virtual` field matches the filter value.

**Observed** (probed 2026-04-14):

- `virtual=true`: status `200`, count `33`, first 3 results: `pk=1934 "AllFlagsPart-TC004-1776159658932-dk4zc" virtual: true`, `pk=2046 "AllFlagsPart-TC004-1776161037644-f0q1k" virtual: true`, `pk=914 "CRM license" virtual: true`
- All returned parts have `virtual: true`
- Matches spec: Yes

**Notes:** Virtual parts represent services or non-physical items (e.g., software licenses). They have no physical stock.
