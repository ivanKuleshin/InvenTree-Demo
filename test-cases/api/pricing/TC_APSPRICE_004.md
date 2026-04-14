# TC-APSPRICE-004: DELETE /api/part/sale-price/{id}/ removes a sale price break

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/part/sale-price/{id}/`

**Preconditions:**

- A sale price break created via POST (precondition setup step)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Create: `POST /api/part/sale-price/` with `{"part": 113, "quantity": 100, "price": "4500.00", "price_currency": "USD"}`; record `pk`
3. Send `DELETE /api/part/sale-price/{pk}/`
4. Verify response status code is `204`
5. Confirm: `GET /api/part/sale-price/{pk}/` returns `404`

**Request:**

- Method: `DELETE`
- URL: `/api/part/sale-price/{pk}/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `204 No Content`. Subsequent GET returns `404`.

**Observed** (probed 2026-04-14):

- DELETE Status: `204`
- Follow-up GET Status: `404`
- Matches spec: Yes

**Notes:** None.
