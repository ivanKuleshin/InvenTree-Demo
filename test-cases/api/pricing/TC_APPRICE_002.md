# TC-APPRICE-002: GET /api/part/internal-price/{id}/ retrieves a single internal price break

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/internal-price/{id}/`

**Preconditions:**

- Internal price break with pk `1` exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/internal-price/1/`
3. Verify response status code is `200`
4. Verify `pk` equals `1`
5. Verify all fields present: `pk`, `part`, `quantity`, `price`, `price_currency`

**Request:**

- Method: `GET`
- URL: `/api/part/internal-price/1/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with single `PartInternalPrice` object.

**Observed** (probed 2026-04-14):

- Status: `200`
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
- Matches spec: Yes (field names match)

**Notes:** None.
