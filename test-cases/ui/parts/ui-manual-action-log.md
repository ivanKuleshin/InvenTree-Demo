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
| Purchaseable              | `boolean-field-purchasable`             | checkbox      | **true** (checked)                                  |
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

---

## Session 2 Exploration (2026-04-14) — Initial Stock, Keep Form Open, Import Validation

### Initial Stock Accordion

- Accordion button text: "**Initial Stock**"
- `aria-expanded="true"` by default — section is **open** on dialog load
- Accordion button ID pattern: `mantine-*-control-OpenByDefault`
- Accordion panel ID pattern: `mantine-*-panel-OpenByDefault`
- Collapsed state: `aria-expanded="false"`, panel `aria-hidden="true"`, CSS `display: none`, `height: 0px`
- Expanded state: `aria-expanded="true"`, panel visible
- "Initial Stock Quantity \*" field: `aria-label="number-field-initial_stock.quantity"`, name=`initial_stock.quantity`, type=`decimal`, required=`true`, default value=`0`
- "Initial Stock Location" field: `aria-label="related-field-initial_stock.location"`, React-Select combobox, placeholder "Search..."

### "Keep Form Open" Checkbox

- Label text: "**Keep form open**", description: "Keep form open after submitting"
- No `aria-label` attribute; id is Mantine-generated (e.g., `mantine-4a229vqyl`) — not stable across sessions
- Default state: `checked=false` (unchecked/OFF)
- Locating strategy for automation: `page.locator('label').filter({ hasText: 'Keep form open' }).locator('input[type="checkbox"]')`

### Import Dialog — Step 1 Validation

- No-file-submit error (actual): "**No file was submitted.**" (inline below field) + "Form Error — Errors exist for one or more form fields" banner
- Unsupported format error (actual, confirmed with `test_unsupported.txt`):
  - Error 1: `File extension "txt" is not allowed. Allowed extensions are: csv, xlsx, tsv.`
  - Error 2: `Unsupported data file format`
  - Both messages displayed below the Data File field simultaneously

### Import Session API (Steps 2–5 confirmation)

- `POST /api/importer/session/` with multipart CSV returns 201
- Auto-mapping confirmed: CSV columns `name`, `description`, `IPN` → mapped to DB fields `name`, `description`, `IPN` respectively
- Unmapped fields: empty string `""` (UI shows "Ignore this field")
- Session response includes `columns`, `column_mappings`, `available_fields`, `field_defaults`
- `row_count: 0` after session creation; rows populated after session accept (Step 3)
- Import session API endpoint: `/api/importer/session/`
- Known issue: UI wizard Step 1 Submit fails with "Invalid field type for field 'data_file': 'undefined'" JS error in browser console on demo v1.4.0 dev | 479; API accepts file correctly when called directly

---

## Divergences from Documentation

| Doc claim                             | Observed                                                   | Notes                                                 |
| ------------------------------------- | ---------------------------------------------------------- | ----------------------------------------------------- |
| `active` default: `true`              | Confirmed — checkbox default is checked                    | Matches                                               |
| `component` default: not specified    | **true** — checkbox pre-checked                            | UI default is true; API docs say `false` — divergence |
| `purchasable` default: not specified | **true** — checkbox pre-checked                            | UI default is true                                    |
| Boolean field `testable`              | Confirmed present in form                                  | Matches                                               |
| `copy_bom` in duplicate               | Not seen in UI                                             | Docs mention it; may be server-only                   |
| `copy_tests` in duplicate             | Not seen in UI                                             | Docs mention it; may be server-only                   |
| Import wizard multi-step              | Step 1 confirmed; steps 2-4 require file upload to observe | Partially confirmed                                   |
| No-file submit error: "This field is required." | Actual error: "**No file was submitted.**" | Divergence — docs incorrect for this version |
| Import wizard Step 1 → Step 2 transition | UI fails to advance in browser (400 from `/api/importer/session/`) | JS error "Invalid field type for field 'data_file': 'undefined'" on v1.4.0 dev |
| Initial Stock accordion collapsed by default | **Expanded by default** (`aria-expanded="true"`) | Divergence — docs say "collapsible", observed as open |
