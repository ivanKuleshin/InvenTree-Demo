# Stock Items CRUD Test Suite

**Prefix:** `TC-ASCRUD-`
**Endpoint family:** `/api/stock/` and `/api/stock/{id}/`
**Auth:** HTTP Basic with a user that has stock read+write permissions. Tests marked [READ-ONLY] may be executed without write credentials on the demo instance.

---

## TC-ASCRUD-001: GET /api/stock/ returns paginated stock item list with full field set

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `GET /api/stock/`

**Preconditions:**

- At least one stock item exists on the server.
- Caller holds stock read permission.

**Test Data:**

- No body required.

**Steps:**

1. Send `GET /api/stock/?limit=5` with valid read credentials.
2. Verify response status is `200`.
3. Verify response body contains field `count` as a positive integer.
4. Verify response body contains field `results` as an array of exactly 5 objects.
5. Verify response body contains field `next` as a non-null URI (more pages available).
6. Verify response body contains field `previous` as `null` (first page).
7. Verify the first result object contains all of the following fields: `pk`, `part`, `quantity`, `serial`, `batch`, `location`, `status`, `status_text`, `in_stock`, `is_building`, `delete_on_deplete`, `barcode_hash`, `updated`, `tracking_items`.
8. Verify `status_text` equals a known label (`"OK"`, `"Attention needed"`, `"Damaged"`, `"Destroyed"`, `"Rejected"`, `"Lost"`, `"Quarantined"`, or `"Returned"`).

**Expected Result:** `200 OK` with a `PaginatedStockItemList`. Each item contains the core field set. `status_text` is a non-empty string matching one of the documented status labels.

---

## TC-ASCRUD-002: GET /api/stock/{id}/ retrieves a single stock item by primary key

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `GET /api/stock/{id}/`

**Preconditions:**

- A stock item with a known PK exists (create one via `POST /api/stock/` if the environment is clean).
- Caller holds stock read permission.

**Test Data:**

- `{id}`: PK of the existing stock item.

**Steps:**

1. Send `GET /api/stock/{id}/` with valid read credentials.
2. Verify response status is `200`.
3. Verify response body field `pk` equals `{id}`.
4. Verify response body fields `part`, `quantity`, `status`, `in_stock`, `barcode_hash` are all present and non-null (except nullable fields documented as nullable).
5. Verify `in_stock` is a boolean.
6. Verify `quantity` is a numeric value greater than or equal to `0`.

**Expected Result:** `200 OK` with the full `StockItem` object. `pk` matches the requested ID. All required read-only fields are present.

---

