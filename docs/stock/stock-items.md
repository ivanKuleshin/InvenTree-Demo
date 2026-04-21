---
source: https://docs.inventree.org/en/stable/stock/stock/
component: stock
topic: Stock Items — Attributes, Lifecycle, Tracking, Test Results
fetched: 2026-04-16
---

> **Source**: [https://docs.inventree.org/en/stable/stock/stock/](https://docs.inventree.org/en/stable/stock/stock/)
> Also: [tracking](https://docs.inventree.org/en/stable/stock/tracking/), [test results](https://docs.inventree.org/en/stable/stock/test/), [traceability](https://docs.inventree.org/en/stable/stock/traceability/), [availability](https://docs.inventree.org/en/stable/stock/availability/), [status](https://docs.inventree.org/en/stable/stock/status/), [expiry](https://docs.inventree.org/en/stable/stock/expiry/), [ownership](https://docs.inventree.org/en/stable/stock/owner/)

# Stock Items

## Table of Contents

- [Stock Item](#stock-item)
  - [Stock Item Details](#stock-item-details)
- [StockItem Schema (API)](#stockitem-schema-api)
- [Stock Item Lifecycle](#stock-item-lifecycle)
- [Serial Numbers and Batch Codes](#serial-numbers-and-batch-codes)
  - [Batch Codes](#batch-codes)
  - [Serial Numbers](#serial-numbers)
  - [Serial Number Generation Patterns](#serial-number-generation-patterns)
  - [Adjusting Serial Numbers](#adjusting-serial-numbers)
  - [Serial Number Errors](#serial-number-errors)
- [Per-item Actions](#per-item-actions)
- [Stock Tracking (History)](#stock-tracking-history)
  - [Tracking Events](#tracking-events)
  - [Viewing Stock Tracking History](#viewing-stock-tracking-history)
  - [StockTracking Schema](#stocktracking-schema)
  - [Stock Tracking Settings](#stock-tracking-settings)
- [Stock Item Test Results](#stock-item-test-results)
  - [Test Result Fields](#test-result-fields)
  - [Multiple Test Results](#multiple-test-results)
  - [StockItemTestResult Schema](#stockitemtestresult-schema)
- [List Endpoint Filters](#list-endpoint-filters)

---

## Stock Item

A _Stock Item_ is an actual instance of a [_Part_](../parts/part-overview.md) item. It represents a physical quantity of the _Part_ in a specific location.

Each Part instance may have multiple stock items associated with it, in various quantities and locations. Additionally, each stock item may have a serial number (if the part is tracked by serial number) and may be associated with a particular supplier part (if the item was purchased from a supplier).

### Stock Item Details

Each _Stock Item_ is linked to the following information:

- **Part** — Which _Part_ this stock item is an instance of
- **Location** — Where is this stock item located?
- **Quantity** — How many items are in stock?
- **Supplier** — If this part was purchased from a _Supplier_, which _Supplier_ did it come from?
- **Supplier Part** — Link to the particular _Supplier Part_, if appropriate.
- **Last Updated** — Date that the stock quantity was last updated
- **Last Stocktake** — Date that this stock item was last counted
- **Status** — Status of this stock item
- **Serial Number** — If the part is tracked by serial number, the unique serial number of this stock item
- **Batch Code** — If the part is tracked by batch code, the batch code of this stock item

## StockItem Schema (API)

Full field list from the live OpenAPI schema (`StockItem` component, `/api/stock/` endpoint). Flag key: `REQ` = required on create, `RO` = read-only, `WO` = write-only, `null` = nullable.

| Field | Type | Flags | Description |
| --- | --- | --- | --- |
| `pk` | integer | REQ, RO | Primary key |
| `part` | integer | REQ | Base Part (FK) |
| `quantity` | number (double) | REQ | Quantity on hand |
| `serial` | string (max 100) | null | Serial number for this item |
| `batch` | string (max 100) | null | Batch code for this stock item |
| `location` | integer | null | StockLocation FK |
| `belongs_to` | integer | null | Is this item installed in another item? |
| `build` | integer | null | Build order that produced this item |
| `consumed_by` | integer | null | Build order which consumed this stock item |
| `customer` | integer | null | Customer (Company with `is_customer=true`) |
| `delete_on_deplete` | boolean |  | Delete this Stock Item when stock is depleted |
| `expiry_date` | string (date) | null | Stock will be considered expired after this date |
| `in_stock` | boolean | REQ, RO | Derived: item is in stock (not consumed/shipped/installed) |
| `is_building` | boolean |  | Item is currently being produced |
| `link` | string (uri, max 2000) |  | Link to external URL |
| `notes` | string (max 50000) | null | Markdown notes (optional) |
| `owner` | integer | null | Owner (user or group) |
| `packaging` | string (max 50) | null | Packaging this stock item is stored in |
| `parent` | integer | RO, null | Parent stock item (if split from another) |
| `purchase_order` | integer | null | Purchase order for this stock item |
| `purchase_order_reference` | string | RO, null | Reference of the linked PO |
| `sales_order` | integer | null | Sales order FK |
| `sales_order_reference` | string | RO, null | Reference of the linked SO |
| `status` | `StockItemStatusEnum` |  | Status code (10 / 50 / 55 / 60 / 65 / 70 / 75 / 85) |
| `status_text` | string | REQ, RO | Human-readable status label |
| `status_custom_key` | integer | null | Additional status information for this item |
| `supplier_part` | integer | null | Supplier part FK |
| `SKU` | string | RO, null | SKU of the linked SupplierPart |
| `MPN` | string | RO, null | Manufacturer part number of the linked ManufacturerPart |
| `barcode_hash` | string | REQ, RO | Unique hash of barcode data |
| `updated` | string (date-time) | RO, null | Timestamp of last update |
| `stocktake_date` | string (date) | RO, null | Date of last stocktake |
| `purchase_price` | string (decimal) | null | Purchase price of this stock item, per unit or pack |
| `purchase_price_currency` | string |  | Purchase currency of this stock item |
| `use_pack_size` | boolean | WO, null | When adding: `quantity` is interpreted as number of packs |
| `serial_numbers` | string | WO, null | Range/list expression for new serialized items |
| `allocated` | number (double) | RO, null | Allocated quantity |
| `expired` | boolean | RO, null | Derived from `expiry_date` |
| `installed_items` | integer | RO, null | Count of items installed inside this one |
| `child_items` | integer | RO, null | Count of child stock items |
| `stale` | boolean | RO, null | Approaching expiry per `STOCK_STALE_DAYS` |
| `part_detail` | `PartBrief` | RO, null | Expanded part info (if requested) |
| `tracking_items` | integer | RO, null | Number of `StockTracking` entries for this item |

## Stock Item Lifecycle

Typical lifecycle states and transitions for a stock item:

1. **Creation** — via POST `/api/stock/` (manual), via Purchase Order receipt (sets `purchase_order`), via Build output (sets `build`), via conversion/split, or via `/api/stock/{id}/serialize/` from a non-serialized parent.
2. **In-stock** — `in_stock = true`; appears in regular stock tables; contributes to `In Stock` rollups.
3. **Allocated** — `allocated` > 0; reserved for a Build or Sales Order. `in_stock` is still true until consumption.
4. **Installed** — `belongs_to` set to another StockItem; moves with its container.
5. **Consumed** — `consumed_by` set; quantity decremented or zeroed by the Build completion.
6. **Sent to customer** — `customer` set; still exists in the system but `in_stock = false`. Can be returned via `/api/stock/return/`.
7. **Depleted** — quantity reaches 0. If `delete_on_deplete = true`, the record is deleted; otherwise it persists as a zero-quantity record.
8. **Deleted** — removed via DELETE `/api/stock/{id}/`; `StockTracking` entries persist under the Part's history.

## Serial Numbers and Batch Codes

Individual stock items can be assigned a batch code, or a serial number, or both, or neither, as requirements dictate.

Out of the box, the default implementations for both batch codes and serial numbers are (intentionally) simplistic. Custom plugins can redefine how batch codes and serial numbers are generated and validated.

### Batch Codes

Batch codes can be used to specify a particular "group" of items, and can be assigned to any stock item without restriction. Batch codes are tracked even as stock items are split into separate items.

Multiple stock items may share the same batch code without restriction, even across different parts.

**Generating batch codes**: Batch codes can be generated automatically based on a provided pattern. The default pattern uses the current date-code. Context variables available in the builtin generator:

| Variable | Description |
| --- | --- |
| `year` | The current year (e.g. `2024`) |
| `month` | The current month number (e.g. `5`) |
| `day` | The current day of month (e.g. `21`) |
| `hour` | The current hour of day, 24-hour format (e.g. `23`) |
| `minute` | The current minute of hour (e.g. `17`) |
| `week` | The current week of year (e.g. `51`) |

**Plugin support**: custom batch-code functionality is implemented via the [Validation Plugin Mixin](https://docs.inventree.org/en/stable/plugins/mixins/validation/#batch-codes).

### Serial Numbers

A serial "number" is used to uniquely identify a single, unique stock item. While "number" is used throughout the documentation, these values are not required to be numeric.

**Uniqueness**: by default, serial numbers must be unique across any given Part instance (including any variants of that part). A global setting allows serial numbers to be enforced globally-unique across all parts.

In the default implementation, InvenTree assumes that serial "numbers" are integer values in a simple incrementing sequence e.g. `{1, 2, 3, 4, 5, 6}`. When generating the _next_ value for a serial number, the algorithm looks for the _most recent_ serial number, attempts to coerce that value into an integer, and increments it. Custom plugins can implement any validation and any increment scheme.

### Serial Number Generation Patterns

When creating a group of serialized stock items, a serial number expression can generate many unique values at once:

**Comma-separated values**:

| Pattern | Serial Numbers |
| --- | --- |
| `1, 2, 45, 99, 101` | `1, 2, 45, 99, 101` |

**Hyphen-separated range** (inclusive of both endpoints):

| Pattern | Serial Numbers |
| --- | --- |
| `10-15` | `10, 11, 12, 13, 14, 15` |

**Starting value + count** (using `+`):

| Pattern | Serial Numbers |
| --- | --- |
| `10+3` | `10, 11, 12, 13` |
| `100 + 2` | `100, 101, 102` |

**Next value** (tilde `~` is replaced by the next available serial). If the next value is `100`:

| Pattern | Serial Numbers |
| --- | --- |
| `~` | `100` |
| `~, ~, ~` | `100, 101, 102` |
| `800, ~, 900` | `800, 100, 900` |
| `~+5` | `100, 101, 102, 103, 104, 105` |

**Combination groups** (any of the above, comma-separated):

| Pattern | Serial Numbers |
| --- | --- |
| `1, 2, 4-7, 10` | `1, 2, 4, 5, 6, 7, 10` |
| `40+4, 50+4` | `40, 41, 42, 43, 44, 50, 51, 52, 53, 54` |
| `10, 14, 20+3, 30-35` | `10, 14, 20, 21, 22, 23, 30, 31, 32, 33, 34, 35` |

> **[IMAGE DESCRIPTION]**: "Serial number entry" dialog showing a form field labelled "Serial Numbers" where the user can enter a comma-separated or range-based expression (e.g. `100+5`). A "Next" preview below the field shows the expanded list of serial numbers that will be assigned.

### Adjusting Serial Numbers

Once a stock item has been created with a serial number, it is possible to adjust that value. On the stock item detail page, select the "Edit" option and modify the serial number field. Any adjustment is subject to the same validation rules as creation; an error message is shown if the new value is invalid.

### Serial Number Errors

Examples of validation failures surfaced by the server:

- **Invalid quantity** — the expression produces more or fewer values than the quantity being created.
- **Duplicate serial numbers** — at least one value in the expression collides with an existing serial.
- **Invalid serial numbers** — plugin-backed validation has rejected a value.

## Per-item Actions

Endpoints operating on a single stock item:

| Endpoint | Method | Purpose |
| --- | --- | --- |
| `/api/stock/{id}/` | GET / PUT / PATCH / DELETE | Standard detail CRUD |
| `/api/stock/{id}/convert/` | POST | Convert to a variant part. Request body: `{ "part": <int> }` (`ConvertStockItem` schema) |
| `/api/stock/{id}/install/` | POST | Install another stock item into this one. Body: `{ "stock_item": <int>, "quantity": <int>, "note": <string> }` (`InstallStockItem`) |
| `/api/stock/{id}/uninstall/` | POST | Remove an installed item. Body: `{ "location": <int>, "note": <string> }` (`UninstallStockItem`) — `location` is the destination for the uninstalled item |
| `/api/stock/{id}/serialize/` | POST | Split a non-serialized stock item into serialized children. Body: `{ "quantity": <int>, "serial_numbers": <expr>, "destination": <int>, "notes": <string> }` (`SerializeStockItem`) |
| `/api/stock/{id}/serial-numbers/` | GET | Return the immediately-preceding and immediately-following serial numbers for this item. Response schema: `StockItemSerialNumbers` with `next` and `previous` (both `StockItem`). |

## Stock Tracking (History)

### Tracking Events

Stock tracking entries record the history of stock item adjustments, including the user who performed the action, the date, and the quantity change. This allows users to maintain a complete history of stock item movements and adjustments over time.

Stock tracking entries are created automatically whenever a stock item is adjusted, either through manual adjustments or automated processes such as order fulfillment or build completion. Examples:

- Manual stock adjustments (e.g. correcting inventory counts)
- Creation of new stock items (e.g. receiving new inventory)
- Allocation of stock items to orders (e.g. shipping items against sales orders)
- Consumption of stock items during build processes (e.g. using items to complete a build order)

### Viewing Stock Tracking History

**Stock Item Tracking History**: per-item view, available on the _Stock Item Detail_ page under the _Stock Tracking_ tab. Displays all tracking entries for that specific stock item.

**Part Tracking History**: per-part view, available on the _Part Detail_ page under the _Stock History_ tab. Displays all tracking entries for any stock item linked to that part.

> **Deleted stock items**: even if a stock item is deleted from the system, the associated stock tracking entries are retained for historical reference. They are visible in the part tracking history but not in the stock item tracking history (since the stock item no longer exists).

### StockTracking Schema

From the live OpenAPI schema (`StockTracking` component, `/api/stock/track/` endpoint):

| Field | Type | Flags | Description |
| --- | --- | --- | --- |
| `pk` | integer | REQ, RO | Primary key |
| `item` | integer | null | StockItem FK |
| `part` | integer | RO, null | Derived Part FK |
| `date` | string (date-time) | REQ, RO | When the event occurred |
| `deltas` | object | REQ, RO | Snapshot of what changed (quantity, status, location, etc.) |
| `label` | string | REQ, RO | Human-readable event label |
| `notes` | string (max 512) | null | Entry notes |
| `tracking_type` | integer | REQ, RO | Numeric event type |
| `user` | integer | RO, null | User who performed the action |

Endpoints:

- `GET /api/stock/track/` — list. Supported filters: `item`, `part`, `user`, `item_detail`, `user_detail`, `include_variants`, `min_date`, `max_date`, `search` (on `notes`), `ordering`, `limit`/`offset`.
- `GET /api/stock/track/{id}/` — detail.
- `GET /api/stock/track/status/` — return the enum of tracking types.

### Stock Tracking Settings

Global settings controlling tracking behavior:

| Name | Description | Default | Units |
| --- | --- | --- | --- |
| **Delete Old Stock Tracking Entries** | Delete stock tracking entries older than the specified number of days | `False` |  |
| **Stock Tracking Deletion Interval** | Stock tracking entries will be deleted after the specified number of days | `365` | days |

## Stock Item Test Results

Stock items which are associated with a [testable part](../parts/part-overview.md#testable) can have associated test data — useful for tracking unit testing / commissioning / acceptance data against a serialized stock item.

The master Part record defines multiple [test templates](../parts/part-test-templates.md), against which test data can be uploaded. Additionally, arbitrary test information can be assigned to the stock item.

> **[IMAGE DESCRIPTION]**: "Stock Item Test Results" table on the Stock Item detail page. Columns show Test Name, Required, Result (pass/fail badge), Value, Notes, Attachment (paperclip icon), User, Date. Each row corresponds to a test template defined on the part; rows without a recorded result show an empty / missing state.

### Test Result Fields

- **Test Template** (`template`) — links to a [Part Test Template](../parts/part-test-templates.md) object. Each test result instance must link to a test template.
- **Result** (`result`) — boolean pass/fail status of the test.
- **Value** (`value`) — optional value uploaded as part of the test data. For example, the firmware version of a programmed device.
- **Notes** (`notes`) — optional extra notes.
- **Attachment** (`attachment`) — optional attached file with extra test information.

### Multiple Test Results

Multiple results can be uploaded against the same test name. When multiple test results are uploaded, the most recent value is used to determine the pass/fail status of the test. Keeping all test records is useful because a given test may need to be re-run after a failure and fix.

**Automated test integration**: the testing framework is especially useful with an automated acceptance testing framework. Test results can be uploaded via the InvenTree REST API or the InvenTree Python Interface.

### StockItemTestResult Schema

From the live OpenAPI schema (`StockItemTestResult` component, `/api/stock/test/` endpoint):

| Field | Type | Flags | Description |
| --- | --- | --- | --- |
| `pk` | integer | REQ, RO | Primary key |
| `stock_item` | integer | REQ | StockItem FK |
| `result` | boolean |  | Test result (pass/fail) |
| `value` | string (max 500) |  | Test output value |
| `attachment` | string (uri) | null | Test result attachment |
| `notes` | string (max 500) |  | Test notes |
| `test_station` | string (max 500) |  | Identifier of the test station where the test was performed |
| `started_datetime` | string (date-time) | null | Timestamp of the test start |
| `finished_datetime` | string (date-time) | null | Timestamp of the test finish |
| `user` | integer | RO, null | User who submitted the result |
| `date` | string (date-time) | REQ, RO | Record date |
| `template` | integer | null | `PartTestTemplate` FK |

Endpoints:

- `GET /api/stock/test/` — list. Filters include `stock_item`, `part`, `build`, `template`, `test` (by name, case-insensitive), `required`, `enabled`, `result`, `value`, `user`, `template_detail`, `user_detail`, `include_installed` (roll up into installed children), `search`, `ordering`.
- `POST /api/stock/test/` — create.
- `DELETE /api/stock/test/` — bulk delete.
- `GET / PUT / PATCH / DELETE /api/stock/test/{id}/` — detail.

## List Endpoint Filters

`GET /api/stock/` supports 71 query parameters for filtering and detail expansion. Grouped for readability:

**Pagination & ordering**: `limit`, `offset`, `ordering`, `search` (searches `batch`, `location__name`, `part__IPN`, `part__name`).

**Part linkage**: `part`, `part_tree`, `part_detail`, `IPN`, `IPN_contains`, `IPN_regex`, `name`, `name_contains`, `name_regex`, `category`, `include_variants`, `assembly`, `salable`, `active`, `tests`.

**Location**: `location` (numeric or literal `null`), `cascade` (include child locations), `location_detail`, `path_detail`, `external`, `ancestor`, `exclude_tree`.

**Quantity and availability**: `in_stock`, `available`, `allocated`, `depleted`, `min_stock`, `max_stock`.

**Batch**: `batch`, `batch_regex`, `has_batch`.

**Serial**: `serial`, `serial_gte`, `serial_lte`, `serialized`.

**Lifecycle flags**: `is_building`, `installed`, `consumed`, `consumed_by`, `has_child_items`, `has_installed_items`, `tracked`.

**Status & dates**: `status`, `expired`, `stale`, `expiry_after`, `expiry_before`, `stocktake_after`, `stocktake_before`, `updated_after`, `updated_before`, `sent_to_customer`.

**Order relationships**: `purchase_order`, `sales_order`, `build`, `bom_item`.

**Supplier/manufacturer**: `supplier`, `supplier_part`, `supplier_part_detail`, `manufacturer`, `manufacturer_part`, `company`, `has_purchase_price`.

**Customer**: `customer`, `belongs_to`.

**Tags**: `tags__name`, `tags__slug`.
