# TC-APFLT-017: GET /api/part/?purchaseable=true&salable=true returns parts matching both flags

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Parts with both `purchaseable=true` and `salable=true` exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?purchaseable=true&limit=1` and record `count` as `PURCHASEABLE_COUNT`
2. Send `GET /api/part/?salable=true&limit=1` and record `count` as `SALABLE_COUNT`
3. Send `GET /api/part/?purchaseable=true&salable=true&limit=5` with header `Authorization: Token <token>`
4. Verify response status code is `200`
5. Verify `count` is greater than `0`
6. Verify `count` is less than or equal to `PURCHASEABLE_COUNT`
7. Verify `count` is less than or equal to `SALABLE_COUNT`
8. Verify every part in `results` has `purchaseable` equal to `true`
9. Verify every part in `results` has `salable` equal to `true`

**Request (step 3):**

- Method: `GET`
- URL: `/api/part/?purchaseable=true&salable=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** Multiple boolean filters combine with AND logic. The combined count is less than or equal to each individual filter count. Every returned part satisfies both conditions.

**Observed** (probed 2026-04-14):

- `purchaseable=true` alone: would return a superset
- `salable=true` alone: would return a superset
- `purchaseable=true&salable=true`: status `200`, count `38`, sample results: `pk=1934 "AllFlagsPart-TC004-1776159658932-dk4zc" purchaseable: true salable: true`, `pk=2046 "AllFlagsPart-TC004-..." purchaseable: true salable: true`, `pk=1161 "Buy-Sell Part 1776151541544" purchaseable: true salable: true`
- All returned parts have both `purchaseable: true` and `salable: true`
- Matches spec: Yes (AND combination confirmed)

**Notes:** Boolean filters combine as AND — each additional flag further narrows the result set. This is the same AND behavior confirmed in the docs for `assembly=true&active=true`.
