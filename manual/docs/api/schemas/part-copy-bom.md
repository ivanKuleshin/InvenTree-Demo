---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PartCopyBOM
fetched: 2026-04-13
---

# PartCopyBOM

Serializer for copying a BOM from another part.

**Type:** object

## Properties

| Field               | Type    | Flags    | Description                                                                  |
| ------------------- | ------- | -------- | ---------------------------------------------------------------------------- |
| `part`              | integer | required | Select part to copy BOM from                                                 |
| `remove_existing`   | boolean |          | Remove existing BOM items before copying; default: `True`                    |
| `include_inherited` | boolean |          | Include BOM items which are inherited from templated parts; default: `False` |
| `skip_invalid`      | boolean |          | Enable this option to skip invalid rows; default: `False`                    |
| `copy_substitutes`  | boolean |          | Copy substitute parts when duplicate BOM items; default: `True`              |
