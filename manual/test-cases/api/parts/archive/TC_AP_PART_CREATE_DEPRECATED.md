# TC_AP_PART_CREATE — API Test Suite: Part Creation

**Application:** InvenTree Demo
**Base URL:** `https://demo.inventree.org`
**Auth:** HTTP Basic (`admin` / `inventree` — do not embed in automation)
**Approach:** Exploratory + Ad-hoc, grounded in live probe responses
**Created:** 2026-04-14

---

## Template Legend

| Field             | Description                                                          |
| ----------------- | -------------------------------------------------------------------- |
| **ID**            | Unique test case identifier                                          |
| **Priority**      | P1 (smoke) / P2 (regression) / P3 (edge case)                        |
| **Type**          | API / Functional / Security / Negative                               |
| **Endpoint**      | HTTP method + path                                                   |
| **Preconditions** | State required before execution                                      |
| **Steps**         | Numbered, executable actions                                         |
| **Request**       | Method, URL, headers, body                                           |
| **Expected**      | Per-spec outcome                                                     |
| **Observed**      | Real response recorded during probe (2026-04-14, demo.inventree.org) |
| **Notes**         | Divergences, caveats, environment behavior                           |

---

## TC-AP-PC-001: POST /api/part/ with minimal valid payload (name only)

**Priority:** P1
**Type:** API / Functional

**Preconditions:**

- Valid HTTP Basic credentials are available
- The name "TC-AP-PC-001-MinimalPart" does not already exist with an empty IPN and empty revision (uniqueness constraint: name + IPN + revision)

**Steps:**

1. Obtain valid HTTP Basic authorization header using admin credentials
2. Send POST request to `/api/part/` with body `{"name": "TC-AP-PC-001-MinimalPart"}`
3. Verify response status code is `201`
4. Verify response body contains field `pk` with an integer value
5. Verify response body contains `name` equal to `"TC-AP-PC-001-MinimalPart"`
6. Verify response body contains `active` equal to `true`
7. Verify response body contains `component` equal to `true`
8. Verify response body contains `purchasable` equal to `true`
9. Verify response body contains `assembly` equal to `false`
10. Verify response body contains `is_template` equal to `false`
11. Verify response body contains `virtual` equal to `false`
12. Verify response body contains `IPN` equal to `""`
13. Verify response body contains `description` equal to `""`
14. Verify response body contains `category` equal to `null`

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-AP-PC-001-MinimalPart" }
  ```

**Expected Result:** Server creates the part and returns `201 Created` with a full part object. All unspecified optional fields use server defaults: `active=true`, `component=true`, `purchasable=true`, all other booleans `false`, relational fields `null`.

**Observed** (probed 2026-04-14):

- Status: `201`
- Response snippet:
  ```json
  {
    "pk": 1454,
    "name": "TC-AP-PC-001-MinimalPart",
    "active": true,
    "component": true,
    "purchasable": true,
    "assembly": false,
    "is_template": false,
    "virtual": false,
    "IPN": "",
    "description": "",
    "category": null
  }
  ```
- Matches spec: Yes

**Notes:** Sending a second identical POST (same name, empty IPN, empty revision) returns `400` with `{"non_field_errors": ["The fields name, IPN, revision must make a unique set."]}`. The uniqueness constraint is enforced at the DB level, not just the application layer. Test data should use unique names per run (e.g., append a timestamp suffix).

---

## TC-AP-PC-002: POST /api/part/ with full optional fields payload

**Priority:** P1
**Type:** API / Functional

**Preconditions:**

- Valid HTTP Basic credentials are available
- A valid category PK exists on the server (obtain via `GET /api/part/category/?limit=5`)
- The name "TC-AP-PC-002-FullPart-v2" with IPN "IPN-TC-AP-002" and revision "B" does not already exist

**Steps:**

1. Send `GET /api/part/category/?limit=5` to retrieve a valid category `pk` (record it as `CATEGORY_PK`)
2. Send POST request to `/api/part/` with the full body shown below
3. Verify response status code is `201`
4. Verify response body field `name` equals `"TC-AP-PC-002-FullPart-v2"`
5. Verify response body field `IPN` equals `"IPN-TC-AP-002"`
6. Verify response body field `revision` equals `"B"`
7. Verify response body field `description` equals `"Full payload test part"`
8. Verify response body field `category` equals `CATEGORY_PK`
9. Verify response body field `keywords` equals `"test qa automation"`
10. Verify response body field `assembly` equals `true`
11. Verify response body field `is_template` equals `false`
12. Verify response body field `full_name` equals `"IPN-TC-AP-002 | TC-AP-PC-002-FullPart-v2 | B"`

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-AP-PC-002-FullPart-v2",
    "IPN": "IPN-TC-AP-002",
    "revision": "B",
    "description": "Full payload test part",
    "category": CATEGORY_PK,
    "keywords": "test qa automation",
    "assembly": true,
    "component": true,
    "purchasable": true,
    "salable": false,
    "is_template": false,
    "trackable": false,
    "virtual": false,
    "active": true
  }
  ```

