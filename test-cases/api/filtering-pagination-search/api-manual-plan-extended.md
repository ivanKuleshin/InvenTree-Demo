---
created: 2026-04-14
source: docs/api/
status: draft — awaiting user confirmation
---

# API Manual Test Plan — Extended Coverage

Builds on existing suites (parts CRUD, categories CRUD, filtering, validation, relational, edge cases).
Targets five functional areas with zero existing coverage.

## Coverage Table

| Area | Suite dir | Endpoints | Methods | TC prefix | Mutating? |
|------|-----------|-----------|---------|-----------|-----------|
| Part Test Templates | `api/test-templates/` | `/api/part/test-template/` | GET, POST, PATCH, DELETE | TC-APTT | **[MUTATING]** POST, PATCH, DELETE |
| Part Internal Pricing | `api/pricing/` | `/api/part/internal-price/`, `/api/part/internal-price/{id}/` | GET, POST, PATCH, DELETE | TC-APPRICE | **[MUTATING]** POST, PATCH, DELETE |
| Part Sale Pricing | `api/pricing/` | `/api/part/sale-price/`, `/api/part/sale-price/{id}/` | GET, POST, PATCH, DELETE | TC-APSPRICE | **[MUTATING]** POST, PATCH, DELETE |
| Part Aggregate Pricing | `api/pricing/` | `/api/part/{id}/pricing/` | GET, PATCH | TC-APAGPRICE | **[MUTATING]** PATCH |
| Part Stocktake | `api/stocktake/` | `/api/part/stocktake/`, `/api/part/stocktake/{id}/`, `/api/part/stocktake/generate/` | GET, POST, PATCH, DELETE | TC-APSTK | **[MUTATING]** POST, PATCH, DELETE |
| Part Related Parts | `api/related-parts/` | `/api/part/related/`, `/api/part/related/{id}/` | GET, POST, PATCH, DELETE | TC-APRELATED | **[MUTATING]** POST, PATCH, DELETE |
| Category Tree | `api/category-extended/` | `/api/part/category/tree/` | GET | TC-APCTREE | read-only |
| Category Parameters | `api/category-extended/` | `/api/part/category/parameters/`, `/api/part/category/parameters/{id}/` | GET, POST, PATCH, DELETE | TC-APCATPARAM | **[MUTATING]** POST, PATCH, DELETE |

## Planned TC count per area

| Area | TCs planned |
|------|-------------|
| Part Test Templates | 6 (list, get, create, patch, delete, filter-by-part) |
| Part Internal Pricing | 5 (list, get, create, patch, delete) |
| Part Sale Pricing | 4 (list, get, create, delete) |
| Part Aggregate Pricing | 3 (get, patch-override, patch-trigger-update) |
| Part Stocktake | 6 (list, get, create, patch, delete, generate) |
| Part Related Parts | 5 (list, get, create, patch, delete) |
| Category Tree | 2 (list, ordering) |
| Category Parameters | 5 (list, get, create, patch, delete) |
| **Total** | **36** |

## Mutating call summary

All POST/PATCH/DELETE calls follow test-data lifecycle:
1. Create entity (POST) → record PK
2. Update entity (PATCH) → verify field change
3. Delete entity (DELETE) → verify 204 + confirm 404 on re-GET
