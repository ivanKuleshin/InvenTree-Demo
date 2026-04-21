---
source: https://docs.inventree.org/en/stable/stock/adjust/
component: stock
topic: Stock Adjustments — Add, Remove, Count, Transfer, Change Status, Assign, Merge, Return
fetched: 2026-04-16
---

> **Source**: [https://docs.inventree.org/en/stable/stock/adjust/](https://docs.inventree.org/en/stable/stock/adjust/)

# Stock Adjustments

## Table of Contents

- [Overview](#overview)
- [Common Shape](#common-shape)
- [Add Stock — `POST /api/stock/add/`](#add-stock--post-apistockadd)
- [Remove Stock — `POST /api/stock/remove/`](#remove-stock--post-apistockremove)
- [Count Stock — `POST /api/stock/count/`](#count-stock--post-apistockcount)
- [Transfer Stock — `POST /api/stock/transfer/`](#transfer-stock--post-apistocktransfer)
- [Change Status — `POST /api/stock/change_status/`](#change-status--post-apistockchange_status)
- [Assign Stock — `POST /api/stock/assign/`](#assign-stock--post-apistockassign)
- [Merge Stock — `POST /api/stock/merge/`](#merge-stock--post-apistockmerge)
- [Return Stock — `POST /api/stock/return/`](#return-stock--post-apistockreturn)
- [Side Effects: Stock Tracking](#side-effects-stock-tracking)

---

## Overview

InvenTree provides multiple stock adjustment actions. Each adjustment is automatically tracked to maintain a complete stock history — every invocation creates one or more `StockTracking` entries linked to the affected stock items and to the current user.

In the UI, stock adjustments are accessed via the "Stock Options" dropdown on any stock-items table.

> **[IMAGE DESCRIPTION]**: "Stock Options" dropdown expanded above a stock items table. Menu entries include "Move Stock", "Add Stock", "Remove Stock", "Count Stock", "Merge Stock", "Transfer Stock", "Change Status", and similar actions, each with an icon on the left.

This page covers the seven stock-mutating actions listed in the module inventory plus `/api/stock/return/` which is part of the same adjustment family.

| Action | Path | Request schema | Effect |
| --- | --- | --- | --- |
| Add | `/api/stock/add/` | `StockAdd` | Increase quantity |
| Remove | `/api/stock/remove/` | `StockRemove` | Decrease quantity |
| Count | `/api/stock/count/` | `StockCount` | Stocktake — record the counted quantity |
| Transfer | `/api/stock/transfer/` | `StockTransfer` | Move to a new location |
| Change Status | `/api/stock/change_status/` | `StockChangeStatus` | Change `status` on multiple items |
| Assign | `/api/stock/assign/` | `StockAssignment` | Assign stock items to a customer |
| Merge | `/api/stock/merge/` | `StockMerge` | Merge multiple stock items into one |
| Return | `/api/stock/return/` | `StockReturn` | Return items (from a customer) into stock |

## Common Shape

Five of the eight actions (Add, Remove, Count, Transfer, Return) share a common request shape: a list of per-item adjustment specs wrapped in an envelope. Each per-item spec conforms to `StockAdjustmentItem`:

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `pk` | integer | yes | StockItem primary key |
| `quantity` | string (decimal) | yes | Adjustment quantity (decimal, not float — sent as a string) |
| `batch` | string (max 100) | no | Batch code for this stock item |
| `status` | integer | no | Stock item status code (see overview) |
| `packaging` | string (max 50) | no | Packaging this stock item is stored in |

The envelope adds:

- `items`: array of `StockAdjustmentItem` (required)
- `notes`: string — free-form stock transaction notes
- Action-specific extras (e.g. `location`, `merge`, etc.) — see each section below.

All actions return the updated stock item(s) or a summary response depending on the endpoint; every successful call creates one `StockTracking` entry per affected stock item.

## Add Stock — `POST /api/stock/add/`

**Description** (from schema): Endpoint for adding a quantity of stock to an existing StockItem.

**Use case**: Putting parts back into stock — e.g. returning unused parts after a build. The in-stock quantity for each selected item is increased by the given amount.

**Request schema** (`StockAdd`):

```json
{
  "items": [
    { "pk": 123, "quantity": "5.000", "batch": "optional", "status": 10, "packaging": "optional" },
    { "pk": 456, "quantity": "10.000" }
  ],
  "notes": "Returned from build B-0042"
}
```

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `items` | array of `StockAdjustmentItem` | yes | Items to adjust |
| `notes` | string | no | Stock transaction notes |

**Effect**: Each item's `quantity` is increased by the specified decimal value. A `StockTracking` entry is recorded per item.

> **[IMAGE DESCRIPTION]**: "Add Stock" modal dialog. A table lists the selected stock items with columns for Part, Location, Current Quantity, and an editable Quantity-to-add. A notes textarea below the table accepts free-form comments. Confirm / Cancel buttons at the bottom.

## Remove Stock — `POST /api/stock/remove/`

**Description** (from schema): Endpoint for removing a quantity of stock from an existing StockItem.

**Use case**: Taking parts out of stock for use. The in-stock quantity for each selected item is decreased by the given amount.

**Request schema** (`StockRemove`): Identical shape to `StockAdd` — `items` (array of `StockAdjustmentItem`) + optional `notes`.

```json
{
  "items": [
    { "pk": 123, "quantity": "2.000" }
  ],
  "notes": "Consumed by repair"
}
```

**Effect**: Each item's `quantity` is decreased. If `delete_on_deplete = true` and the quantity reaches 0, the stock item is auto-deleted. A `StockTracking` entry is recorded per item.

## Count Stock — `POST /api/stock/count/`

**Description** (from schema): Endpoint for counting stock (performing a stocktake).

**Use case**: Record the actual counted quantity at a point in time. The quantity for each part is pre-filled with the current quantity based on stock item history; the user overrides it with the physically-counted value.

**Request schema** (`StockCount`): Same shape as Add/Remove — `items` + `notes`. The `quantity` field is interpreted as the **new absolute quantity**, not a delta.

```json
{
  "items": [
    { "pk": 123, "quantity": "47.000" }
  ],
  "notes": "Q2 stocktake"
}
```

**Effect**: Each item's `quantity` is **set** (not added/subtracted) to the supplied value. `stocktake_date` is updated. A `StockTracking` entry is recorded per item.

## Transfer Stock — `POST /api/stock/transfer/`

**Description** (from schema): API endpoint for performing stock movements.

**Use case**: Multiple stock items can be moved to a new location in a single operation. Each item is moved to the selected destination location, and a stock tracking entry is added to each item's history.

**Request schema** (`StockTransfer`):

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `items` | array of `StockAdjustmentItem` | yes | Items to move |
| `location` | integer | yes | Destination stock location |
| `notes` | string | no | Stock transaction notes |

```json
{
  "items": [ { "pk": 123, "quantity": "5.000" } ],
  "location": 7,
  "notes": "Reorg to shelf A"
}
```

> **[IMAGE DESCRIPTION]**: "Stock Move" modal dialog. A table lists the selected stock items with columns Part, Source Location, Quantity-to-move. A destination-location dropdown above the table prompts the user to pick the target location. Notes textarea below. Confirm / Cancel buttons.

**Effect**: Each item's `location` is set to the supplied destination. `StockTracking` entries are recorded per item.

## Change Status — `POST /api/stock/change_status/`

**Description** (from schema): API endpoint to change the status code of multiple StockItem objects.

**Request schema** (`StockChangeStatus`) — note this does **not** use `StockAdjustmentItem`:

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `items` | array of integer | yes | Select stock items (by PK) to change status |
| `status` | integer | no | Target stock item status code (10/50/55/60/65/70/75/85) |
| `note` | string | no | Add transaction note (optional) — note: field is `note`, not `notes` |

```json
{
  "items": [123, 456, 789],
  "status": 55,
  "note": "Dropped during transport"
}
```

**Effect**: Sets `status` on every listed stock item. A `StockTracking` entry is recorded per item.

## Assign Stock — `POST /api/stock/assign/`

**Description** (from schema): API endpoint for assigning stock to a particular customer.

**Use case**: Transfer stock items from internal inventory to a specific customer (Company with `is_customer=true`) without going through a Sales Order flow.

**Request schema** (`StockAssignment`):

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `items` | array of `StockAssignmentItem` | yes | Items to assign |
| `customer` | integer | yes | Customer (Company FK) to assign stock items |
| `notes` | string | no | Stock assignment notes |

`StockAssignmentItem`:

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `item` | integer | yes | StockItem primary key |

```json
{
  "items": [ { "item": 123 }, { "item": 456 } ],
  "customer": 42,
  "notes": "Shipped under PO-CUST-2024-01"
}
```

**Effect**: For each listed stock item, `customer` is set, and the item is considered to have been "sent to customer". `in_stock` becomes false. A `StockTracking` entry is recorded per item.

## Merge Stock — `POST /api/stock/merge/`

**Description** (from schema): API endpoint for merging multiple stock items.

**Use case**: Combine two or more stock items of the same part into a single record with summed quantity.

**Merge conditions** (hard constraints):

- A stock item cannot be merged with itself.
- Only stock items referring to the same part can be merged.
- Supplier parts between all items must match — unless the user explicitly allows mismatched suppliers.
- Stock status between all items must match — unless the user explicitly allows mismatched status.

**Items that cannot be merged** (regardless of options):

- Assigned to a sales order
- Installed in another item
- Contains other items
- Assigned to a customer
- Currently in production (`is_building`)
- Serialized

**Request schema** (`StockMerge`):

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `items` | array of `StockMergeItem` | yes | Items to merge |
| `location` | integer | yes | Destination stock location for the resulting item |
| `notes` | string | no | Stock merging notes |
| `allow_mismatched_suppliers` | boolean | no (default false) | Allow stock items with different supplier parts to be merged |
| `allow_mismatched_status` | boolean | no (default false) | Allow stock items with different status codes to be merged |

`StockMergeItem`:

| Field | Type | Required |
| --- | --- | --- |
| `item` | integer | yes |

```json
{
  "items": [ { "item": 123 }, { "item": 124 }, { "item": 125 } ],
  "location": 7,
  "allow_mismatched_suppliers": false,
  "allow_mismatched_status": false,
  "notes": "Consolidate after reorg"
}
```

> **[IMAGE DESCRIPTION]**: "Merge Stock Items" modal. Lists the selected stock items with part, current location, quantity, supplier, status. Two checkboxes at the bottom allow the user to explicitly permit mismatched suppliers and mismatched statuses (both unchecked by default). A destination location selector and notes textarea. Submit / Cancel buttons.

**Effect**: Quantities of all input items are summed into a single new stock item at the specified `location`; input items are deleted (or marked consumed depending on the implementation). A `StockTracking` entry records the merge.

## Return Stock — `POST /api/stock/return/`

**Description** (from schema): API endpoint for returning items into stock.

**Use case**: Receive previously-sent stock back into inventory — e.g. from a customer return, or un-assign stock that had been pushed out.

**Request schema** (`StockReturn`):

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `items` | array of `StockAdjustmentItem` | yes | Items to return |
| `location` | integer | yes | Destination stock location |
| `notes` | string | no | Stock transaction notes |
| `merge` | boolean | no (default false) | Merge returned items into existing stock items if possible |

```json
{
  "items": [ { "pk": 123, "quantity": "5.000" } ],
  "location": 1,
  "merge": true,
  "notes": "RMA-2024-003"
}
```

**Effect**: Items are returned to the specified location. If `merge = true` and a compatible stock item already exists at the destination, quantities are combined. `customer` is cleared. Status may be set to 85 (Returned). `StockTracking` entries are recorded.

## Side Effects: Stock Tracking

Every successful stock adjustment produces one `StockTracking` entry per affected stock item. The tracking entry captures:

- `user` — the caller
- `date` — timestamp of the action
- `tracking_type` — numeric code for the action (`add`, `remove`, `count`, `transfer`, `change_status`, `assign`, `merge`, `return`, etc.)
- `deltas` — snapshot of what changed (quantity delta, old/new location, old/new status, etc.)
- `label` — human-readable event label
- `notes` — the `notes` (or `note`) field from the request

See [stock-items.md §Stock Tracking](stock-items.md#stock-tracking-history) for full schema and viewing options. The tracking-type enumeration is available at `GET /api/stock/track/status/`.
