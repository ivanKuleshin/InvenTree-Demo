---
component: api
suite: price-breaks
area: uniqueness-constraints
date: 2026-04-15
---

# Price Breaks Uniqueness Constraint Test Suite

## Overview

Test the `(part, quantity)` unique-together constraint on:
- `POST /api/part/internal-price/` 
- `POST /api/part/sale-price/`

Both endpoints enforce that for a given part, no two price breaks can have the same quantity value.

---

## TC-APPRICES-001: Internal Price — POST Unique (part, quantity) Returns 201

**Test ID:** `TC-APPRICES-001`  
**Priority:** High  
**Category:** Functional — API Constraints

### Given
- User is authenticated with `allaccess` role (full write permissions)
- Part ID 1197 exists
- No existing internal price break with (part=1197, quantity=250.0)

### When
- POST `/api/part/internal-price/` with:
  ```json
  {
    "part": 1197,
    "quantity": 250.0,
    "price": "9.99"
  }
  ```

### Then
- HTTP Status: **201 Created**
- Response body contains:
  ```json
  {
    "pk": <integer>,
    "part": 1197,
    "quantity": 250.0,
    "price": 9.99,
    "price_currency": "EUR"
  }
  ```
- Response includes a valid `pk` (primary key assigned)

### Observed
✓ **Status:** 201  
✓ **Response:** pk=285, all fields match request except EUR currency auto-filled  
✓ **Timestamp:** 2026-04-15

---

## TC-APPRICES-002: Internal Price — POST Duplicate (part, quantity) Returns 400

**Test ID:** `TC-APPRICES-002`  
**Priority:** High  
**Category:** Functional — API Constraints

### Given
- User is authenticated with `allaccess` role
- Part ID 1197 exists
- Internal price break with (part=1197, quantity=250.0) already exists (pk=285)

### When
- POST `/api/part/internal-price/` with same (part, quantity) but different price:
  ```json
  {
    "part": 1197,
    "quantity": 250.0,
    "price": "11.99"
  }
  ```

### Then
- HTTP Status: **400 Bad Request**
- Response body contains `non_field_errors` (not field-specific error):
  ```json
  {
    "non_field_errors": [
      "The fields part, quantity must make a unique set."
    ]
  }
  ```
- No new record is created
- Existing record (pk=285) is unchanged

### Observed
✓ **Status:** 400  
✓ **Error message:** "The fields part, quantity must make a unique set."  
✓ **Error location:** `non_field_errors`  
✓ **Timestamp:** 2026-04-15

---

## TC-APPRICES-003: Internal Price — Different Quantity Allowed (same part)

**Test ID:** `TC-APPRICES-003`  
**Priority:** High  
**Category:** Functional — API Constraints

### Given
- User is authenticated with `allaccess` role
- Part ID 1197 exists
- Internal price break with (part=1197, quantity=250.0) already exists

### When
- POST `/api/part/internal-price/` with same part but different quantity:
  ```json
  {
    "part": 1197,
    "quantity": 300.0,
    "price": "10.99"
  }
  ```

### Then
- HTTP Status: **201 Created**
- Response body contains:
  ```json
  {
    "pk": <integer>,
    "part": 1197,
    "quantity": 300.0,
    "price": 10.99,
    "price_currency": "EUR"
  }
  ```
- New record is created successfully

### Observed
✓ **Status:** 201  
✓ **Response:** pk=286, quantity=300.0  
✓ **Constraint:** Only `(part, quantity)` pair is unique, not `part` alone  
✓ **Timestamp:** 2026-04-15

---

## TC-APPRICES-004: Sale Price — POST Duplicate (part, quantity) Returns 400

**Test ID:** `TC-APPRICES-004`  
**Priority:** High  
**Category:** Functional — API Constraints

### Given
- User is authenticated with `allaccess` role
- Part ID 1211 exists and is salable
- Sale price break with (part=1211, quantity=250.0) already exists (pk=63)

### When
- POST `/api/part/sale-price/` with same (part, quantity) but different price:
  ```json
  {
    "part": 1211,
    "quantity": 250.0,
    "price": "21.99"
  }
  ```

### Then
- HTTP Status: **400 Bad Request**
- Response body contains `non_field_errors`:
  ```json
  {
    "non_field_errors": [
      "The fields part, quantity must make a unique set."
    ]
  }
  ```
- Identical error to internal price breaks
- No new record is created

### Observed
✓ **Status:** 400  
✓ **Error message:** "The fields part, quantity must make a unique set."  
✓ **Constraint applies equally to sale prices**  
✓ **Timestamp:** 2026-04-15

---

## TC-APPRICES-005: Sale Price — Different Quantity Allowed (same part)

**Test ID:** `TC-APPRICES-005`  
**Priority:** High  
**Category:** Functional — API Constraints

### Given
- User is authenticated with `allaccess` role
- Part ID 1211 exists
- Sale price break with (part=1211, quantity=250.0) already exists

### When
- POST `/api/part/sale-price/` with same part but different quantity:
  ```json
  {
    "part": 1211,
    "quantity": 300.0,
    "price": "20.99"
  }
  ```

### Then
- HTTP Status: **201 Created**
- Response body contains:
  ```json
  {
    "pk": <integer>,
    "part": 1211,
    "quantity": 300.0,
    "price": 20.99,
    "price_currency": "EUR"
  }
  ```

### Observed
✓ **Status:** 201  
✓ **Response:** pk=64, quantity=300.0  
✓ **Behavior mirrors internal prices**  
✓ **Timestamp:** 2026-04-15

---

## TC-APPRICES-006: Workaround — PATCH Internal Price to Update Quantity

**Test ID:** `TC-APPRICES-006`  
**Priority:** Medium  
**Category:** Functional — Workaround

