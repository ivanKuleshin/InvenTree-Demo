# InvenTree Test Cases — Master Index

## API Test Suites

### Parts CRUD (api-parts-crud)

| TC ID         | Title                                                                             | Type             | Priority |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APCRUD-001 | GET /api/part/ returns paginated part list with full field set                   | API / Functional | P1       |
| TC-APCRUD-002 | GET /api/part/{id}/ retrieves a single part by primary key                       | API / Functional | P1       |
| TC-APCRUD-003 | GET /api/part/{id}/ with query flags returns expanded fields                     | API / Functional | P2       |
| TC-APCRUD-004 | PUT /api/part/{id}/ replaces all writable fields on an existing part             | API / Functional | P2       |
| TC-APCRUD-005 | PATCH /api/part/{id}/ partially updates selected fields on an existing part      | API / Functional | P2       |
| TC-APCRUD-006 | DELETE /api/part/{id}/ removes an inactive part                                  | API / Functional | P2       |
| TC-APCRUD-007 | POST /api/part/ using the duplicate write-only field                             | API / Functional | P2       |
| TC-APCRUD-008 | POST /api/part/ with initial_stock write-only field                              | API / Functional | P2       |
| TC-APCRUD-009 | POST /api/part/ with initial_supplier write-only field                           | API / Functional | P2       |

### Categories CRUD (api-categories-crud)

| TC ID         | Title                                                                             | Type             | Priority |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- |
| TC-ACCRUD-001 | GET /api/part/category/ returns paginated category list                          | API / Functional | P1       |
| TC-ACCRUD-002 | GET /api/part/category/{id}/ retrieves a single category with path detail        | API / Functional | P1       |
| TC-ACCRUD-003 | POST /api/part/category/ creates a minimal top-level category                    | API / Functional | P1       |
| TC-ACCRUD-004 | POST /api/part/category/ creates a child category with parent assigned           | API / Functional | P1       |
| TC-ACCRUD-005 | PUT /api/part/category/{id}/ replaces all writable fields                        | API / Functional | P2       |
| TC-ACCRUD-006 | PATCH /api/part/category/{id}/ partially updates a single field                  | API / Functional | P2       |
| TC-ACCRUD-007 | DELETE /api/part/category/{id}/ removes a category                               | API / Functional | P2       |

### Filtering (api-filtering)

| TC ID         | Title                                                                             | Type             | Priority |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APFLT-001  | GET /api/part/ with limit and offset returns correct pagination window           | API / Functional | P1       |
| TC-APFLT-002  | GET /api/part/?search= returns parts matching the search term                    | API / Functional | P1       |
| TC-APFLT-003  | GET /api/part/?category= filters parts to a specific category                    | API / Functional | P1       |
| TC-APFLT-004  | GET /api/part/?active= filters parts by active status                            | API / Functional | P2       |
| TC-APFLT-005  | GET /api/part/?ordering= sorts results ascending and descending by name          | API / Functional | P2       |
| TC-APFLT-006  | GET /api/part/category/ supports search and parent filter                        | API / Functional | P2       |

### Field Validation (api-field-validation)

| TC ID         | Title                                                                             | Type             | Priority |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APVAL-001  | POST /api/part/category/ with missing required name field returns 400            | API / Negative   | P1       |
| TC-APVAL-002  | POST /api/part/category/ with duplicate name under the same parent returns 400   | API / Negative   | P2       |
| TC-APVAL-003  | POST /api/part/category/ with name exceeding 100 characters returns 400          | API / Negative   | P3       |
| TC-APVAL-004  | POST /api/part/ with name exceeding 100 characters returns 400                   | API / Negative   | P3       |
| TC-APVAL-005  | POST /api/part/ with IPN exceeding 100 characters returns 400                    | API / Negative   | P3       |
| TC-APVAL-006  | POST /api/part/ with non-integer category value returns 400                      | API / Negative   | P3       |

### Relational Integrity (api-relational-integrity)

