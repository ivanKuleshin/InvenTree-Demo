---
source: https://demo.inventree.org/api/part/internal-price/ and /api/part/sale-price/
component: api
topic: (part, quantity) Uniqueness Constraint for Price Breaks
fetched: 2026-04-15
---

# Price Breaks: (part, quantity) Uniqueness Constraint

## Overview

Both internal price breaks (`/api/part/internal-price/`) and sale price breaks (`/api/part/sale-price/`) enforce a unique constraint on the composite key `(part, quantity)`.

This constraint prevents duplicate quantity levels for the same part within a single price break table.

---

## Constraint Definition

| Aspect                    | Value                                                    |
|---------------------------|----------------------------------------------------------|
| **Field Set**             | `(part, quantity)`                                       |
| **Constraint Type**       | Unique-together (composite primary key uniqueness)      |
| **Applied To**            | `POST /api/part/internal-price/` (create)              |
| **Applied To**            | `POST /api/part/sale-price/` (create)                  |
| **HTTP Status on Breach** | **400 Bad Request**                                     |
| **Error Location**        | `non_field_errors` (not field-specific)                |
| **Error Message**         | "The fields part, quantity must make a unique set."     |

---

## What This Means

### Valid Combinations
You CAN create multiple price breaks for:
- **Same part, different quantities:** `(part=1, qty=100)` and `(part=1, qty=200)` are allowed
- **Same quantity, different parts:** `(part=1, qty=100)` and `(part=2, qty=100)` are allowed

### Invalid Combinations
You CANNOT create:
- **Same part, same quantity:** If `(part=1, qty=100)` exists, creating another `(part=1, qty=100)` will fail with HTTP 400

---

## Example: Duplicate POST Attempt

### Request
```http
POST /api/part/internal-price/
Content-Type: application/json

{
  "part": 1197,
  "quantity": 250.0,
  "price": "11.99"
}
```

### Response (if quantity 250.0 already exists for part 1197)
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "non_field_errors": [
    "The fields part, quantity must make a unique set."
  ]
}
```

---

## Workaround: Update via PATCH

If you need to change the quantity of an existing price break, use **PATCH** instead of POST:

### Problem Scenario
1. Internal price pk=285: `(part=1197, qty=250.0, price=9.99)`
2. You want to change the quantity to 300.0
3. POSTing `(part=1197, qty=300.0)` will work, but the old record (pk=285) still exists at qty=250.0

### Solution: PATCH
```http
PATCH /api/part/internal-price/285/
Content-Type: application/json

{
  "quantity": 300.0
}
```

### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "pk": 285,
  "part": 1197,
  "quantity": 300.0,
  "price": 9.99,
  "price_currency": "EUR"
}
```

**Result:** The record is updated in-place. The old quantity slot (250.0) is now freed and available for new records.

---

## Applies to Both Endpoints

The constraint applies **identically** to:
- `POST /api/part/internal-price/` — PartInternalPriceBreak
- `POST /api/part/sale-price/` — PartSalePriceBreak

Both return the same error message and HTTP status on duplicate attempts.

---

## Implementation Details

### Fields Included in Constraint
- `part` (integer) — The part ID
- `quantity` (number/double) — The quantity threshold

### Fields NOT Included
- `price` (decimal) — Can be anything; not part of uniqueness
- `price_currency` (string) — Can be anything; not part of uniqueness
- `pk` (integer, read-only) — Auto-assigned

---

## Django Model Meta

This constraint likely corresponds to the following Django model Meta configuration:

```python
class PartInternalPriceBreak(models.Model):
    part = models.ForeignKey(Part, ...)
    quantity = models.DecimalField(...)
    price = models.DecimalField(...)
    price_currency = models.CharField(...)

    class Meta:
        unique_together = ('part', 'quantity')
```

(Similarly for `PartSalePriceBreak`)

---

## Test Coverage

See [test-cases/api/price-breaks/price-breaks-uniqueness-test-suite.md](../../test-cases/api/price-breaks/price-breaks-uniqueness-test-suite.md) for comprehensive test cases:

- **TC-APPRICES-001:** Internal price — unique creation (201)
- **TC-APPRICES-002:** Internal price — duplicate rejection (400)
- **TC-APPRICES-003:** Internal price — different quantity allowed (201)
- **TC-APPRICES-004:** Sale price — duplicate rejection (400)
- **TC-APPRICES-005:** Sale price — different quantity allowed (201)
- **TC-APPRICES-006:** Workaround — PATCH internal price
- **TC-APPRICES-007:** Workaround — PATCH sale price
- **TC-APPRICES-008:** Verify slot freed after PATCH

All tests **PASSED** as of 2026-04-15 on demo.inventree.org.

---

## References

- [PartInternalPrice Schema](schemas/part-internal-price.md)
- [PartSalePrice Schema](schemas/part-sale-price.md)
- [POST /api/part/internal-price/](endpoints/part-internal-price-create-POST.md)
- [POST /api/part/sale-price/](endpoints/part-sale-price-create-POST.md)
- [PATCH /api/part/internal-price/{id}/](endpoints/part-internal-price-detail-PATCH.md)
- [PATCH /api/part/sale-price/{id}/](endpoints/part-sale-price-detail-PATCH.md)

---

## Summary

The `(part, quantity)` unique-together constraint is:
- ✓ **Documented** (via error message and observed behavior)
- ✓ **Enforced equally** on internal and sale price breaks
- ✓ **Returns HTTP 400** on violation
- ✓ **Solvable via PATCH** for quantity updates
- ✓ **Tested and verified** on 2026-04-15