## TC-ASCRUD-003: POST /api/stock/ creates a stock item with required fields only

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- An active, non-virtual part exists. Note its PK (`{part_pk}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "part": "{part_pk}",
  "quantity": 10
}
```

**Steps:**

1. Send `POST /api/stock/` with the body above and write credentials.
2. Verify response status is `201`.
3. Verify response body contains `pk` as a positive integer (new stock item PK).
4. Verify response body field `part` equals `{part_pk}`.
5. Verify response body field `quantity` equals `10`.
6. Verify response body field `status` equals `10` (default OK).
7. Verify response body field `in_stock` equals `true`.
8. Verify response body field `location` is `null` (no location provided).
9. Send `GET /api/stock/{new_pk}/` and confirm the item persists with the same values.

**Expected Result:** `201 Created`. New stock item has `status=10`, `in_stock=true`, `location=null`. Record is retrievable.

---

## TC-ASCRUD-004: POST /api/stock/ creates a serialized stock item using serial_numbers write-only field

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- An active, trackable part exists. Note its PK (`{trackable_part_pk}`).
- A non-structural stock location exists. Note its PK (`{location_pk}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "part": "{trackable_part_pk}",
  "quantity": 3,
  "location": "{location_pk}",
  "serial_numbers": "1-3"
}
```

**Steps:**

1. Send `POST /api/stock/` with the body above and write credentials.
2. Verify response status is `201`.
3. Send `GET /api/stock/?part={trackable_part_pk}&serialized=true` and verify exactly 3 new stock items exist with `serial` values `"1"`, `"2"`, and `"3"`.
4. Verify each resulting item has `quantity` equal to `1` (serialized items have qty=1 each).
5. Verify each resulting item has `location` equal to `{location_pk}`.

**Expected Result:** `201 Created`. The `serial_numbers` pattern `"1-3"` expands into 3 individual serialized stock items, each with quantity 1.

---

## TC-ASCRUD-005: POST /api/stock/ creates a stock item with a batch code

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- An active part exists. Note its PK (`{part_pk}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "part": "{part_pk}",
  "quantity": 25,
  "batch": "BATCH-2026-001",
  "notes": "Initial receipt",
  "delete_on_deplete": false
}
```

**Steps:**

1. Send `POST /api/stock/` with the body above and write credentials.
2. Verify response status is `201`.
3. Verify response body field `batch` equals `"BATCH-2026-001"`.
4. Verify response body field `notes` equals `"Initial receipt"`.
5. Verify response body field `delete_on_deplete` equals `false`.

**Expected Result:** `201 Created`. Batch code and notes are persisted as supplied.

---

## TC-ASCRUD-006: GET /api/stock/ filters by part PK

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `GET /api/stock/`

**Preconditions:**

- At least one stock item exists for a specific part (`{part_pk}`).
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `part={part_pk}`

**Steps:**

1. Send `GET /api/stock/?part={part_pk}` with valid read credentials.
2. Verify response status is `200`.
3. Verify every object in `results` has `part` equal to `{part_pk}`.
4. Verify `count` reflects only items belonging to that part.

**Expected Result:** `200 OK`. All returned items have the specified `part` PK. No items from other parts appear in results.

---

## TC-ASCRUD-007: GET /api/stock/ filters by location PK

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `GET /api/stock/`

**Preconditions:**

- At least one stock item exists at a specific location (`{location_pk}`).
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `location={location_pk}`

**Steps:**

1. Send `GET /api/stock/?location={location_pk}` with valid read credentials.
2. Verify response status is `200`.
3. Verify every object in `results` has `location` equal to `{location_pk}`.

**Expected Result:** `200 OK`. All returned items are at the specified location.

---

## TC-ASCRUD-008: GET /api/stock/ filters by status code

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `GET /api/stock/`

**Preconditions:**

- At least one stock item exists with status `10` (OK) and at least one with a different status (e.g. `55` Damaged). Create via POST if needed.
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `status=10`

**Steps:**

1. Send `GET /api/stock/?status=10` with valid read credentials.
2. Verify response status is `200`.
3. Verify every object in `results` has `status` equal to `10`.
4. Send `GET /api/stock/?status=55`.
5. Verify every object in `results` has `status` equal to `55`.

**Expected Result:** `200 OK` for each call. The `status` filter correctly restricts results to items with the given status code.

---

## TC-ASCRUD-009: GET /api/stock/ filters unlocated items using location=null

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `GET /api/stock/`

**Preconditions:**

- At least one stock item exists with `location = null` (unlocated). Create via POST with no `location` field if needed.
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `location=null`

**Steps:**

1. Send `GET /api/stock/?location=null` with valid read credentials.
2. Verify response status is `200`.
3. Verify every object in `results` has `location` equal to `null`.

**Expected Result:** `200 OK`. Only unlocated stock items are returned.

---

## TC-ASCRUD-010: GET /api/stock/ filters serialized items

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `GET /api/stock/`

**Preconditions:**

- At least one serialized stock item exists (has a non-null `serial`).
- Caller holds stock read permission.

**Test Data:**

- Query parameter: `serialized=true`

**Steps:**

1. Send `GET /api/stock/?serialized=true` with valid read credentials.
2. Verify response status is `200`.
3. Verify every object in `results` has `serial` as a non-null, non-empty string.
4. Send `GET /api/stock/?serialized=false`.
5. Verify every object in `results` has `serial` equal to `null` or empty string.

**Expected Result:** `200 OK` for each call. `serialized=true` returns only items with a serial number; `serialized=false` returns items without one.

---

## TC-ASCRUD-011: PUT /api/stock/{id}/ replaces all writable fields

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PUT /api/stock/{id}/`

**Preconditions:**

- A stock item exists with a known PK (`{item_pk}`), created via POST in this session.
- A non-structural stock location exists (`{location_pk}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "part": "{part_pk}",
  "quantity": 50,
  "location": "{location_pk}",
  "status": 50,
  "batch": "BATCH-PUT-TEST",
  "notes": "Updated via PUT",
  "delete_on_deplete": true,
  "packaging": "Box"
}
```

**Steps:**

1. Create a stock item via `POST /api/stock/` with minimal fields; record the returned `pk` as `{item_pk}` and the `part` value as `{part_pk}`.
2. Send `PUT /api/stock/{item_pk}/` with the full body above and write credentials.
3. Verify response status is `200`.
4. Verify response body fields `quantity`, `location`, `status`, `batch`, `notes`, `delete_on_deplete`, and `packaging` match the submitted values.
5. Send `GET /api/stock/{item_pk}/` and confirm all those fields are persisted.

**Expected Result:** `200 OK`. All writable fields are replaced. Subsequent GET confirms persistence.

---

## TC-ASCRUD-012: PATCH /api/stock/{id}/ partially updates quantity

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PATCH /api/stock/{id}/`

**Preconditions:**

- A stock item exists with a known PK (`{item_pk}`) and a known current `quantity`.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "quantity": 99
}
```

**Steps:**

1. Retrieve the current state: `GET /api/stock/{item_pk}/`. Note `quantity` as `{original_quantity}`.
2. Send `PATCH /api/stock/{item_pk}/` with `{ "quantity": 99 }` and write credentials.
3. Verify response status is `200`.
4. Verify response body field `quantity` equals `99`.
5. Verify all other fields remain unchanged compared to step 1.

**Expected Result:** `200 OK`. Only `quantity` is changed; all other fields retain their prior values.

---

## TC-ASCRUD-013: PATCH /api/stock/{id}/ partially updates location

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PATCH /api/stock/{id}/`

**Preconditions:**

- A stock item exists with a known PK (`{item_pk}`), currently at location A.
- A second non-structural location B (`{location_b_pk}`) exists.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "location": "{location_b_pk}"
}
```

**Steps:**

1. Send `PATCH /api/stock/{item_pk}/` with `{ "location": {location_b_pk} }` and write credentials.
2. Verify response status is `200`.
3. Verify response body field `location` equals `{location_b_pk}`.

**Expected Result:** `200 OK`. `location` is updated; quantity and other fields are unchanged.

---

## TC-ASCRUD-014: PATCH /api/stock/{id}/ updates status, batch, notes, owner, packaging, and expiry_date

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `PATCH /api/stock/{id}/`

**Preconditions:**

- A stock item exists with a known PK (`{item_pk}`).
- Stock expiry and ownership features are enabled in server settings, or the test skips those sub-fields.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "status": 55,
  "batch": "BATCH-PATCH",
  "notes": "Patched notes field",
  "packaging": "Bag",
  "expiry_date": "2027-12-31"
}
```

