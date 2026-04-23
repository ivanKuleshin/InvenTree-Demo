# Tech Debt Register

## Medium Issues

### M1 — ConfigManager: no env-var / system-property override
- **Severity**: Medium
- **Location**: `ConfigManager.java:13-24`
- **Description**: All configuration values are read exclusively from the properties file. There is no fallback chain for environment variables or JVM system properties, which prevents injecting secrets at CI runtime without modifying the committed file.
- **Recommended fix**: Add a priority chain — `System.getenv()` first, then `System.getProperty()`, then the properties file. Example: `System.getenv("API_BASE_URL")` overrides `api.base.url`.

---

### M2 — Dead code in SpecBuilder
- **Severity**: Medium
- **Location**: `SpecBuilder.java:43-51`
- **Description**: The methods `responseSpec(int)` and `buildResponse(int)` are defined but never called anywhere in the codebase. They add noise and imply a response-spec pattern that is not in use.
- **Recommended fix**: Remove both methods. If response-spec validation is needed in future, reintroduce at that point.

---

### M3 — POJO annotation inconsistency
- **Severity**: Medium
- **Location**: All model classes under `src/main/java/com/inventree/model/`
- **Description**: Response models use Lombok `@Data` (mutable, generates setters, `equals`, `hashCode`, `toString`). Request models use `@Getter` + `@Builder` (immutable). The mixed pattern makes the model layer inconsistent and response models unnecessarily mutable.
- **Recommended fix**: Standardise response models to `@Getter` + `@NoArgsConstructor` + `@AllArgsConstructor` + `@JsonIgnoreProperties(ignoreUnknown = true)`. Remove `@Data` to eliminate setters.

---

### M4 — CategoryTestData.testCategoryName() omits RUN_ID
- **Severity**: Medium
- **Location**: `CategoryTestData.java:50-52`
- **Description**: The `testCategoryName()` factory method does not append a `RUN_ID` suffix, meaning category names created in parallel or back-to-back runs may collide if the environment is not cleaned between runs.
- **Status**: Fixed — `RUN_ID` added to `CategoryTestData`; `testCategoryName()` now appends it.

---

### M5 — Three independent RUN_ID computations
- **Severity**: Medium
- **Location**: `PartTestData.java`, `StockTestData.java`, `PartTestTemplateTestData.java`
- **Description**: Each test-data class independently computes `RUN_ID = String.valueOf(System.currentTimeMillis() % 100000)`. When classes are loaded at different milliseconds, each gets a different run ID, making cross-class traceability harder and string matching imprecise.
- **Recommended fix**: Centralise into a single `RunContext.RUN_ID` constant loaded once at JVM startup. All test-data classes import `RunContext.RUN_ID` instead of declaring their own.

---

### M6 — Fixed date range in FilteringTestData (partially fixed)
- **Severity**: Medium
- **Location**: `FilteringTestData.java:43-44`
- **Description**: `DATE_RANGE_START` was a static string `"2026-01-01"` that ages out as the real date advances, causing `tc_APFLT_021` to fail when the current date surpasses the range end. `DATE_RANGE_END` was also fixed.
- **Status**: `DATE_RANGE_END` fixed to `LocalDate.now().toString()` and `DATE_RANGE_START` fixed to `LocalDate.now().minusMonths(3).toString()` as part of fix C2. Both values are now computed dynamically at class load.

---

## Low Issues

### L1 — StockItemsCrudTest: created item not tracked for cleanup
- **Severity**: Low
- **Location**: `StockItemsCrudTest.java:449-459`
- **Description**: In `tc_ASCRUD_015`, a stock item is created but its pk is not added to `createdStockItemIds`. If the pre-delete assertion fails, the item leaks and pollutes the environment.
- **Recommended fix**: Add the created item's pk to `createdStockItemIds` immediately after creation, before any assertions. Remove it from the list after a confirmed successful delete.

---

### L2 — PartCrudTest: inline response body assertions bypass ResponseValidator
- **Severity**: Low
- **Location**: `PartCrudTest.java:153-156`
- **Description**: `response.then().body(...)` assertions are called inline instead of going through `ResponseValidator`. This breaks the centralised-assertion contract established by the framework and makes it harder to add uniform failure logging or schema checks later.
- **Recommended fix**: Extract the body assertions into `ResponseValidator` or use a dedicated helper method. Keep all assertion logic behind the centralised validator.

---

### L3 — BaseTest service fields declared static
- **Severity**: Low
- **Location**: `BaseTest.java:26-32`
- **Description**: Service fields (`partService`, `stockService`, etc.) are declared `static`. This creates implicit cross-class coupling — a subclass that reassigns a static field affects all other subclasses. It is safe with the current single-threaded `@BeforeSuite` initialisation, but non-idiomatic and fragile.
- **Recommended fix**: Change service fields to instance fields. Each test class instance gets its own references, which is the standard TestNG pattern.

---

### L4 — StockTestData: hardcoded description in structuralLocation()
- **Severity**: Low
- **Location**: `StockTestData.java:138`
- **Description**: The `structuralLocation()` factory method hardcodes the description string `"Organizational node only"`. Callers cannot override it without calling a different factory or bypassing test data entirely.
- **Recommended fix**: Add an overload `structuralLocation(String name, String description)` so callers can supply their own description, and have the no-arg overload delegate with a sensible default.

---

### L5 — PartCategoryCrudTest: setupPassivesCategory() does not filter for top-level
- **Severity**: Low
- **Location**: `PartCategoryCrudTest.java:41-51`
- **Description**: `setupPassivesCategory()` searches for a category by name `"Passives"` but does not constrain to top-level results. If a subcategory named "Passives" is returned first, the assertions `level == 0` and `parent == null` in `tc_ACCRUD_002` will fail.
- **Recommended fix**: Add `top_level=true` query parameter to the lookup or post-filter results to `parent == null` before selecting the first match.
