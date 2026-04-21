# Part Detail Tabs — Test Suite Index

**Suite**: Part Detail Tabs UI
**Test Part**: Part 77 "Widget Assembly" (WID-TEMPLATE) — primary; Parts 82, 87 for edge cases
**Base URL**: `https://demo.inventree.org`
**Total TCs**: 30
**Auth Required**: All TCs require login as `admin`

## Summary Table

| TC ID           | Title                                                                       | Tab              | Type | Priority | Mutating |
|-----------------|-----------------------------------------------------------------------------|------------------|------|----------|----------|
| TC-UI-TABS-001  | Stock Tab loads and displays stock items for an assembly part               | Stock            | UI   | P1       | No       |
| TC-UI-TABS-002  | Add a new Stock Item from the Stock tab                                     | Stock            | UI   | P1       | Yes      |
| TC-UI-TABS-003  | Export stock data from the Stock tab                                        | Stock            | UI   | P3       | No       |
| TC-UI-TABS-004  | Bill of Materials tab visible only for assembly parts and shows columns     | Bill of Materials| UI   | P1       | No       |
| TC-UI-TABS-005  | Validate BOM from the Bill of Materials tab                                 | Bill of Materials| UI   | P2       | Yes      |
| TC-UI-TABS-006  | Bill of Materials tab is absent for a non-assembly part                     | Bill of Materials| UI   | P2       | No       |
| TC-UI-TABS-007  | Allocations tab loads and shows allocation data for a salable component part| Allocations      | UI   | P1       | No       |
| TC-UI-TABS-008  | Allocations tab visibility condition for component vs non-component parts   | Allocations      | UI   | P2       | No       |
| TC-UI-TABS-009  | Allocations tab shows reserved vs. consumed stock lifecycle state           | Allocations      | UI   | P3       | No       |
| TC-UI-TABS-010  | Build Orders tab loads and lists existing build orders for an assembly part | Build Orders     | UI   | P1       | No       |
| TC-UI-TABS-011  | Create a new Build Order from the Build Orders tab                          | Build Orders     | UI   | P1       | Yes      |
| TC-UI-TABS-012  | Build Orders tab shows correct build status badges                          | Build Orders     | UI   | P2       | No       |
| TC-UI-TABS-013  | Parameters tab loads and displays part parameters                           | Parameters       | UI   | P1       | No       |
| TC-UI-TABS-014  | Add a parameter to a part from the Parameters tab                           | Parameters       | UI   | P2       | Yes      |
| TC-UI-TABS-015  | Parameters tab filter accepts unit-aware values                             | Parameters       | UI   | P3       | No       |
| TC-UI-TABS-016  | Variants tab is visible on a template part and lists variant parts          | Variants         | UI   | P1       | No       |
| TC-UI-TABS-017  | Create a new variant from the Variants tab                                  | Variants         | UI   | P2       | Yes      |
| TC-UI-TABS-018  | Variants tab is absent for a non-template part                              | Variants         | UI   | P2       | No       |
| TC-UI-TABS-019  | Revision selector dropdown appears when a part has multiple revisions       | Revisions        | UI   | P1       | No       |
| TC-UI-TABS-020  | Create a new revision via the Duplicate Part action                         | Revisions        | UI   | P2       | Yes      |
| TC-UI-TABS-021  | Revision selector is absent for a part with no revisions                   | Revisions        | UI   | P3       | No       |
| TC-UI-TABS-022  | Attachments tab loads and shows empty state when no attachments exist       | Attachments      | UI   | P1       | No       |
| TC-UI-TABS-023  | Upload a file attachment from the Attachments tab                           | Attachments      | UI   | P1       | Yes      |
| TC-UI-TABS-024  | Delete an existing attachment from the Attachments tab                      | Attachments      | UI   | P2       | Yes      |
| TC-UI-TABS-025  | Related Parts tab loads and shows empty state when no related parts exist   | Related Parts    | UI   | P1       | No       |
| TC-UI-TABS-026  | Add a related part from the Related Parts tab                               | Related Parts    | UI   | P2       | Yes      |
| TC-UI-TABS-027  | Remove a related part relationship from the Related Parts tab               | Related Parts    | UI   | P3       | Yes      |
| TC-UI-TABS-028  | Test Templates tab loads and displays existing test templates               | Test Templates   | UI   | P1       | No       |
| TC-UI-TABS-029  | Add a new Test Template from the Test Templates tab                         | Test Templates   | UI   | P2       | Yes      |
| TC-UI-TABS-030  | Test Templates tab is absent for a non-testable part                        | Test Templates   | UI   | P2       | No       |

## Coverage by Priority

| Priority | Count | TCs                                                                 |
|----------|-------|---------------------------------------------------------------------|
| P1       | 11    | 001, 002, 004, 007, 010, 011, 013, 016, 019, 022, 023, 025, 028    |
| P2       | 13    | 005, 006, 008, 012, 014, 017, 018, 020, 024, 026, 029, 030         |
| P3       | 6     | 003, 009, 015, 021, 027                                             |

## Confirmed UI Divergences from Documentation

| Documentation Term       | Actual UI Label       | TC IDs Affected        |
|--------------------------|-----------------------|------------------------|
| "BOM Tab"                | "Bill of Materials"   | 004, 005, 006          |
| "Allocated Tab"          | "Allocations"         | 007, 008, 009          |
| "Revisions Tab"          | No dedicated tab — revision navigation is a dropdown selector above the tab bar | 019, 020, 021 |
| "Quantity" column (Stock)| "Stock" column        | 001                    |

## Test Data Reference

| Part | Name             | IPN          | Assembly | Template | Trackable | Testable | Notes                              |
|------|------------------|--------------|----------|----------|-----------|----------|------------------------------------|
| 77   | Widget Assembly  | WID-TEMPLATE | true     | true     | true      | true     | 4 variants, 4 test templates, 209 stock |
| 87   | Doohickey        | (none)       | true     | false    | true      | true     | 4 BOM items, 3 test templates      |
| 82   | 1551ABK          | (none)       | false    | false    | false     | false    | Has revision part pk=2005 (rev="B") |
| 2005 | RevisionPart-TC007-... | (none) | false  | false    | false     | false    | revision="B", revision_of=82       |
