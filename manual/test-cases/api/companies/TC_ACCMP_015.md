# TC-ACCMP-015: POST /api/company/ without authentication returns 403

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/company/`

**Preconditions:**

- No Authorization header is set
- The demo base URL is accessible

**Steps:**

1. Send `POST /api/company/` with body `{"name": "TC-ACCMP-015-NoAuth"}` and NO Authorization header
2. Verify response status code is `403`
3. Verify response body contains key `"detail"` with a message relating to authentication or CSRF
4. Send `GET /api/company/?limit=1` with NO Authorization header
5. Verify response status code is `403` (unlike `/api/part/`, the company endpoint requires authentication even for reads)
6. Send `PUT /api/company/1/` with body `{"name": "NoAuth"}` and NO Authorization header
7. Verify response status code is `403`
8. Send `DELETE /api/company/1/` with NO Authorization header
9. Verify response status code is `403`

**Request (step 1):**

- Method: `POST`
- URL: `/api/company/`
- Headers: `Content-Type: application/json` _(no Authorization header)_
- Body:
  ```json
  { "name": "TC-ACCMP-015-NoAuth" }
  ```

**Expected Result:** All mutating and read operations on `/api/company/` without an Authorization header return `403`. No company is created or modified.

**Observed** (probed 2026-04-17):

- POST without auth: status `403`; body: `{"detail": "CSRF Failed: Referer checking failed - no Referer."}`
- GET without auth: status `403` (company endpoint is not publicly readable on the demo, unlike parts)
- PUT without auth: status `403`
- DELETE without auth: status `403`
- Matches spec: Partial divergence — documentation states `401` for unauthenticated requests; actual response is `403` due to CSRF middleware firing before auth check on write operations. Read operations also return `403` rather than `401` on the company endpoint.

**Notes:** The demo uses Django's CSRF middleware. When no session cookie is present and no `X-CSRFToken` header is provided, the CSRF check fires first and returns `403` before the authentication check. Using Basic Auth header bypasses the CSRF check entirely. The company endpoint differs from `/api/part/` in that even GET requests require authentication on the demo instance.
