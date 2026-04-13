---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PartRequirements
fetched: 2026-04-13
---

# PartRequirements

Serializer for Part requirements.

**Type:** object

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `total_stock` | number (double) | required, read-only | |
| `unallocated_stock` | number (double) | required, read-only | |
| `can_build` | number (double) | required, read-only | |
| `ordering` | number (double) | required, read-only | |
| `building` | number (double) | required, read-only | |
| `scheduled_to_build` | integer | required, read-only | |
| `required_for_build_orders` | number (double) | required, read-only | |
| `allocated_to_build_orders` | number (double) | required, read-only | |
| `required_for_sales_orders` | number (double) | required, read-only | |
| `allocated_to_sales_orders` | number (double) | required, read-only | Return the allocated sales order quantity. |
