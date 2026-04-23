---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PartTestTemplate
fetched: 2026-04-13
---

# PartTestTemplate

Serializer for the PartTestTemplate class.

**Type:** object

## Properties

| Field                 | Type    | Flags               | Description                                                         |
| --------------------- | ------- | ------------------- | ------------------------------------------------------------------- |
| `pk`                  | integer | required, read-only |                                                                     |
| `key`                 | string  | required, read-only |                                                                     |
| `part`                | integer | required            |                                                                     |
| `test_name`           | string  | required            | Enter a name for the test; max length: 100                          |
| `description`         | string  | required            | Enter description for this test; max length: 100; cannot be blank   |
| `enabled`             | boolean |                     | Is this test enabled?                                               |
| `required`            | boolean |                     | Is this test required to pass?                                      |
| `requires_value`      | boolean |                     | Does this test require a value when adding a test result?           |
| `requires_attachment` | boolean |                     | Does this test require a file attachment when adding a test result? |
| `results`             | integer | required, read-only | Number of results recorded against this template                    |
| `choices`             | string  |                     | Valid choices for this test (comma-separated); max length: 5000     |
