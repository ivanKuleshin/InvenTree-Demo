---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PatchedPartStocktake
fetched: 2026-04-13
---

# PatchedPartStocktake

Serializer for the PartStocktake model. Used in PATCH requests where all fields are optional.

**Type:** object

## Properties

| Field               | Type             | Flags               | Description                                                                                            |
| ------------------- | ---------------- | ------------------- | ------------------------------------------------------------------------------------------------------ |
| `pk`                | integer          | read-only           |                                                                                                        |
| `part`              | integer          |                     | Part for stocktake                                                                                     |
| `part_name`         | string           | read-only           |                                                                                                        |
| `part_ipn`          | string           | read-only, nullable |                                                                                                        |
| `part_description`  | string           | read-only, nullable |                                                                                                        |
| `date`              | string (date)    | read-only           | Date stocktake was performed                                                                           |
| `item_count`        | integer          |                     | Number of individual stock entries at time of stocktake; minimum: `-2147483648`; maximum: `2147483647` |
| `quantity`          | number (double)  |                     |                                                                                                        |
| `cost_min`          | string (decimal) | nullable            |                                                                                                        |
| `cost_min_currency` | string           |                     | Select currency from available options                                                                 |
| `cost_max`          | string (decimal) | nullable            |                                                                                                        |
| `cost_max_currency` | string           |                     | Select currency from available options                                                                 |
| `part_detail`       | $ref: PartBrief  | read-only, nullable |                                                                                                        |
