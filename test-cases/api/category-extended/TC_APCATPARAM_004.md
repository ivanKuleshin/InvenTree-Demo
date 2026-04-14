# TC-APCATPARAM-004: PATCH /api/part/category/parameters/{id}/ updates the default_value

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/part/category/parameters/{id}/`

**Preconditions:**

- A category parameter assignment created via POST (precondition setup step)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Create: `POST /api/part/category/parameters/` with `{"category": 6, "template": 233, "default_value": "100"}`; record `pk`
3. Send `PATCH /api/part/category/parameters/{pk}/` with body below
4. Verify response status code is `200`
5. Verify `default_value` equals `"200"`
6. Verify `category` and `template` are unchanged
7. Clean up: `DELETE /api/part/category/parameters/{pk}/`

**Request:**

- Method: `PATCH`
- URL: `/api/part/category/parameters/{pk}/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  { "default_value": "200" }
  ```

**Expected Result:** `200 OK`. `default_value` updated; `category` and `template` unchanged.

**Observed** (probed 2026-04-14):

- Status: `200`
- `default_value` updated to `"200"`
- `category` and `template` unchanged
- Matches spec: Yes

**Notes:** None.
