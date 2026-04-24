---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: Address
fetched: 2026-04-17
---

# Address

Serializer for company address information.

Used when managing addresses associated with a company (billing, shipping, etc.).

**Type:** object

## Properties

| Field        | Type              | Flags                | Description                                                              |
|--------------|-------------------|----------------------|--------------------------------------------------------------------------|
| `id`         | integer           | required, read-only  | The unique identifier (primary key) of the Address object.              |
| `company`    | integer           | required             | Reference to the Company this address belongs to (company ID)           |
| `type`       | string            | nullable             | Type of address (e.g., 'shipping', 'billing'); max length: 50          |
| `title`      | string            | nullable             | Address title or label (e.g., 'Main Office', 'Warehouse'); max length: 100 |
| `line1`      | string            | required             | Street address line 1; max length: 100                                 |
| `line2`      | string            | nullable             | Street address line 2; max length: 100                                 |
| `city`       | string            | nullable             | City/town name; max length: 50                                         |
| `state`      | string            | nullable             | State/province name; max length: 50                                    |
| `postal_code` | string           | nullable             | Postal/ZIP code; max length: 20                                        |
| `country`    | string            | nullable             | Country name or code; max length: 50                                   |
| `phone`      | string            | nullable             | Phone number for this address; max length: 30                         |
| `email`      | string (email)    | nullable             | Email address for this location; max length: 254                       |
| `notes`      | string            | nullable             | Additional notes about this address; max length: 500                   |
| `is_default` | boolean           |                      | Mark this as the default address for the company                       |
| `created`    | string (date-time)| required, read-only  | Timestamp when the address record was created                          |
| `updated`    | string (date-time)| required, read-only  | Timestamp when the address record was last updated                     |

## Notes

- The `id` field is automatically assigned by the system and is read-only.
- The `company` field is required and must reference a valid Company ID.
- Multiple addresses can be associated with a single company.
- The `is_default` field can be used to mark a preferred address for the company.
- Address types are flexible and determined by the organization's needs.
