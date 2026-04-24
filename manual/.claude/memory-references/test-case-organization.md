# Test Case Organization

## Directory Structure

```
test-cases/
├── ui/
│   ├── auth/
│   ├── parts/
│   ├── part-detail-tabs/
│   ├── categories/
│   └── parts/negative/
└── api/
    ├── categories/
    ├── parts/
    ├── edge-cases/
    ├── filtering/
    ├── validation/
    ├── relational/
    ├── category-extended/
    ├── price-breaks/
    ├── stocktake/
    └── test-templates/
```

## UI Test Cases (`test-cases/ui/`)

Organized by feature area:

| Folder | Purpose | Example |
|--------|---------|---------|
| **auth/** | Login, role-based access control | admin login, read-only user access |
| **parts/** | Part creation, detail view, searching | create part, view part details, search |
| **part-detail-tabs/** | Individual tabs in part detail | Stock, BOM, Variants, Revisions, Parameters, Attachments, Test Templates |
| **categories/** | Category hierarchy, parametric filtering | create category, filter by parameters |
| **parts/negative/** | Invalid inputs, boundary conditions, error states | duplicate IPN, inactive part restrictions |

### Test ID Pattern

`TC_UI_<AREA>_<NUMBER>`

Examples:
- `TC_UI_PC_001` — UI Part Creation test 1
- `TC_UI_AUTH_001` — UI Authentication test 1
- `TC_UI_CAT_001` — UI Categories test 1

## API Test Cases (`test-cases/api/`)

Organized by test type:

| Folder | Purpose | Example |
|--------|---------|---------|
| **categories/** | Part categories CRUD | create category, list categories, delete category |
| **parts/** | Part CRUD operations | create part, update part, list parts |
| **edge-cases/** | Invalid payloads, unauthorized access, conflicts | missing required field, 401 unauthorized |
| **filtering/** | Filtering, pagination, search on list endpoints | filter by category, page by 10 items |
| **validation/** | Field-level validation (required, max length, nullable, read-only) | required field validation, max length enforcement |
| **relational/** | Category assignment, default locations, supplier linkage | set default location, assign category |
| **category-extended/** | Category parameter templates | create parameter template, assign to category |
| **price-breaks/** | Pricing, margins, and price tiers | create price break, set internal price |
| **stocktake/** | Stock tracking, stock levels, stocktake operations | create stocktake, adjust stock |
| **test-templates/** | Part test templates for quality control | create test template, assign to part |

### Test ID Pattern

`TC_AP<TYPE>_<NUMBER>`

Examples:
- `TC_APCRUD_001` — API Part CRUD test 1
- `TC_APCATPARAM_001` — API Category Parameter test 1
- `TC_APEDGE_001` — API Edge Case test 1
- `TC_APVAL_001` — API Validation test 1
- `TC_APFLT_001` — API Filtering test 1
- `TC_APREL_001` — API Relational test 1

## Naming Conventions

### File Names

Use test case ID in file name:
- UI: `TC_UI_PC_001.md`, `TC_UI_AUTH_001.md`
- API: `TC_APCRUD_001.md`, `TC_APCATPARAM_001.md`

### Directory Organization

- Group related test cases in the same folder
- Use the feature area or test type as the folder name
- Archive old or superseded test cases in an `archive/` subfolder if needed

## Coverage Planning

Before creating test cases:

1. Check if coverage already exists for the feature area
2. Use the test ID pattern to avoid duplicates
3. Log the coverage plan in a `{suite_name}-manual-plan.md` file
4. Get confirmation before extensive exploration (especially for mutating flows)