### Given
- User is authenticated with `allaccess` role
- Internal price break pk=285 with (part=1197, quantity=250.0, price=9.99) exists

### When
- PATCH `/api/part/internal-price/285/` with:
  ```json
  {
    "quantity": 400.0
  }
  ```

### Then
- HTTP Status: **200 OK**
- Response body contains updated record:
  ```json
  {
    "pk": 285,
    "part": 1197,
    "quantity": 400.0,
    "price": 9.99,
    "price_currency": "EUR"
  }
  ```
- Quantity is changed from 250.0 to 400.0
- Price and part remain unchanged
- The (part=1197, quantity=250.0) slot is now available for new records

### Observed
✓ **Status:** 200  
✓ **Updated quantity:** 400.0  
✓ **Workaround valid:** PATCH allows quantity update, freeing the slot  
✓ **Timestamp:** 2026-04-15

---

## TC-APPRICES-007: Workaround — PATCH Sale Price to Update Quantity

**Test ID:** `TC-APPRICES-007`  
**Priority:** Medium  
**Category:** Functional — Workaround

### Given
- User is authenticated with `allaccess` role
- Sale price break pk=63 with (part=1211, quantity=250.0, price=19.99) exists

### When
- PATCH `/api/part/sale-price/63/` with:
  ```json
  {
    "quantity": 500.0
  }
  ```

### Then
- HTTP Status: **200 OK**
- Response body contains updated record:
  ```json
  {
    "pk": 63,
    "part": 1211,
    "quantity": 500.0,
    "price": 19.99,
    "price_currency": "EUR"
  }
  ```
- Quantity is changed from 250.0 to 500.0
- Price and part remain unchanged

### Observed
✓ **Status:** 200  
✓ **Updated quantity:** 500.0  
✓ **Workaround valid:** PATCH works for sale prices too  
✓ **Timestamp:** 2026-04-15

---

## TC-APPRICES-008: Verify PATCH Frees Slot for New Record

**Test ID:** `TC-APPRICES-008`  
**Priority:** Medium  
**Category:** Functional — Workaround Verification

### Given
- User is authenticated with `allaccess` role
- Sale price pk=63 has been PATCH'd from quantity 250.0 to 500.0
- The (part=1211, quantity=250.0) slot is now free

### When
- POST `/api/part/sale-price/` with:
  ```json
  {
    "part": 1211,
    "quantity": 250.0,
    "price": "25.00"
  }
  ```

### Then
- HTTP Status: **201 Created**
- Response body contains:
  ```json
  {
    "pk": 65,
    "part": 1211,
    "quantity": 250.0,
    "price": 25.0,
    "price_currency": "EUR"
  }
  ```
- New record is successfully created in the freed slot

### Observed
✓ **Status:** 201  
✓ **Response:** pk=65, quantity=250.0  
✓ **Verification:** Slot is freed after PATCH, allowing new creation  
✓ **Timestamp:** 2026-04-15

---

## Test Coverage Summary

| Test ID         | Endpoint                  | Constraint Aspect              | Result |
|-----------------|---------------------------|--------------------------------|--------|
| TC-APPRICES-001 | POST /api/part/internal-price/ | Unique creation              | ✓ PASS |
| TC-APPRICES-002 | POST /api/part/internal-price/ | Duplicate rejection (400)     | ✓ PASS |
| TC-APPRICES-003 | POST /api/part/internal-price/ | Different quantity allowed    | ✓ PASS |
| TC-APPRICES-004 | POST /api/part/sale-price/     | Duplicate rejection (400)     | ✓ PASS |
| TC-APPRICES-005 | POST /api/part/sale-price/     | Different quantity allowed    | ✓ PASS |
| TC-APPRICES-006 | PATCH /api/part/internal-price/ | Quantity update workaround   | ✓ PASS |
| TC-APPRICES-007 | PATCH /api/part/sale-price/    | Quantity update workaround   | ✓ PASS |
| TC-APPRICES-008 | POST /api/part/sale-price/     | Verify slot freed after PATCH | ✓ PASS |

---

## Key Documentation Points

### Constraint Details
- **Field set:** `(part, quantity)` must be unique
- **Applies to:** Both `/api/part/internal-price/` and `/api/part/sale-price/`
- **Error code:** HTTP 400 Bad Request
- **Error location:** `non_field_errors` (not field-specific)
- **Error message:** "The fields part, quantity must make a unique set."

### Workaround
If you need to update a quantity for an existing price break:
1. **Do NOT POST** a new record with the same part and quantity (will fail with 400)
2. **DO PATCH** the existing record with the new quantity value
3. After PATCH, the old quantity slot becomes available for new records

### Scope
- The constraint is composite: `quantity` alone can repeat (different parts), `part` alone can repeat (different quantities)
- Only the combination `(part, quantity)` is constrained
- The `price` and `price_currency` fields are not part of the constraint

---

## Appendix: Raw API Responses

### Duplicate Error Response (Internal Price)
```http
POST /api/part/internal-price/ HTTP/1.1
Content-Type: application/json
Authorization: Basic YWxsYWNjZXNzOm5vbGltaXRz

{
  "part": 1197,
  "quantity": 250.0,
  "price": "11.99"
}

---

HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "non_field_errors": [
    "The fields part, quantity must make a unique set."
  ]
}
```

### Duplicate Error Response (Sale Price)
```http
POST /api/part/sale-price/ HTTP/1.1
Content-Type: application/json
Authorization: Basic YWxsYWNjZXNzOm5vbGltaXRz

{
  "part": 1211,
  "quantity": 250.0,
  "price": "21.99"
}

---

HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "non_field_errors": [
    "The fields part, quantity must make a unique set."
  ]
}
```