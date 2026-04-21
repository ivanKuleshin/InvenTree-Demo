# UI Manual Test Plan — Part Detail Tabs

## Coverage Table

| Coverage Area       | Page URL(s)                                           | TC IDs                              | Auth Required | Notes                        |
|---------------------|-------------------------------------------------------|-------------------------------------|---------------|------------------------------|
| Stock Tab           | `/web/part/77/stock`                                  | TC-UI-TABS-001 to TC-UI-TABS-003    | Yes           | Read + [MUTATING] add stock  |
| BOM Tab             | `/web/part/77/bom`                                    | TC-UI-TABS-004 to TC-UI-TABS-006    | Yes           | Read + [MUTATING] add item   |
| Allocations Tab     | `/web/part/77/allocations`                            | TC-UI-TABS-007 to TC-UI-TABS-009    | Yes           | Read-only                    |
| Build Orders Tab    | `/web/part/77/builds`                                 | TC-UI-TABS-010 to TC-UI-TABS-012    | Yes           | Read + [MUTATING] new build  |
| Parameters Tab      | `/web/part/77/parameters`                             | TC-UI-TABS-013 to TC-UI-TABS-015    | Yes           | Read + [MUTATING] add param  |
| Variants Tab        | `/web/part/77/variants`                               | TC-UI-TABS-016 to TC-UI-TABS-018    | Yes           | Read + [MUTATING] new variant|
| Revisions Tab       | `/web/part/82/details`                                | TC-UI-TABS-019 to TC-UI-TABS-021    | Yes           | Read + revision navigation   |
| Attachments Tab     | `/web/part/77/attachments`                            | TC-UI-TABS-022 to TC-UI-TABS-024    | Yes           | [MUTATING] upload/delete     |
| Related Parts Tab   | `/web/part/77/related_parts`                          | TC-UI-TABS-025 to TC-UI-TABS-027    | Yes           | [MUTATING] add/remove        |
| Test Templates Tab  | `/web/part/77/test_templates`                         | TC-UI-TABS-028 to TC-UI-TABS-030    | Yes           | [MUTATING] add template      |

## Test Part Reference

- **Part 77** — "Widget Assembly" (IPN: WID-TEMPLATE): assembly=true, trackable=true, is_template=true, salable=true, component=true, testable=true. Has 4 variants, 7 BOM items, 209 stock items, 4 test templates.
- **Part 87** — "Doohickey": assembly=true, trackable=true, 4 BOM items, 3 test templates.
- **Part 82** — "1551ABK": has revision_count=1; revision part is 2005 "RevisionPart-TC007-...".

## Mutating Flows

The following flows involve form submissions or data mutations:
- [MUTATING] Stock Tab — "Add Stock Item" dialog
- [MUTATING] BOM Tab — "Add BOM Item" via `action-menu-add-bom-items`
- [MUTATING] Build Orders Tab — "New Build Order" dialog
- [MUTATING] Parameters Tab — add a parameter instance
- [MUTATING] Variants Tab — "New Variant" (Duplicate Part form)
- [MUTATING] Attachments Tab — upload file, delete attachment
- [MUTATING] Related Parts Tab — add related part, remove relationship
- [MUTATING] Test Templates Tab — add test template

## Confirmed Tab Name Divergences from Documentation

| Documentation Name | Actual UI Tab Label |
|--------------------|---------------------|
| BOM Tab            | Bill of Materials   |
| Allocated Tab      | Allocations         |
| Build Orders Tab   | Build Orders        |
| Revisions Tab      | (no separate tab — revision navigation appears as dropdown at top of part page) |
