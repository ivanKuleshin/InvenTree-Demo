# API Test Suite — POST /api/part/ Validation & Negative Cases

**Suite**: eval3-validation-negative  
**TC Prefix**: TC-APVAL  
**Endpoint**: `POST /api/part/`  
**Date**: 2026-04-18  
**Source**: demo.inventree.org (live probes 2026-04-14) + schema docs/api/schemas/part.md  

---

### TC-APVAL-007: POST /api/part/ with empty body returns 400 for missing required `name` field

**Type**: API  
**Priority**: P2  
**Endpoint**: `POST /api/part/`  
**Preconditions**: Valid HTTP Basic credentials available. No specific data state required — request is rejected before persistence.

**Steps**:

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with an empty JSON body `{}`
3. Verify response status code is `400`
4. Verify response body contains key `"name"`
5. Verify the value of `"name"` is an array containing `"This field is required."`
6. Verify response body does NOT contain a `"pk"` field

**Request**:

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {}
  ```

**Expected Result**: Server rejects the request with `400 Bad Request`. Response body identifies `name` as a required field using the standard DRF error structure.

**Observed** (filled during probe):

- Status: `400` [ASSUMED — not directly probed for POST /api/part/ in this session; inferred from TC-APVAL-001 which observed identical DRF behavior on POST /api/part/category/ with `{}` body on 2026-04-14, same live server]
- Response snippet:
  ```json
  { "name": ["This field is required."] }
  ```
- Matches spec: Yes [ASSUMED]

**Notes**: The `name` field is the only required, writable field on the Part schema. All other fields are optional or read-only. DRF serializer enforces required fields consistently across all endpoints. TC-APVAL-001 confirmed this exact pattern for categories on the same server.

---

### TC-APVAL-008: POST /api/part/ with explicit null `name` returns 400

**Type**: API  
**Priority**: P2  
**Endpoint**: `POST /api/part/`  
**Preconditions**: Valid HTTP Basic credentials available. No specific data state required.

**Steps**:

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": null}`
3. Verify response status code is `400`
4. Verify response body contains key `"name"`
5. Verify the value of `"name"` identifies the field as non-nullable or required
6. Verify response body does NOT contain a `"pk"` field

**Request**:

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": null }
  ```

**Expected Result**: Server rejects the request with `400 Bad Request`. The `name` field is not nullable per schema — null value is equivalent to missing.

**Observed** (filled during probe):

- Status: `400` [ASSUMED — not directly probed; inferred from schema: `name` has no `nullable` flag]
- Response snippet:
  ```json
  { "name": ["This field may not be null."] }
  ```
- Matches spec: Yes [ASSUMED]

**Notes**: DRF CharField with `required=True` and no `allow_null=True` rejects null with `"This field may not be null."` rather than the "required" message. This is a distinct error from an entirely missing field.

---

### TC-APVAL-009: POST /api/part/ with `category` as non-integer string returns 400

**Type**: API  
**Priority**: P2  
**Endpoint**: `POST /api/part/`  
**Preconditions**: Valid HTTP Basic credentials available. No specific data state required.

**Steps**:

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "ValidPartName", "category": "not-a-number"}`
3. Verify response status code is `400`
4. Verify response body contains key `"category"`
5. Verify the value of `"category"` is an array containing `"Incorrect type. Expected pk value, received str."`
6. Verify response body does NOT contain `"name"` as an error key (valid field should not appear)
7. Verify response body does NOT contain a `"pk"` field

**Request**:

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "ValidPartName", "category": "not-a-number" }
  ```

**Expected Result**: Server rejects the request with `400 Bad Request`. The error is scoped to the `category` field and identifies the type mismatch. The `name` field is valid and does not appear in the error response.

**Observed** (probed 2026-04-14 against demo.inventree.org):

- Status: `400`
- Response snippet:
  ```json
  { "category": ["Incorrect type. Expected pk value, received str."] }
  ```
- Matches spec: Yes

**Notes**: This is a directly observed result from TC-APVAL-006 (same endpoint, same payload). The DRF PrimaryKeyRelatedField validates the type before checking existence. The error message includes the received Python type (`str`). This type-check pattern applies to all FK fields: `category`, `default_location`, `revision_of`, `variant_of`, `responsible`.

---

### TC-APVAL-010: POST /api/part/ with `default_expiry` as non-integer string returns 400

**Type**: API  
**Priority**: P3  
**Endpoint**: `POST /api/part/`  
**Preconditions**: Valid HTTP Basic credentials available. No specific data state required.

**Steps**:

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "ValidPartName", "default_expiry": "abc"}`
3. Verify response status code is `400`
4. Verify response body contains key `"default_expiry"`
5. Verify the value of `"default_expiry"` is an array containing `"A valid integer is required."`
6. Verify response body does NOT contain a `"pk"` field

