---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: PatchedAddress
fetched: 2026-04-17
---

# PatchedAddress

Serializer for partial updates to an Address object.

Used when performing PATCH requests to update one or more fields of an existing address without affecting other fields.

**Type:** object

## Properties

| Field        | Type              | Flags     | Description                                                              |
|--------------|-------------------|-----------|--------------------------------------------------------------------------|
| `company`    | integer           | optional  | Reference to the Company this address belongs to (company ID)           |
| `type`       | string            | nullable  | Type of address (e.g., 'shipping', 'billing'); max length: 50          |
| `title`      | string            | nullable  | Address title or label (e.g., 'Main Office', 'Warehouse'); max length: 100 |
| `line1`      | string            | optional  | Street address line 1; max length: 100                                 |
| `line2`      | string            | nullable  | Street address line 2; max length: 100                                 |
| `city`       | string            | nullable  | City/town name; max length: 50                                         |
| `state`      | string            | nullable  | State/province name; max length: 50                                    |
| `postal_code` | string           | nullable  | Postal/ZIP code; max length: 20                                        |
| `country`    | string            | nullable  | Country name or code; max length: 50                                   |
| `phone`      | string            | nullable  | Phone number for this address; max length: 30                         |
| `email`      | string (email)    | nullable  | Email address for this location; max length: 254                       |
| `notes`      | string            | nullable  | Additional notes about this address; max length: 500                   |
| `is_default` | boolean           | optional  | Mark this as the default address for the company                       |

## Notes

- All fields are optional when using PATCH. Include only the fields you want to update.
- Fields not included in the request body will remain unchanged.
- Nullable fields can be set to `null` to clear their values.
- The `company` reference cannot typically be changed after address creation, though it is listed as optional here for API consistency.
- See the [Address](address.md) schema for complete field descriptions and constraints.
