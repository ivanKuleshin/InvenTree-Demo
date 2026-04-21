# Stock Location Types CRUD Test Suite

**Prefix:** `TC-ALTCRUD-`
**Endpoint family:** `/api/stock/location-type/` and `/api/stock/location-type/{id}/`
**Auth:** HTTP Basic with a user that has stock location-type read+write permissions.

---

## TC-ALTCRUD-001: GET /api/stock/location-type/ returns a list of location types

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `GET /api/stock/location-type/`

**Preconditions:**

- At least one `StockLocationType` exists on the server, or the response may be an empty list (accepted).
- Caller holds stock read permission.

**Test Data:**

- No parameters.

**Steps:**

1. Send `GET /api/stock/location-type/` with valid read credentials.
2. Verify response status is `200`.
3. Verify response body is an array (or a paginated response with a `results` array — confirm shape from actual response).
4. If the array is non-empty, verify each entry contains fields: `pk`, `name`, `description`, `icon`, `location_count`.
5. Verify `location_count` is an integer greater than or equal to `0`.

**Expected Result:** `200 OK` with a list of `StockLocationType` objects. Each entry includes all schema fields. An empty list is also valid if no types exist.

---

## TC-ALTCRUD-002: GET /api/stock/location-type/{id}/ retrieves a single location type

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `GET /api/stock/location-type/{id}/`

**Preconditions:**

- A `StockLocationType` with a known PK (`{type_pk}`) exists.
- Caller holds stock read permission.

**Test Data:**

- `{id}`: PK of the existing location type.

**Steps:**

1. Send `GET /api/stock/location-type/{type_pk}/` with valid read credentials.
2. Verify response status is `200`.
3. Verify response body field `pk` equals `{type_pk}`.
4. Verify response body contains fields `name`, `description`, `icon`, `location_count`.
5. Verify `name` is a non-empty string.

**Expected Result:** `200 OK`. The `StockLocationType` object for the given PK is returned with all documented fields.

---

## TC-ALTCRUD-003: POST /api/stock/location-type/ creates a location type with required fields only

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/location-type/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "Standard Shelf"
}
```

**Steps:**

1. Send `POST /api/stock/location-type/` with the body above and write credentials.
2. Verify response status is `201`.
3. Verify response body contains `pk` as a positive integer.
4. Verify response body field `name` equals `"Standard Shelf"`.
5. Verify response body field `location_count` equals `0` (no locations reference this type yet).
6. Verify response body fields `description` and `icon` are present (may be empty string or null — note the actual value for regression).

**Expected Result:** `201 Created`. New `StockLocationType` is created. `location_count` starts at 0.

---

## TC-ALTCRUD-004: POST /api/stock/location-type/ creates a location type with all fields

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `POST /api/stock/location-type/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "Deep Bin",
  "description": "Large storage bin for bulk components",
  "icon": "ti:box:outline"
}
```

**Steps:**

1. Send `POST /api/stock/location-type/` with the body above and write credentials.
2. Verify response status is `201`.
3. Verify response body field `name` equals `"Deep Bin"`.
4. Verify response body field `description` equals `"Large storage bin for bulk components"`.
5. Verify response body field `icon` equals `"ti:box:outline"`.

**Expected Result:** `201 Created`. All supplied fields are persisted as submitted.

---

## TC-ALTCRUD-005: PUT /api/stock/location-type/{id}/ replaces all writable fields

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PUT /api/stock/location-type/{id}/`

**Preconditions:**

- A `StockLocationType` exists with a known PK (`{type_pk}`), created in this session.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "Updated Type Name",
  "description": "Updated description",
  "icon": "ti:bookmark:filled"
}
```

**Steps:**

1. Send `PUT /api/stock/location-type/{type_pk}/` with the body above and write credentials.
2. Verify response status is `200`.
3. Verify response body field `name` equals `"Updated Type Name"`.
4. Verify response body field `description` equals `"Updated description"`.
5. Verify response body field `icon` equals `"ti:bookmark:filled"`.

**Expected Result:** `200 OK`. All writable fields are replaced with the supplied values.

---

## TC-ALTCRUD-006: PATCH /api/stock/location-type/{id}/ partially updates description

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PATCH /api/stock/location-type/{id}/`

**Preconditions:**

- A `StockLocationType` exists with a known PK (`{type_pk}`), with `name` set to a known value.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "description": "Patched description only"
}
```

**Steps:**

1. Retrieve initial state: `GET /api/stock/location-type/{type_pk}/`. Note `name` and `icon`.
2. Send `PATCH /api/stock/location-type/{type_pk}/` with `{ "description": "Patched description only" }` and write credentials.
3. Verify response status is `200`.
4. Verify response body field `description` equals `"Patched description only"`.
5. Verify `name` and `icon` are unchanged compared to step 1.

**Expected Result:** `200 OK`. Only `description` changes; `name` and `icon` are unaffected.

---

## TC-ALTCRUD-007: PATCH /api/stock/location-type/{id}/ updates icon

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PATCH /api/stock/location-type/{id}/`

