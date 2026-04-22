# InvenTree Test Cases — Master Index

## API Test Suites

### Parts CRUD (api-parts-crud)

| TC ID         | Title                                                                             | Type             | Priority | Automated | Test Class     |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- | --------- | -------------- |
| TC-APCRUD-001 | GET /api/part/ returns paginated part list with full field set                   | API / Functional | P1       | Yes       | PartCrudTest   |
| TC-APCRUD-002 | GET /api/part/{id}/ retrieves a single part by primary key                       | API / Functional | P1       | Yes       | PartCrudTest   |
| TC-APCRUD-003 | GET /api/part/{id}/ with query flags returns expanded fields                     | API / Functional | P2       | Yes       | PartCrudTest   |
| TC-APCRUD-004 | PUT /api/part/{id}/ replaces all writable fields on an existing part             | API / Functional | P2       | Yes       | PartCrudTest   |
| TC-APCRUD-005 | PATCH /api/part/{id}/ partially updates selected fields on an existing part      | API / Functional | P2       | Yes       | PartCrudTest   |
| TC-APCRUD-006 | DELETE /api/part/{id}/ removes an inactive part                                  | API / Functional | P2       | Yes       | PartCrudTest   |
| TC-APCRUD-007 | POST /api/part/ using the duplicate write-only field                             | API / Functional | P2       | Yes       | PartCrudTest   |
| TC-APCRUD-008 | POST /api/part/ with initial_stock write-only field                              | API / Functional | P2       | Yes       | PartCrudTest   |
| TC-APCRUD-009 | POST /api/part/ with initial_supplier write-only field                           | API / Functional | P2       | Yes       | PartCrudTest   |

### Categories CRUD (api-categories-crud)

| TC ID         | Title                                                                             | Type             | Priority | Automated | Test Class             |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------------------- |
| TC-ACCRUD-001 | GET /api/part/category/ returns paginated category list                          | API / Functional | P1       | Yes       | PartCategoryCrudTest   |
| TC-ACCRUD-002 | GET /api/part/category/{id}/ retrieves a single category with path detail        | API / Functional | P1       | Yes       | PartCategoryCrudTest   |
| TC-ACCRUD-003 | POST /api/part/category/ creates a minimal top-level category                    | API / Functional | P1       | Yes       | PartCategoryCrudTest   |
| TC-ACCRUD-004 | POST /api/part/category/ creates a child category with parent assigned           | API / Functional | P1       | Yes       | PartCategoryCrudTest   |
| TC-ACCRUD-005 | PUT /api/part/category/{id}/ replaces all writable fields                        | API / Functional | P2       | Yes       | PartCategoryCrudTest   |
| TC-ACCRUD-006 | PATCH /api/part/category/{id}/ partially updates a single field                  | API / Functional | P2       | Yes       | PartCategoryCrudTest   |
| TC-ACCRUD-007 | DELETE /api/part/category/{id}/ removes a category                               | API / Functional | P2       | Yes       | PartCategoryCrudTest   |

### Filtering (api-filtering)

| TC ID         | Title                                                                             | Type             | Priority | Automated | Test Class                          |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ----------------------------------- |
| TC-APFLT-001  | GET /api/part/ with limit and offset returns correct pagination window           | API / Functional | P1       | Yes       | PartFilteringPaginationSearchTest   |
| TC-APFLT-002  | GET /api/part/?search= returns parts matching the search term                    | API / Functional | P1       | Yes       | PartFilteringPaginationSearchTest   |
| TC-APFLT-003  | GET /api/part/?category= filters parts to a specific category                    | API / Functional | P1       | Yes       | PartFilteringPaginationSearchTest   |
| TC-APFLT-004  | GET /api/part/?active= filters parts by active status                            | API / Functional | P2       | Yes       | PartFilteringPaginationSearchTest   |
| TC-APFLT-005  | GET /api/part/?ordering= sorts results ascending and descending by name          | API / Functional | P2       | Yes       | PartFilteringPaginationSearchTest   |
| TC-APFLT-006  | GET /api/part/category/ supports search and parent filter                        | API / Functional | P2       | Yes       | PartFilteringPaginationSearchTest   |

### Field Validation (api-field-validation)

| TC ID         | Title                                                                             | Type             | Priority | Automated | Test Class |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-APVAL-001  | POST /api/part/category/ with missing required name field returns 400            | API / Negative   | P1       | No        | -          |
| TC-APVAL-002  | POST /api/part/category/ with duplicate name under the same parent returns 400   | API / Negative   | P2       | No        | -          |
| TC-APVAL-003  | POST /api/part/category/ with name exceeding 100 characters returns 400          | API / Negative   | P3       | No        | -          |
| TC-APVAL-004  | POST /api/part/ with name exceeding 100 characters returns 400                   | API / Negative   | P3       | No        | -          |
| TC-APVAL-005  | POST /api/part/ with IPN exceeding 100 characters returns 400                    | API / Negative   | P3       | No        | -          |
| TC-APVAL-006  | POST /api/part/ with non-integer category value returns 400                      | API / Negative   | P3       | No        | -          |

### Relational Integrity (api-relational-integrity)

