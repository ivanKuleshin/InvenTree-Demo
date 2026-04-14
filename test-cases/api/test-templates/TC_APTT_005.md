# TC-APTT-005: DELETE /api/part/test-template/{id}/ removes a test template

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/part/test-template/{id}/`

**Preconditions:**

- A test template created via `POST /api/part/test-template/` (precondition setup step)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Create test template: `POST /api/part/test-template/` with `{"part": 74, "test_name": "DELETE-Target-Test"}`; record `pk`
3. Send `DELETE /api/part/test-template/{pk}/`
4. Verify response status code is `204` (no body)
5. Confirm deletion: `GET /api/part/test-template/{pk}/`
6. Verify that follow-up GET returns `404`

**Request:**

- Method: `DELETE`
- URL: `/api/part/test-template/{pk}/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `204 No Content`. Subsequent GET on the same pk returns `404`.

**Observed** (probed 2026-04-14):

- DELETE Status: `204`
- Follow-up GET Status: `404`
- Matches spec: Yes

**Notes:** None.