**Preconditions:**

- A `StockLocationType` with a known PK (`{type_pk}`) exists.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "icon": "ti:archive:outline"
}
```

**Steps:**

1. Send `PATCH /api/stock/location-type/{type_pk}/` with the body above and write credentials.
2. Verify response status is `200`.
3. Verify response body field `icon` equals `"ti:archive:outline"`.

**Expected Result:** `200 OK`. The `icon` field is updated to the new icon identifier.

---

## TC-ALTCRUD-008: DELETE /api/stock/location-type/{id}/ removes a location type not referenced by any location

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `DELETE /api/stock/location-type/{id}/`

**Preconditions:**

- A `StockLocationType` exists with a known PK (`{type_pk}`), created in this session.
- No stock location references `{type_pk}` (i.e. `location_count = 0`).
- Caller holds stock write permission.

**Test Data:**

- No body required.

**Steps:**

1. Confirm `location_count` is `0`: `GET /api/stock/location-type/{type_pk}/`.
2. Send `DELETE /api/stock/location-type/{type_pk}/` with write credentials.
3. Verify response status is `204`.
4. Send `GET /api/stock/location-type/{type_pk}/`.
5. Verify response status is `404`.

**Expected Result:** `204 No Content`. Subsequent GET returns `404`.

---

## TC-ALTCRUD-009: DELETE /api/stock/location-type/{id}/ on a type referenced by locations returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `DELETE /api/stock/location-type/{id}/`

**Preconditions:**

- A `StockLocationType` with PK `{type_pk}` is assigned to at least one stock location (i.e. `location_count` > 0).
- Caller holds stock write permission.

**Test Data:**

- No body required.

**Steps:**

1. Confirm `location_count` is greater than `0`: `GET /api/stock/location-type/{type_pk}/`.
2. Send `DELETE /api/stock/location-type/{type_pk}/` with write credentials.
3. Verify response status is `400` or `409`.
4. Verify the error response indicates the type is in use by one or more locations.

**Expected Result:** `400` or `409`. A type that is in use by existing locations cannot be deleted while those references exist.

**Note:** The documentation does not explicitly state the server enforces this constraint. Mark with `[ASSUMED]` if the DELETE succeeds (nullifying the FK on child locations) and adjust the assertion to verify `200`/`204` with FK cleared.

---

## TC-ALTCRUD-010: POST /api/stock/location-type/ with missing required name returns 400

**Type:** API / Negative
**Priority:** P1
**Endpoint:** `POST /api/stock/location-type/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "description": "No name provided"
}
```

**Steps:**

1. Send `POST /api/stock/location-type/` with the body above and write credentials.
2. Verify response status is `400`.
3. Verify response body contains an error referencing the `name` field as required.

**Expected Result:** `400 Bad Request`. `name` is a required field.

---

## TC-ALTCRUD-011: POST /api/stock/location-type/ with name exceeding 100 characters returns 400

**Type:** API / Negative
**Priority:** P3
**Endpoint:** `POST /api/stock/location-type/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB"
}
```

(101 characters — 101 `A` characters)

**Steps:**

1. Send `POST /api/stock/location-type/` with the body above (101-char name) and write credentials.
2. Verify response status is `400`.
3. Verify response body references the `name` field and mentions a length constraint.

**Expected Result:** `400 Bad Request`. `name` has a max length of 100 characters; 101 characters is rejected.

---

## TC-ALTCRUD-012: GET /api/stock/location-type/{id}/ with non-existent PK returns 404

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `GET /api/stock/location-type/{id}/`

**Preconditions:**

- Caller holds stock read permission.
- The PK `999999999` does not exist.

**Test Data:**

- `{id}`: `999999999`

**Steps:**

1. Send `GET /api/stock/location-type/999999999/` with valid read credentials.
2. Verify response status is `404`.

**Expected Result:** `404 Not Found`. A non-existent location type PK returns 404.

---

## TC-ALTCRUD-013: POST /api/stock/location-type/ without authentication returns 401 or 403

**Type:** API / Security
**Priority:** P1
**Endpoint:** `POST /api/stock/location-type/`

**Preconditions:**

- No authentication header is sent.

**Test Data:**

```json
{
  "name": "Unauthorized Type"
}
```

**Steps:**

1. Send `POST /api/stock/location-type/` with no `Authorization` header.
2. Verify response status is `401` or `403`.
3. Verify no location type is created.

**Expected Result:** `401` or `403`. Unauthenticated callers cannot create location types.