**Expected Result:** Server returns `201 Created`. All provided fields are persisted and returned in the response. The computed field `full_name` is formatted as `"<IPN> | <name> | <revision>"`.

**Observed** (probed 2026-04-14):

- Status: `201`
- Response snippet:
  ```json
  {
    "pk": 1490,
    "name": "TC-AP-PC-002-FullPart-v2",
    "IPN": "IPN-TC-AP-002",
    "revision": "B",
    "description": "Full payload test part",
    "category": 1,
    "keywords": "test qa automation",
    "assembly": true,
    "full_name": "IPN-TC-AP-002 | TC-AP-PC-002-FullPart-v2 | B"
  }
  ```
- Matches spec: Yes

**Notes:** `full_name` is auto-computed and read-only — it cannot be set via POST. Format is `"<IPN> | <name> | <revision>"` when both IPN and revision are non-empty; when IPN is empty, IPN segment is omitted; when revision is empty, revision segment is omitted.

---

## TC-AP-PC-003: POST /api/part/ using the `duplicate` write-only field

**Priority:** P2
**Type:** API / Functional

**Preconditions:**

- Valid HTTP Basic credentials are available
- A source part exists with a known PK (obtain via `GET /api/part/?limit=5`, record as `SOURCE_PK`)
- The source part has a category assigned (to verify whether category is inherited)
- The name "TC-AP-PC-003-DuplicatePart" does not already exist

**Steps:**

1. Send `GET /api/part/?limit=5` to retrieve a source part PK with a non-null category (record as `SOURCE_PK` and `SOURCE_CATEGORY`)
2. Send POST to `/api/part/` with body containing `name` and `duplicate.part = SOURCE_PK`
3. Verify response status code is `201`
4. Verify response body field `name` equals `"TC-AP-PC-003-DuplicatePart"`
5. Verify response body does NOT contain the key `duplicate` (write-only — not returned)
6. Verify response body field `category` equals `null` (category is NOT inherited from the source part)
7. Retrieve the new part via `GET /api/part/<new_pk>/` and verify it exists and is independent of the source

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-AP-PC-003-DuplicatePart",
    "duplicate": { "part": SOURCE_PK, "copy_image": false, "copy_notes": false, "copy_parameters": false, "copy_bom": false }
  }
  ```

**Expected Result:** Server creates a new part using the source part as a template. The `duplicate` field is write-only and is not included in the response. The new part is independent (category is not automatically inherited from source).

**Observed** (probed 2026-04-14):

- Status: `201`
- Response snippet:
  ```json
  {
    "pk": 1494,
    "name": "TC-AP-PC-003-DuplicatePart",
    "category": null
  }
  ```
- The key `duplicate` is absent from the response body
- Matches spec: Yes

**Notes:** Category is not inherited from the source part when using the `duplicate` write-only field — the new part has `category: null` unless explicitly set in the same POST payload. This is a behavioral distinction to document for automation teams.

---

## TC-AP-PC-004: POST /api/part/ with `initial_stock` write-only field

**Priority:** P2
**Type:** API / Functional

**Preconditions:**

- Valid HTTP Basic credentials are available
- A valid stock location PK exists (obtain via `GET /api/stock/location/?limit=5`, record as `LOCATION_PK`)
- The name "TC-AP-PC-004-InitialStockPart" does not already exist

**Steps:**

1. Send `GET /api/stock/location/?limit=5` to retrieve a valid location PK (record as `LOCATION_PK`)
2. Send POST to `/api/part/` with body containing `name` and `initial_stock` object with `quantity=50` and `location=LOCATION_PK`
3. Verify response status code is `201`; record new part PK as `NEW_PART_PK`
4. Verify response body does NOT contain the key `initial_stock` (write-only)
5. Send `GET /api/stock/?part=NEW_PART_PK` to retrieve stock items for the new part
6. Verify the stock item list contains exactly one item
7. Verify the stock item has `quantity` equal to `50.0`
8. Verify the stock item has `location` equal to `LOCATION_PK`
9. Verify the stock item has `status` equal to `10` (OK status code)

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-AP-PC-004-InitialStockPart",
    "initial_stock": { "quantity": 50, "location": LOCATION_PK }
  }
  ```

