---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PartRelation
fetched: 2026-04-13
---

# PartRelation

Serializer for a PartRelated model.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `pk` | integer | required, read-only | |
| `part_1` | integer | required | |
| `part_1_detail` | $ref: [Part](part.md) | required, read-only | |
| `part_2` | integer | required | Select Related Part |
| `part_2_detail` | $ref: [Part](part.md) | required, read-only | |
| `note` | string | | Note for this relationship; max length: 500 |
