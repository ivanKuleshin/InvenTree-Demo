# Parts List & Detail — Read-Only Test Suite

**Suite prefix**: TC-APCRUD
**Endpoints**: `GET /api/part/`, `GET /api/part/{id}/`
**Date observed**: 2026-04-18
**Source**: demo.inventree.org

---

### TC-APCRUD-001: List parts with pagination — baseline response shape

**Type**: API
**Priority**: P1 (smoke)
**Endpoint**: `GET /api/part/`
**Preconditions**: At least one part exists in the system; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/?limit=5`.
3. User verifies status code is `200`.
4. User verifies response body is a paginated envelope with `count`, `next`, `previous`, and `results` fields.
5. User verifies `results` contains exactly 5 items.
6. User verifies each item has core fields: `pk`, `name`, `full_name`, `category`, `category_name`, `active`, `description`.

**Request**:

- Method: `GET`
- URL: `/api/part/?limit=5`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 200 OK with paginated envelope; `count` reflects total parts in system; each result item is a flat part object with category as integer ID (not expanded).

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "count": 435,
    "next": "https://demo.inventree.org/api/part/?limit=5&offset=5",
    "previous": null,
    "results": [
      {
        "pk": 82,
        "name": "1551ABK",
        "full_name": "1551ABK",
        "category": 15,
        "category_name": "Enclosures",
        "active": true,
        "description": "Small plastic enclosure, black"
      }
    ]
  }
  ```
- Matches spec: Yes

**Notes**: `category` is returned as an integer FK; `category_name` is a convenience string. No `parameters`, `category_detail`, or `tags` fields without flags.

---

### TC-APCRUD-002: List parts with `parameters=true` — parameters array injected

**Type**: API
**Priority**: P1 (smoke)
**Endpoint**: `GET /api/part/`
**Preconditions**: At least one part with associated parameters exists; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/?limit=3&parameters=true`.
3. User verifies status code is `200`.
4. User verifies each result item includes a `parameters` array.
5. User verifies each entry in `parameters` has fields: `pk`, `template`, `data`, `data_numeric`, `template_detail`.
6. User verifies `template_detail` contains `name`, `units`, `description`, `checkbox`, `choices`.

**Request**:

- Method: `GET`
- URL: `/api/part/?limit=3&parameters=true`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 200 OK; each part includes `parameters` array with full `template_detail` sub-object. Parts with no parameters have an empty array.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 82,
    "name": "1551ABK",
    "parameters": [
      {
        "pk": 14495,
        "template": 235,
        "model_type": "part.part",
        "model_id": 82,
        "data": "35",
        "data_numeric": 35.0,
        "note": "",
        "updated": null,
        "updated_by": null,
        "template_detail": {
          "pk": 235,
          "name": "Length",
          "units": "mm",
          "description": "Part length",
          "checkbox": false,
          "choices": "",
          "enabled": true
        },
        "updated_by_detail": null
      }
    ]
  }
  ```
- Matches spec: Yes

**Notes**: Part 82 (1551ABK) has 4 parameters: Length, Width, Height, Color. `data` is always a string; `data_numeric` is float for numeric values or null for non-numeric (e.g., "Black" → null). `updated_by_detail` is null when not set.

---

### TC-APCRUD-003: List parts with `category_detail=true` — category object injected

**Type**: API
**Priority**: P1 (smoke)
**Endpoint**: `GET /api/part/`
**Preconditions**: At least one part assigned to a category exists; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/?limit=3&category_detail=true`.
3. User verifies status code is `200`.
4. User verifies each result item includes a `category_detail` object.
5. User verifies `category_detail` contains: `pk`, `name`, `description`, `pathstring`, `level`, `parent`, `starred`, `structural`.
6. User verifies `category_name` (string) is still present alongside `category_detail`.

**Request**:

- Method: `GET`
- URL: `/api/part/?limit=3&category_detail=true`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 200 OK; each part has `category_detail` object replacing the integer FK with a full category object; `category` integer field is still present.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 82,
    "name": "1551ABK",
    "category": 15,
    "category_name": "Enclosures",
    "category_detail": {
      "pk": 15,
      "name": "Enclosures",
      "description": "Enclosures, boxes, etc",
      "default_location": null,
      "default_keywords": "",
      "level": 1,
      "parent": 2,
      "part_count": null,
      "subcategories": null,
      "pathstring": "Mechanical/Enclosures",
      "starred": false,
      "structural": false,
      "icon": "",
      "parent_default_location": null
    }
  }
  ```
- Matches spec: Yes