| TC ID         | Title                                                                             | Type             | Priority |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APREL-001  | POST /api/part/ with non-existent category FK returns 400                        | API / Negative   | P2       |
| TC-APREL-002  | POST /api/part/category/ with valid default_location FK persists the relationship | API / Functional | P2       |
| TC-APREL-003  | POST /api/part/ with valid default_location FK persists the relationship         | API / Functional | P2       |
| TC-APREL-004  | POST /api/part/ with variant_of FK links variant to template part                | API / Functional | P2       |

### Edge Cases (api-edge-cases)

| TC ID         | Title                                                                             | Type             | Priority |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APEDGE-001 | POST /api/part/ without authentication returns 403                               | API / Security   | P1       |
| TC-APEDGE-002 | POST /api/part/ with read-only credentials returns 403                           | API / Security   | P1       |
| TC-APEDGE-003 | GET /api/part/{id}/ with non-existent PK returns 404                             | API / Negative   | P2       |
| TC-APEDGE-004 | GET /api/part/category/{id}/ with non-existent PK returns 404                    | API / Negative   | P2       |
| TC-APEDGE-005 | POST /api/part/ with duplicate name+IPN+revision combination returns 400         | API / Negative   | P2       |
| TC-APEDGE-006 | DELETE /api/part/{id}/ on a non-existent PK returns 404                          | API / Negative   | P3       |
| TC-APEDGE-007 | DELETE /api/part/{id}/ on an active part returns 400                             | API / Negative   | P2       |

### Part Test Templates (api-test-templates)

| TC ID       | Title                                                                              | Type             | Priority |
| ----------- | ---------------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APTT-001 | GET /api/part/test-template/ returns paginated test template list                  | API / Functional | P1       |
| TC-APTT-002 | GET /api/part/test-template/{id}/ retrieves a single test template                 | API / Functional | P1       |
| TC-APTT-003 | POST /api/part/test-template/ creates a new test template                          | API / Functional | P1       |
| TC-APTT-004 | PATCH /api/part/test-template/{id}/ updates description and boolean flags          | API / Functional | P2       |
| TC-APTT-005 | DELETE /api/part/test-template/{id}/ removes a test template                       | API / Functional | P2       |
| TC-APTT-006 | GET /api/part/test-template/?part={id} filters templates by part                   | API / Functional | P2       |

### Part Internal Pricing (api-internal-pricing)

| TC ID          | Title                                                                          | Type             | Priority |
| -------------- | ------------------------------------------------------------------------------ | ---------------- | -------- |
| TC-APPRICE-001 | GET /api/part/internal-price/ returns paginated internal price break list      | API / Functional | P1       |
| TC-APPRICE-002 | GET /api/part/internal-price/{id}/ retrieves a single internal price break     | API / Functional | P1       |
| TC-APPRICE-003 | POST /api/part/internal-price/ creates a new internal price break              | API / Functional | P1       |
| TC-APPRICE-004 | PATCH /api/part/internal-price/{id}/ updates the price of a price break        | API / Functional | P2       |
| TC-APPRICE-005 | DELETE /api/part/internal-price/{id}/ removes a price break                    | API / Functional | P2       |

### Part Sale Pricing (api-sale-pricing)

| TC ID            | Title                                                                        | Type             | Priority |
| ---------------- | ---------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APSPRICE-001  | GET /api/part/sale-price/ returns paginated sale price break list            | API / Functional | P1       |
| TC-APSPRICE-002  | GET /api/part/sale-price/{id}/ retrieves a single sale price break           | API / Functional | P1       |
| TC-APSPRICE-003  | POST /api/part/sale-price/ creates a new sale price break                    | API / Functional | P1       |
| TC-APSPRICE-004  | DELETE /api/part/sale-price/{id}/ removes a sale price break                 | API / Functional | P2       |

### Part Aggregate Pricing (api-aggregate-pricing)

| TC ID             | Title                                                                       | Type             | Priority |
| ----------------- | --------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APAGPRICE-001  | GET /api/part/{id}/pricing/ retrieves the aggregate pricing summary         | API / Functional | P1       |
| TC-APAGPRICE-002  | PATCH /api/part/{id}/pricing/ sets override_min and override_max            | API / Functional | P2       |
| TC-APAGPRICE-003  | PATCH /api/part/{id}/pricing/ clears override values with null              | API / Functional | P2       |

