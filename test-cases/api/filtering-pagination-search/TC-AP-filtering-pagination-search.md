# Manual API Test Suite: Filtering, Pagination, and Search on `GET /api/part/`

**Source doc:** `docs/api-parts-list-filtering-pagination-search.md`  
**Probed against:** `https://demo.inventree.org` (2026-04-14)  
**Auth:** `Authorization: Token <token>` — obtain via `POST /api/user/token/` with Basic `allaccess:nolimits`  
**Endpoint under test:** `GET /api/part/`

---

## TC-APFLT-001: limit and offset return correct non-overlapping pages

**Type:** API | **Priority:** P1

**Preconditions:** At least 6 parts exist on the server.

**Steps:**

1. Send `GET /api/part/?limit=3` — record `pk` values as `PAGE_1_PKS`
2. Verify status `200`; verify `results` length is `3`; verify `previous` is `null`; verify `next` contains `limit=3` and `offset=3`
3. Send `GET /api/part/?limit=3&offset=3` — record `pk` values as `PAGE_2_PKS`
4. Verify status `200`; verify `results` length is `3`; verify `previous` is non-null
5. Verify no pk in `PAGE_2_PKS` appears in `PAGE_1_PKS`
6. Verify `count` is identical in both responses

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?limit=3`
- Headers: `Authorization: Token <token>`

**Request (step 3):**

- Method: `GET`
- URL: `/api/part/?limit=3&offset=3`
- Headers: `Authorization: Token <token>`

**Expected Result:** Each page returns exactly `limit` items. Pages do not overlap. `previous` is null on the first page. `count` is the same across all pages of the same query.

**Observed** (probed 2026-04-14):

- Page 1 (`limit=3`): status `200`, pks `[82, 84, 83]`, `previous: null`, `next` contains `offset=3`
- Page 2 (`limit=3&offset=3`): status `200`, pks `[86, 85, 69]`, `previous` non-null
- No pk overlap; `count` identical in both responses: `907`
- Matches spec: Yes

---

## TC-APFLT-002: search returns parts matching the search term across multiple fields

**Type:** API | **Priority:** P1

**Preconditions:** Parts with "resistor" in their name, description, or keywords exist.

**Steps:**

1. Send `GET /api/part/?search=resistor&limit=10`
2. Verify status `200`; verify `count > 0`
3. Verify each result has at least one searchable field (`name`, `description`, `keywords`, `IPN`) containing `"resistor"` (case-insensitive)
4. Record `count` as `LOWER_COUNT`
5. Send `GET /api/part/?search=RESISTOR&limit=10`
6. Verify status `200`; verify `count` equals `LOWER_COUNT` (search is case-insensitive)
7. Send `GET /api/part/?search=xyznonexistentterm12345&limit=10`
8. Verify status `200`; verify `count` equals `0`; verify `results` is `[]`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?search=resistor&limit=10`
- Headers: `Authorization: Token <token>`

**Expected Result:** Search returns parts where any searchable field contains the term. Search is case-insensitive. A search with no matches returns `count: 0` and empty `results`.

**Observed** (probed 2026-04-14):

- `search=resistor`: count `55`, first result pk `43` name `"R_100K_0402_1%"` description `"100K resistor in 0402 SMD package"`
- `search=RESISTOR`: same count `55` — case-insensitive confirmed
- `search=xyznonexistentterm12345`: count `0`, results `[]`
- Matches spec: Yes

---

## TC-APFLT-003: category filter returns parts in the specified category

**Type:** API | **Priority:** P1

**Preconditions:** Category `pk=1` ("Electronics") exists with parts. `category=null` parts exist.

**Steps:**

1. Send `GET /api/part/?category=1&limit=5` — record `count` as `DIRECT_COUNT`
2. Verify status `200`; verify `count > 0`
3. Send `GET /api/part/?category=1&cascade=true&limit=5`
4. Verify status `200`; verify `count >= DIRECT_COUNT`
5. Send `GET /api/part/?category=null&limit=5`
6. Verify status `200`; verify every result has `category` equal to `null`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?category=1&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `category=<pk>` filters by category. `cascade=true` broadens to include descendant categories. `category=null` returns uncategorized parts.