| TC ID         | Title                                                                              | Type             | Priority | Automated | Test Class |
| ------------- | ---------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-APREL-001  | POST /api/part/ with non-existent category FK returns 400                        | API / Negative   | P2       | No        | -          |
| TC-APREL-002  | POST /api/part/category/ with valid default_location FK persists the relationship | API / Functional | P2       | No        | -          |
| TC-APREL-003  | POST /api/part/ with valid default_location FK persists the relationship         | API / Functional | P2       | No        | -          |
| TC-APREL-004  | POST /api/part/ with variant_of FK links variant to template part                | API / Functional | P2       | No        | -          |

### Edge Cases (api-edge-cases)

| TC ID          | Title                                                                             | Type             | Priority | Automated | Test Class |
| -------------- | --------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-APEDGE-001 | POST /api/part/ without authentication returns 403                               | API / Security   | P1       | No        | -          |
| TC-APEDGE-002 | POST /api/part/ with read-only credentials returns 403                           | API / Security   | P1       | No        | -          |
| TC-APEDGE-003 | GET /api/part/{id}/ with non-existent PK returns 404                             | API / Negative   | P2       | No        | -          |
| TC-APEDGE-004 | GET /api/part/category/{id}/ with non-existent PK returns 404                    | API / Negative   | P2       | No        | -          |
| TC-APEDGE-005 | POST /api/part/ with duplicate name+IPN+revision combination returns 400         | API / Negative   | P2       | No        | -          |
| TC-APEDGE-006 | DELETE /api/part/{id}/ on a non-existent PK returns 404                          | API / Negative   | P3       | No        | -          |
| TC-APEDGE-007 | DELETE /api/part/{id}/ on an active part returns 400                             | API / Negative   | P2       | No        | -          |

### Part Test Templates (api-test-templates)

| TC ID       | Title                                                                              | Type             | Priority | Automated | Test Class |
| ----------- | ---------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-APTT-001 | GET /api/part/test-template/ returns paginated test template list                  | API / Functional | P1       | No        | -          |
| TC-APTT-002 | GET /api/part/test-template/{id}/ retrieves a single test template                 | API / Functional | P1       | No        | -          |
| TC-APTT-003 | POST /api/part/test-template/ creates a new test template                          | API / Functional | P1       | No        | -          |
| TC-APTT-004 | PATCH /api/part/test-template/{id}/ updates description and boolean flags          | API / Functional | P2       | No        | -          |
| TC-APTT-005 | DELETE /api/part/test-template/{id}/ removes a test template                       | API / Functional | P2       | No        | -          |
| TC-APTT-006 | GET /api/part/test-template/?part={id} filters templates by part                   | API / Functional | P2       | No        | -          |

### Part Internal Pricing (api-internal-pricing)

| TC ID              | Title                                                                          | Type             | Priority | Automated | Test Class      |
| ------------------ | ------------------------------------------------------------------------------ | ---------------- | -------- | --------- | --------------- |
| TC-APPRICE-001     | GET /api/part/internal-price/ returns paginated internal price break list      | API / Functional | P1       | Yes       | PartPricingTest |
| TC-APPRICE-002     | GET /api/part/internal-price/{id}/ retrieves a single internal price break     | API / Functional | P1       | Yes       | PartPricingTest |
| TC-APPRICE-003     | POST /api/part/internal-price/ creates a new internal price break              | API / Functional | P1       | Yes       | PartPricingTest |
| TC-APPRICE-004     | PATCH /api/part/internal-price/{id}/ updates the price of a price break        | API / Functional | P2       | Yes       | PartPricingTest |
| TC-APPRICE-005     | DELETE /api/part/internal-price/{id}/ removes a price break                    | API / Functional | P2       | Yes       | PartPricingTest |
| TC-APPRICE-NEG-001 | GET /api/part/internal-price/{nonexistent}/ returns 404                        | API / Negative   | P2       | Yes       | PartPricingTest |
| TC-APPRICE-NEG-002 | POST /api/part/internal-price/ with non-existent part FK returns 400           | API / Negative   | P2       | Yes       | PartPricingTest |
| TC-APPRICE-NEG-003 | POST /api/part/internal-price/ with READER role returns 403                    | API / Security   | P1       | Yes       | PartPricingTest |

### Part Sale Pricing (api-sale-pricing)

| TC ID               | Title                                                                        | Type             | Priority | Automated | Test Class      |
| ------------------- | ---------------------------------------------------------------------------- | ---------------- | -------- | --------- | --------------- |
| TC-APSPRICE-001     | GET /api/part/sale-price/ returns paginated sale price break list            | API / Functional | P1       | Yes       | PartPricingTest |
| TC-APSPRICE-002     | GET /api/part/sale-price/{id}/ retrieves a single sale price break           | API / Functional | P1       | Yes       | PartPricingTest |
| TC-APSPRICE-003     | POST /api/part/sale-price/ creates a new sale price break                    | API / Functional | P1       | Yes       | PartPricingTest |
| TC-APSPRICE-004     | DELETE /api/part/sale-price/{id}/ removes a sale price break                 | API / Functional | P2       | Yes       | PartPricingTest |
| TC-APSPRICE-NEG-001 | GET /api/part/sale-price/{nonexistent}/ returns 404                          | API / Negative   | P2       | Yes       | PartPricingTest |
| TC-APSPRICE-NEG-002 | POST /api/part/sale-price/ with non-existent part FK returns 400             | API / Negative   | P2       | Yes       | PartPricingTest |

