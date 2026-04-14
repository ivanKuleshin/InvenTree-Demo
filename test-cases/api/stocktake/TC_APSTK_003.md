# TC-APSTK-003: POST /api/part/stocktake/ creates a stocktake record

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/stocktake/`

**Preconditions:**

- A part with an existing stock level exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `POST /api/part/stocktake/` with body below
3. Verify response status code is `201`
4. Verify `pk` is a new integer, `part` equals the requested part
5. Verify `date` is auto-set to today
6. Clean up: `DELETE /api/part/stocktake/{new_pk}/`

**Request:**

- Method: `POST`
- URL: `/api/part/stocktake/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "part": 907,
    "quantity": 5
  }
  ```

**Expected Result:** `201 Created` with the new stocktake record including read-only `date` set to the current date.

**Observed** (probed 2026-04-14):

- Status: `500`
- Response: `{"error": "TypeError", "error_class": "<class 'TypeError'>", "detail": "Error details can be found in the admin panel"}`
- Matches spec: **No** — server returns 500 instead of 201

**Notes:** [DIVERGENCE] POST /api/part/stocktake/ returns 500 TypeError on the demo instance as of 2026-04-14. Reproduced with multiple payloads (with and without optional fields). This appears to be a server-side bug in the demo environment. Automation implementing this TC should be prepared for environment-specific failure; retry when the demo is patched.