**Expected Result:** Server creates the part and simultaneously creates one stock item with the specified quantity at the specified location. `initial_stock` is write-only and not returned in the part response.

**Observed** (probed 2026-04-14):

- Status: `201`; new part PK: `1498`
- Part response: key `initial_stock` absent from body
- `GET /api/stock/?part=1498` response snippet:
  ```json
  {
    "count": 1,
    "results": [
      {
        "pk": 2459,
        "quantity": 50.0,
        "location": 1,
        "status": 10
      }
    ]
  }
  ```
- Matches spec: Yes

**Notes:** The stock item is created atomically with the part in a single POST. Status code `10` corresponds to the "OK" stock status in InvenTree's internal enum. Verify `GET /api/stock/?part=<pk>` immediately after creation.

---

## TC-AP-PC-005: POST /api/part/ with `initial_supplier` write-only field

**Priority:** P2
**Type:** API / Functional

**Preconditions:**

- Valid HTTP Basic credentials are available
- A valid supplier company PK exists (obtain via `GET /api/company/?is_supplier=true&limit=5`, record as `SUPPLIER_PK`)
- The name "TC-AP-PC-005-InitialSupplierPart" does not already exist

**Steps:**

1. Send `GET /api/company/?is_supplier=true&limit=5` to retrieve a valid supplier PK (record as `SUPPLIER_PK`)
2. Send POST to `/api/part/` with body containing `name`, `purchasable=true`, and `initial_supplier` object with `supplier=SUPPLIER_PK` and a SKU
3. Verify response status code is `201`; record new part PK as `NEW_PART_PK`
4. Verify response body does NOT contain the key `initial_supplier` (write-only)
5. Send `GET /api/company/part/?part=NEW_PART_PK` to retrieve supplier parts for the new part
6. Verify the list contains exactly one supplier part
7. Verify the supplier part has `supplier` equal to `SUPPLIER_PK`
8. Verify the supplier part has `SKU` equal to `"SKU-TC-AP-005"`

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-AP-PC-005-InitialSupplierPart",
    "purchasable": true,
    "initial_supplier": { "supplier": SUPPLIER_PK, "SKU": "SKU-TC-AP-005" }
  }
  ```

**Expected Result:** Server creates the part and simultaneously creates one supplier part record linking the part to the specified supplier with the given SKU. `initial_supplier` is write-only and not returned in the part response.

**Observed** (probed 2026-04-14):

- Status: `201`; new part PK: `1502`
- Supplier used: PK `1` (DigiKey)
- Part response: key `initial_supplier` absent from body
- `GET /api/company/part/?part=1502` response snippet:
  ```json
  {
    "count": 1,
    "results": [
      {
        "pk": 302,
        "part": 1502,
        "supplier": 1,
        "SKU": "SKU-TC-AP-005"
      }
    ]
  }
  ```
- Matches spec: Yes

**Notes:** The `purchasable` flag must be `true` for a supplier part to be creatable. If `purchasable=false`, the `initial_supplier` sub-object is likely ignored or returns a validation error — this boundary was not probed but is worth a separate edge-case test.

---

## TC-AP-PC-006: POST /api/part/ with missing required field (name) returns 400

**Priority:** P2
**Type:** API / Negative

**Preconditions:**

- Valid HTTP Basic credentials are available
- No specific data state required (request will be rejected before persistence)

**Steps:**

1. Send POST request to `/api/part/` with an empty JSON body `{}`
2. Verify response status code is `400`
3. Verify response body contains the key `name`
4. Verify the value of `name` is an array containing the string `"This field is required."`
5. Verify no `pk` field is present in the response (no part was created)

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {}
  ```