### Part Aggregate Pricing (api-aggregate-pricing)

| TC ID             | Title                                                                       | Type             | Priority | Automated | Test Class      |
| ----------------- | --------------------------------------------------------------------------- | ---------------- | -------- | --------- | --------------- |
| TC-APAGPRICE-001  | GET /api/part/{id}/pricing/ retrieves the aggregate pricing summary         | API / Functional | P1       | Yes       | PartPricingTest |
| TC-APAGPRICE-002  | PATCH /api/part/{id}/pricing/ sets override_min and override_max            | API / Functional | P2       | Yes       | PartPricingTest |
| TC-APAGPRICE-003  | PATCH /api/part/{id}/pricing/ clears override values with null              | API / Functional | P2       | Yes       | PartPricingTest |

### Part Stocktake (api-stocktake)

| TC ID        | Title                                                                              | Type             | Priority | Automated | Test Class |
| ------------ | ---------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-APSTK-001 | GET /api/part/stocktake/ returns paginated stocktake list                          | API / Functional | P1       | No        | -          |
| TC-APSTK-002 | GET /api/part/stocktake/{id}/ retrieves a single stocktake record                  | API / Functional | P1       | No        | -          |
| TC-APSTK-003 | POST /api/part/stocktake/ creates a stocktake record [DIVERGENCE: 500 on demo]     | API / Functional | P1       | No        | -          |
| TC-APSTK-004 | PATCH /api/part/stocktake/{id}/ updates a stocktake record                         | API / Functional | P2       | No        | -          |
| TC-APSTK-005 | DELETE /api/part/stocktake/{id}/ returns 404 for non-existent record               | API / Negative   | P2       | No        | -          |

### Part Related Parts CRUD (api-related-parts)

| TC ID            | Title                                                                        | Type             | Priority | Automated | Test Class |
| ---------------- | ---------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-APRELATED-001 | GET /api/part/related/ returns paginated related part list                   | API / Functional | P1       | No        | -          |
| TC-APRELATED-002 | GET /api/part/related/{id}/ retrieves a single related part entry            | API / Functional | P1       | No        | -          |
| TC-APRELATED-003 | POST /api/part/related/ creates a new related part link                      | API / Functional | P1       | No        | -          |
| TC-APRELATED-004 | PATCH /api/part/related/{id}/ updates the note field                         | API / Functional | P2       | No        | -          |
| TC-APRELATED-005 | DELETE /api/part/related/{id}/ removes a related part link                   | API / Functional | P2       | No        | -          |
| TC-APRELATED-006 | POST /api/part/related/ with duplicate pair returns 400                      | API / Negative   | P2       | No        | -          |

### Category Parameters CRUD (api-category-parameters)

| TC ID             | Title                                                                       | Type             | Priority | Automated | Test Class |
| ----------------- | --------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-APCATPARAM-001 | GET /api/part/category/parameters/ returns paginated category parameter list | API / Functional | P1       | No        | -          |
| TC-APCATPARAM-002 | GET /api/part/category/parameters/{id}/ retrieves a single assignment        | API / Functional | P1       | No        | -          |
| TC-APCATPARAM-003 | POST /api/part/category/parameters/ creates a category parameter assignment  | API / Functional | P1       | No        | -          |
| TC-APCATPARAM-004 | PATCH /api/part/category/parameters/{id}/ updates the default_value          | API / Functional | P2       | No        | -          |
| TC-APCATPARAM-005 | DELETE /api/part/category/parameters/{id}/ removes a category parameter      | API / Functional | P2       | No        | -          |

### Companies CRUD (api-companies-crud)