**Notes**: `category_detail.part_count` and `category_detail.subcategories` are `null` in list context. `pathstring` provides the full breadcrumb path (e.g., "Mechanical/Enclosures"). Both `category` (integer) and `category_name` remain present.

---

### TC-APCRUD-004: List parts with `path_detail=true` — category breadcrumb array injected

**Type**: API
**Priority**: P2 (regression)
**Endpoint**: `GET /api/part/`
**Preconditions**: At least one part assigned to a nested category exists; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/?limit=3&path_detail=true`.
3. User verifies status code is `200`.
4. User verifies each result item includes a `category_path` array.
5. User verifies each entry in `category_path` has `pk` and `name`.

**Request**:

- Method: `GET`
- URL: `/api/part/?limit=3&path_detail=true`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 200 OK; each part gains `category_path` array listing ancestor categories from root to direct parent.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 82,
    "name": "1551ABK",
    "category_path": [
      { "pk": 2, "name": "Mechanical" },
      { "pk": 15, "name": "Enclosures" }
    ]
  }
  ```
- Matches spec: Yes

**Notes**: The query parameter is `path_detail=true` but the response field key is `category_path` (an array). Order is root-to-leaf. This is a breadcrumb representation distinct from the `pathstring` in `category_detail`.

---

### TC-APCRUD-005: List parts without `limit` — returns unbounded array (no pagination envelope)

**Type**: API
**Priority**: P2 (regression)
**Endpoint**: `GET /api/part/`
**Preconditions**: Parts exist; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/` (no `limit` parameter).
3. User verifies status code is `200`.
4. User verifies response is a JSON array (not an object with `count`/`results`).

**Request**:

- Method: `GET`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 200 OK; response is a plain JSON array containing all parts without pagination envelope.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  [
    { "pk": 82, "name": "1551ABK", ... },
    ...
  ]
  ```
  Total array length: 435 items.
- Matches spec: No (schema docs show PaginatedPartList envelope, but actual behavior without limit returns a plain array)

**Notes**: This is a divergence from the documented `PaginatedPartList` schema. The API silently switches response format when `limit` is omitted — returning a flat array of all 435 parts instead of a paginated envelope. Callers should always pass `limit` to get predictable response shape.

---

### TC-APCRUD-006: Single part detail — baseline response shape

**Type**: API
**Priority**: P1 (smoke)
**Endpoint**: `GET /api/part/{id}/`
**Preconditions**: Part with known ID exists; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/82/`.
3. User verifies status code is `200`.
4. User verifies response contains fields: `pk`, `name`, `full_name`, `description`, `category`, `category_name`, `active`, `notes`.
5. User verifies `pk` equals `82`.

**Request**:

- Method: `GET`
- URL: `/api/part/82/`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 200 OK; single part object with all fields. Detail endpoint includes `notes` field absent from list results.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 82,
    "name": "1551ABK",
    "full_name": "1551ABK",
    "description": "Small plastic enclosure, black",
    "category": 15,
    "category_name": "Enclosures",
    "active": true,
    "notes": null,
    "in_stock": 166.0,
    "stock_item_count": 3
  }
  ```
- Matches spec: Yes

**Notes**: The detail endpoint returns `notes` (null if not set), which is absent from list endpoint results. All numeric stock/allocation fields are floats. `purchaseable` field spelling observed in response (note: differs from `purchasable` seen in some docs).

---

### TC-APCRUD-007: Single part detail with `parameters=true` — parameters array injected

**Type**: API
**Priority**: P1 (smoke)
**Endpoint**: `GET /api/part/{id}/`
**Preconditions**: Part with parameters exists; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/82/?parameters=true`.
3. User verifies status code is `200`.
4. User verifies response includes a `parameters` array.
5. User verifies each parameter entry contains `pk`, `template`, `data`, `data_numeric`, `template_detail`.

**Request**:

- Method: `GET`
- URL: `/api/part/82/?parameters=true`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 200 OK; `parameters` array present with one entry per parameter. Each has full `template_detail` sub-object.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 82,
    "name": "1551ABK",
    "parameters": [
      {
        "pk": 14495,
        "template": 235,
        "data": "35",
        "data_numeric": 35.0,
        "template_detail": {
          "pk": 235,
          "name": "Length",
          "units": "mm"
        }
      },
      {
        "pk": 14518,
        "template": 239,
        "data": "Black",
        "data_numeric": null,
        "template_detail": {
          "pk": 239,
          "name": "Color",
          "choices": "Red, Blue, Green, Orange, Yellow, Black, White, Pink, Purple"
        }
      }
    ]
  }
  ```
- Matches spec: Yes

**Notes**: 4 parameters returned for part 82. Non-numeric parameters (e.g., "Black") have `data_numeric: null`. `choices` field in `template_detail` lists valid values as comma-separated string.

