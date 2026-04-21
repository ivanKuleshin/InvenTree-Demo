# TC-APFLT-016: GET /api/part/?has_stock=true returns only parts with stock; has_stock=false returns the complement

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Parts with and without stock both exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?has_stock=true&limit=5` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Verify `count` is greater than `0`
4. Verify every part in `results` has `in_stock` greater than `0`
5. Record `count` as `HAS_STOCK_COUNT`
6. Send `GET /api/part/?has_stock=false&limit=5`
7. Verify response status code is `200`
8. Verify every part in `results` has `in_stock` equal to `0`
9. Record `count` as `NO_STOCK_COUNT`
10. Send `GET /api/part/?limit=1` and record `count` as `TOTAL_COUNT`
11. Verify `HAS_STOCK_COUNT + NO_STOCK_COUNT` equals `TOTAL_COUNT`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?has_stock=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `has_stock=true` returns parts with at least one unit in stock (`in_stock > 0`). `has_stock=false` returns parts with zero stock. The two counts sum to the total unfiltered count.

**Observed** (probed 2026-04-14):

- `has_stock=true`: status `200`, count `404`, sample results: `pk=82 "1551ABK" in_stock: 1867`, `pk=84 "1551ACLR" in_stock: 100`, `pk=83 "1551AGY" in_stock: 123`
- `has_stock=false`: status `200`, count `1038`, sample results: `pk=86 "1553WDBK" in_stock: 0`, `pk=2063 "5mm Red LED" in_stock: 0`
- `404 + 1038 = 1442` — counts sum to total
- Matches spec: Yes

**Notes:** `has_stock` checks the `in_stock` field (direct stock items). Parts with only variant stock but no direct stock items may appear in `has_stock=false` depending on server configuration. Automation assertions should check the `in_stock` field value on each returned part rather than relying on specific count values.
