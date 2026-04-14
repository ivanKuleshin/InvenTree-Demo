# TC-APEDGE-002: POST /api/part/ with read-only credentials returns 403

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Read-only credentials `reader` / `readonly` are available
- The demo base URL is accessible

**Steps:**

1. Construct HTTP Basic authorization header using credentials `reader:readonly`
2. Send `POST /api/part/` with body `{"name": "TC-APEDGE-002-ReaderPost"}` and the reader authorization header
3. Verify response status code is `403`
4. Verify response body contains key `"detail"`
5. Verify the value of `"detail"` equals `"You do not have permission to perform this action."`
6. Verify response body does NOT contain a `pk` field (no part was created)
7. Send `GET /api/part/?limit=1` with the same reader authorization header
8. Verify response status code is `200` (read access is permitted)

**Request (step 2):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(reader:readonly)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-APEDGE-002-ReaderPost" }
  ```

**Expected Result:** The `reader` user is authenticated but lacks create permissions. POST returns `403 Forbidden`. GET with the same credentials returns `200`, confirming read access is working.

**Observed** (probed 2026-04-14):

- POST status: `403`
- POST body:
  ```json
  { "detail": "You do not have permission to perform this action." }
  ```
- GET status: `200`
- Matches spec: Yes

**Notes:** `403` (authenticated but unauthorized) is distinct from `401` (unauthenticated). The `reader` user is successfully authenticated — the rejection is purely permission-based. This confirms the RBAC model is enforced at the view level.