---

### TC-APCRUD-008: Single part detail with `category_detail=true` — category object injected

**Type**: API
**Priority**: P1 (smoke)
**Endpoint**: `GET /api/part/{id}/`
**Preconditions**: Part assigned to a category exists; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/82/?category_detail=true`.
3. User verifies status code is `200`.
4. User verifies response includes a `category_detail` object.
5. User verifies `category_detail` contains `pk`, `name`, `pathstring`, `level`, `parent`.

**Request**:

- Method: `GET`
- URL: `/api/part/82/?category_detail=true`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 200 OK; `category_detail` object present with full category metadata. `category` integer field still present.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 82,
    "category": 15,
    "category_name": "Enclosures",
    "category_detail": {
      "pk": 15,
      "name": "Enclosures",
      "description": "Enclosures, boxes, etc",
      "pathstring": "Mechanical/Enclosures",
      "level": 1,
      "parent": 2
    }
  }
  ```
- Matches spec: Yes

**Notes**: Same `category_detail` shape as in list endpoint. `pathstring` allows reading the full path without a separate category lookup.

---

### TC-APCRUD-009: Single part detail with `parameters=true` and `category_detail=true` combined

**Type**: API
**Priority**: P2 (regression)
**Endpoint**: `GET /api/part/{id}/`
**Preconditions**: Part with parameters assigned to a category exists; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/82/?parameters=true&category_detail=true`.
3. User verifies status code is `200`.
4. User verifies response includes both `parameters` array AND `category_detail` object.
5. User verifies the two expanded fields are independent and non-overlapping.

**Request**:

- Method: `GET`
- URL: `/api/part/82/?parameters=true&category_detail=true`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 200 OK; both `parameters` and `category_detail` are present simultaneously; no field collisions.

**Observed** (filled during probe):

- Status: `200`
- Observed: `category_detail` present with 14 keys; `parameters` array with 4 entries.
- Matches spec: Yes

**Notes**: Multiple expansion flags can be combined without conflict. Response size increases proportionally. `category_detail` keys: `pk`, `name`, `description`, `default_location`, `default_keywords`, `level`, `parent`, `part_count`, `subcategories`, `pathstring`, `starred`, `structural`, `icon`, `parent_default_location`.

---

### TC-APCRUD-010: Single part detail — non-existent ID returns 404

**Type**: API
**Priority**: P2 (regression)
**Endpoint**: `GET /api/part/{id}/`
**Preconditions**: No part with ID 99999 exists; valid credentials available.

**Steps**:

1. User obtains authorization token.
2. User sends a GET request to `/api/part/99999/`.
3. User verifies status code is `404`.
4. User verifies response body contains `detail` field with error message.

**Request**:

- Method: `GET`
- URL: `/api/part/99999/`
- Headers: `Authorization: Basic <base64>`

**Expected Result**: 404 Not Found; response body: `{"detail": "No Part matches the given query."}`.

**Observed** (filled during probe):

- Status: `404`
- Response snippet:
  ```json
  { "detail": "No Part matches the given query." }
  ```
- Matches spec: Yes

**Notes**: Error message follows DRF standard format with `detail` key. The message "No Part matches the given query." is the standard Django REST Framework lookup failure message.

---

### TC-APCRUD-011: List parts without authentication — 401 returned

**Type**: API
**Priority**: P3 (edge case)
**Endpoint**: `GET /api/part/`
**Preconditions**: No Authorization header is sent.

**Steps**:

1. User sends a GET request to `/api/part/?limit=3` with no Authorization header.
2. User verifies status code is `401`.

**Request**:

- Method: `GET`
- URL: `/api/part/?limit=3`
- Headers: (none)

**Expected Result**: 401 Unauthorized; access denied without credentials.

**Observed** (filled during probe):

- Status: `401`
- Matches spec: Yes

**Notes**: Both the list and detail endpoints require authentication. The demo site does not allow anonymous read access.

---

### TC-APCRUD-012: Single part detail without authentication — 401 returned

**Type**: API
**Priority**: P3 (edge case)
**Endpoint**: `GET /api/part/{id}/`
**Preconditions**: No Authorization header is sent.

**Steps**:

1. User sends a GET request to `/api/part/82/` with no Authorization header.
2. User verifies status code is `401`.

**Request**:

- Method: `GET`
- URL: `/api/part/82/`
- Headers: (none)

**Expected Result**: 401 Unauthorized; access denied without credentials.

**Observed** (filled during probe):

- Status: `401`
- Matches spec: Yes

**Notes**: Consistent with list endpoint — all Parts API endpoints require authentication.