### Part Stocktake (api-stocktake)

| TC ID        | Title                                                                              | Type             | Priority |
| ------------ | ---------------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APSTK-001 | GET /api/part/stocktake/ returns paginated stocktake list                          | API / Functional | P1       |
| TC-APSTK-002 | GET /api/part/stocktake/{id}/ retrieves a single stocktake record                  | API / Functional | P1       |
| TC-APSTK-003 | POST /api/part/stocktake/ creates a stocktake record [DIVERGENCE: 500 on demo]     | API / Functional | P1       |
| TC-APSTK-004 | PATCH /api/part/stocktake/{id}/ updates a stocktake record                         | API / Functional | P2       |
| TC-APSTK-005 | DELETE /api/part/stocktake/{id}/ returns 404 for non-existent record               | API / Negative   | P2       |

### Part Related Parts CRUD (api-related-parts)

| TC ID            | Title                                                                        | Type             | Priority |
| ---------------- | ---------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APRELATED-001 | GET /api/part/related/ returns paginated related part list                   | API / Functional | P1       |
| TC-APRELATED-002 | GET /api/part/related/{id}/ retrieves a single related part entry            | API / Functional | P1       |
| TC-APRELATED-003 | POST /api/part/related/ creates a new related part link                      | API / Functional | P1       |
| TC-APRELATED-004 | PATCH /api/part/related/{id}/ updates the note field                         | API / Functional | P2       |
| TC-APRELATED-005 | DELETE /api/part/related/{id}/ removes a related part link                   | API / Functional | P2       |
| TC-APRELATED-006 | POST /api/part/related/ with duplicate pair returns 400                      | API / Negative   | P2       |

### Category Parameters CRUD (api-category-parameters)

| TC ID             | Title                                                                       | Type             | Priority |
| ----------------- | --------------------------------------------------------------------------- | ---------------- | -------- |
| TC-APCATPARAM-001 | GET /api/part/category/parameters/ returns paginated category parameter list | API / Functional | P1       |
| TC-APCATPARAM-002 | GET /api/part/category/parameters/{id}/ retrieves a single assignment        | API / Functional | P1       |
| TC-APCATPARAM-003 | POST /api/part/category/parameters/ creates a category parameter assignment  | API / Functional | P1       |
| TC-APCATPARAM-004 | PATCH /api/part/category/parameters/{id}/ updates the default_value          | API / Functional | P2       |
| TC-APCATPARAM-005 | DELETE /api/part/category/parameters/{id}/ removes a category parameter      | API / Functional | P2       |

## UI Test Suites

### Part Creation (ui-parts-creation)

| TC ID         | Title                                                                             | Type             | Priority |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- |
| TC-UI-PC-001  | Create part via manual entry — required fields only (happy path)                 | UI / Functional  | P1       |
| TC-UI-PC-002  | Create part with all optional text fields populated                              | UI / Functional  | P2       |
| TC-UI-PC-003  | Create part assigned to a specific category                                      | UI / Functional  | P1       |
| TC-UI-PC-004  | Boolean attribute toggles in the "Add Part" form                                 | UI / Functional  | P2       |
| TC-UI-PC-005  | Duplicate an existing part                                                       | UI / Functional  | P2       |
| TC-UI-PC-006  | Validation errors — missing required field and invalid input                     | UI / Negative    | P2       |
| TC-UI-PC-007  | Import parts from CSV — happy path                                               | UI / Functional  | P1       |
| TC-UI-PC-008  | Import parts from CSV — error rows with inline correction                        | UI / Negative    | P3       |

### Part Attribute Toggles (ui-parts-attributes)

