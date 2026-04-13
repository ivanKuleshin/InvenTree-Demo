---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PaginatedCategoryList
fetched: 2026-04-13
---

# PaginatedCategoryList

Paginated list response wrapping an array of Category objects.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `count` | integer | required | Total number of results |
| `next` | string (uri) | nullable | URL of the next page of results |
| `previous` | string (uri) | nullable | URL of the previous page of results |
| `results` | array of $ref: [Category](category.md) | required | |
