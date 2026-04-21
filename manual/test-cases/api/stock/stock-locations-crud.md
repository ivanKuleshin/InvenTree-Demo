# Stock Locations CRUD Test Suite

**Prefix:** `TC-ALCRUD-`
**Endpoint family:** `/api/stock/location/` and `/api/stock/location/{id}/` and `/api/stock/location/tree/`
**Auth:** HTTP Basic with a user that has stock location read+write permissions.

---

## TC-ALCRUD-001: GET /api/stock/location/ returns paginated location list with full field set

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `GET /api/stock/location/`

**Preconditions:**

- At least one stock location exists on the server.
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `limit=5`

**Steps:**

1. Send `GET /api/stock/location/?limit=5` with valid read credentials.
2. Verify response status is `200`.
3. Verify response body contains field `count` as a positive integer.
4. Verify response body contains field `results` as an array of at most 5 objects.
5. Verify the first result contains fields: `pk`, `name`, `description`, `parent`, `pathstring`, `items`, `sublocations`, `structural`, `external`, `owner`, `icon`, `location_type`, `level`, `barcode_hash`.
6. Verify `pathstring` is a non-empty string.
7. Verify `structural` and `external` are booleans.

**Expected Result:** `200 OK` with a paginated `Location` list. Each location exposes the full field set including computed read-only fields `pathstring`, `items`, `sublocations`, and `level`.

---

## TC-ALCRUD-002: GET /api/stock/location/{id}/ retrieves a single location by primary key

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `GET /api/stock/location/{id}/`

**Preconditions:**

- A stock location with a known PK (`{location_pk}`) exists.
- Caller holds stock read permission.

**Test Data:**

- `{id}`: PK of the existing location.

**Steps:**

1. Send `GET /api/stock/location/{location_pk}/` with valid read credentials.
2. Verify response status is `200`.
3. Verify response body field `pk` equals `{location_pk}`.
4. Verify response body contains `name`, `pathstring`, `level`, `items`, `sublocations`.
5. Verify `level` is an integer greater than or equal to `0`.

**Expected Result:** `200 OK`. The location object includes all schema fields. `pathstring` reflects the hierarchy path from root to this location.

---

## TC-ALCRUD-003: GET /api/stock/location/tree/ returns all locations as lightweight tree nodes

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `GET /api/stock/location/tree/`

**Preconditions:**

- At least one stock location exists.
- Caller holds stock read permission.

**Test Data:**

- No parameters.

**Steps:**

1. Send `GET /api/stock/location/tree/` with valid read credentials.
2. Verify response status is `200`.
3. Verify response body is an array of objects (not a paginated envelope — tree endpoint returns a flat list).
4. Verify each object contains fields: `pk`, `name`, `parent`, `icon`, `structural`, `sublocations`.
5. Verify no `description`, `pathstring`, or `items` fields appear (those are not part of `LocationTree`).

**Expected Result:** `200 OK`. Response is a flat array of `LocationTree` objects with exactly the lightweight field set. The response is not paginated.

---

## TC-ALCRUD-004: GET /api/stock/location/ filters top-level locations

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `GET /api/stock/location/`

**Preconditions:**

- At least one root-level location (no parent) and at least one child location exist.
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `top_level=true`

**Steps:**

1. Send `GET /api/stock/location/?top_level=true` with valid read credentials.
2. Verify response status is `200`.
3. Verify every object in `results` has `parent` equal to `null`.

**Expected Result:** `200 OK`. Only root-level locations (those without a parent) appear in results.

---

## TC-ALCRUD-005: GET /api/stock/location/ filters structural locations

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `GET /api/stock/location/`

**Preconditions:**

- At least one structural location exists.
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `structural=true`

**Steps:**

1. Send `GET /api/stock/location/?structural=true` with valid read credentials.
2. Verify response status is `200`.
3. Verify every object in `results` has `structural` equal to `true`.

**Expected Result:** `200 OK`. Only structural locations are returned.

---

## TC-ALCRUD-006: GET /api/stock/location/ filters external locations

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `GET /api/stock/location/`

**Preconditions:**

- At least one external location exists. Create one if needed.
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `external=true`

**Steps:**