| TC ID         | Title                                                                              | Type             | Priority | Automated | Test Class |
| ------------- | ---------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-ACCMP-001  | GET /api/company/ returns paginated company list with full field set               | API / Functional | P1       | No        | -          |
| TC-ACCMP-002  | GET /api/company/{id}/ retrieves a single company by primary key                   | API / Functional | P1       | No        | -          |
| TC-ACCMP-003  | POST /api/company/ creates a company with required fields only                     | API / Functional | P1       | No        | -          |
| TC-ACCMP-004  | POST /api/company/ creates a company with all optional fields populated            | API / Functional | P2       | No        | -          |
| TC-ACCMP-005  | PUT /api/company/{id}/ replaces all writable fields on an existing company         | API / Functional | P2       | No        | -          |
| TC-ACCMP-006  | PATCH /api/company/{id}/ partially updates selected fields on an existing company  | API / Functional | P2       | No        | -          |
| TC-ACCMP-007  | DELETE /api/company/{id}/ removes a company                                        | API / Functional | P2       | No        | -          |
| TC-ACCMP-008  | GET /api/company/ filters by is_supplier, is_manufacturer, is_customer             | API / Functional | P2       | No        | -          |
| TC-ACCMP-009  | GET /api/company/ search and ordering query parameters                             | API / Functional | P2       | No        | -          |
| TC-ACCMP-010  | PATCH /api/company/{id}/ sets active=false to deactivate a company                 | API / Functional | P2       | No        | -          |
| TC-ACCMP-011  | POST /api/company/ without required name field returns 400                         | API / Negative   | P1       | No        | -          |
| TC-ACCMP-012  | POST /api/company/ with name exceeding 100 characters returns 400                  | API / Negative   | P3       | No        | -          |
| TC-ACCMP-013  | POST /api/company/ with invalid email or URL format returns 400                    | API / Negative   | P3       | No        | -          |
| TC-ACCMP-014  | GET /api/company/{id}/ with non-existent id returns 404                            | API / Negative   | P2       | No        | -          |
| TC-ACCMP-015  | POST /api/company/ without authentication returns 403                              | API / Security   | P1       | No        | -          |
| TC-ACCMP-016  | GET /api/company/address/ returns paginated address list                           | API / Functional | P1       | No        | -          |
| TC-ACCMP-017  | POST /api/company/address/ creates an address linked to a company                  | API / Functional | P1       | No        | -          |
| TC-ACCMP-018  | GET /api/company/address/{id}/ retrieves a single address by primary key           | API / Functional | P1       | No        | -          |
| TC-ACCMP-019  | PUT /api/company/address/{id}/ replaces all writable address fields                | API / Functional | P2       | No        | -          |
| TC-ACCMP-020  | PATCH /api/company/address/{id}/ partially updates an address                      | API / Functional | P2       | No        | -          |
| TC-ACCMP-021  | DELETE /api/company/address/{id}/ removes an address                               | API / Functional | P2       | No        | -          |
| TC-ACCMP-022  | POST /api/company/address/ with missing required fields returns 400                | API / Negative   | P2       | No        | -          |
| TC-ACCMP-023  | POST /api/company/address/ with non-existent company FK returns 400                | API / Negative   | P2       | No        | -          |
| TC-ACCMP-024  | GET /api/company/address/ filters addresses by company FK                          | API / Functional | P2       | No        | -          |

## Stock Module API Test Suites

### Stock Items CRUD (api-stock-items-crud)

| TC ID          | Title                                                                                          | Type             | Priority | Automated | Test Class          |
| -------------- | ---------------------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ------------------- |
| TC-ASCRUD-001  | GET /api/stock/ returns paginated stock item list with full field set                          | API / Functional | P1       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-002  | GET /api/stock/{id}/ retrieves a single stock item by primary key                             | API / Functional | P1       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-003  | POST /api/stock/ creates a stock item with required fields only                                | API / Functional | P1       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-004  | POST /api/stock/ creates a serialized stock item using serial_numbers write-only field         | API / Functional | P1       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-005  | POST /api/stock/ creates a stock item with a batch code                                        | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-006  | GET /api/stock/ filters by part PK                                                             | API / Functional | P1       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-007  | GET /api/stock/ filters by location PK                                                         | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-008  | GET /api/stock/ filters by status code                                                         | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-009  | GET /api/stock/ filters unlocated items using location=null                                    | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-010  | GET /api/stock/ filters serialized items                                                        | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-011  | PUT /api/stock/{id}/ replaces all writable fields                                              | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-012  | PATCH /api/stock/{id}/ partially updates quantity                                              | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-013  | PATCH /api/stock/{id}/ partially updates location                                              | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-014  | PATCH /api/stock/{id}/ updates status, batch, notes, packaging, and expiry_date               | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-015  | DELETE /api/stock/{id}/ removes a stock item                                                   | API / Functional | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-016  | POST /api/stock/ with non-existent part FK returns 400                                         | API / Negative   | P1       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-017  | POST /api/stock/ with non-existent location FK returns 400                                     | API / Negative   | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-018  | POST /api/stock/ with a structural location returns 400                                        | API / Negative   | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-019  | POST /api/stock/ with missing required fields returns 400                                      | API / Negative   | P1       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-020  | POST /api/stock/ without authentication returns 401 or 403                                     | API / Security   | P1       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-021  | DELETE /api/stock/{id}/ on a non-existent PK returns 404                                       | API / Negative   | P3       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-022  | POST /api/stock/ with negative quantity returns 400                                            | API / Negative   | P2       | Yes       | StockItemsCrudTest  |
| TC-ASCRUD-023  | POST /api/stock/ with serial_numbers pattern collision returns 400                              | API / Negative   | P2       | Yes       | StockItemsCrudTest  |

### Stock Locations CRUD (api-stock-locations-crud)

