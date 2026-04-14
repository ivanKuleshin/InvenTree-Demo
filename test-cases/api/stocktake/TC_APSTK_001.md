# TC-APSTK-001: GET /api/part/stocktake/ returns paginated stocktake list

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/stocktake/`

**Preconditions:**

- At least one stocktake record exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/stocktake/?limit=5`
3. Verify response status code is `200`
4. Verify `count` > `0`
5. Verify each item contains: `pk`, `part`, `part_name`, `part_ipn`, `part_description`, `date`, `item_count`, `quantity`, `cost_min`, `cost_min_currency`, `cost_max`, `cost_max_currency`
6. Verify `date` is a date string (e.g., `"2024-07-27"`)

**Request:**

- Method: `GET`
- URL: `/api/part/stocktake/?limit=5`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with paginated `PaginatedPartStocktakeList`. Each entry includes read-only denormalized part fields.

**Observed** (probed 2026-04-14):

- Status: `200`
- count: `849`
- Response snippet:
  ```json
  {
    "pk": 849,
    "part": 907,
    "part_name": "Widget Board (assembled)",
    "part_ipn": "002.01-PCBA",
    "part_description": "Assembled PCB for converting electricity into magic smoke",
    "date": "2024-07-27",
    "item_count": 0,
    "quantity": 0,
    "cost_min": 0,
    "cost_min_currency": "USD",
    "cost_max": 0,
    "cost_max_currency": "USD"
  }
  ```
- `part_name`, `part_ipn`, `part_description` are read-only denormalized fields
- Matches spec: Yes

**Notes:** None.
