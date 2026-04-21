---
source: https://docs.inventree.org/en/stable/stock/stock/
component: stock
topic: Stock Locations — Hierarchy, Types, External and Structural Flags
fetched: 2026-04-16
---

> **Source**: [https://docs.inventree.org/en/stable/stock/stock/](https://docs.inventree.org/en/stable/stock/stock/) (sections: Stock Locations, Stock Location Type, External Stock Location, Structural Locations)

# Stock Locations

## Table of Contents

- [Overview](#overview)
- [Stock Location Hierarchy](#stock-location-hierarchy)
- [Pathstring](#pathstring)
- [Stock Location Type](#stock-location-type)
- [External Stock Location](#external-stock-location)
- [Structural Locations](#structural-locations)
- [Icons](#icons)
- [Ownership](#ownership)
- [Location Schema (API)](#location-schema-api)
- [StockLocationType Schema (API)](#stocklocationtype-schema-api)
- [LocationTree Schema (API)](#locationtree-schema-api)
- [Endpoints](#endpoints)

---

## Overview

A stock location represents a physical real-world location where _Stock Items_ are stored. Locations are arranged in a cascading (tree) structure, and each location may contain multiple sub-locations, stock items, or both.

> **[IMAGE DESCRIPTION]**: External-stock indicator icon on a build order line item — a small outline icon next to an allocated stock quantity that flags the allocated items as coming from an external stock location.

## Stock Location Hierarchy

Each location has:

- An optional `parent` (self-FK; the root has `parent = null`).
- A computed `level` (depth from root).
- A count of `items` (direct stock items at this location).
- A count of `sublocations` (direct child locations).
- An optional `owner`.
- Optional `structural` and `external` flags.

Locations can be nested arbitrarily deep. Stock items can be placed at any non-structural location in the tree.

The lightweight `/api/stock/location/tree/` endpoint returns the full tree as `LocationTree` objects (pk, name, parent, icon, structural, sublocations) — intended for rendering as a tree view.

## Pathstring

Each location exposes a `pathstring` (read-only) representing the full path from the root to that location, separated by slashes. For example: `Warehouse A/Shelf 3/Bin 12`. Used for display in breadcrumbs and search results.

## Stock Location Type

A stock location type represents a specific type of location (e.g. a specific size of drawer, shelf, or box) that can be assigned to multiple stock locations. Primary uses:

- Specify an icon and keep the icon in sync for all locations that share the type.
- Serve as a data tag to quickly see what type of location something is.
- Future: carry drawer dimension information to enable a "find a matching, empty stock location" tool.

## External Stock Location

Stock locations can be flagged as _external_. External locations indicate that items there might not be available for immediate usage. Stock items in an external location are marked with an additional icon in the build order line items view where material is allocated.

> **Inheritance**: the `external` flag does **not** cascade to sub-locations. Each location's `external` flag is set independently.

## Structural Locations

A stock location may optionally be marked as _structural_. Structural locations are used to represent physical locations that are not directly associated with stock items, but serve as an organizational container in the hierarchy. For example, a structural location might represent a particular shelf within a warehouse, while the actual stock items are stored in sub-locations within that shelf.

> **Constraint**: Stock items may not be directly located into a structural location; they must be located in a non-structural child of that location.

## Icons

Stock locations can be assigned custom icons — either directly or through Stock Location Types. In the web UI there is a custom icon picker component. In CUI, the icon must be entered manually.

Format: `<package-prefix>:<icon-name>:<variant>` (e.g. `ti:bookmark:outline` or `ti:bookmark:filled`). Default package: Tabler icons (`ti`). If no variant is specified, the outline variant is used.

Custom icon packs can be added via a plugin implementing `IconPackMixin`.

## Ownership

A location can be assigned an `owner` (user or group). Setting the owner on a location automatically propagates to:

- All children locations.
- All stock items at this location.

Inherited ownership changes with stock transfer: moving a stock item from a location owned by A to a location owned by B changes the item owner to B. Directly-set ownership on an item is preserved when the item is moved.

See the Ownership section in [overview.md](overview.md#stock-ownership) for the full permission-model interaction.

## Location Schema (API)

From the live OpenAPI schema (`Location` component, `/api/stock/location/` endpoint):

| Field | Type | Flags | Description |
| --- | --- | --- | --- |
| `pk` | integer | REQ, RO | Primary key |
| `barcode_hash` | string | REQ, RO | Unique hash of barcode data |
| `name` | string (max 100) | REQ | Name |
| `level` | integer | REQ, RO | Depth in the tree |
| `description` | string (max 250) |  | Description (optional) |
| `parent` | integer | null | Parent stock location |
| `pathstring` | string | REQ, RO | Full path from root (slash-separated) |
| `items` | integer | REQ, RO | Count of stock items directly at this location |
| `sublocations` | integer | REQ, RO | Count of direct child locations |
| `owner` | integer | null | Owner (user or group) |
| `icon` | string | REQ, RO | Resolved icon identifier |
| `custom_icon` | string (max 100) | null | Override icon (optional) |
| `structural` | boolean |  | Stock items may not be directly located in structural locations |
| `external` | boolean |  | This is an external stock location |
| `location_type` | integer | null | Stock location type FK |
| `location_type_detail` | `StockLocationType` | RO, null | Expanded location type info |

**Related schemas**:

- `LocationBrief` — minimal inline representation (`pk`, `name`, `pathstring`).
- `LocationTree` — lightweight tree node (`pk`, `name`, `parent`, `icon`, `structural`, `sublocations`).
- `DefaultLocation` — used elsewhere to reference a default location FK.

## StockLocationType Schema (API)

From the live OpenAPI schema (`StockLocationType` component, `/api/stock/location-type/` endpoint):

| Field | Type | Flags | Description |
| --- | --- | --- | --- |
| `pk` | integer | REQ, RO | Primary key |
| `name` | string (max 100) | REQ | Name |
| `description` | string (max 250) |  | Description (optional) |
| `icon` | string (max 100) |  | Default icon for all locations that have no icon set |
| `location_count` | integer | RO, null | Number of locations that reference this type |

## LocationTree Schema (API)

Returned by `GET /api/stock/location/tree/`:

| Field | Type | Flags | Description |
| --- | --- | --- | --- |
| `pk` | integer | REQ, RO | Primary key |
| `name` | string (max 100) | REQ | Location name |
| `parent` | integer | null | Parent location FK |
| `icon` | string | REQ, RO | Resolved icon identifier |
| `structural` | boolean |  | See structural-locations section |
| `sublocations` | integer | REQ, RO | Count of direct children |

## Endpoints

### Stock Locations

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/api/stock/location/` | List locations (paginated). Filters: `parent`, `cascade` (include sub-locations), `top_level`, `structural`, `external`, `location_type`, `has_location_type`, `depth`, `name`, `search` (on `description`, `name`, `pathstring`, `tags__name`), `path_detail`, `ordering`, `limit`, `offset` (14 params). |
| POST | `/api/stock/location/` | Create a location. |
| PUT | `/api/stock/location/` | Bulk update. |
| PATCH | `/api/stock/location/` | Bulk partial update. |
| GET | `/api/stock/location/{id}/` | Retrieve a single location. |
| PUT | `/api/stock/location/{id}/` | Update. |
| PATCH | `/api/stock/location/{id}/` | Partial update. |
| DELETE | `/api/stock/location/{id}/` | Delete. |
| GET | `/api/stock/location/tree/` | Return all locations as lightweight tree nodes (`LocationTree`). Intended for rendering a tree view. |

### Stock Location Types

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/api/stock/location-type/` | List stock location types. |
| POST | `/api/stock/location-type/` | Create a stock location type. |
| GET | `/api/stock/location-type/{id}/` | Retrieve. |
| PUT | `/api/stock/location-type/{id}/` | Update. |
| PATCH | `/api/stock/location-type/{id}/` | Partial update. |
| DELETE | `/api/stock/location-type/{id}/` | Delete. |

### Representative Query Filters — `GET /api/stock/location/`

| Filter | Type | Description |
| --- | --- | --- |
| `cascade` | boolean | Include sub-locations in filtered results |
| `depth` | number | Filter by tree depth |
| `external` | boolean | Only external locations |
| `has_location_type` | boolean | Only locations with a `location_type` assigned |
| `location_type` | integer | Filter by specific location type |
| `parent` | integer | Filter by parent location |
| `top_level` | boolean | Only root-level locations (no parent) |
| `structural` | boolean | Only structural locations |
| `name` | string | Exact name match |
| `path_detail` | boolean | Include detailed pathstring info |
| `search` | string | Search across `description`, `name`, `pathstring`, `tags__name` |
| `ordering` | string | Sort key |
| `limit` / `offset` | integer | Pagination |