| TC ID          | Title                                                                                          | Type             | Priority | Automated | Test Class              |
| -------------- | ---------------------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ----------------------- |
| TC-ALCRUD-001  | GET /api/stock/location/ returns paginated location list with full field set                   | API / Functional | P1       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-002  | GET /api/stock/location/{id}/ retrieves a single location by primary key                       | API / Functional | P1       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-003  | GET /api/stock/location/tree/ returns all locations as lightweight tree nodes                  | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-004  | GET /api/stock/location/ filters top-level locations                                           | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-005  | GET /api/stock/location/ filters structural locations                                          | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-006  | GET /api/stock/location/ filters external locations                                            | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-007  | POST /api/stock/location/ creates a root-level location with required fields only              | API / Functional | P1       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-008  | POST /api/stock/location/ creates a nested child location                                      | API / Functional | P1       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-009  | POST /api/stock/location/ creates a structural location                                        | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-010  | POST /api/stock/location/ creates an external location                                         | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-011  | POST /api/stock/location/ creates a location with a location_type assigned                     | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-012  | PUT /api/stock/location/{id}/ renames a location and updates pathstring                        | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-013  | PATCH /api/stock/location/{id}/ toggles structural flag                                        | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-014  | PATCH /api/stock/location/{id}/ toggles external flag                                          | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-015  | PATCH /api/stock/location/{id}/ reparents a location to a new parent                           | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-016  | PATCH /api/stock/location/{id}/ changes location_type                                          | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-017  | DELETE /api/stock/location/{id}/ removes an empty location                                     | API / Functional | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-018  | DELETE /api/stock/location/{id}/ on a location with stock items returns 400                    | API / Negative   | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-019  | DELETE /api/stock/location/{id}/ on a location with child locations returns 400                | API / Negative   | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-020  | POST /api/stock/location/ with missing required name field returns 400                         | API / Negative   | P1       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-021  | POST /api/stock/location/ with non-existent parent FK returns 400                              | API / Negative   | P2       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-022  | PATCH /api/stock/location/{id}/ with a cyclic parent reference returns 400                     | API / Negative   | P3       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-023  | POST /api/stock/location/ without authentication returns 401 or 403                            | API / Security   | P1       | Yes       | StockLocationsCrudTest  |
| TC-ALCRUD-024  | GET /api/stock/location/ search filter matches name and pathstring                             | API / Functional | P2       | Yes       | StockLocationsCrudTest  |

### Stock Location Types CRUD (api-stock-location-types-crud)

| TC ID           | Title                                                                                         | Type             | Priority | Automated | Test Class                  |
| --------------- | --------------------------------------------------------------------------------------------- | ---------------- | -------- | --------- | --------------------------- |
| TC-ALTCRUD-001  | GET /api/stock/location-type/ returns a list of location types                                | API / Functional | P1       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-002  | GET /api/stock/location-type/{id}/ retrieves a single location type                           | API / Functional | P1       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-003  | POST /api/stock/location-type/ creates a location type with required fields only              | API / Functional | P1       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-004  | POST /api/stock/location-type/ creates a location type with all fields                        | API / Functional | P2       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-005  | PUT /api/stock/location-type/{id}/ replaces all writable fields                               | API / Functional | P2       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-006  | PATCH /api/stock/location-type/{id}/ partially updates description                            | API / Functional | P2       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-007  | PATCH /api/stock/location-type/{id}/ updates icon                                             | API / Functional | P2       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-008  | DELETE /api/stock/location-type/{id}/ removes a location type not referenced by any location  | API / Functional | P2       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-009  | DELETE /api/stock/location-type/{id}/ on a type referenced by locations returns 400           | API / Negative   | P2       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-010  | POST /api/stock/location-type/ with missing required name returns 400                         | API / Negative   | P1       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-011  | POST /api/stock/location-type/ with name exceeding 100 characters returns 400                 | API / Negative   | P3       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-012  | GET /api/stock/location-type/{id}/ with non-existent PK returns 404                           | API / Negative   | P2       | Yes       | StockLocationTypesCrudTest  |
| TC-ALTCRUD-013  | POST /api/stock/location-type/ without authentication returns 401 or 403                      | API / Security   | P1       | Yes       | StockLocationTypesCrudTest  |

### Stock Adjustments (api-stock-adjustments)

| TC ID         | Title                                                                                          | Type             | Priority | Automated | Test Class             |
| ------------- | ---------------------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------------------- |
| TC-ASADJ-001  | POST /api/stock/add/ increases quantity on a stock item                                        | API / Functional | P1       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-002  | POST /api/stock/remove/ decreases quantity on a stock item                                     | API / Functional | P1       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-003  | POST /api/stock/count/ sets the absolute quantity (stocktake)                                  | API / Functional | P1       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-004  | POST /api/stock/transfer/ moves a stock item to a new location                                 | API / Functional | P1       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-005  | POST /api/stock/change_status/ sets a new status code on multiple items                        | API / Functional | P1       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-006  | POST /api/stock/assign/ assigns stock items to a customer                                      | API / Functional | P1       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-007  | POST /api/stock/merge/ merges two same-part stock items into one                               | API / Functional | P1       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-008  | POST /api/stock/return/ returns customer-assigned items into stock                             | API / Functional | P1       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-009  | POST /api/stock/merge/ with items from different parts returns 400                             | API / Negative   | P2       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-010  | POST /api/stock/transfer/ to a structural location returns 400                                 | API / Negative   | P2       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-011  | POST /api/stock/change_status/ with an invalid status code returns 400                         | API / Negative   | P2       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-012  | POST /api/stock/add/ with empty items array returns 400                                        | API / Negative   | P3       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-013  | POST /api/stock/assign/ with a non-customer company returns 400                                | API / Negative   | P2       | Yes       | StockAdjustmentsTest   |
| TC-ASADJ-014  | POST /api/stock/remove/ without authentication returns 401 or 403                              | API / Security   | P1       | Yes       | StockAdjustmentsTest   |

## UI Test Suites

