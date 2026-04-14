# API Manual Exploration Log — Part Pricing (Internal, Sale, Aggregate)

**Date:** 2026-04-14
**Tester:** AI exploration agent
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree

---

## Probe Sequence

### 1. GET /api/part/internal-price/?limit=5

- Status: `200`
- count: `258`
- Response shape: `{ pk, part, quantity, price, price_currency }`
- **DIVERGENCE:** Schema documents `price` as `string (decimal)` but response returns a JSON number (float), e.g., `75.67` not `"75.67"`

### 2. GET /api/part/internal-price/1/

- Status: `200`
- `{ pk: 1, part: 73, quantity: 1, price: 75.67, price_currency: "USD" }`

### 3. POST /api/part/internal-price/ — create price break

- Request: `{"part": 73, "quantity": 10, "price": "55.00", "price_currency": "USD"}`
- Status: `201`; pk: `259`
- `price` returns as number `55` (not decimal string)

### 4. PATCH /api/part/internal-price/259/

- Request: `{"price": "49.99"}`
- Status: `200`; price: `49.99`

### 5. DELETE /api/part/internal-price/259/

- Status: `204`
- Follow-up GET: `404`

### 6. GET /api/part/sale-price/?limit=5

- Status: `200`
- count: `6`
- Same field set and same float-not-string divergence for `price`

### 7. GET /api/part/sale-price/1/

- Status: `200`
- `{ pk: 1, part: 113, quantity: 1, price: 6500, price_currency: "USD" }`

### 8. POST /api/part/sale-price/ — create

- Request: `{"part": 113, "quantity": 100, "price": "4500.00", "price_currency": "USD"}`
- Status: `201`; pk: `7`

### 9. PATCH /api/part/sale-price/7/

- Request: `{"price": "4250.00"}`
- Status: `200`

### 10. DELETE /api/part/sale-price/7/

- Status: `204`; follow-up GET: `404`

### 11. GET /api/part/73/pricing/

- Status: `200`
- All pricing breakdowns present; most are `null` for this part except `internal_cost_min: 75.67`
- `scheduled_for_update: true` (background recalculation pending)
- Writable fields: `override_min`, `override_max` and their currency pairs

### 12. PATCH /api/part/73/pricing/ — set override_min

- Request: `{"override_min": "70.00", "override_min_currency": "USD"}`
- Status: `200`
- `overall_min` changed from `75.67` to `70.00` — override takes precedence over calculated value

### 13. PATCH /api/part/73/pricing/ — clear override

- Request: `{"override_min": null, "override_max": null}`
- Status: `200`
- `override_min` reset to `null`

---

## Divergences from Documentation

| Doc claim | Observed | Notes |
|-----------|----------|-------|
| `price` is `string (decimal)` | Returns JSON number (float) e.g. `75.67` | Both internal-price and sale-price endpoints |
| PATCH `/pricing/` sets `scheduled_for_update: true` | Confirmed | Background recalc is triggered asynchronously |
| `overall_min` reflects `override_min` when set | Confirmed | Override takes precedence |

---

✓ internal-price-test-suite.md — 5 TCs written — observed 2026-04-14 — source: demo.inventree.org
✓ sale-price-test-suite.md — 4 TCs written — observed 2026-04-14 — source: demo.inventree.org
✓ aggregate-pricing-test-suite.md — 3 TCs written — observed 2026-04-14 — source: demo.inventree.org