| TC ID        | Title                                                                 | Type                        | Priority |
| ------------ | --------------------------------------------------------------------- | --------------------------- | -------- |
| TC-UI-PA-001 | Toggle Active attribute OFF — part becomes inactive                   | UI / Functional             | P1       |
| TC-UI-PA-002 | Inactive part excluded from BOM, PO, SO, and Build Order selections   | UI / Functional / Negative  | P1       |
| TC-UI-PA-003 | Toggle Virtual ON — stock UI elements are hidden                      | UI / Functional             | P1       |
| TC-UI-PA-004 | Toggle Virtual OFF — stock actions become available                   | UI / Functional             | P2       |
| TC-UI-PA-005 | Template attribute — Variants tab appears; stock aggregation visible  | UI / Functional             | P1       |
| TC-UI-PA-006 | Assembly attribute — BOM tab accessible and editable                  | UI / Functional             | P1       |
| TC-UI-PA-007 | Assembly + Locked — BOM tab is read-only                              | UI / Functional             | P1       |
| TC-UI-PA-008 | Component attribute — part appears in BOM sub-part search             | UI / Functional             | P2       |
| TC-UI-PA-009 | Trackable attribute — stock creation requires batch or serial number  | UI / Functional             | P1       |
| TC-UI-PA-010 | Purchaseable attribute — Suppliers tab accessible with supplier parts | UI / Functional             | P1       |
| TC-UI-PA-011 | Salable attribute — Sales Orders tab is accessible                    | UI / Functional             | P1       |
| TC-UI-PA-012 | Testable attribute — Test Templates tab appears with templates        | UI / Functional             | P2       |
| TC-UI-PA-013 | Toggle all attributes ON — verify full persistence across page reload | UI / Functional             | P2       |
| TC-UI-PA-014 | Assembly + Component — part is buildable and usable in other BOMs     | UI / Functional             | P2       |
| TC-UI-PA-015 | Purchaseable = false — Suppliers tab is absent                        | UI / Negative               | P2       |
| TC-UI-PA-016 | Salable = false — Sales Orders tab is absent                          | UI / Negative               | P2       |
| TC-UI-PA-017 | Template = false — Variants tab is absent                             | UI / Negative               | P2       |
| TC-UI-PA-018 | Boundary — Virtual + Trackable combination set simultaneously         | UI / Exploratory / Boundary | P3       |
| TC-UI-PA-019 | Boundary — Locked attribute prevents part deletion                    | UI / Negative / Boundary    | P3       |
| TC-UI-PA-020 | Boundary — Toggling Component OFF on a part already used in a BOM     | UI / Exploratory / Boundary | P3       |

### Units of Measure (ui-parts-units)

| TC ID           | Title                                                                                     | Type                       | Priority |
| --------------- | ----------------------------------------------------------------------------------------- | -------------------------- | -------- |
| TC-UI-UNIT-001  | Assign a valid SI unit (metres) to a new part at creation                                 | UI / Functional            | P1       |
| TC-UI-UNIT-002  | Assign a valid dimensionless unit (piece, each, dozen) to a new part                      | UI / Functional            | P1       |
| TC-UI-UNIT-003  | Leave the Units field blank — part defaults to dimensionless (pcs)                        | UI / Functional            | P1       |
| TC-UI-UNIT-004  | Entering an invalid/unrecognized unit string is rejected with an error                    | UI / Negative              | P1       |
| TC-UI-UNIT-005  | Case-sensitivity validation — uppercase KG is rejected, lowercase kg is accepted          | UI / Negative / Boundary   | P2       |
| TC-UI-UNIT-006  | Edit and change the unit on an existing part                                               | UI / Functional            | P2       |
| TC-UI-UNIT-007  | View Physical Units in System Settings                                                    | UI / Functional            | P2       |
| TC-UI-UNIT-008  | Supplier part with incompatible unit is rejected with a conversion error                  | UI / Negative              | P1       |
| TC-UI-UNIT-009  | Supplier part with compatible unit is accepted and native quantity is converted            | UI / Functional            | P2       |
| TC-UI-UNIT-010  | Unit string at the 20-character boundary is handled correctly                             | UI / Boundary              | P3       |
| TC-UI-UNIT-011  | Valid volume unit (litres) assigned to a new part                                         | UI / Functional            | P2       |

### Part Categories (ui-categories)