**Observed** (probed 2026-04-14):

- `category=1&limit=5`: status `200`, count `144`, first result `pk=69 category=13` — demo defaults cascade to true
- `category=null&limit=5`: status `200`, all results `category: null`
- Matches spec: Partial — cascade appears to default true on demo instance

---

## TC-APFLT-004: active filter separates active and inactive parts

**Type:** API | **Priority:** P2

**Preconditions:** Both active and inactive parts exist.

**Steps:**

1. Send `GET /api/part/?limit=5` — record `count` as `TOTAL_COUNT`
2. Send `GET /api/part/?active=true&limit=5`
3. Verify status `200`; verify all results have `active: true`; record `count` as `ACTIVE_COUNT`
4. Send `GET /api/part/?active=false&limit=5`
5. Verify status `200`; verify all results have `active: false`; record `count` as `INACTIVE_COUNT`
6. Verify `ACTIVE_COUNT + INACTIVE_COUNT == TOTAL_COUNT`

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?active=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `active=true` returns only active parts; `active=false` returns only inactive. The two counts sum to the total.

**Observed** (probed 2026-04-14):

- Total: `907`; active: `883`; inactive: `24`; `883 + 24 = 907`
- All results match their filter value
- Matches spec: Yes

---

## TC-APFLT-005: ordering=name and ordering=-name sort results alphabetically

**Type:** API | **Priority:** P2

**Preconditions:** At least 3 parts exist.

**Steps:**

1. Send `GET /api/part/?ordering=name&limit=3` — record names as `NAMES_ASC`
2. Verify status `200`; verify `NAMES_ASC[0] <= NAMES_ASC[1] <= NAMES_ASC[2]` (lexicographic)
3. Send `GET /api/part/?ordering=-name&limit=3` — record names as `NAMES_DESC`
4. Verify status `200`; verify `NAMES_DESC[0] >= NAMES_DESC[1] >= NAMES_DESC[2]`
5. Verify `NAMES_ASC[0] != NAMES_DESC[0]`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?ordering=name&limit=3`
- Headers: `Authorization: Token <token>`

**Expected Result:** `ordering=name` sorts A→Z; `ordering=-name` sorts Z→A.

**Observed** (probed 2026-04-14):

- Ascending: `["1551ABK", "1551ACLR", "1551AGY"]`; descending: `["Zero Qty Comp", "Zero Qty Comp", "Zero Qty Assembly"]`
- Order confirmed; first names differ
- Matches spec: Yes

---

## TC-APFLT-006: category search and parent filter on /api/part/category/

**Type:** API | **Priority:** P2

**Preconditions:** Category "Electronics" (pk=1) exists with child subcategories.

**Steps:**

1. Send `GET /api/part/category/?search=Electronics&limit=10`
2. Verify status `200`; verify `count > 0`; verify result with `name="Electronics"` is present
3. Verify additional results with `pathstring` containing `"Electronics"` are present
4. Send `GET /api/part/category/?parent=1&limit=10`
5. Verify status `200`; verify all results have `parent == 1`
6. Send `GET /api/part/category/?top_level=true&limit=10`
7. Verify status `200`; verify all results have `parent == null`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/category/?search=Electronics&limit=10`
- Headers: `Authorization: Token <token>`

**Expected Result:** Search matches `name`, `description`, and `pathstring`. `parent` filter returns direct children only. `top_level=true` returns root categories.

**Observed** (probed 2026-04-14):

- `search=Electronics`: count `13`, results include `"Electronics"`, `"Connectors"`, `"Pin Headers"` etc.
- `parent=1`: count `6`, all `parent: 1`; `top_level=true`: all `parent: null`
- Matches spec: Yes

---

## TC-APFLT-007: limit=0 returns unpaginated raw JSON array

**Type:** API | **Priority:** P2

**Preconditions:** At least one part exists.

**Steps:**