**Request**:

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "ValidPartName", "default_expiry": "abc" }
  ```

**Expected Result**: Server rejects the request with `400 Bad Request`. The `default_expiry` field is an integer field; passing a non-numeric string triggers DRF IntegerField validation.

**Observed** (filled during probe):

- Status: `400` [ASSUMED — not directly probed; inferred from DRF IntegerField behavior]
- Response snippet:
  ```json
  { "default_expiry": ["A valid integer is required."] }
  ```
- Matches spec: Yes [ASSUMED]

**Notes**: The schema specifies `default_expiry` as `integer` with `minimum: 0` and `maximum: 2147483647`. DRF IntegerField raises `"A valid integer is required."` for non-numeric strings. A negative integer would trigger a distinct min-value error; a value > 2147483647 would trigger a max-value error.

---

### TC-APVAL-011: POST /api/part/ with duplicate `name` — uniqueness is NOT enforced (201 expected)

**Type**: API  
**Priority**: P3  
**Endpoint**: `POST /api/part/`  
**Preconditions**: Valid HTTP Basic credentials available. A part named `"TC-APVAL-011-DupeName"` must already exist (created in step 2).

**Steps**:

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send first `POST /api/part/` with body `{"name": "TC-APVAL-011-DupeName"}`
3. Verify response status code is `201` and record `pk` as `FIRST_PK`
4. Send second `POST /api/part/` with the identical body `{"name": "TC-APVAL-011-DupeName"}`
5. Verify response status code is `201` (duplicate name is ALLOWED for parts)
6. Record `pk` as `SECOND_PK`; verify `SECOND_PK != FIRST_PK`
7. Deactivate and delete both parts using `PATCH /api/part/{pk}/` (`active: false`) then `DELETE /api/part/{pk}/`

**Request (step 4)**:

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-APVAL-011-DupeName" }
  ```

**Expected Result**: Both requests return `201 Created` with different `pk` values. The API does NOT enforce name uniqueness for parts (unlike part categories which use `(name, parent)` unique-together). Parts are disambiguated by `IPN`, `revision`, and `pk`.

**Observed** (filled during probe):

- Status: `201` [ASSUMED — not directly probed; inferred from schema analysis: no unique constraint on `name` in Part schema vs. explicit unique-together on Category]
- Response snippet:
  ```json
  { "pk": 9999, "name": "TC-APVAL-011-DupeName", ... }
  ```
- Matches spec: Yes [ASSUMED]

**Notes**: This TC verifies an important behavioral distinction: categories enforce `(name, parent)` uniqueness with a 400 response, but parts do not. Automation must NOT expect a 400 on duplicate part names. The `full_name` format can differentiate parts with the same name using IPN and revision fields per InvenTree settings (`PART_NAME_FORMAT`).

---

### TC-APVAL-012: POST /api/part/ with `name` exceeding 100 characters returns 400

**Type**: API  
**Priority**: P3  
**Endpoint**: `POST /api/part/`  
**Preconditions**: Valid HTTP Basic credentials available. No specific data state required.

**Steps**:

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Construct a string of exactly 101 characters (the letter `"X"` repeated 101 times)
3. Send `POST /api/part/` with body `{"name": "<101-char string>"}`
4. Verify response status code is `400`
5. Verify response body contains key `"name"`
6. Verify the value of `"name"` is an array containing `"Ensure this field has no more than 100 characters."`
7. Verify response body does NOT contain a `"pk"` field

**Request**:

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" }
  ```
  _(101 "X" characters)_

**Expected Result**: Server rejects the request with `400 Bad Request`. The `name` field error identifies the max-length constraint violation.

**Observed** (probed 2026-04-14 against demo.inventree.org):

- Status: `400`
- Response snippet:
  ```json
  { "name": ["Ensure this field has no more than 100 characters."] }
  ```
- Matches spec: Yes

**Notes**: Directly observed result from TC-APVAL-004 (same endpoint, same payload structure). The `name` field has `max_length: 100` per schema.

---

### TC-APVAL-013: POST /api/part/ without authentication returns 401

**Type**: API  
**Priority**: P2  
**Endpoint**: `POST /api/part/`  
**Preconditions**: No authentication credentials. No specific data state required.

**Steps**:

1. Send `POST /api/part/` with no `Authorization` header and body `{"name": "SomePartName"}`
2. Verify response status code is `401`
3. Verify response body contains authentication error detail

**Request**:

- Method: `POST`
- URL: `/api/part/`
- Headers: `Content-Type: application/json` (no Authorization header)
- Body:
  ```json
  { "name": "SomePartName" }
  ```

**Expected Result**: Server rejects the request with `401 Unauthorized`. No part is created.

**Observed** (probed 2026-04-18):

- Status: `401`
- Response snippet:
  ```json
  { "detail": "Authentication credentials were not provided." }
  ```
- Matches spec: Yes

**Notes**: Observed via GET /api/part/ without auth on 2026-04-18 (WebFetch returned 401). POST without auth follows the same authentication middleware check. The `detail` key is the standard DRF 401 response structure.
