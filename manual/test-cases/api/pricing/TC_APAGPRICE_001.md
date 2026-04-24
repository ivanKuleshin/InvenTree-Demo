# TC-APAGPRICE-001: GET /api/part/{id}/pricing/ retrieves the aggregate pricing summary

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/{id}/pricing/`

**Preconditions:**

- Part with pk `73` exists and has at least one internal price break
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/73/pricing/`
3. Verify response status code is `200`
4. Verify all computed fields are present: `currency`, `updated`, `scheduled_for_update`, `bom_cost_min`, `bom_cost_max`, `purchase_cost_min`, `purchase_cost_max`, `internal_cost_min`, `internal_cost_max`, `supplier_price_min`, `supplier_price_max`, `overall_min`, `overall_max`, `sale_price_min`, `sale_price_max`, `sale_history_min`, `sale_history_max`
5. Verify writable fields present: `override_min`, `override_max`, `override_min_currency`, `override_max_currency`
6. Verify `internal_cost_min` is non-null (part has internal price breaks)

**Request:**

- Method: `GET`
- URL: `/api/part/73/pricing/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with full `PartPricing` object. All cost breakdowns present; override fields are writable.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "currency": "USD",
    "updated": "2026-04-14 09:49",
    "scheduled_for_update": true,
    "internal_cost_min": 75.67,
    "internal_cost_max": 75.67,
    "override_min": null,
    "override_max": null,
    "overall_min": 75.67,
    "overall_max": 75.67,
    "sale_history_min": 10.22,
    "sale_history_max": 111
  }
  ```
- `scheduled_for_update: true` immediately after a prior PATCH — background recalc pending
- Matches spec: Yes

**Notes:** `scheduled_for_update` may be `true` when a background recalculation is queued. Most cost aggregates are `null` unless the part has supplier links or BOM entries.