1. Send `GET /api/part/?limit=0`
2. Verify status `200`
3. Verify response body is a JSON array (not an object)
4. Verify array length is greater than `0`
5. Verify the response does not contain `count`, `next`, or `previous` fields

**Request:**

- Method: `GET`
- URL: `/api/part/?limit=0`
- Headers: `Authorization: Token <token>`

**Expected Result:** `limit=0` bypasses pagination and returns a raw JSON array of all matching parts. No envelope fields.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response: raw JSON array, length `1442`
- No `count`, `next`, or `previous` fields
- First element: `{ "pk": 82, "name": "1551ABK", "active": true, ... }`
- Matches spec: Yes

---

## TC-APFLT-008: limit=-1 is treated as limit=0 — returns unpaginated raw array

**Type:** API | **Priority:** P3

**Preconditions:** At least one part exists.

**Steps:**

1. Send `GET /api/part/?limit=-1`
2. Verify status `200`; verify response body is a JSON array
3. Record array length as `NEG_LENGTH`
4. Send `GET /api/part/?limit=0` — record array length as `ZERO_LENGTH`
5. Verify `NEG_LENGTH == ZERO_LENGTH`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?limit=-1`
- Headers: `Authorization: Token <token>`

**Expected Result:** `limit=-1` is normalized to `limit=0`: response is a raw JSON array with no envelope. No HTTP error is raised.

**Observed** (probed 2026-04-14):

- `limit=-1`: status `200`, raw array, length `1442`
- `limit=0`: status `200`, raw array, length `1442`
- Lengths equal — behavior is identical
- Matches spec: Yes

---

## TC-APFLT-009: offset beyond total count returns empty results with accurate count

**Type:** API | **Priority:** P3

**Preconditions:** At least one part exists.

**Steps:**

1. Send `GET /api/part/?limit=5` — record `count` as `TOTAL_COUNT`
2. Send `GET /api/part/?limit=5&offset=99999`
3. Verify status `200`
4. Verify response is a paginated envelope object
5. Verify `count == TOTAL_COUNT`
6. Verify `results` is `[]`
7. Verify `next` is `null`
8. Verify `previous` is a non-null URI

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?limit=5&offset=99999`
- Headers: `Authorization: Token <token>`

**Expected Result:** HTTP 200 returned (no 404/400). Envelope contains actual total in `count`, empty `results`, `next=null`, and a `previous` URL.

**Observed** (probed 2026-04-14):

- Status: `200`
- `count: 1442`, `results: []`, `next: null`, `previous: "...offset=99994"`
- Matches spec: Yes

---

## TC-APFLT-010: offset=-1 is treated as offset=0 — returns first page

**Type:** API | **Priority:** P3

**Preconditions:** At least 5 parts exist.

**Steps:**

1. Send `GET /api/part/?limit=5&offset=0` — record pk values as `PAGE_0_PKS`
2. Send `GET /api/part/?limit=5&offset=-1`
3. Verify status `200`; verify `previous` is `null`
4. Record pk values as `NEG_OFFSET_PKS`
5. Verify `NEG_OFFSET_PKS == PAGE_0_PKS`

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?limit=5&offset=-1`
- Headers: `Authorization: Token <token>`

**Expected Result:** Negative `offset` is silently normalized to `0`. First page returned without error.

**Observed** (probed 2026-04-14):

- Status: `200`, `previous: null`, first pk `82` — same as `offset=0`
- Matches spec: Yes

---

## TC-APFLT-011: limit exceeding total count returns all parts in a single page

**Type:** API | **Priority:** P2

**Preconditions:** Valid auth token available.

**Steps:**

1. Send `GET /api/part/?limit=1` — record `count` as `TOTAL_COUNT`
2. Send `GET /api/part/?limit=5000`
3. Verify status `200`
4. Verify `count == TOTAL_COUNT`
5. Verify `results` length equals `TOTAL_COUNT`
6. Verify `next` is `null`

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?limit=5000`
- Headers: `Authorization: Token <token>`

