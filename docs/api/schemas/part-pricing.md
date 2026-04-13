---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PartPricing
fetched: 2026-04-13
---

# PartPricing

Serializer for Part pricing information.

**Type:** object

## Currency Enum Values

The `override_min_currency` and `override_max_currency` fields accept the following values:

| Value | Description |
|-------|-------------|
| `AUD` | Australian Dollar |
| `CAD` | Canadian Dollar |
| `CNY` | Chinese Yuan |
| `EUR` | Euro |
| `GBP` | British Pound |
| `JPY` | Japanese Yen |
| `NZD` | New Zealand Dollar |
| `USD` | US Dollar |

> **Note:** Other valid currencies may be found in the `CURRENCY_CODES` global setting.

## Properties

| Field | Type | Flags | Description |
|-------|------|-------|-------------|
| `currency` | string | read-only, nullable | |
| `updated` | string (date-time) | read-only, nullable | |
| `scheduled_for_update` | boolean | required, read-only | |
| `bom_cost_min` | string (decimal) | read-only, nullable | |
| `bom_cost_max` | string (decimal) | read-only, nullable | |
| `purchase_cost_min` | string (decimal) | read-only, nullable | |
| `purchase_cost_max` | string (decimal) | read-only, nullable | |
| `internal_cost_min` | string (decimal) | read-only, nullable | |
| `internal_cost_max` | string (decimal) | read-only, nullable | |
| `supplier_price_min` | string (decimal) | read-only, nullable | |
| `supplier_price_max` | string (decimal) | read-only, nullable | |
| `variant_cost_min` | string (decimal) | read-only, nullable | |
| `variant_cost_max` | string (decimal) | read-only, nullable | |
| `override_min` | string (decimal) | nullable | Override calculated value for minimum price |
| `override_min_currency` | string | | Select currency from available options (see enum above) |
| `override_max` | string (decimal) | nullable | Override calculated value for maximum price |
| `override_max_currency` | string | | Select currency from available options (see enum above) |
| `overall_min` | string (decimal) | read-only, nullable | |
| `overall_max` | string (decimal) | read-only, nullable | |
| `sale_price_min` | string (decimal) | read-only, nullable | |
| `sale_price_max` | string (decimal) | read-only, nullable | |
| `sale_history_min` | string (decimal) | read-only, nullable | |
| `sale_history_max` | string (decimal) | read-only, nullable | |
| `update` | boolean | write-only, nullable | Update pricing for this part; default: `False` |
