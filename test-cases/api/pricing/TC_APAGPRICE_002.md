# TC-APAGPRICE-002: PATCH /api/part/{id}/pricing/ sets override_min and override_max

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/part/{id}/pricing/`

**Preconditions:**

- Part with pk `73` exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `PATCH /api/part/73/pricing/` with body below
3. Verify response status code is `200`
4. Verify `override_min` equals `70` in the response
5. Verify `overall_min` reflects the override (equals `70`, lower than calculated `internal_cost_min`)
6. Revert: `PATCH /api/part/73/pricing/` with `{"override_min": null, "override_max": null}`

**Request:**

- Method: `PATCH`
- URL: `/api/part/73/pricing/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "override_min": "70.00",
    "override_min_currency": "USD"
  }
  ```

**Expected Result:** `200 OK`. `overall_min` changes to the override value; `override_min` persisted; `scheduled_for_update: true`.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "currency": "USD",
    "updated": "2026-04-14 09:50",
    "scheduled_for_update": true,
    "internal_cost_min": 75.67,
    "override_min": 70,
    "override_min_currency": "USD",
    "override_max": null,
    "overall_min": 70,
    "overall_max": 75.67
  }
  ```
- `overall_min` changed from `75.67` to `70` after setting `override_min`
- Matches spec: Yes

**Notes:** Override takes precedence over all calculated values. `scheduled_for_update` is set `true` after every PATCH, indicating async recalculation is queued.
