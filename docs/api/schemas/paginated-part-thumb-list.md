---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PaginatedPartThumbList
fetched: 2026-04-13
---

# PaginatedPartThumbList

Paginated list response wrapping an array of PartThumb objects.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `count` | integer | required | Total number of results |
| `next` | string (uri) | nullable | URL of the next page of results |
| `previous` | string (uri) | nullable | URL of the previous page of results |
| `results` | array of $ref: PartThumb | required | |