1. Send `GET /api/stock/location/?external=true` with valid read credentials.
2. Verify response status is `200`.
3. Verify every object in `results` has `external` equal to `true`.

**Expected Result:** `200 OK`. Only external locations are returned.

---

## TC-ALCRUD-007: POST /api/stock/location/ creates a root-level location with required fields only

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/location/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "Test Root Location"
}
```

**Steps:**

1. Send `POST /api/stock/location/` with the body above and write credentials.
2. Verify response status is `201`.
3. Verify response body contains `pk` as a positive integer.
4. Verify response body field `name` equals `"Test Root Location"`.
5. Verify response body field `parent` is `null`.
6. Verify response body field `level` equals `0`.
7. Verify response body field `pathstring` equals `"Test Root Location"`.
8. Verify `structural` equals `false` and `external` equals `false` (defaults).

**Expected Result:** `201 Created`. A root location is created with default flags. `pathstring` equals the location name. `level` is 0.

---

## TC-ALCRUD-008: POST /api/stock/location/ creates a nested child location

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/location/`

**Preconditions:**

- A parent location exists with a known PK (`{parent_pk}`) and known `pathstring` (`{parent_pathstring}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "Child Shelf",
  "parent": "{parent_pk}",
  "description": "Second-level shelf under parent"
}
```

**Steps:**

1. Send `POST /api/stock/location/` with the body above and write credentials.
2. Verify response status is `201`.
3. Verify response body field `parent` equals `{parent_pk}`.
4. Verify response body field `pathstring` equals `"{parent_pathstring}/Child Shelf"`.
5. Verify response body field `level` equals the parent's `level` plus 1.

**Expected Result:** `201 Created`. The child location's `pathstring` is the parent's path appended with `"/Child Shelf"`. `level` is incremented by 1.

---

## TC-ALCRUD-009: POST /api/stock/location/ creates a structural location

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `POST /api/stock/location/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "Structural Container",
  "structural": true,
  "description": "Organizational node only"
}
```

**Steps:**

1. Send `POST /api/stock/location/` with the body above and write credentials.
2. Verify response status is `201`.
3. Verify response body field `structural` equals `true`.

**Expected Result:** `201 Created`. Location is created with `structural=true`.

---

## TC-ALCRUD-010: POST /api/stock/location/ creates an external location

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `POST /api/stock/location/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "External Supplier Warehouse",
  "external": true
}
```

**Steps:**

1. Send `POST /api/stock/location/` with the body above and write credentials.
2. Verify response status is `201`.
3. Verify response body field `external` equals `true`.

**Expected Result:** `201 Created`. Location is created with `external=true`.

---

## TC-ALCRUD-011: POST /api/stock/location/ creates a location with a location_type assigned

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `POST /api/stock/location/`

**Preconditions:**

- A `StockLocationType` exists with a known PK (`{type_pk}`). Create one via `POST /api/stock/location-type/` if needed.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "Typed Bin Location",
  "location_type": "{type_pk}"
}
```

**Steps:**

1. Send `POST /api/stock/location/` with the body above and write credentials.
2. Verify response status is `201`.
3. Verify response body field `location_type` equals `{type_pk}`.
4. Verify response body field `location_type_detail` is a non-null object containing `pk` equal to `{type_pk}`.

**Expected Result:** `201 Created`. `location_type` is set and `location_type_detail` is expanded inline.

---

## TC-ALCRUD-012: PUT /api/stock/location/{id}/ renames a location and updates pathstring

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PUT /api/stock/location/{id}/`

**Preconditions:**

- A root-level location with a known PK (`{location_pk}`) and name `"Old Name"` exists (created in this session).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "New Name"
}
```

**Steps:**

1. Send `PUT /api/stock/location/{location_pk}/` with body `{ "name": "New Name" }` and write credentials.
2. Verify response status is `200`.
3. Verify response body field `name` equals `"New Name"`.
4. Verify response body field `pathstring` now contains `"New Name"`.

**Expected Result:** `200 OK`. Name is updated. `pathstring` is recomputed to reflect the new name.

---

## TC-ALCRUD-013: PATCH /api/stock/location/{id}/ toggles structural flag

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PATCH /api/stock/location/{id}/`

**Preconditions:**

