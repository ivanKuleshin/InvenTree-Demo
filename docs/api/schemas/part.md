---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: Part
fetched: 2026-04-13
---

# Part

Serializer for complete detail information of a part.

Used when displaying all details of a single component.

**Type:** object

## Properties

| Field                       | Type                  | Flags               | Description                                                                                            |
| --------------------------- | --------------------- | ------------------- | ------------------------------------------------------------------------------------------------------ |
| `active`                    | boolean               |                     | Is this part active?                                                                                   |
| `assembly`                  | boolean               |                     | Can this part be built from other parts?                                                               |
| `barcode_hash`              | string                | required, read-only | Unique hash of barcode data                                                                            |
| `category`                  | integer               | nullable            |                                                                                                        |
| `category_name`             | string                | required, read-only |                                                                                                        |
| `component`                 | boolean               |                     | Can this part be used to build other parts?                                                            |
| `creation_date`             | string (date)         | read-only, nullable |                                                                                                        |
| `creation_user`             | integer               | nullable            |                                                                                                        |
| `default_expiry`            | integer               |                     | Expiry time (in days) for stock items of this part; minimum: `0`; maximum: `2147483647`                |
| `default_location`          | integer               | nullable            | Where is this item normally stored?                                                                    |
| `description`               | string                |                     | Part description (optional); max length: 250                                                           |
| `full_name`                 | string                | required, read-only | Format a 'full name' for this Part based on the format PART_NAME_FORMAT defined in InvenTree settings. |
| `image`                     | string (uri)          | nullable            |                                                                                                        |
| `remote_image`              | string (uri)          | write-only          | URL of remote image file                                                                               |
| `existing_image`            | string                | write-only          | Filename of an existing part image                                                                     |
| `IPN`                       | string                |                     | max length: 100; default: ``                                                                           |
| `is_template`               | boolean               |                     | Is this part a template part?                                                                          |
| `keywords`                  | string                | nullable            | Part keywords to improve visibility in search results; max length: 250                                 |
| `link`                      | string (uri)          | nullable            | Link to external URL; max length: 2000                                                                 |
| `locked`                    | boolean               |                     | Locked parts cannot be edited                                                                          |
| `minimum_stock`             | number (double)       |                     | default: `0.0`                                                                                         |
| `name`                      | string                | required            | Part name; max length: 100                                                                             |
| `pk`                        | integer               | required, read-only |                                                                                                        |
| `purchaseable`              | boolean               |                     | Can this part be purchased from external suppliers?                                                    |
| `revision`                  | string                | nullable            | max length: 100; default: ``                                                                           |
| `revision_of`               | integer               | nullable            | Is this part a revision of another part?                                                               |
| `revision_count`            | integer               | read-only, nullable |                                                                                                        |
| `salable`                   | boolean               |                     | Can this part be sold to customers?                                                                    |
| `starred`                   | boolean               | required, read-only | Return "true" if the part is starred by the current user.                                              |
| `thumbnail`                 | string                | required, read-only |                                                                                                        |
| `testable`                  | boolean               |                     | Can this part have test results recorded against it?                                                   |
| `trackable`                 | boolean               |                     | Does this part have tracking for unique items?                                                         |
| `units`                     | string                | nullable            | Units of measure for this part; max length: 20                                                         |
| `variant_of`                | integer               | nullable            | Is this part a variant of another part?                                                                |
| `virtual`                   | boolean               |                     | Is this a virtual part, such as a software product or license?                                         |
| `pricing_min`               | string (decimal)      | read-only, nullable |                                                                                                        |
| `pricing_max`               | string (decimal)      | read-only, nullable |                                                                                                        |
| `pricing_updated`           | string (date-time)    | read-only, nullable |                                                                                                        |
| `responsible`               | integer               | nullable            |                                                                                                        |
| `allocated_to_build_orders` | number (double)       | read-only, nullable |                                                                                                        |
| `allocated_to_sales_orders` | number (double)       | read-only, nullable |                                                                                                        |
| `building`                  | number (double)       | read-only, nullable | Quantity of this part currently being in production                                                    |
| `scheduled_to_build`        | number (double)       | read-only, nullable | Outstanding quantity of this part scheduled to be built                                                |
| `category_default_location` | integer               | read-only, nullable |                                                                                                        |
| `in_stock`                  | number (double)       | read-only, nullable |                                                                                                        |
| `ordering`                  | number (double)       | read-only, nullable |                                                                                                        |
| `required_for_build_orders` | integer               | read-only, nullable |                                                                                                        |
| `required_for_sales_orders` | integer               | read-only, nullable |                                                                                                        |
| `stock_item_count`          | integer               | read-only, nullable |                                                                                                        |
| `total_in_stock`            | number (double)       | read-only, nullable |                                                                                                        |
| `external_stock`            | number (double)       | read-only, nullable |                                                                                                        |
| `unallocated_stock`         | number (double)       | read-only, nullable |                                                                                                        |
| `variant_stock`             | number (double)       | read-only, nullable |                                                                                                        |
| `duplicate`                 | $ref: DuplicatePart   | write-only          | Copy initial data from another Part                                                                    |
| `initial_stock`             | $ref: InitialStock    | write-only          | Create Part with initial stock quantity                                                                |
| `initial_supplier`          | $ref: InitialSupplier | write-only          | Add initial supplier information for this part                                                         |
| `copy_category_parameters`  | boolean               | write-only          | Copy parameter templates from selected part category; default: `True`                                  |
