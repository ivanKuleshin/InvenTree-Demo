---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: CategoryParameterTemplate
fetched: 2026-04-13
---

# CategoryParameterTemplate

Serializer for the PartCategoryParameterTemplate model.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `pk` | integer | required, read-only | |
| `category` | integer | required | Part Category |
| `category_detail` | $ref: [Category](category.md) | read-only, nullable | |
| `template` | integer | required | |
| `template_detail` | $ref: ParameterTemplate | required, read-only | |
| `default_value` | string | | Default Parameter Value; max length: 500 |
