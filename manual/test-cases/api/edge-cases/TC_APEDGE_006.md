# TC-APEDGE-006: DELETE /api/part/{id}/ on a non-existent PK returns 404

**Type:** API
**Priority:** P3
**Endpoint:** `DELETE /api/part/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- Part PK `99999999` does not exist on the server

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/part/99999999/` and confirm status is `404` (pre-condition check)
3. Send `DELETE /api/part/99999999/`
4. Verify response status code is `404`
5. Verify response body contains key `"detail"`
6. Verify the value of `"detail"` equals `"No Part matches the given query."`

**Request (step 3):**

- Method: `DELETE`
- URL: `/api/part/99999999/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** DELETE on a non-existent PK returns `404`. The response body uses the same model-specific message as GET on a non-existent PK.

**Observed** (probed 2026-04-14):

- Status: `404`
- Response body:
  ```json
  { "detail": "No Part matches the given query." }
  ```
- Matches spec: Yes

**Notes:** DELETE is idempotent in REST semantics but InvenTree returns `404` on the second delete attempt (not `204`). This means automation that calls DELETE twice on the same PK should expect `404` on the second call. The `404` message is identical between GET and DELETE for the same resource type.
