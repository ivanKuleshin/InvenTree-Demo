---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/views.md
component: parts
topic: Part Detail - Allocated Tab
fetched: 2026-04-14
---

# Part Detail — Allocated Tab

## Table of Contents

- [Overview](#overview)
- [Visibility Condition](#visibility-condition)
- [Allocation Concept](#allocation-concept)
- [Untracked vs Tracked Stock Allocation](#untracked-vs-tracked-stock-allocation)
- [Allocation Methods](#allocation-methods)
- [Stock Consumption Lifecycle](#stock-consumption-lifecycle)

## Overview

The *Allocated* tab displays how many units of a part have been allocated to pending build orders and/or sales orders.

## Visibility Condition

This tab is only visible if the Part is:
- A **component** (meaning it can be used to make assemblies), OR
- **Salable** (meaning it can be sold to customers)

## Allocation Concept

Stock allocations link specific stock items to build orders or sales orders without immediately removing them from inventory. They signal intent to use those items for a build or sale.

Key behaviors:
- Multiple allocations can be made per BOM line item if quantities are insufficient from a single stock item
- Stock is only subtracted from inventory upon build or order completion
- Allocated items are reserved but remain in inventory until consumed

## Untracked vs Tracked Stock Allocation

### Untracked Items

Untracked items are consumed as a batch when the build completes. For example: "You require 15 x 47K resistors to make a batch of PCBs" from a reel of 1,000—upon build completion, available stock drops to 985.

Untracked items are allocated against the build order itself.

### Tracked Items

Tracked items must be allocated to specific build outputs and are marked as "installed" within the assembled product. These require serial numbers and careful tracking throughout the assembly process.

Tracked items are consumed per-output rather than per-build-order.

## Allocation Methods

### Automatic Allocation

The "Auto Allocate" button attempts to allocate stock automatically. Automatic allocation works when:
- A single stock item exists for a BOM line, OR
- Multiple interchangeable items are available (if the option to allow this is enabled)

Options include:
- Specifying a source location to restrict which stock is used
- Allowing substitute parts to be considered

### Manual Allocation

Manual allocation allows users to select specific stock items row-by-row when:
- Automatic allocation is not possible (e.g., multiple stock items exist)
- The user wants control over which specific items are used

### Row-level Allocation

Granular control is available for individual BOM line items, allowing precise selection of stock per component.

## Stock Consumption Lifecycle

Stock allocation has three stages:

1. **Allocated** — Items are marked as allocated against the build or order; they remain in inventory
2. **Consumed** — Items are removed from inventory, either manually by the user or automatically when the build/order completes
3. **Returned** (optional) — Consumed items can be returned to stock if needed

> **Warning:** Marking a build as *complete* will remove allocated items from stock. This operation cannot be reversed, so take care!

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/views.md
> https://github.com/inventree/InvenTree/blob/master/docs/docs/manufacturing/allocate.md
