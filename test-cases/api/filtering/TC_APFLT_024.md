# TC-APFLT-024: GET /api/part/?ordering=-in_stock sorts results by stock quantity descending

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least 3 parts with different `in_stock` values exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?ordering=-in_stock&limit=3` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Record `in_stock` values from `results` as `STOCK_VALUES`
4. Verify `STOCK_VALUES[0]` is greater than or equal to `STOCK_VALUES[1]`
5. Verify `STOCK_VALUES[1]` is greater than or equal to `STOCK_VALUES[2]`
6. Send `GET /api/part/?ordering=in_stock&limit=3`
7. Verify response status code is `200`
8. Record `in_stock` values as `STOCK_VALUES_ASC`
9. Verify `STOCK_VALUES_ASC[0]` is less than or equal to `STOCK_VALUES_ASC[1]`
10. Verify `STOCK_VALUES_ASC[1]` is less than or equal to `STOCK_VALUES_ASC[2]`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?ordering=-in_stock&limit=3`
- Headers: `Authorization: Token <token>`

**Request (step 6):**

- Method: `GET`
- URL: `/api/part/?ordering=in_stock&limit=3`
- Headers: `Authorization: Token <token>`

**Expected Result:** `ordering=-in_stock` returns highest-stock parts first. `ordering=in_stock` returns lowest-stock parts first. Results are in monotonically descending or ascending order respectively.

**Observed** (probed 2026-04-14):

- `ordering=-in_stock`: status `200`, count `1453`, results: `pk=23 "R_4.7K_0603_0.1%" in_stock: 19625`, `pk=46 "R_220K_0402_1%" in_stock: 18090`, `pk=38 "R_56K_0603_1%" in_stock: 15803`
- `19625 >= 18090 >= 15803` — descending order confirmed
- Matches spec: Yes

**Notes:** Stock quantities on the demo change as other users create or consume stock. Use relative ordering assertions (`a >= b >= c`) rather than asserting specific stock values.
