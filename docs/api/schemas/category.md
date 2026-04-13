---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: Category
fetched: 2026-04-13
---

# Category

Serializer for PartCategory.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `pk` | integer | required, read-only | |
| `name` | string | required | Name; max length: 100 |
| `description` | string | | Description (optional); max length: 250 |
| `default_location` | integer | nullable | Default location for parts in this category |
| `default_keywords` | string | nullable | Default keywords for parts in this category; max length: 250 |
| `level` | integer | required, read-only | |
| `parent` | integer | nullable | Parent part category |
| `part_count` | integer | read-only, nullable | |
| `subcategories` | integer | read-only, nullable | |
| `pathstring` | string | required, read-only | Path |
| `starred` | boolean | required, read-only | Return True if the category is directly "starred" by the current user. |
| `structural` | boolean | | Parts may not be directly assigned to a structural category, but may be assigned to child categories. |
| `icon` | string | nullable | Icon (optional); max length: 100 |
| `parent_default_location` | integer | read-only, nullable | |
