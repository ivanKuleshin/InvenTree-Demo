# Stock Adjustments Test Suite

**Prefix:** `TC-ASADJ-`
**Endpoint family:** `/api/stock/add/`, `/api/stock/remove/`, `/api/stock/count/`, `/api/stock/transfer/`, `/api/stock/change_status/`, `/api/stock/assign/`, `/api/stock/merge/`, `/api/stock/return/`
**Auth:** HTTP Basic with a user that has stock write permission. All adjustment endpoints are `[MUTATING]`.

---

## TC-ASADJ-001: POST /api/stock/add/ increases quantity on a stock item

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/add/`

**Preconditions:**

- A stock item with a known PK (`{item_pk}`) and known current `quantity` (`{original_qty}`) exists.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "pk": "{item_pk}", "quantity": "10.000" }
  ],
  "notes": "TC-ASADJ-001: add quantity test"
}
```

**Steps:**

1. Record the current quantity: `GET /api/stock/{item_pk}/` — note `quantity` as `{original_qty}`.
2. Send `POST /api/stock/add/` with the body above and write credentials.
3. Verify response status is `200` (or `201` — note actual status code from response).
4. Send `GET /api/stock/{item_pk}/` to read the updated state.
5. Verify `quantity` equals `{original_qty} + 10`.
6. Send `GET /api/stock/track/?item={item_pk}&limit=1&ordering=-date` and verify the latest tracking entry exists with a tracking type corresponding to "add".

**Expected Result:** The stock item's `quantity` increases by exactly 10. A `StockTracking` entry is created for the operation.

---

## TC-ASADJ-002: POST /api/stock/remove/ decreases quantity on a stock item

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/remove/`

**Preconditions:**

- A stock item with a known PK (`{item_pk}`) and `quantity` of at least `5` exists.
- `delete_on_deplete` is `false` on the item (to avoid accidental deletion).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "pk": "{item_pk}", "quantity": "5.000" }
  ],
  "notes": "TC-ASADJ-002: remove quantity test"
}
```

**Steps:**

1. Record current `quantity` as `{original_qty}`: `GET /api/stock/{item_pk}/`.
2. Send `POST /api/stock/remove/` with the body above and write credentials.
3. Verify response status is `200` (or `201`).
4. Send `GET /api/stock/{item_pk}/` and verify `quantity` equals `{original_qty} - 5`.
5. Verify a `StockTracking` entry for this item is created with a tracking type corresponding to "remove".

**Expected Result:** Quantity decreases by 5. A tracking entry is recorded.

---

## TC-ASADJ-003: POST /api/stock/count/ sets the absolute quantity (stocktake)

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/count/`

**Preconditions:**

- A stock item with a known PK (`{item_pk}`) and any current quantity exists.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "pk": "{item_pk}", "quantity": "47.000" }
  ],
  "notes": "TC-ASADJ-003: Q2 stocktake"
}
```

**Steps:**

1. Record the current quantity: `GET /api/stock/{item_pk}/`.
2. Send `POST /api/stock/count/` with the body above and write credentials.
3. Verify response status is `200` (or `201`).
4. Send `GET /api/stock/{item_pk}/` and verify `quantity` equals `47` (absolute, not a delta).
5. Verify `stocktake_date` has been updated to today's date (format: `YYYY-MM-DD`).
6. Verify a `StockTracking` entry is created with a tracking type corresponding to "count".

**Expected Result:** Quantity is set to exactly `47` regardless of the prior value. `stocktake_date` is refreshed. A tracking entry is recorded.

---

## TC-ASADJ-004: POST /api/stock/transfer/ moves a stock item to a new location

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/transfer/`

**Preconditions:**

- A stock item with a known PK (`{item_pk}`) exists at location `{source_location_pk}`.
- A non-structural destination location with a known PK (`{dest_location_pk}`) exists.
- `{dest_location_pk}` is different from `{source_location_pk}`.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "pk": "{item_pk}", "quantity": "5.000" }
  ],
  "location": "{dest_location_pk}",
  "notes": "TC-ASADJ-004: transfer to new shelf"
}
```

**Steps:**

1. Confirm current `location` of the item is `{source_location_pk}`: `GET /api/stock/{item_pk}/`.
2. Send `POST /api/stock/transfer/` with the body above and write credentials.
3. Verify response status is `200` (or `201`).
4. Send `GET /api/stock/{item_pk}/` and verify `location` equals `{dest_location_pk}`.
5. Verify a `StockTracking` entry is created for the item with tracking type corresponding to "transfer".

**Expected Result:** The stock item's `location` is updated to `{dest_location_pk}`. A tracking entry records the movement.

---

