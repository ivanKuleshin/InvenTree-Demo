# TC-APAGPRICE-003: PATCH /api/part/{id}/pricing/ clears override values with null

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/part/{id}/pricing/`

**Preconditions:**

- Part with pk `73` has an `override_min` set (e.g., prior TC-APAGPRICE-002 run)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. First set an override: `PATCH /api/part/73/pricing/` with `{"override_min": "70.00"}`
3. Send `PATCH /api/part/73/pricing/` with `{"override_min": null, "override_max": null}`
4. Verify response status code is `200`
5. Verify `override_min` is `null` in the response
6. Verify `overall_min` reverts to the calculated `internal_cost_min` value

**Request:**

- Method: `PATCH`
- URL: `/api/part/73/pricing/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "override_min": null,
    "override_max": null
  }
  ```

**Expected Result:** `200 OK`. `override_min` and `override_max` reset to `null`; `overall_min` reverts to the calculated value.

**Observed** (probed 2026-04-14):

- Status: `200`
- `override_min` set to `null` after the PATCH
- `overall_min` reverted to calculated value
- Matches spec: Yes

**Notes:** Sending `null` for override fields clears them. No special endpoint needed; a standard PATCH with null values is sufficient.
