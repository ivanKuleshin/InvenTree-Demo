# TC-APPRICE-005: DELETE /api/part/internal-price/{id}/ removes a price break

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/part/internal-price/{id}/`

**Preconditions:**

- An internal price break created via POST (precondition setup step)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Create price break: `POST /api/part/internal-price/` with `{"part": 73, "quantity": 10, "price": "55.00", "price_currency": "USD"}`; record `pk`
3. Send `DELETE /api/part/internal-price/{pk}/`
4. Verify response status code is `204` (no body)
5. Confirm: `GET /api/part/internal-price/{pk}/` returns `404`

**Request:**

- Method: `DELETE`
- URL: `/api/part/internal-price/{pk}/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `204 No Content`. Subsequent GET returns `404`.

**Observed** (probed 2026-04-14):

- DELETE Status: `204`
- Follow-up GET Status: `404`
- Matches spec: Yes

**Notes:** None.