**Steps:**

1. Send `PATCH /api/stock/{item_pk}/` with the body above and write credentials.
2. Verify response status is `200`.
3. Verify `status` equals `55`.
4. Verify `batch` equals `"BATCH-PATCH"`.
5. Verify `notes` equals `"Patched notes field"`.
6. Verify `packaging` equals `"Bag"`.
7. Verify `expiry_date` equals `"2027-12-31"`.

**Expected Result:** `200 OK`. Each patched field reflects the new value.

---

## TC-ASCRUD-015: DELETE /api/stock/{id}/ removes a stock item

**Type:** API / Functional
**Priority:** P2
**Endpoint:** `DELETE /api/stock/{id}/`

**Preconditions:**

- A stock item with a known PK (`{item_pk}`) exists (created in this session via POST to avoid deleting production data).
- Caller holds stock write permission.

**Test Data:**

- No body required.

**Steps:**

1. Confirm the item exists: `GET /api/stock/{item_pk}/` returns `200`.
2. Send `DELETE /api/stock/{item_pk}/` with write credentials.
3. Verify response status is `204`.
4. Send `GET /api/stock/{item_pk}/` again.
5. Verify response status is `404`.

**Expected Result:** `204 No Content` on delete. Subsequent GET returns `404`.

---

## TC-ASCRUD-016: POST /api/stock/ with non-existent part FK returns 400