**Expected Result:** A single-page envelope containing all parts when `limit` exceeds the total. `next: null`.

**Observed** (probed 2026-04-14):

- `count: 1453`, `results` length: `1453`, `next: null`
- `results.length == count` confirmed
- Matches spec: Yes

---

## TC-APFLT-012: search= empty string is equivalent to no search filter

**Type:** API | **Priority:** P2

**Preconditions:** At least one part exists.

**Steps:**

1. Send `GET /api/part/?limit=3` — record `count` as `UNFILTERED_COUNT`
2. Send `GET /api/part/?search=&limit=3`
3. Verify status `200`; verify `count == UNFILTERED_COUNT`

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?search=&limit=3`
- Headers: `Authorization: Token <token>`

**Expected Result:** An empty `search` string is a no-op — same count as unfiltered request.

**Observed** (probed 2026-04-14):

- Unfiltered `count: 1453`; `search=` count: `1453`
- Matches spec: Yes

---

## TC-APFLT-013: assembly=true filters to assembly parts only

**Type:** API | **Priority:** P1

**Preconditions:** Parts with `assembly=true` and `assembly=false` both exist.

**Steps:**

1. Send `GET /api/part/?assembly=true&limit=5`
2. Verify status `200`; verify `count > 0`; verify all results have `assembly: true`
3. Record `count` as `ASSEMBLY_COUNT`
4. Send `GET /api/part/?assembly=false&limit=5`
5. Verify status `200`; verify all results have `assembly: false`; record `count` as `NON_ASSEMBLY_COUNT`
6. Send `GET /api/part/?limit=1` — record `count` as `TOTAL_COUNT`
7. Verify `ASSEMBLY_COUNT + NON_ASSEMBLY_COUNT == TOTAL_COUNT`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?assembly=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `assembly=true` returns only assembly parts. The two counts sum to the total.

**Observed** (probed 2026-04-14):

- `assembly=true`: count `136`, sample: `pk=1934 "AllFlagsPart-TC004-..." assembly: true`, `pk=1921 "APIPart-01ad2779" assembly: true`
- All results have `assembly: true`
- Matches spec: Yes

---

## TC-APFLT-014: virtual=true filters to virtual parts only

**Type:** API | **Priority:** P2

**Preconditions:** Parts with `virtual=true` exist.

**Steps:**

1. Send `GET /api/part/?virtual=true&limit=5`
2. Verify status `200`; verify `count > 0`; verify all results have `virtual: true`
3. Send `GET /api/part/?virtual=false&limit=5`
4. Verify status `200`; verify all results have `virtual: false`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?virtual=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `virtual=true` returns only virtual parts. Every returned part has `virtual: true`.

**Observed** (probed 2026-04-14):

- `virtual=true`: count `33`, sample: `pk=914 "CRM license" virtual: true`, `pk=1934 "AllFlagsPart-TC004-..." virtual: true`
- Matches spec: Yes

---

## TC-APFLT-015: trackable=true filters to serial-tracked parts only

**Type:** API | **Priority:** P2

**Preconditions:** Parts with `trackable=true` exist.

**Steps:**

1. Send `GET /api/part/?trackable=true&limit=5`
2. Verify status `200`; verify `count > 0`; verify all results have `trackable: true`
3. Send `GET /api/part/?trackable=false&limit=5`
4. Verify status `200`; verify all results have `trackable: false`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?trackable=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `trackable=true` returns only serialized parts. Every returned part has `trackable: true`.

**Observed** (probed 2026-04-14):

- `trackable=true`: count `58`, sample: `pk=1217 "AUTO_QA_TRACKABLE_PART" trackable: true`
- Matches spec: Yes

---

## TC-APFLT-016: has_stock=true and has_stock=false partition all parts by stock presence

**Type:** API | **Priority:** P1

**Preconditions:** Parts with and without stock both exist.

**Steps:**

1. Send `GET /api/part/?has_stock=true&limit=5`
2. Verify status `200`; verify `count > 0`; verify all results have `in_stock > 0`; record `count` as `HAS_STOCK_COUNT`
3. Send `GET /api/part/?has_stock=false&limit=5`
4. Verify status `200`; verify all results have `in_stock == 0`; record `count` as `NO_STOCK_COUNT`
5. Send `GET /api/part/?limit=1` — record `count` as `TOTAL_COUNT`
6. Verify `HAS_STOCK_COUNT + NO_STOCK_COUNT == TOTAL_COUNT`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?has_stock=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `has_stock=true` → `in_stock > 0` for all results. `has_stock=false` → `in_stock == 0`. The two counts sum to total.

**Observed** (probed 2026-04-14):

- `has_stock=true`: count `404`, sample: `pk=82 "1551ABK" in_stock: 1867`
- `has_stock=false`: count `1038`, sample: `pk=86 "1553WDBK" in_stock: 0`
- `404 + 1038 = 1442` — sum matches total
- Matches spec: Yes

---

## TC-APFLT-017: purchaseable=true&salable=true combines boolean filters with AND logic

**Type:** API | **Priority:** P2

**Preconditions:** Parts with `purchaseable=true` and `salable=true` both exist.

**Steps:**

1. Send `GET /api/part/?purchaseable=true&limit=1` — record `count` as `PURCHASEABLE_COUNT`
2. Send `GET /api/part/?salable=true&limit=1` — record `count` as `SALABLE_COUNT`
3. Send `GET /api/part/?purchaseable=true&salable=true&limit=5`
4. Verify status `200`; verify `count > 0`
5. Verify `count <= PURCHASEABLE_COUNT` and `count <= SALABLE_COUNT`
6. Verify all results have `purchaseable: true` and `salable: true`

**Request (step 3):**

- Method: `GET`
- URL: `/api/part/?purchaseable=true&salable=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** Boolean filters combine as AND. Combined count ≤ each individual filter count. Every result satisfies both conditions.

