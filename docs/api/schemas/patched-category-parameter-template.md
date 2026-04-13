---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PatchedCategoryParameterTemplate
fetched: 2026-04-13
---

# PatchedCategoryParameterTemplate

Serializer for the PartCategoryParameterTemplate model. Used in PATCH requests where all fields are optional.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `pk` | integer | read-only | |
| `category` | integer | | Part Category |
| `category_detail` | $ref: [Category](category.md) | read-only, nullable | |
| `template` | integer | | |
| `template_detail` | $ref: ParameterTemplate | read-only | |
| `default_value` | string | | Default Parameter Value; max length: 500 |
