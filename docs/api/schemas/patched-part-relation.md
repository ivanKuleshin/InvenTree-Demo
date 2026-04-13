---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PatchedPartRelation
fetched: 2026-04-13
---

# PatchedPartRelation

Serializer for a PartRelated model. Used in PATCH requests where all fields are optional.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `pk` | integer | read-only | |
| `part_1` | integer | | |
| `part_1_detail` | $ref: [Part](part.md) | read-only | |
| `part_2` | integer | | Select Related Part |
| `part_2_detail` | $ref: [Part](part.md) | read-only | |
| `note` | string | | Note for this relationship; max length: 500 |