**Observed** (probed 2026-04-14):

- Combined: count `38`, sample: `pk=1161 "Buy-Sell Part 1776151541544" purchaseable: true salable: true`
- All results have both flags true
- Matches spec: Yes

---

## TC-APFLT-018: IPN exact match returns the single matching part; non-existent IPN returns empty

**Type:** API | **Priority:** P1

**Preconditions:** Part with `IPN="RES-001"` (pk=1692) exists. No part with `IPN="RES-999"` exists.

**Steps:**

1. Send `GET /api/part/?IPN=RES-001&limit=5`
2. Verify status `200`; verify `count == 1`; verify `results[0].IPN == "RES-001"`; verify `results[0].pk == 1692`
3. Send `GET /api/part/?IPN=RES-999&limit=5`
4. Verify status `200`; verify `count == 0`; verify `results == []`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?IPN=RES-001&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `IPN` filter returns exactly the part with that IPN. A non-existent IPN returns `count: 0`.

**Observed** (probed 2026-04-14):

- `IPN=RES-001`: count `1`, result `pk=1692 name="Full Field Part" IPN="RES-001"`
- `IPN=RES-999`: count `0`, results `[]`
- Note: `IPN=res-001` (lowercase) also returned `pk=1692` — demo DB collation is case-insensitive despite docs stating case-sensitive
- Matches spec: Partial (case-insensitivity diverges from documented behavior)

---

## TC-APFLT-019: IPN_regex=^RES matches all parts whose IPN starts with "RES"

**Type:** API | **Priority:** P2

**Preconditions:** Multiple parts with `IPN` starting with `"RES"` exist.

**Steps:**

1. Send `GET /api/part/?IPN_regex=%5ERES&limit=10`
   (`%5E` is URL-encoded `^`)