## TC-ASADJ-005: POST /api/stock/change_status/ sets a new status code on multiple items

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/change_status/`

**Preconditions:**

- Two stock items with known PKs (`{item_pk_1}`, `{item_pk_2}`) exist, both currently with status `10` (OK).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": ["{item_pk_1}", "{item_pk_2}"],
  "status": 55,
  "note": "TC-ASADJ-005: marked damaged after drop"
}
```

**Steps:**

1. Confirm both items have `status = 10`: `GET /api/stock/{item_pk_1}/` and `GET /api/stock/{item_pk_2}/`.
2. Send `POST /api/stock/change_status/` with the body above and write credentials.
3. Verify response status is `200` (or `201`).
4. Send `GET /api/stock/{item_pk_1}/` and verify `status` equals `55` and `status_text` equals `"Damaged"`.
5. Send `GET /api/stock/{item_pk_2}/` and verify the same.
6. Verify `StockTracking` entries are created for both items.

**Expected Result:** Both items have `status=55` and `status_text="Damaged"`. Two tracking entries are created, one per item.

---

## TC-ASADJ-006: POST /api/stock/assign/ assigns stock items to a customer

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/assign/`

**Preconditions:**

- A stock item with a known PK (`{item_pk}`) and `in_stock=true` exists.
- A company with `is_customer=true` exists with a known PK (`{customer_pk}`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "item": "{item_pk}" }
  ],
  "customer": "{customer_pk}",
  "notes": "TC-ASADJ-006: assigned for direct shipment"
}
```

**Steps:**

1. Confirm item has `in_stock=true` and `customer=null`: `GET /api/stock/{item_pk}/`.
2. Send `POST /api/stock/assign/` with the body above and write credentials.
3. Verify response status is `200` (or `201`).
4. Send `GET /api/stock/{item_pk}/` and verify `customer` equals `{customer_pk}`.
5. Verify `in_stock` is `false` (item is considered sent to customer).
6. Verify a `StockTracking` entry is created for the item.

**Expected Result:** The item's `customer` field is set to `{customer_pk}` and `in_stock` becomes `false`. A tracking entry is recorded.

---

## TC-ASADJ-007: POST /api/stock/merge/ merges two same-part stock items into one

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/merge/`

**Preconditions:**

- Two stock items (`{item_pk_1}` with `quantity=10`, `{item_pk_2}` with `quantity=20`) both referencing the same part (`{part_pk}`) exist.
- Both items have the same status (`10`) and no supplier part assigned, or `allow_mismatched_suppliers=true` is set.
- Neither item is serialized, sold, installed, in production, or assigned to a customer.
- A non-structural destination location (`{dest_location_pk}`) exists.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "item": "{item_pk_1}" },
    { "item": "{item_pk_2}" }
  ],
  "location": "{dest_location_pk}",
  "allow_mismatched_suppliers": false,
  "allow_mismatched_status": false,
  "notes": "TC-ASADJ-007: consolidate after reorg"
}
```

**Steps:**

1. Confirm both items exist, belong to `{part_pk}`, and have non-zero quantities.
2. Send `POST /api/stock/merge/` with the body above and write credentials.
3. Verify response status is `200` (or `201`).
4. Send `GET /api/stock/{item_pk_1}/` — verify it still exists OR that one of the items has been deleted (implementation detail — note actual behavior).
5. Verify that a single surviving stock item for `{part_pk}` at `{dest_location_pk}` has `quantity` equal to `30` (10 + 20).
6. Verify a `StockTracking` entry records the merge.

**Expected Result:** The two items are merged. The surviving item has the combined quantity of 30. Input items are deleted or marked consumed. A tracking entry is recorded.

---

## TC-ASADJ-008: POST /api/stock/return/ returns customer-assigned items into stock

**Type:** API / Functional
**Priority:** P1
**Endpoint:** `POST /api/stock/return/`

**Preconditions:**

- A stock item (`{item_pk}`) that has been assigned to a customer exists (i.e. `customer` is non-null and `in_stock=false`). Use TC-ASADJ-006 as a setup step if needed.
- A non-structural return destination location (`{return_location_pk}`) exists.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "pk": "{item_pk}", "quantity": "5.000" }
  ],
  "location": "{return_location_pk}",
  "merge": false,
  "notes": "TC-ASADJ-008: customer return RMA-001"
}
```

**Steps:**

1. Confirm item has `customer` set and `in_stock=false`: `GET /api/stock/{item_pk}/`.
2. Send `POST /api/stock/return/` with the body above and write credentials.
3. Verify response status is `200` (or `201`).
4. Send `GET /api/stock/{item_pk}/` and verify `customer` is `null` or the item is now at `{return_location_pk}`.
5. Verify `in_stock` is `true`.
6. Verify a `StockTracking` entry is created with a tracking type corresponding to "return".

**Expected Result:** Customer assignment is cleared. Item is returned to stock at the specified location. `in_stock` becomes `true`. A tracking entry is recorded.

---

## TC-ASADJ-009: POST /api/stock/merge/ with items from different parts returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `POST /api/stock/merge/`

**Preconditions:**

- Stock item `{item_pk_1}` belongs to part `{part_a_pk}`.
- Stock item `{item_pk_2}` belongs to part `{part_b_pk}` (a different part).
- A non-structural destination location (`{dest_location_pk}`) exists.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "item": "{item_pk_1}" },
    { "item": "{item_pk_2}" }
  ],
  "location": "{dest_location_pk}",
  "notes": "TC-ASADJ-009: incompatible parts merge"
}
```

