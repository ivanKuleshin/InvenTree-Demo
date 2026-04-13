---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PatchedPartSalePrice
fetched: 2026-04-13
---

# PatchedPartSalePrice

Serializer for sale prices for Part model. Used in PATCH requests where all fields are optional.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `pk` | integer | read-only | |
| `part` | integer | | |
| `quantity` | number (double) | | |
| `price` | string (decimal) | nullable | |
| `price_currency` | string | | Purchase currency of this stock item |