2. Verify status `200`; verify `count > 1`
3. Verify all results have `IPN` starting with `"RES"`
4. Send `GET /api/part/?IPN_regex=%5EXYZ_NOMATCH_99999&limit=5`
5. Verify status `200`; verify `count == 0`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?IPN_regex=%5ERES&limit=10`
- Headers: `Authorization: Token <token>`

**Expected Result:** `IPN_regex` applies a regex match on the IPN field. `^RES` returns all parts whose IPN begins with "RES".

**Observed** (probed 2026-04-14):

- count `7`, sample: `IPN="RES-001"`, `IPN="RES-10K-001"`, `IPN="RES-1K-1776148821705"`
- All IPNs start with `"RES"` — regex anchor works
- Matches spec: Yes

---

## TC-APFLT-020: name_regex=^R filters parts whose name starts with "R"

**Type:** API | **Priority:** P2

**Preconditions:** Multiple parts with names starting with `"R"` exist.

**Steps:**

1. Send `GET /api/part/?name_regex=%5ER&limit=5`
2. Verify status `200`; verify `count > 1`
3. Verify all results have `name` starting with `"R"`

**Request:**

- Method: `GET`
- URL: `/api/part/?name_regex=%5ER&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `name_regex=^R` returns only parts whose `name` begins with `"R"`.

**Observed** (probed 2026-04-14):

- count `93`, first 5: `"R_100K_0402_1%"`, `"R_100K_0603_1%"`, `"R_100K_0805_1%"`, `"R_100R_0402_1%"`, `"R_100R_0603_1%"`
- All names start with `"R"`
- Matches spec: Yes

---

## TC-APFLT-021: created_after and created_before filter by creation date range

**Type:** API | **Priority:** P2

**Preconditions:** Parts created between 2026-01-01 and 2026-04-14 exist. No parts created after 2030-01-01.

**Steps:**

1. Send `GET /api/part/?created_after=2026-01-01&created_before=2026-04-14&limit=10`
2. Verify status `200`; verify `count > 0`
3. Verify all results have `creation_date >= "2026-01-01"` and `creation_date <= "2026-04-14"`
4. Send `GET /api/part/?created_after=2030-01-01&limit=5`
5. Verify status `200`; verify `count == 0`; verify `results == []`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?created_after=2026-01-01&created_before=2026-04-14&limit=10`
- Headers: `Authorization: Token <token>`

**Expected Result:** Date-range filters restrict results to the specified `creation_date` window. A fully future range returns zero results.

**Observed** (probed 2026-04-14):

- Date range: count `7`, sample: `pk=914 "CRM license" creation_date="2026-03-30"`, `pk=915 "Encabulator" creation_date="2026-04-03"`
- All dates within range
- Future date `2030-01-01`: count `0`, results `[]`
- Matches spec: Yes

---

## TC-APFLT-022: category with cascade=true includes parts in child categories

**Type:** API | **Priority:** P1

**Preconditions:**

- Category `pk=24` ("Category 1", `parent=null`) has at least one direct part (pk=1933)
- Category `pk=26` ("Category 3", `parent=24`) has at least one part (pk=2267)

**Steps:**

1. Send `GET /api/part/?category=24&cascade=true&limit=10`
2. Verify status `200`; verify `count > 0`
3. Verify at least one result has `category == 24`
4. Verify at least one result has `category == 26` (child category)
5. Send `GET /api/part/?category=26&limit=5`
6. Verify all results have `category == 26`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?category=24&cascade=true&limit=10`
- Headers: `Authorization: Token <token>`

**Expected Result:** `cascade=true` includes parts assigned to all descendant categories. Results include parts from both the specified category and its children.

**Observed** (probed 2026-04-14):

- `category=24&cascade=true`: count `2`, results: `pk=1933 category=24`, `pk=2267 category=26`
- Category 26 confirmed as child of 24 (`parent=24`)
- Both parent and child category parts returned
- Note: demo also returns child parts without explicit `cascade=true` — cascade behavior defaults to true
- Matches spec: Yes (cascade inclusion confirmed)

---

## TC-APFLT-023: ordering=invalid_field silently returns HTTP 200 with default order

**Type:** API | **Priority:** P3

**Preconditions:** At least 3 parts exist.

**Steps:**

1. Send `GET /api/part/?ordering=invalid_field&limit=3`
2. Verify status `200`
3. Verify response body has `count`, `next`, `previous`, and `results` fields
4. Verify `results` length equals `3`
5. Verify no `error` or validation field appears in the response body

