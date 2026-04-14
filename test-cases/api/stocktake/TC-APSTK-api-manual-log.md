# API Manual Exploration Log — Part Stocktake

**Date:** 2026-04-14
**Tester:** AI exploration agent
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree

---

## Probe Sequence

### 1. GET /api/part/stocktake/?limit=5

- Status: `200`
- count: `849`
- Response shape: `{ pk, part, part_name, part_ipn, part_description, date, item_count, quantity, cost_min, cost_min_currency, cost_max, cost_max_currency }`
- `date` is read-only; `part_name`, `part_ipn`, `part_description` are read-only denormalized fields

### 2. GET /api/part/stocktake/849/

- Status: `200`
- Same field set as list, no nested objects

### 3. POST /api/part/stocktake/ — minimal payload

- Request: `{"part": 907, "quantity": 5}`
- Status: `500` — `{"error": "TypeError", "error_class": "<class 'TypeError'>", "detail": "Error details can be found in the admin panel"}`
- Retried with `{"part": 110, "quantity": 10, "cost_min": "10.00", "cost_min_currency": "USD"}` — same `500`
- **DIVERGENCE:** POST consistently returns 500; this is a server-side bug on the demo instance

### 4. PATCH /api/part/stocktake/849/

- Request: `{"quantity": 0}` (non-destructive — existing value was 0)
- Status: `200`
- **DIVERGENCE:** PATCH response includes `part_detail` nested object (full part info) not present in GET list or GET detail responses

### 5. DELETE /api/part/stocktake/99999999/

- Status: `404` — `{"detail": "No PartStocktake matches the given query."}`

---

## Divergences from Documentation

| Doc claim | Observed | Notes |
|-----------|----------|-------|
| POST /api/part/stocktake/ creates a stocktake record | Returns 500 TypeError | Server bug on demo; create is broken |
| GET /api/part/stocktake/{id}/ response shape | Does not include `part_detail` | PATCH response does include `part_detail`; inconsistency |
| DELETE returns 404 for non-existent | Confirmed | Error message: "No PartStocktake matches the given query." |

---

✓ stocktake-test-suite.md — 5 TCs written (POST TC marked [ASSUMED]) — observed 2026-04-14 — source: demo.inventree.org
