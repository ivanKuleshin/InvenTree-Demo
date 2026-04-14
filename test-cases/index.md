# InvenTree Test Cases — Master Index

| TC ID         | Title                                                                             | Suite                    | Type             | Priority |
| ------------- | --------------------------------------------------------------------------------- | ------------------------ | ---------------- | -------- |
| TC-AP-PC-001  | POST /api/part/ with minimal valid payload (name only)                            | api-parts-crud           | API / Functional | P1       |
| TC-AP-PC-002  | POST /api/part/ with full optional fields payload                                 | api-parts-crud           | API / Functional | P1       |
| TC-AP-PC-003  | POST /api/part/ using the duplicate write-only field                              | api-parts-crud           | API / Functional | P2       |
| TC-AP-PC-004  | POST /api/part/ with initial_stock write-only field                               | api-parts-crud           | API / Functional | P2       |
| TC-AP-PC-005  | POST /api/part/ with initial_supplier write-only field                            | api-parts-crud           | API / Functional | P2       |
| TC-AP-PC-006  | POST /api/part/ with missing required field (name) returns 400                    | api-parts-crud           | API / Negative   | P2       |
| TC-AP-PC-007  | POST /api/part/ with invalid category foreign key returns 400                     | api-parts-crud           | API / Negative   | P2       |
| TC-AP-PC-008  | POST /api/part/ without authentication returns 401                                | api-parts-crud           | API / Security   | P3       |
| TC-AP-PC-009  | Import session API — create session, verify structure and auto-mapping            | api-parts-crud           | API / Functional | P1       |
| TC-ACCRUD-001 | GET /api/part/category/ returns paginated category list                           | api-categories-crud      | API / Functional | P1       |
| TC-ACCRUD-002 | GET /api/part/category/{id}/ retrieves a single category with path detail         | api-categories-crud      | API / Functional | P1       |
| TC-ACCRUD-003 | POST /api/part/category/ creates a minimal top-level category                     | api-categories-crud      | API / Functional | P1       |
| TC-ACCRUD-004 | POST /api/part/category/ creates a child category with parent assigned            | api-categories-crud      | API / Functional | P1       |
| TC-ACCRUD-005 | PUT /api/part/category/{id}/ replaces all writable fields                         | api-categories-crud      | API / Functional | P2       |
| TC-ACCRUD-006 | PATCH /api/part/category/{id}/ partially updates a single field                   | api-categories-crud      | API / Functional | P2       |
| TC-ACCRUD-007 | DELETE /api/part/category/{id}/ removes a category                                | api-categories-crud      | API / Functional | P2       |
| TC-APCRUD-001 | GET /api/part/ returns paginated part list with full field set                    | api-parts-crud           | API / Functional | P1       |
| TC-APCRUD-002 | GET /api/part/{id}/ retrieves a single part by primary key                        | api-parts-crud           | API / Functional | P1       |
| TC-APCRUD-003 | GET /api/part/{id}/ with query flags returns expanded fields                      | api-parts-crud           | API / Functional | P2       |
| TC-APCRUD-004 | PUT /api/part/{id}/ replaces all writable fields on an existing part              | api-parts-crud           | API / Functional | P2       |
| TC-APCRUD-005 | PATCH /api/part/{id}/ partially updates selected fields on an existing part       | api-parts-crud           | API / Functional | P2       |
| TC-APCRUD-006 | DELETE /api/part/{id}/ removes an inactive part                                   | api-parts-crud           | API / Functional | P2       |
| TC-APFLT-001  | GET /api/part/ with limit and offset returns correct pagination window            | api-filtering            | API / Functional | P1       |
| TC-APFLT-002  | GET /api/part/?search= returns parts matching the search term                     | api-filtering            | API / Functional | P1       |
| TC-APFLT-003  | GET /api/part/?category= filters parts to a specific category                     | api-filtering            | API / Functional | P1       |
| TC-APFLT-004  | GET /api/part/?active= filters parts by active status                             | api-filtering            | API / Functional | P2       |
| TC-APFLT-005  | GET /api/part/?ordering= sorts results ascending and descending by name           | api-filtering            | API / Functional | P2       |
| TC-APFLT-006  | GET /api/part/category/ supports search and parent filter                         | api-filtering            | API / Functional | P2       |
| TC-APVAL-001  | POST /api/part/category/ with missing required name field returns 400             | api-field-validation     | API / Negative   | P1       |
| TC-APVAL-002  | POST /api/part/category/ with duplicate name under the same parent returns 400    | api-field-validation     | API / Negative   | P2       |
| TC-APVAL-003  | POST /api/part/category/ with name exceeding 100 characters returns 400           | api-field-validation     | API / Negative   | P3       |
| TC-APVAL-004  | POST /api/part/ with name exceeding 100 characters returns 400                    | api-field-validation     | API / Negative   | P3       |
| TC-APVAL-005  | POST /api/part/ with IPN exceeding 100 characters returns 400                     | api-field-validation     | API / Negative   | P3       |
| TC-APVAL-006  | POST /api/part/ with non-integer category value returns 400                       | api-field-validation     | API / Negative   | P3       |
| TC-APREL-001  | POST /api/part/ with non-existent category FK returns 400                         | api-relational-integrity | API / Negative   | P2       |
| TC-APREL-002  | POST /api/part/category/ with valid default_location FK persists the relationship | api-relational-integrity | API / Functional | P2       |
| TC-APREL-003  | POST /api/part/ with valid default_location FK persists the relationship          | api-relational-integrity | API / Functional | P2       |
| TC-APREL-004  | POST /api/part/ with variant_of FK links variant to template part                 | api-relational-integrity | API / Functional | P2       |
| TC-APEDGE-001 | POST /api/part/ without authentication returns 403                                | api-edge-cases           | API / Security   | P1       |
| TC-APEDGE-002 | POST /api/part/ with read-only credentials returns 403                            | api-edge-cases           | API / Security   | P1       |
| TC-APEDGE-003 | GET /api/part/{id}/ with non-existent PK returns 404                              | api-edge-cases           | API / Negative   | P2       |
| TC-APEDGE-004 | GET /api/part/category/{id}/ with non-existent PK returns 404                     | api-edge-cases           | API / Negative   | P2       |
| TC-APEDGE-005 | POST /api/part/ with duplicate name+IPN+revision combination returns 400          | api-edge-cases           | API / Negative   | P2       |
| TC-APEDGE-006 | DELETE /api/part/{id}/ on a non-existent PK returns 404                           | api-edge-cases           | API / Negative   | P3       |
