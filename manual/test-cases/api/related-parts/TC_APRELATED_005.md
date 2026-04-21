# TC-APRELATED-005: DELETE /api/part/related/{id}/ removes a related part link

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/part/related/{id}/`

**Preconditions:**

- A related part entry created via POST (precondition setup step)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Create: `POST /api/part/related/` with `{"part_1": 73, "part_2": 75}`; record `pk`
3. Send `DELETE /api/part/related/{pk}/`
4. Verify response status code is `204`
5. Confirm: `GET /api/part/related/{pk}/` returns `404`

**Request:**

- Method: `DELETE`
- URL: `/api/part/related/{pk}/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `204 No Content`. Subsequent GET returns `404`.

**Observed** (probed 2026-04-14):

- DELETE Status: `204`
- Follow-up GET Status: `404`
- Matches spec: Yes

**Notes:** None.
