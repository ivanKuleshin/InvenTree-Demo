---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/manufacturing/build.md
component: parts
topic: Part Detail - Build Orders Tab
fetched: 2026-04-14
---

# Part Detail — Build Orders Tab

## Table of Contents

- [Overview](#overview)
- [Build Order Concept](#build-order-concept)
- [Build Order Status](#build-order-status)
- [Build Order Parameters](#build-order-parameters)
- [Build Outputs](#build-outputs)
- [Stock Management in Builds](#stock-management-in-builds)
- [Build Lifecycle](#build-lifecycle)
- [Build Order Views](#build-order-views)
- [Configuration Settings](#configuration-settings)

## Overview

The *Build Orders* tab on the Part detail page shows a list of the builds for this part. It provides a view of important build information including quantity, status, creation and completion dates.

Build Orders are used to manufacture assemblies by combining component parts according to a Bill of Materials (BOM). When a build order is created, stock items can be allocated to it, signaling intent to reserve those items for the build process.

## Build Order Concept

A Build Order is used to create new stock by assembling component parts. When a Build Order is created:
- Stock items can be allocated to the build (signaling intent to use them)
- Allocation reserves items without removing them from inventory
- Stock is subtracted only when the build completes

## Build Order Status

Build Orders are tracked through five states:

| Status | Description |
| --- | --- |
| Pending | Build order created but not yet started |
| Production | Build is actively in progress |
| On Hold | Build has been paused |
| Cancelled | Build has been cancelled |
| Completed | Build has been finished and outputs created |

## Build Order Parameters

| Parameter | Description |
| --- | --- |
| Reference | Unique reference number for the build order |
| Description | Optional text description of the build |
| Quantity | Target quantity of assemblies to build |
| Sales Order | Associated sales order (if applicable) |
| Source Location | Where stock will be sourced from |
| Destination Location | Where completed assemblies will be stored |
| Start Date | Scheduled start date |
| Target Date | Target completion date |
| Responsible Party | User or group responsible for the build |
| Notes | Optional notes |

## Build Outputs

Each Build Order requires at least one build output (a created stock instance). Multiple outputs can be specified for batch completion.

- For trackable parts, test results can be recorded against individual outputs
- Each output creates a new stock item when the build is completed
- Outputs must be created before the build can be completed

## Stock Management in Builds

### Allocation vs Consumption

- **Allocated**: Items are reserved against the build but remain in inventory
- **Consumed**: Items are physically removed from inventory (happens at build completion or when manually consumed)
- Consumed stock can be returned to inventory if needed

### Multiple Allocations

Multiple allocations can be made per BOM line item if quantities are insufficient from a single stock item.

### Child Builds

Child build orders can be auto-generated for sub-assemblies required by the parent build.

## Build Lifecycle

1. **Create** — Build Order created from Part Detail Page or the Build Order overview
2. **Allocate** — Stock items allocated to BOM line items
3. **Produce** — Outputs created (build outputs / completed assemblies)
4. **Complete** — Build validated and completed; allocated stock consumed

> **Warning:** Marking a build as *complete* will remove allocated items from stock. This operation cannot be reversed.

Once completed or cancelled, builds cannot be reopened.

### Completion Requirements

Completion requires validation of:
- Outputs
- Allocations
- Stock levels

Override options are available if strict validation cannot be met.

## Build Order Views

Build Orders can be accessed through Manufacturing > Build Orders, with two display modes:

- **Table View** — Filterable list of build orders with column sorting
- **Calendar View** — Month-by-month display based on order dates

## Configuration Settings

The following global settings control Build Order behavior:

| Setting | Description |
| --- | --- |
| Reference Pattern | Controls the format of automatically generated build reference numbers |
| Responsibility Required | Whether a responsible party must be assigned |
| BOM Validation | Whether BOM validation is required before completing a build |

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/manufacturing/build.md
