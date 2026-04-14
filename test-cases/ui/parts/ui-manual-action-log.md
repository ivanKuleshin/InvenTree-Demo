# UI Manual Exploration Action Log — Part Creation

**Date:** 2026-04-14  
**Tester:** AI exploration agent  
**Base URL:** https://demo.inventree.org  
**Auth:** admin / inventree

---

## Session Flow

### Login

- URL: `https://demo.inventree.org/web/login`
- Page title: "InvenTree Demo Server"
- Fields: `login-username` (textbox, placeholder "Your username"), `login-password` (textbox, placeholder "Your password")
- Buttons: "Send me an email", "Log In"
- On success: redirect to `https://demo.inventree.org/web/home`, title "InvenTree Demo Server | Dashboard"
- "Superuser Mode" alert banner visible on all pages after admin login

---

### Parts List Page

- URL: `https://demo.inventree.org/web/part/category/index/parts`
- Page title: "InvenTree Demo Server | Parts"
- Breadcrumb: "Parts"
- Left panel tabs: "Category Details", "Part Categories", "Parts" (tabs in panel-tabs-partcategory)
- Parts table columns: Part, IPN, Revision, Units, Description, Category, Total Stock
- Table toolbar buttons (with aria-labels):
  - `action-menu-printing-actions` (disabled when no rows selected)
  - `action-menu-part-actions` (disabled when no rows selected)
  - `action-menu-add-parts` (always enabled — green "+" icon with dropdown)
  - `table-search-input` (Search textbox)
  - `table-refresh`, `table-select-columns`, `table-select-filters`, `table-export-data`
- Pagination: "1 - 25 / 681" records shown
- `action-menu-add-parts` dropdown reveals:
  - `action-menu-add-parts-create-part` — label: "Create Part"
  - `action-menu-add-parts-import-from-file` — label: "Import from File"

---

### "Add Part" Modal (Create Part)

- Triggered by: clicking `action-menu-add-parts` → "Create Part"
- Dialog role: `[role="dialog"]`, heading: "**Add Part**"
- Modal is scrollable; many fields hidden below the fold

#### Confirmed Fields (from JS extraction):

| UI Label                  | aria-label                               | Field Type    | Default Value                                       |
| ------------------------- | ---------------------------------------- | ------------- | --------------------------------------------------- |
| Category                  | `related-field-category`                 | text/combobox | (empty, placeholder "Search...")                    |
| Name \*                   | `text-field-name`                        | string/text   | (empty)                                             |
| IPN                       | `text-field-IPN`                         | string/text   | (empty)                                             |
| Description               | `text-field-description`                 | string/text   | (empty), subtitle "Part description (optional)"     |
| Revision                  | `text-field-revision`                    | string/text   | (empty), subtitle "Part revision or version number" |
| Revision Of               | `related-field-revision_of`              | text/combobox | (empty)                                             |
| Variant Of                | `related-field-variant_of`               | text/combobox | (empty)                                             |
| Keywords                  | `text-field-keywords`                    | string/text   | (empty)                                             |
| Units                     | `text-field-units`                       | string/text   | (empty)                                             |
| Link                      | `text-field-link`                        | url           | (empty)                                             |
| Default Location          | `related-field-default_location`         | text/combobox | (empty)                                             |
| Default Expiry            | `number-field-default_expiry`            | integer       | 0                                                   |
| Minimum Stock             | `number-field-minimum_stock`             | float         | 0                                                   |
| Responsible               | `related-field-responsible`              | text/combobox | (empty)                                             |
| Component                 | `boolean-field-component`                | checkbox      | **true** (checked)                                  |
| Assembly                  | `boolean-field-assembly`                 | checkbox      | false                                               |
| Is Template               | `boolean-field-is_template`              | checkbox      | false                                               |
| Testable                  | `boolean-field-testable`                 | checkbox      | false                                               |
| Trackable                 | `boolean-field-trackable`                | checkbox      | false                                               |
| Purchaseable              | `boolean-field-purchaseable`             | checkbox      | **true** (checked)                                  |
| Salable                   | `boolean-field-salable`                  | checkbox      | false                                               |
| Virtual                   | `boolean-field-virtual`                  | checkbox      | false                                               |
| Locked                    | `boolean-field-locked`                   | checkbox      | false                                               |
| Active                    | `boolean-field-active`                   | checkbox      | **true** (checked)                                  |
| Copy Category Parameters  | `boolean-field-copy_category_parameters` | checkbox      | **true** (checked)                                  |
| Initial Stock Quantity \* | `number-field-initial_stock.quantity`    | decimal       | 0                                                   |
| Initial Stock Location    | `related-field-initial_stock.location`   | text/combobox | (empty)                                             |