| TC ID          | Title                                                                                      | Type             | Priority |
| -------------- | ------------------------------------------------------------------------------------------ | ---------------- | -------- |
| TC-UI-CAT-001  | Top-level category list loads with expected columns and data                               | UI / Functional  | P1       |
| TC-UI-CAT-002  | Navigating into a child category opens its dedicated page                                  | UI / Functional  | P1       |
| TC-UI-CAT-003  | Category Details tab shows all metadata fields                                             | UI / Functional  | P1       |
| TC-UI-CAT-004  | Three-level nested category shows full pathstring and multi-segment breadcrumb             | UI / Functional  | P2       |
| TC-UI-CAT-005  | Structural category shows Structural = YES and contains no directly assigned parts         | UI / Functional  | P2       |
| TC-UI-CAT-006  | Breadcrumb link navigates up to parent category                                            | UI / Functional  | P2       |
| TC-UI-CAT-007  | Sub-categories tab lists direct children of a parent category                             | UI / Functional  | P2       |
| TC-UI-CAT-008  | Leaf-node category shows empty Subcategories tab                                           | UI / Edge Case   | P3       |
| TC-UI-CAT-009  | Parts tab shows all parts including sub-category parts by default (cascade on)             | UI / Functional  | P1       |
| TC-UI-CAT-010  | Parts table search filter narrows results by part name                                     | UI / Functional  | P1       |
| TC-UI-CAT-011  | "Table Filters" drawer opens and contains an "Add Filter" button                           | UI / Functional  | P2       |
| TC-UI-CAT-012  | Category search in top-level list filters categories by name                               | UI / Functional  | P2       |
| TC-UI-CAT-013  | Parts table Name column is sortable                                                        | UI / Functional  | P2       |
| TC-UI-CAT-014  | Parts table shows pagination controls and navigates between pages                          | UI / Functional  | P3       |
| TC-UI-CAT-015  | Clicking the Parametric View button switches the parts table to parametric mode            | UI / Functional  | P1       |
| TC-UI-CAT-016  | Clicking the standard view button returns the table to standard mode                       | UI / Functional  | P2       |
| TC-UI-CAT-017  | Clicking a parameter column header sorts the parametric table                              | UI / Functional  | P1       |
| TC-UI-CAT-018  | Parameter column filter dialog shows operator dropdown and value input                     | UI / Functional  | P1       |
| TC-UI-CAT-019  | Filtering by a parameter value narrows the parts list                                      | UI / Functional  | P1       |
| TC-UI-CAT-020  | Multiple parameter filters applied simultaneously narrow results with AND logic            | UI / Functional  | P2       |
| TC-UI-CAT-021  | Two filters on the same parameter create a range query                                     | UI / Functional  | P2       |
| TC-UI-CAT-022  | Removing a parameter filter restores the unfiltered count                                  | UI / Functional  | P2       |
| TC-UI-CAT-023  | Unit-aware filter interprets abbreviated unit notation correctly                           | UI / Functional  | P3       |

## Deprecated Test Cases

The following test cases have been archived and replaced with the standardized TC_APCRUD_* naming convention:

| Deprecated ID | Replaced By   | Location                                              |
| ------------- | ------------- | ----------------------------------------------------- |
| TC-AP-PC-001  | TC-APCRUD-001 | test-cases/api/parts/archive/TC_AP_PART_CREATE_DEPRECATED.md |
| TC-AP-PC-002  | TC-APCRUD-002 | test-cases/api/parts/archive/TC_AP_PART_CREATE_DEPRECATED.md |
| TC-AP-PC-003  | TC-APCRUD-007 | Unique test extracted and renumbered                  |
| TC-AP-PC-004  | TC-APCRUD-008 | Unique test extracted and renumbered                  |
| TC-AP-PC-005  | TC-APCRUD-009 | Unique test extracted and renumbered                  |
| TC-AP-PC-006  | TC-APVAL-001  | Moved to validation suite                             |
| TC-AP-PC-007  | TC-APREL-001  | Moved to relational integrity suite                   |
| TC-AP-PC-008  | TC-APEDGE-001 | Moved to edge cases suite                             |
| TC-AP-PC-009  | (Future)      | Import session API test (to be implemented)           |

## Summary Statistics

- **Total Test Cases:** 117
- **API Test Cases:** 75
- **UI Test Cases:** 42
- **P1 Priority:** 54
- **P2 Priority:** 55
- **P3 Priority:** 8