- A non-structural location with no stock items and no children exists with a known PK (`{location_pk}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "structural": true
}
```

**Steps:**

1. Confirm current state: `GET /api/stock/location/{location_pk}/` — verify `structural` is `false`.
2. Send `PATCH /api/stock/location/{location_pk}/` with `{ "structural": true }` and write credentials.
3. Verify response status is `200`.
4. Verify response body field `structural` equals `true`.
5. Attempt to create a stock item at this location: `POST /api/stock/` with `{ "part": {any_part_pk}, "quantity": 1, "location": {location_pk} }`.
6. Verify this POST returns `400`.

**Expected Result:** `200 OK` on PATCH. Subsequent stock item creation targeting the structural location is rejected with `400`.

---

## TC-ALCRUD-014: PATCH /api/stock/location/{id}/ toggles external flag

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PATCH /api/stock/location/{id}/`

**Preconditions:**

- A non-external location with a known PK (`{location_pk}`) exists.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "external": true
}
```

**Steps:**

1. Send `PATCH /api/stock/location/{location_pk}/` with `{ "external": true }` and write credentials.
2. Verify response status is `200`.
3. Verify response body field `external` equals `true`.
4. Send `PATCH /api/stock/location/{location_pk}/` with `{ "external": false }`.
5. Verify response status is `200`.
6. Verify response body field `external` equals `false`.

**Expected Result:** `200 OK` for both PATCHes. `external` flag toggles correctly in both directions.

---

## TC-ALCRUD-015: PATCH /api/stock/location/{id}/ reparents a location to a new parent

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PATCH /api/stock/location/{id}/`

**Preconditions:**

- A location `A` (root, `parent=null`) exists with PK `{location_a_pk}`.
- A location `B` (root, `parent=null`) exists with PK `{location_b_pk}`.
- A child location `C` under `A` exists with PK `{location_c_pk}` and `pathstring` `"A/C"`.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "parent": "{location_b_pk}"
}
```

**Steps:**

1. Send `PATCH /api/stock/location/{location_c_pk}/` with `{ "parent": {location_b_pk} }` and write credentials.
2. Verify response status is `200`.
3. Verify response body field `parent` equals `{location_b_pk}`.
4. Verify response body field `pathstring` now starts with the name of location B.

**Expected Result:** `200 OK`. Location C is moved under B. `pathstring` is recomputed accordingly.

---

## TC-ALCRUD-016: PATCH /api/stock/location/{id}/ changes location_type

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PATCH /api/stock/location/{id}/`

**Preconditions:**

- A location with a known PK (`{location_pk}`) exists.
- Two `StockLocationType` records exist: `{type_a_pk}` and `{type_b_pk}`.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "location_type": "{type_b_pk}"
}
```

**Steps:**

1. Confirm initial state has `location_type` set to `{type_a_pk}` or null.
2. Send `PATCH /api/stock/location/{location_pk}/` with `{ "location_type": {type_b_pk} }` and write credentials.
3. Verify response status is `200`.
4. Verify response body field `location_type` equals `{type_b_pk}`.
5. Verify response body field `location_type_detail.pk` equals `{type_b_pk}`.

**Expected Result:** `200 OK`. `location_type` is updated and `location_type_detail` reflects the new type.

---

## TC-ALCRUD-017: DELETE /api/stock/location/{id}/ removes an empty location

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `DELETE /api/stock/location/{id}/`

**Preconditions:**

- A location with no stock items and no child locations exists, created in this session with a known PK (`{location_pk}`).
- Caller holds stock write permission.

**Test Data:**

- No body required.

**Steps:**

1. Confirm `items` and `sublocations` are both `0` for `{location_pk}`: `GET /api/stock/location/{location_pk}/`.
2. Send `DELETE /api/stock/location/{location_pk}/` with write credentials.
3. Verify response status is `204`.
4. Send `GET /api/stock/location/{location_pk}/`.
5. Verify response status is `404`.

**Expected Result:** `204 No Content`. Subsequent GET returns `404`.

---

## TC-ALCRUD-018: DELETE /api/stock/location/{id}/ on a location with stock items returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `DELETE /api/stock/location/{id}/`

**Preconditions:**

- A location with at least one stock item directly assigned to it exists (`{location_pk}`).
- Caller holds stock write permission.

**Test Data:**

- No body required.

**Steps:**

1. Confirm `items` is greater than `0` for `{location_pk}`: `GET /api/stock/location/{location_pk}/`.
2. Send `DELETE /api/stock/location/{location_pk}/` with write credentials.
3. Verify response status is `400` (or `409`).
4. Verify response body contains an error message indicating the location cannot be deleted because it contains items.

**Expected Result:** `400` or `409`. A location with stock items cannot be deleted directly.

---

## TC-ALCRUD-019: DELETE /api/stock/location/{id}/ on a location with child locations returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `DELETE /api/stock/location/{id}/`

**Preconditions:**

- A parent location with at least one child sub-location exists (`{parent_pk}`).
- Caller holds stock write permission.

**Test Data:**

- No body required.

**Steps:**

1. Confirm `sublocations` is greater than `0` for `{parent_pk}`: `GET /api/stock/location/{parent_pk}/`.
2. Send `DELETE /api/stock/location/{parent_pk}/` with write credentials.
3. Verify response status is `400` (or `409`).
4. Verify the response indicates the location has child locations and cannot be deleted.

**Expected Result:** `400` or `409`. A location with child sub-locations cannot be deleted.

---

## TC-ALCRUD-020: POST /api/stock/location/ with missing required name field returns 400

**Type:** API / Negative
**Priority:** P1
**Endpoint:** `POST /api/stock/location/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "description": "No name supplied"
}
```

**Steps:**

1. Send `POST /api/stock/location/` with the body above and write credentials.
2. Verify response status is `400`.
3. Verify response body contains an error referencing the `name` field as required.

**Expected Result:** `400 Bad Request`. `name` is required; its absence produces a field-level validation error.

---

## TC-ALCRUD-021: POST /api/stock/location/ with non-existent parent FK returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `POST /api/stock/location/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "name": "Orphan Location",
  "parent": 999999999
}
```

**Steps:**

1. Send `POST /api/stock/location/` with the body above and write credentials.
2. Verify response status is `400`.
3. Verify response body references the `parent` field.

**Expected Result:** `400 Bad Request`. Non-existent parent FK is rejected.

---

## TC-ALCRUD-022: PATCH /api/stock/location/{id}/ with a cyclic parent reference returns 400

**Type:** API / Negative
**Priority:** P3
**Endpoint:** `PATCH /api/stock/location/{id}/`

**Preconditions:**

- Location A is the parent of location B. Both exist with known PKs (`{A_pk}`, `{B_pk}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "parent": "{B_pk}"
}
```

**Steps:**

1. Send `PATCH /api/stock/location/{A_pk}/` with `{ "parent": {B_pk} }` and write credentials (attempting to make A a child of its own child B).
2. Verify response status is `400`.
3. Verify response body contains an error indicating a cyclic reference or that the location cannot be its own ancestor.

**Expected Result:** `400 Bad Request`. Cyclic parent references are rejected.

---

## TC-ALCRUD-023: POST /api/stock/location/ without authentication returns 401 or 403

**Type:** API / Security
**Priority:** P1
**Endpoint:** `POST /api/stock/location/`

**Preconditions:**

- No authentication header is sent.

**Test Data:**

```json
{
  "name": "Unauthorized Location"
}
```

**Steps:**

1. Send `POST /api/stock/location/` with no `Authorization` header.
2. Verify response status is `401` or `403`.
3. Verify no location is created.

**Expected Result:** `401` or `403`. Unauthenticated callers cannot create locations.

---

## TC-ALCRUD-024: GET /api/stock/location/ search filter matches name and pathstring

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `GET /api/stock/location/`

**Preconditions:**

- At least one location exists whose `name` or `pathstring` contains the search term `"Shelf"`.
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `search=Shelf`

**Steps:**

1. Send `GET /api/stock/location/?search=Shelf` with valid read credentials.
2. Verify response status is `200`.
3. Verify every result has `name` or `pathstring` containing `"Shelf"` (case-insensitive).

**Expected Result:** `200 OK`. Search is applied across `name`, `description`, and `pathstring`. Results include only locations matching the term.