### Part Creation (ui-parts-creation)

| TC ID         | Title                                                                             | Type             | Priority | Automated | Test Class |
| ------------- | --------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-UI-PC-001  | Create part via manual entry — required fields only (happy path)                 | UI / Functional  | P1       | No        | -          |
| TC-UI-PC-002  | Create part with all optional text fields populated                              | UI / Functional  | P2       | No        | -          |
| TC-UI-PC-003  | Create part assigned to a specific category                                      | UI / Functional  | P1       | No        | -          |
| TC-UI-PC-004  | Boolean attribute toggles in the "Add Part" form                                 | UI / Functional  | P2       | No        | -          |
| TC-UI-PC-005  | Duplicate an existing part                                                       | UI / Functional  | P2       | No        | -          |
| TC-UI-PC-006  | Validation errors — missing required field and invalid input                     | UI / Negative    | P2       | No        | -          |
| TC-UI-PC-007  | Import parts from CSV — happy path                                               | UI / Functional  | P1       | No        | -          |
| TC-UI-PC-008  | Import parts from CSV — error rows with inline correction                        | UI / Negative    | P3       | No        | -          |

### Part Attribute Toggles (ui-parts-attributes)

| TC ID        | Title                                                                 | Type                        | Priority | Automated | Test Class |
| ------------ | --------------------------------------------------------------------- | --------------------------- | -------- | --------- | ---------- |
| TC-UI-PA-001 | Toggle Active attribute OFF — part becomes inactive                   | UI / Functional             | P1       | No        | -          |
| TC-UI-PA-002 | Inactive part excluded from BOM, PO, SO, and Build Order selections   | UI / Functional / Negative  | P1       | No        | -          |
| TC-UI-PA-003 | Toggle Virtual ON — stock UI elements are hidden                      | UI / Functional             | P1       | No        | -          |
| TC-UI-PA-004 | Toggle Virtual OFF — stock actions become available                   | UI / Functional             | P2       | No        | -          |
| TC-UI-PA-005 | Template attribute — Variants tab appears; stock aggregation visible  | UI / Functional             | P1       | No        | -          |
| TC-UI-PA-006 | Assembly attribute — BOM tab accessible and editable                  | UI / Functional             | P1       | No        | -          |
| TC-UI-PA-007 | Assembly + Locked — BOM tab is read-only                              | UI / Functional             | P1       | No        | -          |
| TC-UI-PA-008 | Component attribute — part appears in BOM sub-part search             | UI / Functional             | P2       | No        | -          |
| TC-UI-PA-009 | Trackable attribute — stock creation requires batch or serial number  | UI / Functional             | P1       | No        | -          |
| TC-UI-PA-010 | Purchaseable attribute — Suppliers tab accessible with supplier parts | UI / Functional             | P1       | No        | -          |
| TC-UI-PA-011 | Salable attribute — Sales Orders tab is accessible                    | UI / Functional             | P1       | No        | -          |
| TC-UI-PA-012 | Testable attribute — Test Templates tab appears with templates        | UI / Functional             | P2       | No        | -          |
| TC-UI-PA-013 | Toggle all attributes ON — verify full persistence across page reload | UI / Functional             | P2       | No        | -          |
| TC-UI-PA-014 | Assembly + Component — part is buildable and usable in other BOMs     | UI / Functional             | P2       | No        | -          |
| TC-UI-PA-015 | Purchaseable = false — Suppliers tab is absent                        | UI / Negative               | P2       | No        | -          |
| TC-UI-PA-016 | Salable = false — Sales Orders tab is absent                          | UI / Negative               | P2       | No        | -          |
| TC-UI-PA-017 | Template = false — Variants tab is absent                             | UI / Negative               | P2       | No        | -          |
| TC-UI-PA-018 | Boundary — Virtual + Trackable combination set simultaneously         | UI / Exploratory / Boundary | P3       | No        | -          |
| TC-UI-PA-019 | Boundary — Locked attribute prevents part deletion                    | UI / Negative / Boundary    | P3       | No        | -          |
| TC-UI-PA-020 | Boundary — Toggling Component OFF on a part already used in a BOM     | UI / Exploratory / Boundary | P3       | No        | -          |

### Units of Measure (ui-parts-units)

| TC ID           | Title                                                                                     | Type                       | Priority | Automated | Test Class |
| --------------- | ----------------------------------------------------------------------------------------- | -------------------------- | -------- | --------- | ---------- |
| TC-UI-UNIT-001  | Assign a valid SI unit (metres) to a new part at creation                                 | UI / Functional            | P1       | No        | -          |
| TC-UI-UNIT-002  | Assign a valid dimensionless unit (piece, each, dozen) to a new part                      | UI / Functional            | P1       | No        | -          |
| TC-UI-UNIT-003  | Leave the Units field blank — part defaults to dimensionless (pcs)                        | UI / Functional            | P1       | No        | -          |
| TC-UI-UNIT-004  | Entering an invalid/unrecognized unit string is rejected with an error                    | UI / Negative              | P1       | No        | -          |
| TC-UI-UNIT-005  | Case-sensitivity validation — uppercase KG is rejected, lowercase kg is accepted          | UI / Negative / Boundary   | P2       | No        | -          |
| TC-UI-UNIT-006  | Edit and change the unit on an existing part                                               | UI / Functional            | P2       | No        | -          |
| TC-UI-UNIT-007  | View Physical Units in System Settings                                                    | UI / Functional            | P2       | No        | -          |
| TC-UI-UNIT-008  | Supplier part with incompatible unit is rejected with a conversion error                  | UI / Negative              | P1       | No        | -          |
| TC-UI-UNIT-009  | Supplier part with compatible unit is accepted and native quantity is converted            | UI / Functional            | P2       | No        | -          |
| TC-UI-UNIT-010  | Unit string at the 20-character boundary is handled correctly                             | UI / Boundary              | P3       | No        | -          |
| TC-UI-UNIT-011  | Valid volume unit (litres) assigned to a new part                                         | UI / Functional            | P2       | No        | -          |

