# TC-ACCRUD — Part Categories CRUD Test Suite

**Suite:** TC-ACCRUD (eval2-categories-mutating)  
**Endpoint base:** `/api/part/category/`  
**Probed:** 2026-04-18 against `https://demo.inventree.org`  
**Auth:** HTTP Basic — credentials loaded from environment; never hard-coded.

---

### TC-ACCRUD-001: List part categories returns paginated result

**Type**: API  
**Priority**: P1 (smoke)  
**Endpoint**: `GET /api/part/category/`  
**Preconditions**: At least one part category exists in the system.

**Steps**:

1. User obtains authorization credentials.
2. User sends a GET request to `/api/part/category/?limit=5&offset=0`.
3. User verifies status code is `200`.
4. User verifies response body is a paginated envelope with `count`, `next`, `previous`, and `results` fields.
5. User verifies each item in `results` contains the expected category fields.

**Request**:

- Method: `GET`
- URL: `/api/part/category/?limit=5&offset=0`
- Headers: `Authorization: Basic <base64>`
- Body: none

**Expected Result**: Server returns HTTP 200 with a paginated list object. Each result item contains category fields: `pk`, `name`, `description`, `level`, `parent`, `part_count`, `subcategories`, `pathstring`, `starred`, `structural`, `icon`, `default_location`, `default_keywords`, `parent_default_location`.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "count": 27,
    "next": "https://demo.inventree.org/api/part/category/?limit=5&offset=5",
    "previous": null,
    "results": [
      {
        "pk": 23,
        "name": "Category 0",
        "description": "Part category, level 1",
        "default_location": null,
        "default_keywords": null,
        "level": 0,
        "parent": null,
        "part_count": 0,
        "subcategories": 5,
        "pathstring": "Category 0",
        "starred": false,
        "structural": false,
        "icon": "",
        "parent_default_location": null
      }
    ]
  }
  ```
- Matches spec: Yes

**Notes**: `icon` field returns empty string `""` rather than `null` when unset. `limit` query param was accepted and respected. Total of 27 categories in demo.

---

### TC-ACCRUD-002: Retrieve single part category by ID

**Type**: API  
**Priority**: P1 (smoke)  
**Endpoint**: `GET /api/part/category/{id}/`  
**Preconditions**: A part category with a known `pk` exists (e.g., pk=23 from list call).

**Steps**:

1. User obtains authorization credentials.
2. User sends a GET request to `/api/part/category/23/`.
3. User verifies status code is `200`.
4. User verifies response body is a single Category object (not paginated).
5. User verifies all schema fields are present in the response.

**Request**:

- Method: `GET`
- URL: `/api/part/category/23/`
- Headers: `Authorization: Basic <base64>`
- Body: none

**Expected Result**: Server returns HTTP 200 with a single Category object containing all schema fields: `pk`, `name`, `description`, `level`, `parent`, `part_count`, `subcategories`, `pathstring`, `starred`, `structural`, `icon`, `default_location`, `default_keywords`, `parent_default_location`.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 23,
    "name": "Category 0",
    "description": "Part category, level 1",
    "default_location": null,
    "default_keywords": null,
    "level": 0,
    "parent": null,
    "part_count": 0,
    "subcategories": 5,
    "pathstring": "Category 0",
    "starred": false,
    "structural": false,
    "icon": "",
    "parent_default_location": null
  }
  ```
- Matches spec: Yes

**Notes**: Single-resource endpoint returns the object directly (not wrapped in a list/paginated envelope). `level: 0` confirms this is a top-level category.

---

### TC-ACCRUD-003: Create a new part category

**Type**: API  
**Priority**: P1 (smoke)  
**Endpoint**: `POST /api/part/category/`  
**Preconditions**: None (creating a net-new top-level category).

**Steps**:

1. User obtains authorization credentials with write access.
2. User sends a POST request to `/api/part/category/` with a JSON body containing `name`, `description`, and `structural`.
3. User verifies status code is `201`.
4. User verifies response body contains the created category with a server-assigned `pk`.
5. User verifies `name` and `description` in the response match the request body.
6. User verifies `pathstring` equals the category name (top-level category, no parent).

**Request**:

- Method: `POST`
- URL: `/api/part/category/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-ACCRUD-Eval-Category",
    "description": "Test category for eval CRUD",
    "structural": false
  }
  ```

