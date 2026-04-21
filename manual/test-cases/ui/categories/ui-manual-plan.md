# UI Manual Test Plan — Part Categories

## Coverage Areas

| Coverage Area | Page URL(s) | TC IDs to Produce | Auth Required | Mutating? |
|---|---|---|---|---|
| Category hierarchy — top-level list | `/part/category/` | TC-UI-CAT-001, TC-UI-CAT-002 | Yes | No |
| Category hierarchy — child category navigation | `/part/category/{id}/` | TC-UI-CAT-003, TC-UI-CAT-004 | Yes | No |
| Structural category display | `/part/category/{id}/` | TC-UI-CAT-005 | Yes | No |
| Category breadcrumb navigation | `/part/category/{id}/` | TC-UI-CAT-006 | Yes | No |
| Category filtering — cascade on/off | `/part/category/{id}/` | TC-UI-CAT-007, TC-UI-CAT-008 | Yes | No |
| Category filtering — user-configurable filters | `/part/category/{id}/` | TC-UI-CAT-009 | Yes | No |
| Parametric view toggle | `/part/category/{id}/` | TC-UI-CAT-010 | Yes | No |
| Parametric table — sorting by parameter column | `/part/category/{id}/` | TC-UI-CAT-011 | Yes | No |
| Parametric table — filter by parameter value | `/part/category/{id}/` | TC-UI-CAT-012 | Yes | No |
| Parametric table — multiple parameter filters (AND) | `/part/category/{id}/` | TC-UI-CAT-013 | Yes | No |
| Parametric table — two filters on same parameter | `/part/category/{id}/` | TC-UI-CAT-014 | Yes | No |
| Parametric table — unit-aware filtering | `/part/category/{id}/` | TC-UI-CAT-015 | Yes | No |
| Parametric table — remove filter | `/part/category/{id}/` | TC-UI-CAT-016 | Yes | No |

## Notes

- All pages require authentication as `admin`.
- No mutating flows planned — all test cases are read/navigation/filter operations.