### Part Categories (ui-categories)

| TC ID          | Title                                                                                      | Type             | Priority | Automated | Test Class |
| -------------- | ------------------------------------------------------------------------------------------ | ---------------- | -------- | --------- | ---------- |
| TC-UI-CAT-001  | Top-level category list loads with expected columns and data                               | UI / Functional  | P1       | No        | -          |
| TC-UI-CAT-002  | Navigating into a child category opens its dedicated page                                  | UI / Functional  | P1       | No        | -          |
| TC-UI-CAT-003  | Category Details tab shows all metadata fields                                             | UI / Functional  | P1       | No        | -          |
| TC-UI-CAT-004  | Three-level nested category shows full pathstring and multi-segment breadcrumb             | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-005  | Structural category shows Structural = YES and contains no directly assigned parts         | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-006  | Breadcrumb link navigates up to parent category                                            | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-007  | Sub-categories tab lists direct children of a parent category                             | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-008  | Leaf-node category shows empty Subcategories tab                                           | UI / Edge Case   | P3       | No        | -          |
| TC-UI-CAT-009  | Parts tab shows all parts including sub-category parts by default (cascade on)             | UI / Functional  | P1       | No        | -          |
| TC-UI-CAT-010  | Parts table search filter narrows results by part name                                     | UI / Functional  | P1       | No        | -          |
| TC-UI-CAT-011  | "Table Filters" drawer opens and contains an "Add Filter" button                           | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-012  | Category search in top-level list filters categories by name                               | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-013  | Parts table Name column is sortable                                                        | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-014  | Parts table shows pagination controls and navigates between pages                          | UI / Functional  | P3       | No        | -          |
| TC-UI-CAT-015  | Clicking the Parametric View button switches the parts table to parametric mode            | UI / Functional  | P1       | No        | -          |
| TC-UI-CAT-016  | Clicking the standard view button returns the table to standard mode                       | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-017  | Clicking a parameter column header sorts the parametric table                              | UI / Functional  | P1       | No        | -          |
| TC-UI-CAT-018  | Parameter column filter dialog shows operator dropdown and value input                     | UI / Functional  | P1       | No        | -          |
| TC-UI-CAT-019  | Filtering by a parameter value narrows the parts list                                      | UI / Functional  | P1       | No        | -          |
| TC-UI-CAT-020  | Multiple parameter filters applied simultaneously narrow results with AND logic            | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-021  | Two filters on the same parameter create a range query                                     | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-022  | Removing a parameter filter restores the unfiltered count                                  | UI / Functional  | P2       | No        | -          |
| TC-UI-CAT-023  | Unit-aware filter interprets abbreviated unit notation correctly                           | UI / Functional  | P3       | No        | -          |

### Part Revisions (ui-parts-revisions)

| TC ID          | Title                                                                              | Type             | Priority | Automated | Test Class |
| -------------- | ---------------------------------------------------------------------------------- | ---------------- | -------- | --------- | ---------- |
| TC-UI-REV-001  | Create a revision from an existing part (happy path)                              | UI / Functional  | P1       | No        | -          |
| TC-UI-REV-002  | Create a revision with a duplicate revision code (validation error)               | UI / Negative    | P2       | No        | -          |
| TC-UI-REV-003  | Create a revision when PART_ENABLE_REVISION is disabled (feature hidden)          | UI / Negative    | P2       | No        | -          |
| TC-UI-REV-004  | Navigate between revisions using the revision drop-down                           | UI / Functional  | P1       | No        | -          |
| TC-UI-REV-005  | Verify revision inherits parent data correctly (name, category, description, IPN) | UI / Functional  | P2       | No        | -          |
| TC-UI-REV-006  | Set a revision as active (promotion analog)                                       | UI / Functional  | P1       | No        | -          |
| TC-UI-REV-007  | Set a revision as inactive (deactivation)                                         | UI / Functional  | P2       | No        | -          |
| TC-UI-REV-008  | Attempt to create a revision of a template part (blocked at submission)           | UI / Negative    | P2       | No        | -          |
| TC-UI-REV-009  | View all revisions of a part (revision history / list)                            | UI / Functional  | P1       | No        | -          |
| TC-UI-REV-010  | Attempt to set a circular revision reference via Edit                             | UI / Negative    | P3       | No        | -          |

### Negative / Boundary Scenarios (ui-parts-negative)

