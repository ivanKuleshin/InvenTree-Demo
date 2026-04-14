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

- **Total Test Cases:** 48
- **API Test Cases:** 40
- **UI Test Cases:** 8
- **P1 Priority:** 16
- **P2 Priority:** 27
- **P3 Priority:** 5
