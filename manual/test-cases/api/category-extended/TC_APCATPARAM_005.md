# TC-APCATPARAM-005: DELETE /api/part/category/parameters/{id}/ removes a category parameter assignment

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/part/category/parameters/{id}/`

**Preconditions:**

- A category parameter assignment created via POST (precondition setup step)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Create: `POST /api/part/category/parameters/` with `{"category": 6, "template": 233, "default_value": "100"}`; record `pk`
3. Send `DELETE /api/part/category/parameters/{pk}/`
4. Verify response status code is `204`
5. Confirm: `GET /api/part/category/parameters/{pk}/` returns `404`

**Request:**

- Method: `DELETE`
- URL: `/api/part/category/parameters/{pk}/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `204 No Content`. Subsequent GET returns `404`.

**Observed** (probed 2026-04-14):

- DELETE Status: `204`
- Follow-up GET Status: `404`
- Matches spec: Yes

**Notes:** None.