**Expected Result:** Server rejects the request with `400 Bad Request`. Response body identifies `name` as a required field.

**Observed** (probed 2026-04-14):

- Status: `400`
- Response body:
  ```json
  { "name": ["This field is required."] }
  ```
- Matches spec: Yes

**Notes:** The error response follows Django REST Framework's standard validation error structure — field name as key, array of error strings as value. Automation assertions should use exact string matching on `"This field is required."`.

---

## TC-AP-PC-007: POST /api/part/ with invalid category foreign key returns 400

**Priority:** P2
**Type:** API / Negative

**Preconditions:**

- Valid HTTP Basic credentials are available
- Category PK `99999999` does not exist on the server (verify via `GET /api/part/category/99999999/` returning 404)

**Steps:**

1. Send `GET /api/part/category/99999999/` and confirm it returns `404` (confirming the PK does not exist)
2. Send POST to `/api/part/` with body `{"name": "TC-AP-PC-007-InvalidCategory", "category": 99999999}`
3. Verify response status code is `400`
4. Verify response body contains the key `category`
5. Verify the value of `category` is an array containing the string `"Invalid pk \"99999999\" - object does not exist."`
6. Verify no `pk` field is present in the response

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-AP-PC-007-InvalidCategory", "category": 99999999 }
  ```

**Expected Result:** Server rejects the request with `400 Bad Request`. Response body identifies `category` as the invalid field with a message naming the invalid PK.

**Observed** (probed 2026-04-14):

- Status: `400`
- Response body:
  ```json
  { "category": ["Invalid pk \"99999999\" - object does not exist."] }
  ```
- Matches spec: Yes

**Notes:** DRF FK validation returns `400` (not `404`) when a related object does not exist. The error message embeds the submitted PK value in the error string — automation assertions should use substring match `"Invalid pk"` + `"object does not exist"` to avoid hardcoding PK values.

---

## TC-AP-PC-008: POST /api/part/ without authentication returns 401

**Priority:** P3
**Type:** API / Security

**Preconditions:**

- No Authorization header is available (unauthenticated request)
- Optionally, an Authorization header with invalid credentials is prepared for the second scenario

**Steps:**

1. Send POST to `/api/part/` with body `{"name": "TC-AP-PC-008-NoAuth"}` and NO Authorization header
2. Verify response status code is `401`
3. Verify response body contains `detail` equal to `"Authentication credentials were not provided."`
4. Verify response headers contain `WWW-Authenticate` header (challenges the client to authenticate)
5. Send POST to `/api/part/` with body `{"name": "TC-AP-PC-008-BadCreds"}` and Authorization header containing invalid credentials (e.g., `Basic aW52YWxpZDppbnZhbGlk` = `invalid:invalid`)
6. Verify response status code is `401`
7. Verify response body contains `detail` equal to `"Invalid username/password."`

**Request (step 1):**

- Method: `POST`
- URL: `/api/part/`
- Headers: _(none — no Authorization header)_
- Body:
  ```json
  { "name": "TC-AP-PC-008-NoAuth" }
  ```

**Request (step 5):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic aW52YWxpZDppbnZhbGlk`
- Body:
  ```json
  { "name": "TC-AP-PC-008-BadCreds" }
  ```