#### Bottom controls:

- **Keep form open** toggle (switch) — off by default, subtitle "Keep form open after submitting"
- **Cancel** button
- **Submit** button (green)

#### Validation behavior (empty submit):

- Red banner at top of form: "**Form Error** — Errors exist for one or more form fields"
- Name field highlighted with red border
- Error text below Name: "**This field is required.**"

#### Success behavior (Name = "TC-UI-PC-001-TestPart", all other defaults):

- Dialog closes
- Redirects to new part detail page: `https://demo.inventree.org/web/part/1375/details`
- Page title: "InvenTree Demo Server | Part: TC-UI-PC-001-TestPart"
- Heading: "**Part: TC-UI-PC-001-TestPart**"
- Green "**NO STOCK**" badge in header
- Green toast notification: "**Success** — Item Created"

---

### Part Detail Page

- URL: `https://demo.inventree.org/web/part/{id}/details`
- Page heading: "Part: {part name}"
- Header buttons (aria-labels): `action-button-open-in-admin-interface`, `action-button-subscribe-to-notifications`, `action-menu-barcode-actions`, `action-menu-printing-actions`, `action-menu-stock-actions`, `action-menu-part-actions`
- Left navigation tabs (observed): Part Details, Stock, Allocations, Used In, Part Pricing, Suppliers, Purchase Orders, Stock History, Related Parts, Parameters, Attachments, Notes

#### Part Actions Menu (`action-menu-part-actions`):

- **Duplicate** (`action-menu-part-actions-duplicate`)
- **Edit** (`action-menu-part-actions-edit`)
- **Delete** (`action-menu-part-actions-delete`) — grayed out for parts with no stock (policy enforced by server)

---

### "Add Part" Modal (Duplicate flow)

- Triggered by: `action-menu-part-actions` → "Duplicate"
- Dialog heading: "**Add Part**" (same as Create)
- Pre-fills "Name" field with original part name
- Contains all same fields as Create form, PLUS three additional checkboxes at the bottom:
  - **Copy Image** — "Copy image from original part"
  - **Copy Notes** — "Copy notes from original part"
  - **Copy Parameters** — "Copy parameter data from original part"
- Note: "Copy BOM" and "Copy Tests" not observed in UI (may be server-side or hidden)

---

### "Import Parts" Modal (Import from File)

- Triggered by: `action-menu-add-parts` → "Import from File"
- Dialog heading: "**Import Parts**"
- Fields:
  - **Data File \*** (required) — label "Data file to import", type=file, "Select file to upload" button
- Buttons: **Cancel**, **Submit** (green)
- This is Step 1 of a multi-step wizard; subsequent steps (field mapping, row review) appear after file upload

---

## Divergences from Documentation

| Doc claim                             | Observed                                                   | Notes                                                 |
| ------------------------------------- | ---------------------------------------------------------- | ----------------------------------------------------- |
| `active` default: `true`              | Confirmed — checkbox default is checked                    | Matches                                               |
| `component` default: not specified    | **true** — checkbox pre-checked                            | UI default is true; API docs say `false` — divergence |
| `purchaseable` default: not specified | **true** — checkbox pre-checked                            | UI default is true                                    |
| Boolean field `testable`              | Confirmed present in form                                  | Matches                                               |
| `copy_bom` in duplicate               | Not seen in UI                                             | Docs mention it; may be server-only                   |
| `copy_tests` in duplicate             | Not seen in UI                                             | Docs mention it; may be server-only                   |
| Import wizard multi-step              | Step 1 confirmed; steps 2-4 require file upload to observe | Partially confirmed                                   |
