# TC-APPRICE-004: PATCH /api/part/internal-price/{id}/ updates the price of a price break

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/part/internal-price/{id}/`

**Preconditions:**

- An internal price break created via POST (precondition setup step)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Create price break: `POST /api/part/internal-price/` with `{"part": 73, "quantity": 10, "price": "55.00", "price_currency": "USD"}`; record `pk`
3. Send `PATCH /api/part/internal-price/{pk}/` with body below
4. Verify response status code is `200`
5. Verify `price` equals `49.99` (numeric)
6. Verify `quantity` and `part` are unchanged
7. Clean up: `DELETE /api/part/internal-price/{pk}/`

**Request:**

- Method: `PATCH`
- URL: `/api/part/internal-price/{pk}/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  { "price": "49.99" }
  ```

**Expected Result:** `200 OK` with updated price break. Only `price` changes; `quantity` and `part` unchanged.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 259,
    "part": 73,
    "quantity": 10,
    "price": 49.99,
    "price_currency": "USD"
  }
  ```
- Matches spec: Yes

**Notes:** None.
