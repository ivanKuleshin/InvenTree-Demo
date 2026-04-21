---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PartStocktake
fetched: 2026-04-13
---

# PartStocktake

Serializer for the PartStocktake model.

**Type:** object

## Properties

| Field               | Type             | Flags               | Description                                                                                            |
| ------------------- | ---------------- | ------------------- | ------------------------------------------------------------------------------------------------------ |
| `pk`                | integer          | required, read-only |                                                                                                        |
| `part`              | integer          | required            | Part for stocktake                                                                                     |
| `part_name`         | string           | required, read-only |                                                                                                        |
| `part_ipn`          | string           | read-only, nullable |                                                                                                        |
| `part_description`  | string           | read-only, nullable |                                                                                                        |
| `date`              | string (date)    | required, read-only | Date stocktake was performed                                                                           |
| `item_count`        | integer          |                     | Number of individual stock entries at time of stocktake; minimum: `-2147483648`; maximum: `2147483647` |
| `quantity`          | number (double)  | required            |                                                                                                        |
| `cost_min`          | string (decimal) | nullable            |                                                                                                        |
| `cost_min_currency` | string           |                     | Select currency from available options                                                                 |
| `cost_max`          | string (decimal) | nullable            |                                                                                                        |
| `cost_max_currency` | string           |                     | Select currency from available options                                                                 |
