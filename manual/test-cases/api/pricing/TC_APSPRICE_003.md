# TC-APSPRICE-003: POST /api/part/sale-price/ creates a new sale price break

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/sale-price/`

**Preconditions:**

- Part with pk `113` exists (salable part)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `POST /api/part/sale-price/` with body below
3. Verify response status code is `201`
4. Verify `pk` is a new integer
5. Verify `part` equals `113`, `quantity` equals `100`, `price` equals `4500` (numeric)
6. Clean up: `DELETE /api/part/sale-price/{new_pk}/`

**Request:**

- Method: `POST`
- URL: `/api/part/sale-price/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "part": 113,
    "quantity": 100,
    "price": "4500.00",
    "price_currency": "USD"
  }
  ```

**Expected Result:** `201 Created` with the new sale price break.

**Observed** (probed 2026-04-14):

- Status: `201`
- pk: `7`
- Response snippet:
  ```json
  {
    "pk": 7,
    "part": 113,
    "quantity": 100,
    "price": 4500,
    "price_currency": "USD"
  }
  ```
- Matches spec: Yes

**Notes:** `price` returned as integer `4500`, not string `"4500.00"`.
