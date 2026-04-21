---
component: api
suite: price-breaks
date: 2026-04-15
source: demo.inventree.org
---

# Price Breaks Uniqueness Constraint — API Manual Testing Log

## Objective

Verify that the `(part, quantity)` unique-together constraint is enforced for both:
- `/api/part/internal-price/` (PartInternalPriceBreak)
- `/api/part/sale-price/` (PartSalePriceBreak)

Confirm error response, HTTP status code, and valid workaround (PATCH).

## Test Environment

- **Base URL:** `https://demo.inventree.org`
- **Auth:** HTTP Basic (`allaccess:nolimits`)
- **Date Observed:** 2026-04-15

## Test Data

- **Part for Internal Prices:** Part ID 1197 ("100Ω Resistor 0402", salable=false)
- **Part for Sale Prices:** Part ID 1211 ("aaaa", salable=true)
- **Test Quantity:** 250.0 (unlikely to conflict with existing data)

## Probe Results

### 1. Internal Price — Create First Record

**Request:**
```json
POST /api/part/internal-price/
Content-Type: application/json

{
  "part": 1197,
  "quantity": 250.0,
  "price": "9.99"
}
```

**Response (201 Created):**
```json
{
  "pk": 285,
  "part": 1197,
  "quantity": 250.0,
  "price": 9.99,
  "price_currency": "EUR"
}
```

**Observation:** Successfully created. PK 285 assigned.

---

### 2. Internal Price — Attempt Duplicate (same part, same quantity)

**Request:**
```json
POST /api/part/internal-price/
Content-Type: application/json

{
  "part": 1197,
  "quantity": 250.0,
  "price": "11.99"
}
```

**Response (400 Bad Request):**
```json
{
  "non_field_errors": [
    "The fields part, quantity must make a unique set."
  ]
}
```

**Observation:** 
- HTTP Status: **400**
- Error is in `non_field_errors` (not field-specific)
- Exact message: "The fields part, quantity must make a unique set."
- Constraint is enforced regardless of price difference

---

### 3. Internal Price — Create Different Quantity (same part, different qty)

**Request:**
```json
POST /api/part/internal-price/
Content-Type: application/json

{
  "part": 1197,
  "quantity": 300.0,
  "price": "10.99"
}
```

**Response (201 Created):**
```json
{
  "pk": 286,
  "part": 1197,
  "quantity": 300.0,
  "price": 10.99,
  "price_currency": "EUR"
}
```

**Observation:** Allowed. Only the `(part, quantity)` pair is constrained, not `part` alone.

---

### 4. Sale Price — Create First Record

**Request:**
```json
POST /api/part/sale-price/
Content-Type: application/json

{
  "part": 1211,
  "quantity": 250.0,
  "price": "19.99"
}
```

**Response (201 Created):**
```json
{
  "pk": 63,
  "part": 1211,
  "quantity": 250.0,
  "price": 19.99,
  "price_currency": "EUR"
}
```

**Observation:** Successfully created. PK 63 assigned.

---

### 5. Sale Price — Attempt Duplicate (same part, same quantity)

**Request:**
```json
POST /api/part/sale-price/
Content-Type: application/json

{
  "part": 1211,
  "quantity": 250.0,
  "price": "21.99"
}
```

**Response (400 Bad Request):**
```json
{
  "non_field_errors": [
    "The fields part, quantity must make a unique set."
  ]
}
```

**Observation:**
- HTTP Status: **400**
- Error identical to internal price constraint
- Same message: "The fields part, quantity must make a unique set."
- Applies equally to sale prices

---

### 6. Sale Price — Create Different Quantity (same part, different qty)

**Request:**
```json
POST /api/part/sale-price/
Content-Type: application/json

{
  "part": 1211,
  "quantity": 300.0,
  "price": "20.99"
}
```

**Response (201 Created):**
```json
{
  "pk": 64,
  "part": 1211,
  "quantity": 300.0,
  "price": 20.99,
  "price_currency": "EUR"
}
```

**Observation:** Allowed. Behavior mirrors internal prices.

---

### 7. Workaround Test 1: PATCH Internal Price

**Request:**
```json
PATCH /api/part/internal-price/285/
Content-Type: application/json

{
  "quantity": 400.0
}
```

**Response (200 OK):**
```json
{
  "pk": 285,
  "part": 1197,
  "quantity": 400.0,
  "price": 9.99,
  "price_currency": "EUR"
}
```

**Observation:** PATCH successfully updates quantity of existing record. This is a valid workaround to update a price break quantity.

---

### 8. Workaround Test 2: PATCH Sale Price

**Request:**
```json
PATCH /api/part/sale-price/63/
Content-Type: application/json

{
  "quantity": 500.0
}
```

**Response (200 OK):**
```json
{
  "pk": 63,
  "part": 1211,
  "quantity": 500.0,
  "price": 19.99,
  "price_currency": "EUR"
}
```

**Observation:** PATCH works identically for sale prices. After PATCH, the original quantity slot (250.0) is now available for creation.

---

### 9. Verification: POST to Previously-Occupied Quantity Slot

**Request (after PATCH pk 63 to qty 500.0):**
```json
POST /api/part/sale-price/
Content-Type: application/json

{
  "part": 1211,
  "quantity": 250.0,
  "price": "25.00"
}
```

**Response (201 Created):**
```json
{
  "pk": 65,
  "part": 1211,
  "quantity": 250.0,
  "price": 25.0,
  "price_currency": "EUR"
}
```

**Observation:** Confirmed. After PATCH freed the (part=1211, quantity=250.0) slot, a new record can be created with that quantity.

---

## Summary of Findings

| Question | Answer |
|----------|--------|
| Is `(part, quantity)` unique constraint documented? | **YES** — confirmed in error message: "The fields part, quantity must make a unique set." |
| Does it apply to internal price breaks? | **YES** — HTTP 400, error in `non_field_errors` |
| Does it apply to sale price breaks? | **YES** — HTTP 400, identical error message |
| What is the HTTP status on violation? | **400 Bad Request** |
| Is PATCH a valid workaround? | **YES** — PATCH updates quantity, freeing the slot for new records with different quantity |
| Can you POST after PATCH? | **YES** — Verified: PATCH qty to 500.0, then POST new record with qty 250.0 succeeds |

---

## Conclusion

✓ The `(part, quantity)` unique-together constraint is an **official, enforced API requirement** for both `/api/part/internal-price/` and `/api/part/sale-price/` endpoints.

✓ Violations return **HTTP 400** with `non_field_errors: "The fields part, quantity must make a unique set."`

✓ **Recommended workaround**: Use PATCH to update quantity of an existing record, rather than attempting to replace it with POST.

✓ Constraint applies identically to both internal and sale price breaks.