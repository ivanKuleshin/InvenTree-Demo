# TC-APEDGE-001: POST /api/part/ without authentication returns 403

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/`

**Preconditions:**

- No Authorization header is set
- The demo base URL is accessible

**Steps:**

1. Send `POST /api/part/` with body `{"name": "TC-APEDGE-001-NoAuth"}` and NO Authorization header
2. Verify response status code is `403`
3. Verify response body contains key `"detail"`
4. Verify the value of `"detail"` contains `"CSRF"` (CSRF enforcement on unauthenticated session requests)
5. Send `GET /api/part/?limit=1` with NO Authorization header
6. Verify response status code is `200` (public read is allowed on the demo instance)

**Request (step 1):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Content-Type: application/json` _(no Authorization header)_
- Body:
  ```json
  { "name": "TC-APEDGE-001-NoAuth" }
  ```

**Expected Result:** Unauthenticated POST returns `403`. Unauthenticated GET returns `200` (demo-specific public read access). No part is created.

**Observed** (probed 2026-04-14):

- POST status: `403`
- POST body: `{"detail": "CSRF Failed: Referer checking failed - no Referer."}`
- GET status: `200` (no auth required for reads on demo)
- Matches spec: Partial divergence — documentation states `401` for unauthenticated requests; actual response is `403` due to CSRF middleware firing before auth check on write operations

**Notes:** The demo uses Django's CSRF middleware. When no session cookie is present and no `X-CSRFToken` header is provided, the CSRF check fires first and returns `403` before the authentication check that would return `401`. Using Basic Auth header bypasses the CSRF check entirely. Automation tests using Basic Auth will not encounter this `403` path.
