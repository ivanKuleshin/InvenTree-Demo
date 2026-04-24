# TC-APPRICE-001: GET /api/part/internal-price/ returns paginated internal price break list

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/internal-price/`

**Preconditions:**

- At least one internal price break exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/internal-price/?limit=5`
3. Verify response status code is `200`
4. Verify `count` is an integer > `0`
5. Verify each item contains fields: `pk`, `part`, `quantity`, `price`, `price_currency`
6. Verify `price` is returned as a JSON number (not a quoted decimal string)

**Request:**

- Method: `GET`
- URL: `/api/part/internal-price/?limit=5`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with paginated list. Each price break contains part PK, quantity threshold, price, and currency.

**Observed** (probed 2026-04-14):

- Status: `200`
- count: `258`
- Response snippet:
  ```json
  {
    "pk": 1,
    "part": 73,
    "quantity": 1,
    "price": 75.67,
    "price_currency": "USD"
  }
  ```
- `price` is a JSON number, not a decimal string
- Matches spec: Partial — schema documents `price` as `string (decimal)` but actual response is a float

**Notes:** `price` is returned as a float number (e.g., `75.67`), not a quoted decimal string as the schema specifies. Automation assertions should use numeric comparison, not string equality.
