---
component: api
suite: price-breaks
date: 2026-04-15
---

# Price Breaks Uniqueness Constraint Probe Plan

## Coverage Area

Test the `(part, quantity)` uniqueness constraint for internal and sale price breaks.

## Endpoints to Probe

| Endpoint                          | Method | [MUTATING] | Purpose                              |
|-----------------------------------|--------|----------|--------------------------------------|
| `/api/part/internal-price/`       | GET    |          | List existing internal price breaks  |
| `/api/part/internal-price/`       | POST   | YES      | Create internal price break (test 1) |
| `/api/part/internal-price/`       | POST   | YES      | Create duplicate (test 2 - expect error) |
| `/api/part/sale-price/`           | GET    |          | List existing sale price breaks      |
| `/api/part/sale-price/`           | POST   | YES      | Create sale price break (test 1)    |
| `/api/part/sale-price/`           | POST   | YES      | Create duplicate (test 2 - expect error) |

## Test Cases to Produce

- `TC-APPRICES-001`: Internal price — POST unique (part, quantity) → 201
- `TC-APPRICES-002`: Internal price — POST duplicate (part, quantity) → 400 (with error detail)
- `TC-APPRICES-003`: Sale price — POST unique (part, quantity) → 201
- `TC-APPRICES-004`: Sale price — POST duplicate (part, quantity) → 400 (with error detail)
- `TC-APPRICES-005`: Internal price — PATCH existing to update quantity (valid workaround)
- `TC-APPRICES-006`: Sale price — PATCH existing to update quantity (valid workaround)

## Notes

- All mutating calls create test data; cleanup via DELETE is acceptable.
- Part ID 1 is used as the target (if it exists in demo).
- Quantity 100.0 is used as the test quantity (unlikely to exist in demo data).
- Auth: HTTP Basic with `allaccess:nolimits` (full write permissions).