**Expected Result:** Both unauthenticated and invalid-credential requests are rejected with HTTP `401`. The error detail message differs between the two scenarios.

**Observed** (probed 2026-04-14):

- No-auth status: `401`
- No-auth body: `{"detail": "Authentication credentials were not provided."}`
- Bad-creds status: `401`
- Bad-creds body: `{"detail": "Invalid username/password."}`
- Matches spec: Yes

**Notes:** The API returns `401` (not `403`) for both scenarios, which is consistent with HTTP semantics (unauthenticated vs. unauthorized). If the InvenTree instance is configured with `INVENTREE_LOGIN_ATTEMPTS_LOCKOUT`, repeated invalid-credential requests may eventually trigger a lockout — avoid hammering this endpoint in automation loops.

---

## TC-AP-PC-009: Import session API — create session, verify structure and auto-mapping

**Priority:** P1
**Type:** API / Functional

**Preconditions:**

- Valid HTTP Basic credentials are available
- A CSV file is prepared containing at minimum the columns: `name`, `description`, `IPN` with at least one data row
- The import session API is accessible at `/api/importer/session/`

**Steps:**

1. Send `GET /api/importer/session/` with valid auth
2. Verify response status code is `200`
3. Verify response body contains a `count` field and a `results` array
4. Prepare a CSV file with the following content:
   ```
   name,description,IPN
   ImportTestPart001,Imported via API test,IPN-IMPORT-001
   ```
5. Send POST to `/api/importer/session/` with `Content-Type: multipart/form-data`, field `model_type=part`, and field `data_file` containing the CSV file
6. Verify response status code is `201`
7. Verify response body contains field `pk` with an integer value (record as `SESSION_PK`)
8. Verify response body contains field `status` equal to `10` (initial/pending status)
9. Verify response body contains field `model_type` equal to `"part"`
10. Verify response body contains field `columns` as an array including `"name"`, `"description"`, and `"IPN"`
11. Verify response body contains field `column_mappings` as an array where columns `"name"`, `"description"`, and `"IPN"` are each mapped to the corresponding InvenTree field
12. Verify response body contains field `available_fields` as a non-empty array listing writable Part fields

**Request (step 1):**

- Method: `GET`
- URL: `/api/importer/session/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Request (step 5):**

- Method: `POST`
- URL: `/api/importer/session/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: multipart/form-data`
- Body (form fields):
  - `model_type`: `part`
  - `data_file`: `<CSV file attachment>`

**Expected Result:** Session is created with `201`. The server auto-detects and maps CSV column headers that match known Part field names. `available_fields` lists all writable Part fields, enabling field mapping in subsequent steps.

**Observed** (probed 2026-04-14):

- GET status: `200`, response: `{"count": N, "results": [...]}`
- POST status: `201`
- Response snippet:
  ```json
  {
    "pk": 1,
    "status": 10,
    "model_type": "part",
    "columns": ["name", "description", "IPN"],
    "column_mappings": [
      { "column": "name", "field": "name" },
      { "column": "description", "field": "description" },
      { "column": "IPN", "field": "IPN" }
    ],
    "available_fields": [
      { "field": "name", "label": "Name", "required": true },
      { "field": "description", "label": "Description", "required": false },
      { "field": "IPN", "label": "IPN", "required": false }
    ]
  }
  ```
- Matches spec: Yes

**Notes:** The `/api/importer/` root path returns `404` — only `/api/importer/session/` and its sub-resources are accessible. Auto-mapping works by exact case-sensitive match between CSV column headers and InvenTree field names (`name`, `description`, `IPN`). Columns that do not match any known field are included in `columns` but have no entry in `column_mappings`. Subsequent import steps (row processing, commit) require additional API calls to `/api/importer/session/<pk>/rows/` and `/api/importer/session/<pk>/accept_rows/` — these are out of scope for this test case but should be covered in a separate import flow test suite.
