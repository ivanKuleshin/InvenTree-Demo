---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/views.md
component: parts
topic: Part Detail - Stock Tab
fetched: 2026-04-14
---

# Part Detail — Stock Tab

## Table of Contents

- [Overview](#overview)
- [Stock Item Concept](#stock-item-concept)
- [Stock Tab Functions](#stock-tab-functions)
- [Stock Item Attributes](#stock-item-attributes)
- [Stock Item Status Codes](#stock-item-status-codes)
- [Stock History Tab](#stock-history-tab)
- [Location Management](#location-management)
- [Traceability](#traceability)

## Overview

The *Stock* tab on the Part detail view page shows all the stock items for the selected Part. The user can quickly determine how many parts are in stock, where they are located, and the status of each Stock Item.

> **[IMAGE DESCRIPTION]**: A table view labeled "Part Stock" showing stock items for a part. Columns include location, quantity, batch code, serial number, status, and last updated date. Each row represents one stock item at a specific physical location. Action buttons for stock operations are visible in a toolbar above the table.

## Stock Item Concept

A *Stock Item* is an actual instance of a Part item. It represents a physical quantity of the Part in a specific location.

Multiple stock items can exist for a single part, each with different quantities and locations.

## Stock Tab Functions

The following functions are available from the Part Stock view:

### Export

Exports the stocktake data for the selected Part. Launches a dialog to select export options, then downloads a file containing data for all stock items for this Part.

### New Stock Item

Launches a dialog to create a new Stock Item for the selected Part.

### Stock Actions

If stock items are selected in the table, stock actions are enabled via the drop-down menu. Available stock actions include adjustments, transfers, and status updates.

## Stock Item Attributes

Stock Items track the following details:

| Attribute | Description |
| --- | --- |
| Part | Which Part this stock item represents |
| Location | Physical storage location |
| Quantity | Number of items in stock |
| Supplier | Source of purchase (if applicable) |
| Serial Number | Serial number (if the part is trackable) |
| Batch Code | Batch/lot code (if applicable) |
| Status | Current condition/availability status |
| Last Updated | Timestamp of most recent adjustment |
| Last Stocktake | Timestamp of most recent stocktake |

## Stock Item Status Codes

Stock items are assigned a status code indicating their current condition. Status codes fall into two groups:

### Available Statuses

| Status | Description |
| --- | --- |
| OK | Stock item is healthy; nothing wrong to report |
| Attention Needed | Item has not been checked or tested yet |
| Damaged | Item is not functional in its present state |

### Unavailable Statuses

| Status | Description |
| --- | --- |
| Destroyed | Item has been destroyed |
| Lost | Item has been lost |
| Rejected | Item did not pass quality control standards |
| Quarantined | Item has been intentionally isolated and is unavailable |

The default status for new stock items is **OK**. Users can update status through the Edit Stock Item dialog.

The system also supports custom status codes in addition to the standard options.

## Stock History Tab

The *Stock History* tab on the Part detail page provides historical stock level information.

Every time a Stock Item is adjusted, a Stock Tracking entry is automatically created. This maintains a complete audit trail with user attribution.

> **[IMAGE DESCRIPTION]**: A "Part stock tracking history" view showing a timeline of stock level changes. Columns include date, user, action type (e.g., "Stock Received", "Stock Consumed"), quantity change, and notes. All adjustments to stock items linked to this part are listed chronologically.

> **Note:** Even if a stock item is deleted from the system, the associated stock tracking entries are retained for historical reference. They will be visible in the part tracking history, but not in the stock item tracking history (as the stock item itself has been deleted).

### Stock Tracking Events

Stock tracking entries are created automatically whenever a stock item is adjusted, through manual adjustments or automated processes. Examples include:

- Manual stock adjustments (e.g., correcting inventory counts)
- Creation of new stock items (e.g., receiving new inventory)
- Allocation of stock items to orders (e.g., shipping items against sales orders)
- Consumption of stock items during build processes (e.g., using items to complete a build order)

## Location Management

Stock locations are hierarchical and support sub-locations. Locations can be:

- **External locations**: Items stored in external locations are flagged as potentially unavailable for immediate use, with visual indicators in build order views.
- **Structural locations**: Organizational containers that do not hold stock directly; used to structure the location hierarchy.

Locations can be customized with icons (defaulting to the Tabler icons package).

## Traceability

Items can be tracked via serial numbers and batch codes, enabling users to trace individual products back to their source. This is particularly useful for:

- Tracking the history of specific items
- Supplier verification
- Quality control and recall scenarios

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/views.md
> https://github.com/inventree/InvenTree/blob/master/docs/docs/stock/index.md
> https://github.com/inventree/InvenTree/blob/master/docs/docs/stock/status.md
> https://github.com/inventree/InvenTree/blob/master/docs/docs/stock/tracking.md
