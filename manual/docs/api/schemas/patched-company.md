---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: PatchedCompany
fetched: 2026-04-17
---

# PatchedCompany

Serializer for partial updates to a Company object.

Used when performing PATCH requests to update one or more fields of an existing company without affecting other fields.

**Type:** object

## Properties

| Field             | Type              | Flags     | Description                                                                          |
|-------------------|-------------------|-----------|--------------------------------------------------------------------------------------|
| `name`            | string            | optional  | Company name; max length: 100                                                       |
| `description`     | string            | nullable  | Company description (optional); max length: 500                                     |
| `website`         | string (uri)      | nullable  | Company website URL; max length: 2000                                               |
| `address`         | string            | nullable  | Company address; max length: 250                                                    |
| `phone`           | string            | nullable  | Company phone number; max length: 30                                                |
| `email`           | string (email)    | nullable  | Company email address; max length: 254                                              |
| `contact`         | string            | nullable  | Primary contact person name; max length: 100                                        |
| `link`            | string (uri)      | nullable  | Link to external URL; max length: 2000                                              |
| `image`           | string (uri)      | nullable  | Company logo/image URL                                                              |
| `currency`        | string            | nullable  | Currency code for pricing (e.g., USD, EUR, GBP)                                    |
| `active`          | boolean           | optional  | Is this company active? (For marking companies as inactive without deletion)        |
| `is_supplier`     | boolean           | optional  | Can this company act as a supplier?                                                 |
| `is_manufacturer` | boolean           | optional  | Is this company a manufacturer?                                                     |
| `is_customer`     | boolean           | optional  | Can this company purchase from us?                                                  |
| `notes`           | string            | nullable  | Additional notes about the company; max length: 500                                 |

## Notes

- All fields are optional when using PATCH. Include only the fields you want to update.
- Fields not included in the request body will remain unchanged.
- Nullable fields can be set to `null` to clear their values.
- See the [Company](company.md) schema for complete field descriptions and constraints.
