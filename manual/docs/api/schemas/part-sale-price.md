---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PartSalePrice
fetched: 2026-04-13
---

# PartSalePrice

Serializer for sale prices for Part model.

**Type:** object

## Properties

| Field            | Type             | Flags               | Description                          |
| ---------------- | ---------------- | ------------------- | ------------------------------------ |
| `pk`             | integer          | required, read-only |                                      |
| `part`           | integer          | required            |                                      |
| `quantity`       | number (double)  | required            |                                      |
| `price`          | string (decimal) | nullable            |                                      |
| `price_currency` | string           |                     | Purchase currency of this stock item |