**Steps:**

1. Confirm the two items reference different parts: `GET /api/stock/{item_pk_1}/` and `GET /api/stock/{item_pk_2}/`.
2. Send `POST /api/stock/merge/` with the body above and write credentials.
3. Verify response status is `400`.
4. Verify response body contains an error indicating the items belong to different parts.

**Expected Result:** `400 Bad Request`. Merging stock items of different parts is not permitted.

---

## TC-ASADJ-010: POST /api/stock/transfer/ to a structural location returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `POST /api/stock/transfer/`

**Preconditions:**

- A stock item with a known PK (`{item_pk}`) exists.
- A structural location with a known PK (`{structural_location_pk}`) exists (where `structural=true`).
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "pk": "{item_pk}", "quantity": "1.000" }
  ],
  "location": "{structural_location_pk}",
  "notes": "TC-ASADJ-010: transfer to structural location"
}
```

**Steps:**

1. Confirm the destination is structural: `GET /api/stock/location/{structural_location_pk}/` — verify `structural=true`.
2. Send `POST /api/stock/transfer/` with the body above and write credentials.
3. Verify response status is `400`.
4. Verify response body contains an error indicating stock items cannot be transferred to a structural location.

**Expected Result:** `400 Bad Request`. Transferring stock to a structural location is not allowed.

---

## TC-ASADJ-011: POST /api/stock/change_status/ with an invalid status code returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `POST /api/stock/change_status/`

**Preconditions:**

- A stock item with a known PK (`{item_pk}`) exists.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": ["{item_pk}"],
  "status": 999,
  "note": "TC-ASADJ-011: invalid status"
}
```

**Steps:**

1. Send `POST /api/stock/change_status/` with `status=999` (not in the documented enum 10/50/55/60/65/70/75/85) and write credentials.
2. Verify response status is `400`.
3. Verify response body references the `status` field and indicates an invalid choice.

**Expected Result:** `400 Bad Request`. Status code `999` is not a valid `StockItemStatusEnum` value.

---

## TC-ASADJ-012: POST /api/stock/add/ with empty items array returns 400

**Type:** API / Negative
**Priority:** P3
**Endpoint:** `POST /api/stock/add/`

**Preconditions:**

- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [],
  "notes": "TC-ASADJ-012: empty items"
}
```

**Steps:**

1. Send `POST /api/stock/add/` with `items` as an empty array and write credentials.
2. Verify response status is `400`.
3. Verify response body contains an error indicating the `items` array must not be empty.

**Expected Result:** `400 Bad Request`. An empty `items` array is not a valid adjustment request.

---

## TC-ASADJ-013: POST /api/stock/assign/ with a non-customer company returns 400

**Type:** API / Negative
**Priority:** P2
**Endpoint:** `POST /api/stock/assign/`

**Preconditions:**

- A stock item with a known PK (`{item_pk}`) exists.
- A company with `is_customer=false` (e.g. a supplier-only company) exists with PK `{non_customer_pk}`.
- Caller holds stock write permission.

**Test Data:**

```json
{
  "items": [
    { "item": "{item_pk}" }
  ],
  "customer": "{non_customer_pk}",
  "notes": "TC-ASADJ-013: assign to non-customer"
}
```

**Steps:**

1. Confirm the company at `{non_customer_pk}` has `is_customer=false`.
2. Send `POST /api/stock/assign/` with the body above and write credentials.
3. Verify response status is `400`.
4. Verify response body references the `customer` field and indicates the company is not a customer.

**Expected Result:** `400 Bad Request`. Stock can only be assigned to companies flagged as customers.

---

## TC-ASADJ-014: POST /api/stock/remove/ without authentication returns 401 or 403

**Type:** API / Security
**Priority:** P1
**Endpoint:** `POST /api/stock/remove/`

**Preconditions:**

- No authentication header is sent.

**Test Data:**

```json
{
  "items": [
    { "pk": 1, "quantity": "1.000" }
  ]
}
```

**Steps:**

1. Send `POST /api/stock/remove/` with no `Authorization` header.
2. Verify response status is `401` or `403`.

**Expected Result:** `401` or `403`. Unauthenticated callers cannot perform stock adjustments.
