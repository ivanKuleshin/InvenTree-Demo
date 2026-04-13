---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PatchedCategory
fetched: 2026-04-13
---

# PatchedCategory

Serializer for PartCategory. Used in PATCH requests where all fields are optional.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `pk` | integer | read-only | |
| `name` | string | | Name; max length: 100 |
| `description` | string | | Description (optional); max length: 250 |
| `default_location` | integer | nullable | Default location for parts in this category |
| `default_keywords` | string | nullable | Default keywords for parts in this category; max length: 250 |
| `level` | integer | read-only | |
| `parent` | integer | nullable | Parent part category |
| `part_count` | integer | read-only, nullable | |
| `subcategories` | integer | read-only, nullable | |
| `pathstring` | string | read-only | Path |
| `starred` | boolean | read-only | Return True if the category is directly "starred" by the current user. |
| `structural` | boolean | | Parts may not be directly assigned to a structural category, but may be assigned to child categories. |
| `icon` | string | nullable | Icon (optional); max length: 100 |
| `parent_default_location` | integer | read-only, nullable | |
| `path` | array of $ref: TreePath | read-only, nullable | |