| TC ID          | Title                                                                                          | Type                        | Priority | Automated | Test Class |
| -------------- | ---------------------------------------------------------------------------------------------- | --------------------------- | -------- | --------- | ---------- |
| TC-UI-NEG-001  | Submit "Add Part" with empty Name field — required field error                                 | UI / Negative               | P1       | No        | -          |
| TC-UI-NEG-002  | Submit "Add Part" with Name at exactly 100 characters — accepted at boundary                   | UI / Boundary               | P2       | No        | -          |
| TC-UI-NEG-003  | Submit "Add Part" with Name at 101 characters — rejected above boundary                        | UI / Negative / Boundary    | P2       | No        | -          |
| TC-UI-NEG-004  | Submit "Add Part" with whitespace-only Name — rejected as blank                                | UI / Negative / Boundary    | P2       | No        | -          |
| TC-UI-NEG-005  | Submit "Edit Part" with IPN at 101 characters — rejected above boundary                        | UI / Negative / Boundary    | P3       | No        | -          |
| TC-UI-NEG-006  | Submit "Edit Part" with Description at 251 characters — rejected above boundary                | UI / Negative / Boundary    | P3       | No        | -          |
| TC-UI-NEG-007  | Submit "Edit Part" with unrecognized unit string — rejected with invalid unit error             | UI / Negative               | P1       | No        | -          |
| TC-UI-NEG-008  | Submit "Edit Part" with uppercase unit "KG" — rejected due to case sensitivity                 | UI / Negative / Boundary    | P2       | No        | -          |
| TC-UI-NEG-009  | Submit "Edit Part" with Units field exceeding 20 characters — rejected with two errors         | UI / Negative / Boundary    | P3       | No        | -          |
| TC-UI-NEG-010  | Submit "Edit Part" with invalid URL in Link field — rejected with URL format error              | UI / Negative               | P2       | No        | -          |
| TC-UI-NEG-011  | Submit "Edit Part" with javascript: scheme in Link field — rejected as invalid URL             | UI / Negative / Security    | P2       | No        | -          |
| TC-UI-NEG-012  | Submit "Edit Part" with Default Expiry set to -1 — rejected below minimum                      | UI / Negative / Boundary    | P2       | No        | -          |
| TC-UI-NEG-013  | Default Expiry at integer maximum (2147483647) accepted; one above (2147483648) rejected        | UI / Boundary               | P2       | No        | -          |
| TC-UI-NEG-014  | Submit "Edit Part" with Minimum Stock set to -1 — rejected below minimum                       | UI / Negative / Boundary    | P2       | No        | -          |
| TC-UI-NEG-015  | Virtual part detail page — Stock tab is absent                                                  | UI / Negative / Attribute   | P1       | No        | -          |
| TC-UI-NEG-016  | Locked assembly part — BOM tab shows read-only message, no edit controls                        | UI / Negative / Attribute   | P1       | No        | -          |
| TC-UI-NEG-017  | Submit duplicate part (same name + IPN + revision) — uniqueness constraint error in banner      | UI / Negative               | P1       | No        | -          |
| TC-UI-NEG-018  | Submit "Add Part" with revision_of set but revision code left blank — error on Revision field   | UI / Negative / Relational  | P2       | No        | -          |

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

## Future UI Coverage

The following Stock module areas would benefit from UI smoke tests in a future sprint:

- Stock item creation modal (select part, enter quantity, serial/batch, location, status).
- Stock location tree view — expand/collapse, structural badge, external icon.
- Stock adjustments via the "Stock Options" dropdown: Add, Remove, Count, Transfer dialogs.
- Stocktake date and expiry date columns in the stock items table.
- Stock tracking history tab on a Stock Item detail page.

## Summary Statistics

- **Total Test Cases:** 248
- **API Test Cases:** 178
- **UI Test Cases:** 70
- **P1 Priority:** 109
- **P2 Priority:** 123
- **P3 Priority:** 16

### Automation Coverage

- **Automated (API):** 113 / 178 (63%)
- **Not Automated (API):** 65 / 178 (37%)
- **Automated (UI):** 0 / 70 (0%)
- **Overall Automated:** 113 / 248 (46%)

#### Covered API Suites (100%)
- Parts CRUD — `PartCrudTest` (9/9)
- Categories CRUD — `PartCategoryCrudTest` (7/7)
- Filtering — `PartFilteringPaginationSearchTest` (6/6)
- Part Internal Pricing — `PartPricingTest` (8/8)
- Part Sale Pricing — `PartPricingTest` (6/6)
- Part Aggregate Pricing — `PartPricingTest` (3/3)
- Stock Items CRUD — `StockItemsCrudTest` (23/23)
- Stock Locations CRUD — `StockLocationsCrudTest` (24/24)
- Stock Location Types CRUD — `StockLocationTypesCrudTest` (13/13)
- Stock Adjustments — `StockAdjustmentsTest` (14/14)

#### Not Covered API Suites (0%)
- Field Validation (0/6)
- Relational Integrity (0/4)
- Edge Cases (0/7)
- Part Test Templates (0/6)
- Part Stocktake (0/5)
- Part Related Parts CRUD (0/6)
- Category Parameters CRUD (0/5)
- Companies CRUD (0/24)
