---
source: https://docs.inventree.org/en/stable/stock/
component: stock
topic: Stock Domain Overview
fetched: 2026-04-16
---

> **Source**: [https://docs.inventree.org/en/stable/stock/](https://docs.inventree.org/en/stable/stock/)
> **Live schema**: [https://demo.inventree.org/api/schema/](https://demo.inventree.org/api/schema/) (OpenAPI 3.0.3, API version 479)

# Stock Overview

## Table of Contents

- [Scope of This Document](#scope-of-this-document)
- [What Stock Is in InvenTree](#what-stock-is-in-inventree)
- [Relation to Other Modules](#relation-to-other-modules)
- [Key Entities](#key-entities)
- [Stock Item Status Codes](#stock-item-status-codes)
- [Stock Availability Semantics](#stock-availability-semantics)
- [Stock Ownership](#stock-ownership)
- [Stock Expiry](#stock-expiry)
- [Key Flags — Quick Reference](#key-flags--quick-reference)
- [Base API Path Inventory](#base-api-path-inventory)
- [Related Documents](#related-documents)

---

## Scope of This Document

This file is the domain overview for the InvenTree Stock module. It covers the conceptual model that is shared across three related API surfaces:

1. **Stock** — `/api/stock/` (26 paths): StockItem, StockItemTracking, StockItemTestResult
2. **Stock Locations** — `/api/stock/location/`, `/api/stock/location-type/`: StockLocation, StockLocationType
3. **Stock Adjustments** — `/api/stock/add/`, `/api/stock/remove/`, `/api/stock/count/`, `/api/stock/transfer/`, `/api/stock/change_status/`, `/api/stock/assign/`, `/api/stock/merge/`, plus `/api/stock/return/`

Per-area detail lives in sibling files (`stock-items.md`, `stock-locations.md`, `stock-adjustments.md`).

## What Stock Is in InvenTree

A **Stock Item** is an actual instance of a [Part](../parts/part-overview.md). It represents a physical quantity of the Part in a specific location.

Each Part instance may have multiple stock items associated with it, in various quantities and locations. Additionally, each stock item may have a serial number (if the part is tracked by serial number) and may be associated with a particular supplier part (if the item was purchased from a supplier).

Key facts about stock items:

- A stock item is always linked to exactly one `Part`.
- A stock item's physical location is a `StockLocation` (FK, nullable — may be unlocated).
- A stock item can optionally have a `serial` (for serialized/trackable parts) and a `batch` code.
- A stock item has a numeric `status` (see status codes table below).
- A stock item can be `belongs_to` another stock item (installation hierarchy), `consumed_by` a Build order, or assigned to a `customer`.
- A stock item can have a `purchase_order` (origin) and a `sales_order` (outbound destination).
- A stock item can be marked `delete_on_deplete` — when the quantity reaches zero, the record is deleted automatically.
- Every mutation to a stock item creates a `StockTracking` entry for full history.

## Relation to Other Modules

| Related module | Link type | Field on StockItem |
| --- | --- | --- |
| [Parts](../parts/part-overview.md) | FK (required) | `part` |
| Stock Locations | FK (nullable) | `location` |
| Supplier Parts / Companies | FK (nullable) | `supplier_part` |
| Purchase Orders | FK (nullable) — origin of this stock | `purchase_order` |
| Sales Orders | FK (nullable) — outbound allocation | `sales_order` |
| Build Orders (Manufacturing) | FK (nullable) — produced by / consumed by | `build`, `consumed_by` |
| Customers (Companies) | FK (nullable) — current holder if sent to customer | `customer` |
| Installation (other StockItem) | Self-FK (nullable) — parent container | `belongs_to` |
| StockItemTracking | 1-to-many (reverse) — history log | `tracking_items` (count) |
| StockItemTestResult | 1-to-many (reverse) — per-template results | `tests` (boolean filter) |

## Key Entities

| Entity | Purpose | API schema name | Primary path |
| --- | --- | --- | --- |
| StockItem | Physical instance of a Part at a location with a quantity | `StockItem` | `/api/stock/` |
| StockLocation | Hierarchical storage location | `Location` | `/api/stock/location/` |
| StockLocationType | Reusable location category carrying an icon | `StockLocationType` | `/api/stock/location-type/` |
| StockItemTracking | Audit log entry for every stock adjustment | `StockTracking` | `/api/stock/track/` |
| StockItemTestResult | Pass/fail record against a `PartTestTemplate` | `StockItemTestResult` | `/api/stock/test/` |

## Stock Item Status Codes

Each stock item has a numeric `status` attribute. Status codes are defined in the server source (`src/backend/InvenTree/stock/status_codes.py`) and exposed via the `StockItemStatusEnum` in the API schema. The `/api/stock/status/` endpoint returns the full enum list at runtime.

| Code | Label | Description | Counts as Available |
| --- | --- | --- | --- |
| 10 | OK | Stock item is healthy, nothing wrong to report | Yes |
| 50 | Attention needed | Stock item hasn't been checked or tested yet | Yes |
| 55 | Damaged | Stock item is not functional in its present state | Yes |
| 60 | Destroyed | Stock item has been destroyed | No |
| 65 | Rejected | Stock item did not pass the quality control standards | No |
| 70 | Lost | Stock item has been lost | No |
| 75 | Quarantined | Stock item has been intentionally isolated and is unavailable | No |
| 85 | Returned | Item has been returned from a customer | Treated as warning |

Colors assigned in the server source:

```python
class StockStatus(StatusCode):
    OK = 10, _('OK'), ColorEnum.success
    ATTENTION = 50, _('Attention needed'), ColorEnum.warning
    DAMAGED = 55, _('Damaged'), ColorEnum.warning
    DESTROYED = 60, _('Destroyed'), ColorEnum.danger
    REJECTED = 65, _('Rejected'), ColorEnum.danger
    LOST = 70, _('Lost'), ColorEnum.dark
    QUARANTINED = 75, _('Quarantined'), ColorEnum.info
    RETURNED = 85, _('Returned'), ColorEnum.warning
```

**Default status**: the default status code for any newly created Stock Item is `10` (OK).

**Custom status codes**: Stock Status supports [custom states](https://docs.inventree.org/en/stable/concepts/custom_states/). A `status_custom_key` integer field on the stock item provides per-item overrides.

**Bulk status change**: the `/api/stock/change_status/` endpoint accepts a list of stock item IDs and a target status code (see `stock-adjustments.md`).

## Stock Availability Semantics

InvenTree distinguishes four quantities when reasoning about whether stock is usable:

| Term | Definition |
| --- | --- |
| **Required** | Total number of a given part needed to fulfill all current orders, builds, or other commitments. Independent of the actual stock levels. |
| **In Stock** | Total number of items physically present in the stock location, regardless of allocation. |
| **Allocated** | Number of stock items reserved for specific orders, builds, or other commitments. Always less than or equal to In Stock. |
| **Available** | `In Stock - Allocated`. |

**Deficit** appears on part-level rollups and equals `Required - Available` when positive.

**Consumption**: once allocated stock is consumed (shipping a sales order, completing a build), the `In Stock` quantity of the stock item is reduced and `Available` falls accordingly.

**Delete on Deplete**: when a stock item has `delete_on_deplete = true` and its quantity reaches zero, the stock item record is automatically deleted from the system.

Part-level rollups (In Stock / Allocated / Available / Required / Deficit) are visible on the Part detail view. The Allocations tab on the Part view shows, per open order, which stock items are reserved where.

> **[IMAGE DESCRIPTION]**: Stock overview and stock detail panels on the Part detail page. The overview shows three summary tiles labelled "In Stock", "Available", and "Required" with numeric values (e.g. 138 / 123 / 657 for a "Red Widget" part). The detail section breaks "Required" down by open build orders and open sales orders, with a separate column showing how much of each requirement is already allocated. A "Deficit" row highlights the gap between required and available.

## Stock Ownership

Stock ownership is an optional feature (disabled by default; enable via settings). When enabled, groups or users can be assigned as "owner" of a stock location or stock item. Ownership overlays the admin-panel permission model — it hides edit/add/delete buttons for non-owners. A user must already have stock write permissions in admin; ownership then restricts *which* items that user can edit.

Owner types:
- **Group owner**: all users in the group can edit.
- **User owner**: only that specific user can edit.

Inheritance rules:
- Setting an owner on a location auto-sets the owner on all children locations and on all stock items at that location.
- Inherited ownership changes with stock transfer: moving a stock item from a location owned by A to a location owned by B changes the item owner to B.
- Directly-set ownership does not change when items are moved.
- If a child location/item is already owned by a subset of the parent owner (e.g. a user in the group), the owner is not overwritten.

## Stock Expiry

Stock expiry is an optional feature (disabled by default; enable via settings). When enabled:

- Stock items can have a manually-set `expiry_date`.
- A Part can define a `default_expiry` in days; new stock items for that Part get their expiry auto-computed.
- Leaving the expiry date blank or 0 marks the item as non-expiring.
- Stock tables can be filtered by `expired` status.
- Expired stock is listed on the InvenTree home page.
- By default, expired stock cannot be added to Sales Orders or Build Orders. The `Sell Expired Stock` and `Build Expired Stock` settings override this.

**Stale stock notifications**: the `STOCK_STALE_DAYS` setting sends daily email notifications for stock expiring within N days (or expired within the last N days). Requires `STOCK_ENABLE_EXPIRY`, a positive `STOCK_STALE_DAYS`, configured email, and users subscribed to the relevant parts.

## Key Flags — Quick Reference

Flags that drive stock-item behavior (API field names in backticks):

| Flag | Type | Description |
| --- | --- | --- |
| `delete_on_deplete` | boolean | Delete this Stock Item when stock is depleted (quantity = 0). |
| `is_building` | boolean | Item is currently being produced by a Build order (not yet usable). |
| `in_stock` | boolean (read-only) | Derived: not consumed, not shipped to customer, not installed in another item. |
| `expired` | boolean (read-only) | Derived from `expiry_date`. |
| `stale` | boolean (read-only) | Derived — close to expiry per `STOCK_STALE_DAYS`. |
| `serial` | string (nullable) | Unique-per-part serial number; populated for serialized stock items. |
| `batch` | string (nullable) | Batch code; not uniqueness-constrained. |
| `use_pack_size` | boolean (write-only) | When creating: `quantity` is in packs, not individual units. |
| `serial_numbers` | string (write-only) | Range/list expression used on creation to bulk-serialize new items. |

See `stock-items.md` for the full StockItem field list.

## Base API Path Inventory

| Path | Methods | Purpose |
| --- | --- | --- |
| `/api/stock/` | GET, POST, PUT, PATCH, DELETE | List / create / bulk update / bulk delete stock items |
| `/api/stock/{id}/` | GET, PUT, PATCH, DELETE | Detail ops on a single stock item |
| `/api/stock/{id}/convert/` | POST | Convert a stock item to a variant part |
| `/api/stock/{id}/install/` | POST | Install another stock item into this one |
| `/api/stock/{id}/uninstall/` | POST | Remove (uninstall) items from this one |
| `/api/stock/{id}/serialize/` | POST | Split a non-serialized stock item into serialized children |
| `/api/stock/{id}/serial-numbers/` | GET | Get neighboring serial numbers for this item |
| `/api/stock/status/` | GET | Return the `StockItemStatusEnum` metadata |
| `/api/stock/track/` | GET | List stock-tracking audit entries |
| `/api/stock/track/{id}/` | GET | Detail of a single tracking entry |
| `/api/stock/track/status/` | GET | Return the stock-tracking action/status enum |
| `/api/stock/test/` | GET, POST, DELETE | List / create / bulk delete stock-item test results |
| `/api/stock/test/{id}/` | GET, PUT, PATCH, DELETE | Detail ops on a test result |
| `/api/stock/location/` | GET, POST, PUT, PATCH | List / create / bulk update stock locations |
| `/api/stock/location/{id}/` | GET, PUT, PATCH, DELETE | Detail ops on a stock location |
| `/api/stock/location/tree/` | GET | Stock location tree (lightweight schema) |
| `/api/stock/location-type/` | GET, POST | List / create stock location types |
| `/api/stock/location-type/{id}/` | GET, PUT, PATCH, DELETE | Detail ops on a stock location type |
| `/api/stock/add/` | POST | Increase quantity on one or more stock items |
| `/api/stock/remove/` | POST | Decrease quantity on one or more stock items |
| `/api/stock/count/` | POST | Stocktake — record the counted quantity |
| `/api/stock/transfer/` | POST | Move one or more stock items to a new location |
| `/api/stock/change_status/` | POST | Change the status code of multiple stock items |
| `/api/stock/assign/` | POST | Assign stock items to a customer |
| `/api/stock/merge/` | POST | Merge multiple stock items into one |
| `/api/stock/return/` | POST | Return items into stock (from a customer) |

Total: 26 distinct paths.

## Related Documents

- [stock-items.md](stock-items.md) — StockItem attributes, lifecycle, tracking history, test results.
- [stock-locations.md](stock-locations.md) — StockLocation hierarchy, StockLocationType, external/structural flags.
- [stock-adjustments.md](stock-adjustments.md) — The seven stock-mutating actions (add, remove, count, transfer, change_status, assign, merge) plus return.
- [../parts/part-overview.md](../parts/part-overview.md) — Part domain.
- [../api/inventree-modules.md](../inventree-modules.md) — Top-level module coverage map.
