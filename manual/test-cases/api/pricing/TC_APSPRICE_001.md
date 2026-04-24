# TC-APSPRICE-001: GET /api/part/sale-price/ returns paginated sale price break list

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/sale-price/`

**Preconditions:**

- At least one sale price break exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/sale-price/?limit=5`
3. Verify response status code is `200`
4. Verify `count` > `0`
5. Verify each item contains fields: `pk`, `part`, `quantity`, `price`, `price_currency`
6. Verify `price` is returned as a JSON number

**Request:**

- Method: `GET`
- URL: `/api/part/sale-price/?limit=5`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with paginated `PaginatedPartSalePriceList`.

**Observed** (probed 2026-04-14):

- Status: `200`
- count: `6`
- Response snippet:
  ```json
  {
    "pk": 1,
    "part": 113,
    "quantity": 1,
    "price": 6500,
    "price_currency": "USD"
  }
  ```
- `price` is a JSON number (not decimal string), same divergence as internal-price
- Matches spec: Partial

**Notes:** Same `price`-as-float divergence as `/api/part/internal-price/`.
