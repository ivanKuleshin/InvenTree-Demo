---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PartStocktakeGenerate
fetched: 2026-04-13
---

# PartStocktakeGenerate

Serializer for generating PartStocktake entries.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `part` | integer | nullable | Select a part to generate stocktake information for that part (and any variant parts) |
| `category` | integer | nullable | Select a category to include all parts within that category (and subcategories) |
| `location` | integer | nullable | Select a location to include all parts with stock in that location (including sub-locations) |
| `generate_entry` | boolean | write-only | Save stocktake entries for the selected parts; default: `False` |
| `generate_report` | boolean | write-only | Generate a stocktake report for the selected parts; default: `False` |
| `output` | $ref: DataOutput | required, read-only | |
