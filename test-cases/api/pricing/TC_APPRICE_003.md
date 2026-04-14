# TC-APPRICE-003: POST /api/part/internal-price/ creates a new internal price break

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/internal-price/`

**Preconditions:**

- Part with pk `73` exists and is active
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `POST /api/part/internal-price/` with body below
3. Verify response status code is `201`
4. Verify `pk` is a new integer
5. Verify `part` equals `73`, `quantity` equals `10`, `price` equals `55` (numeric)
6. Clean up: `DELETE /api/part/internal-price/{new_pk}/`

**Request:**

- Method: `POST`
- URL: `/api/part/internal-price/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "part": 73,
    "quantity": 10,
    "price": "55.00",
    "price_currency": "USD"
  }
  ```

**Expected Result:** `201 Created` with the new price break. `price` in response is a numeric float.

**Observed** (probed 2026-04-14):

- Status: `201`
- pk: `259`
- Response snippet:
  ```json
  {
    "pk": 259,
    "part": 73,
    "quantity": 10,
    "price": 55,
    "price_currency": "USD"
  }
  ```
- Matches spec: Yes (field names) / Partial (`price` returned as integer `55`, not `"55.00"`)

**Notes:** `price` is sent as decimal string `"55.00"` but returned as numeric `55`. Trailing zeros are dropped.
