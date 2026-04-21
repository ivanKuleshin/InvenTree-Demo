---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: Company
fetched: 2026-04-17
---

# Company

Serializer for complete detail information of a company.

Used when displaying all details of a single company record.

**Type:** object

## Properties

| Field             | Type              | Flags                | Description                                                                          |
|-------------------|-------------------|----------------------|--------------------------------------------------------------------------------------|
| `id`              | integer           | required, read-only  | The unique identifier (primary key) of the Company object.                          |
| `name`            | string            | required             | Company name; max length: 100                                                       |
| `description`     | string            | nullable             | Company description (optional); max length: 500                                     |
| `website`         | string (uri)      | nullable             | Company website URL; max length: 2000                                               |
| `address`         | string            | nullable             | Company address; max length: 250                                                    |
| `phone`           | string            | nullable             | Company phone number; max length: 30                                                |
| `email`           | string (email)    | nullable             | Company email address; max length: 254                                              |
| `contact`         | string            | nullable             | Primary contact person name; max length: 100                                        |
| `link`            | string (uri)      | nullable             | Link to external URL; max length: 2000                                              |
| `image`           | string (uri)      | nullable             | Company logo/image URL                                                              |
| `currency`        | string            | nullable             | Currency code for pricing (e.g., USD, EUR, GBP)                                    |
| `active`          | boolean           |                      | Is this company active? (For marking companies as inactive without deletion)        |
| `is_supplier`     | boolean           |                      | Can this company act as a supplier?                                                 |
| `is_manufacturer` | boolean           |                      | Is this company a manufacturer?                                                     |
| `is_customer`     | boolean           |                      | Can this company purchase from us?                                                  |
| `notes`           | string            | nullable             | Additional notes about the company; max length: 500                                 |
| `created`         | string (date-time)| required, read-only  | Timestamp when the company record was created                                       |
| `updated`         | string (date-time)| required, read-only  | Timestamp when the company record was last updated                                  |

## Notes

- The `id` field is automatically assigned by the system and is read-only.
- Company records can have multiple roles (supplier, manufacturer, customer) simultaneously.
- Company contacts and addresses are managed through separate API endpoints (`/api/company/contact/`, `/api/company/address/`).
- The `link` field can be used to reference external systems or databases associated with the company.
- When deactivating a company, set `active` to `false` rather than deleting the record to maintain referential integrity.