**Type:** API / Negative
**Priority:** P1
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- Caller holds stock write permission.
- A part PK that does not exist in the system is used (`{nonexistent_part_pk}`).

**Test Data:**

```json
{
  "part": 999999999,
  "quantity": 1
}
```

**Steps:**

1. Send `POST /api/stock/` with the body above and write credentials.
2. Verify response status is `400`.
3. Verify response body contains an error referencing the `part` field.

**Expected Result:** `400 Bad Request`. The response body identifies `part` as invalid or non-existent.

---

## TC-ASCRUD-017: POST /api/stock/ with non-existent location FK returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- An active part exists (`{part_pk}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "part": "{part_pk}",
  "quantity": 1,
  "location": 999999999
}
```

**Steps:**

1. Send `POST /api/stock/` with the body above and write credentials.
2. Verify response status is `400`.
3. Verify response body contains an error referencing the `location` field.

**Expected Result:** `400 Bad Request`. The response identifies `location` as invalid.

---

## TC-ASCRUD-018: POST /api/stock/ with a structural location returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- An active part exists (`{part_pk}`).
- A structural stock location exists (`{structural_location_pk}`) — one where `structural=true`.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "part": "{part_pk}",
  "quantity": 5,
  "location": "{structural_location_pk}"
}
```

**Steps:**

1. Confirm the target location is structural: `GET /api/stock/location/{structural_location_pk}/` and verify `structural` is `true`.
2. Send `POST /api/stock/` with the body above and write credentials.
3. Verify response status is `400`.
4. Verify response body contains an error indicating stock items cannot be placed in a structural location.

**Expected Result:** `400 Bad Request`. Stock items must not be located directly into a structural location.

---

## TC-ASCRUD-019: POST /api/stock/ with missing required fields returns 400

**Type:** API / Negative
**Priority:** P1
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{}
```

**Steps:**

1. Send `POST /api/stock/` with an empty JSON body and write credentials.
2. Verify response status is `400`.
3. Verify response body contains an error referencing the `part` field as required.

**Expected Result:** `400 Bad Request`. `part` is a required field; its absence causes a validation error.

---

## TC-ASCRUD-020: POST /api/stock/ without authentication returns 401 or 403

**Type:** API / Security
**Priority:** P1
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- No authentication header is sent.

**Test Data:**

```json
{
  "part": 1,
  "quantity": 1
}
```

**Steps:**

1. Send `POST /api/stock/` with no `Authorization` header.
2. Verify response status is `401` or `403`.
3. Verify no stock item is created.

**Expected Result:** `401 Unauthorized` or `403 Forbidden`. Unauthenticated callers cannot create stock items.

---

## TC-ASCRUD-021: DELETE /api/stock/{id}/ on a non-existent PK returns 404

**Type:** API / Negative
**Priority:** P3
**Endpoint:** `DELETE /api/stock/{id}/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

- `{id}`: a PK that does not exist (e.g. `999999999`).

**Steps:**

1. Send `DELETE /api/stock/999999999/` with write credentials.
2. Verify response status is `404`.

**Expected Result:** `404 Not Found`. Deleting a non-existent stock item returns 404.

---

## TC-ASCRUD-022: POST /api/stock/ with negative quantity returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- An active part exists (`{part_pk}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "part": "{part_pk}",
  "quantity": -5
}
```

**Steps:**

1. Send `POST /api/stock/` with the body above and write credentials.
2. Verify response status is `400`.
3. Verify response body references the `quantity` field.

**Expected Result:** `400 Bad Request`. Quantity must be a non-negative value.

---

## TC-ASCRUD-023: GET /api/stock/ with serial_numbers pattern collision returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `POST /api/stock/`

**Preconditions:**

- An active, trackable part exists (`{trackable_part_pk}`).
- A stock item with serial number `"SN-001"` already exists for that part.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "part": "{trackable_part_pk}",
  "quantity": 1,
  "serial_numbers": "SN-001"
}
```

**Steps:**

1. Send `POST /api/stock/` with the body above and write credentials.
2. Verify response status is `400`.
3. Verify response body contains an error mentioning duplicate or invalid serial numbers.

**Expected Result:** `400 Bad Request`. Duplicate serial numbers are rejected per the uniqueness constraint.
