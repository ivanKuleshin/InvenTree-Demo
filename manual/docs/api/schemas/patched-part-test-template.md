---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PatchedPartTestTemplate
fetched: 2026-04-13
---

# PatchedPartTestTemplate

Serializer for the PartTestTemplate class. Used in PATCH requests where all fields are optional.

**Type:** object

## Properties

| Field                 | Type    | Flags     | Description                                                         |
| --------------------- | ------- | --------- | ------------------------------------------------------------------- |
| `pk`                  | integer | read-only |                                                                     |
| `key`                 | string  | read-only |                                                                     |
| `part`                | integer |           |                                                                     |
| `test_name`           | string  |           | Enter a name for the test; max length: 100                          |
| `description`         | string  |           | Enter description for this test; max length: 100; cannot be blank if provided |
| `enabled`             | boolean |           | Is this test enabled?                                               |
| `required`            | boolean |           | Is this test required to pass?                                      |
| `requires_value`      | boolean |           | Does this test require a value when adding a test result?           |
| `requires_attachment` | boolean |           | Does this test require a file attachment when adding a test result? |
| `results`             | integer | read-only | Number of results recorded against this template                    |
| `choices`             | string  |           | Valid choices for this test (comma-separated); max length: 5000     |