**Request:**

- Method: `GET`
- URL: `/api/part/?ordering=invalid_field&limit=3`
- Headers: `Authorization: Token <token>`

**Expected Result:** Unrecognized `ordering` value is silently ignored. HTTP 200 returned with results in default database order. No validation error.

**Observed** (probed 2026-04-14):

- Status: `200`, count `1452`, results_length `3`, first name `"1551ABK"`
- No error field in response
- Matches spec: Yes

---

## TC-APFLT-024: ordering=-in_stock sorts results by stock quantity descending

**Type:** API | **Priority:** P2

**Preconditions:** At least 3 parts with different `in_stock` values exist.

**Steps:**

1. Send `GET /api/part/?ordering=-in_stock&limit=3`
2. Verify status `200`; record `in_stock` values as `STOCK_VALUES`
3. Verify `STOCK_VALUES[0] >= STOCK_VALUES[1] >= STOCK_VALUES[2]`
4. Send `GET /api/part/?ordering=in_stock&limit=3`
5. Verify status `200`; record `in_stock` values as `STOCK_VALUES_ASC`
6. Verify `STOCK_VALUES_ASC[0] <= STOCK_VALUES_ASC[1] <= STOCK_VALUES_ASC[2]`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?ordering=-in_stock&limit=3`
- Headers: `Authorization: Token <token>`

**Expected Result:** `ordering=-in_stock` returns highest-stock parts first. `ordering=in_stock` returns lowest-stock first.

**Observed** (probed 2026-04-14):

- `ordering=-in_stock`: `pk=23 in_stock: 19625`, `pk=46 in_stock: 18090`, `pk=38 in_stock: 15803`
- `19625 >= 18090 >= 15803` — descending confirmed
- Matches spec: Yes

---

## TC-APFLT-025: combined search + boolean filters apply AND logic to narrow results

**Type:** API | **Priority:** P1

**Preconditions:**

- Parts with `assembly=true` and `active=true` exist
- At least one such part has `"resistor"` in a searchable field (pk=1692 has `keywords="resistor test"`)

**Steps:**

1. Send `GET /api/part/?assembly=true&limit=1` — record `count` as `ASSEMBLY_COUNT`
2. Send `GET /api/part/?active=true&limit=1` — record `count` as `ACTIVE_COUNT`
3. Send `GET /api/part/?assembly=true&active=true&limit=5`
4. Verify status `200`; record `count` as `COMBINED_COUNT`
5. Verify `COMBINED_COUNT <= ASSEMBLY_COUNT` and `COMBINED_COUNT <= ACTIVE_COUNT`
6. Verify all results have `assembly: true` and `active: true`
7. Send `GET /api/part/?search=resistor&assembly=true&limit=5`
8. Verify status `200`; verify `count <= COMBINED_COUNT`
9. Verify all results have `assembly: true`

**Request (step 3):**

- Method: `GET`
- URL: `/api/part/?assembly=true&active=true&limit=5`
- Headers: `Authorization: Token <token>`

**Request (step 7):**

- Method: `GET`
- URL: `/api/part/?search=resistor&assembly=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** Multiple filters combine as AND. Each additional filter narrows the result set. All returned parts satisfy all active filter conditions.

**Observed** (probed 2026-04-14):

- `assembly=true`: count `136`
- `assembly=true&active=true`: count `135`; all results have `assembly: true active: true`
- `search=resistor&assembly=true`: count `1`, result `pk=1692 "Full Field Part" assembly: true`
- `136 → 135 → 1` — progressive AND narrowing confirmed
- Matches spec: Yes

---

## Divergences from Documentation

| Parameter | Documented Behavior | Observed Behavior |
|-----------|--------------------|--------------------|
| `IPN` filter | Case-sensitive exact match | Demo returns match on lowercase `res-001` for `IPN="RES-001"` — DB collation is case-insensitive |
| `category` without `cascade` | Direct members only | Child-category parts included without `cascade=true` — cascade defaults true on this instance |