**Expected Result**: Server returns HTTP 201 with the newly created Category object. The `pk` is server-assigned. `name`, `description`, and `structural` match the request. `level` is `0`, `parent` is `null`, `pathstring` equals the name.

**Observed** (filled during probe):

- Status: `201`
- Response snippet:
  ```json
  {
    "pk": 30,
    "name": "TC-ACCRUD-Eval-Category",
    "description": "Test category for eval CRUD",
    "default_location": null,
    "default_keywords": null,
    "level": 0,
    "parent": null,
    "part_count": null,
    "subcategories": null,
    "pathstring": "TC-ACCRUD-Eval-Category",
    "starred": false,
    "structural": false,
    "icon": "",
    "parent_default_location": null,
    "path": [{"pk": 30, "name": "TC-ACCRUD-Eval-Category"}]
  }
  ```
- Matches spec: Yes

**Notes**: `part_count` and `subcategories` are `null` on newly created category (not `0`). Response includes an extra `path` array field (breadcrumb path) not listed in the schema snapshot — this field appears on create and update responses. Automation should assert `pk` is a positive integer rather than an exact value.

---

### TC-ACCRUD-004: Rename a part category with PATCH

**Type**: API  
**Priority**: P1 (smoke)  
**Endpoint**: `PATCH /api/part/category/{id}/`  
**Preconditions**: A part category exists with a known `pk` (created in TC-ACCRUD-003 or dynamically created as a precondition step). Category `pk` used in probe: `30`.

**Steps**:

1. User obtains authorization credentials with write access.
2. User creates a test category (or reuses one created for this purpose) to get its `pk`.
3. User sends a PATCH request to `/api/part/category/{pk}/` with a JSON body containing only `name`.
4. User verifies status code is `200`.
5. User verifies `name` in the response matches the new name sent.
6. User verifies `pathstring` is updated to reflect the new name.
7. User verifies other fields (e.g., `description`) are unchanged.

**Request**:

- Method: `PATCH`
- URL: `/api/part/category/30/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-ACCRUD-Eval-Category-Renamed"
  }
  ```

**Expected Result**: Server returns HTTP 200 with the updated Category object. `name` reflects the new value. `pathstring` is updated to match the new name. `description` and other fields remain unchanged from creation.

**Observed** (filled during probe):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 30,
    "name": "TC-ACCRUD-Eval-Category-Renamed",
    "description": "Test category for eval CRUD",
    "default_location": null,
    "default_keywords": null,
    "level": 0,
    "parent": null,
    "part_count": 0,
    "subcategories": 0,
    "pathstring": "TC-ACCRUD-Eval-Category-Renamed",
    "starred": false,
    "structural": false,
    "icon": "",
    "parent_default_location": null,
    "path": [{"pk": 30, "name": "TC-ACCRUD-Eval-Category-Renamed"}]
  }
  ```
- Matches spec: Yes

**Notes**: PATCH is a true partial update — only `name` was sent and only `name`/`pathstring` changed; `description` was preserved. After PATCH, `part_count` and `subcategories` resolve to `0` (not `null` as on create). The extra `path` array field updates alongside `name`.

---

### TC-ACCRUD-005: Delete a part category

**Type**: API  
**Priority**: P1 (smoke)  
**Endpoint**: `DELETE /api/part/category/{id}/`  
**Preconditions**: A part category exists with a known `pk` that has no child categories and no parts assigned (to avoid relational constraint issues). Category `pk` used in probe: `30`.

**Steps**:

1. User obtains authorization credentials with write access.
2. User creates a test category (or uses one already created for this purpose) to get its `pk`.
3. User sends a DELETE request to `/api/part/category/{pk}/`.
4. User verifies status code is `204`.
5. User verifies the response body is empty.
6. User optionally sends a GET request to `/api/part/category/{pk}/` and verifies a `404` response to confirm deletion.

**Request**:

- Method: `DELETE`
- URL: `/api/part/category/30/`
- Headers: `Authorization: Basic <base64>`
- Body: none

**Expected Result**: Server returns HTTP 204 with no response body, indicating successful deletion.

**Observed** (filled during probe):

- Status: `204`
- Response snippet: (empty body)
- Matches spec: Yes

**Notes**: 204 No Content with empty body matches the schema spec exactly. Automation should create a fresh category in the precondition step rather than relying on a hardcoded `pk`, since the demo environment is shared and pks increment.
