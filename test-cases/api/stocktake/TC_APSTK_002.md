# TC-APSTK-002: GET /api/part/stocktake/{id}/ retrieves a single stocktake record

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/stocktake/{id}/`

**Preconditions:**

- Stocktake record with pk `849` exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/stocktake/849/`
3. Verify response status code is `200`
4. Verify `pk` equals `849`, `part` equals `907`
5. Verify `date` is a date string
6. Verify `part_detail` is **absent** from the response (unlike PATCH response)

**Request:**

- Method: `GET`
- URL: `/api/part/stocktake/849/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with a flat `PartStocktake` object. No nested `part_detail`.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 849,
    "part": 907,
    "part_name": "Widget Board (assembled)",
    "part_ipn": "002.01-PCBA",
    "date": "2024-07-27",
    "item_count": 0,
    "quantity": 0,
    "cost_min": 0,
    "cost_min_currency": "USD"
  }
  ```
- `part_detail` absent (only appears in PATCH response)
- Matches spec: Yes

**Notes:** PATCH response includes `part_detail` nested object but GET does not — inconsistent response shape between verbs.
