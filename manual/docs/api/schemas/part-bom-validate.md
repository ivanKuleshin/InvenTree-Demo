---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PartBomValidate
fetched: 2026-04-13
---

# PartBomValidate

Serializer for Part BOM information.

**Type:** object

## Properties

| Field                   | Type          | Flags               | Description                                         |
| ----------------------- | ------------- | ------------------- | --------------------------------------------------- |
| `pk`                    | integer       | required, read-only |                                                     |
| `bom_validated`         | boolean       | required, read-only | Is the BOM for this part valid?                     |
| `bom_checksum`          | string        | required, read-only | Stored BOM checksum                                 |
| `bom_checked_by`        | integer       | read-only, nullable |                                                     |
| `bom_checked_by_detail` | $ref: User    | read-only, nullable |                                                     |
| `bom_checked_date`      | string (date) | read-only, nullable |                                                     |
| `valid`                 | boolean       | write-only          | Validate entire Bill of Materials; default: `False` |
