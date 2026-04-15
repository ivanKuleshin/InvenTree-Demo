# TC_UI_PART_ATTRIBUTES — Part Attribute Toggles UI Test Suite

**Application:** InvenTree Demo
**URL:** https://demo.inventree.org
**User under test:** `admin` / `inventree` (Display name: Adam Administrator)
**Approach:** Functional + Exploratory
**Created:** 2026-04-14

---

## Template Legend

| Field                | Description                                   |
| -------------------- | --------------------------------------------- |
| **ID**               | Unique test case identifier                   |
| **Priority**         | P1 (smoke) / P2 (regression) / P3 (edge case) |
| **Type**             | Functional / Exploratory / Negative           |
| **Preconditions**    | State required before execution               |
| **Steps**            | Numbered actions with expected results        |
| **Observed**         | Data confirmed from live UI/API exploration   |
| **Automation Hints** | Selectors and assertions for automation       |

---

## TC-UI-PA-001: Toggle Active attribute OFF — part becomes inactive

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- A test part named `TC-UI-PA-001-ActivePart` exists or is created via `action-menu-add-parts` → "Create Part" with only the name filled in (Active defaults to checked)

**Steps:**

| #   | Action                                                                                                       | Expected Result                                                                                                                  |
| --- | ------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/{id}/details` for the test part                             | Part detail page loads; page heading reads "**Part: TC-UI-PA-001-ActivePart**"                                                   |
| 2   | Locate the attributes panel in the right-hand section of the Details tab                                     | The panel is visible and shows attribute toggles including "Active"                                                              |
| 3   | Click the `action-menu-part-actions` button (three-dot menu icon in the part header)                         | A dropdown menu opens with items: "Duplicate", "Edit", "Delete"                                                                  |
| 4   | Click the "Edit" menu item (aria-label: `action-menu-part-actions-edit`)                                     | The "Edit Part" dialog opens with all current attribute values populated                                                         |
| 5   | Verify the "Active" checkbox (aria-label: `boolean-field-active`) is currently **checked**                   | Active checkbox is checked                                                                                                       |
| 6   | Click the "Active" checkbox to **uncheck** it                                                                | Active checkbox becomes unchecked                                                                                                |
| 7   | Click the "Submit" button                                                                                    | Dialog closes; the part detail page reloads                                                                                      |
| 8   | Verify that a visual "Inactive" indicator appears in the part header or attributes panel                     | An "**INACTIVE**" badge or label is visible near the part name or in the header area                                             |
| 9   | Verify the part attributes panel reflects Active = false                                                     | The "Active" attribute is shown as off/false/unchecked in the panel                                                              |
| 10  | Re-open the Edit dialog via `action-menu-part-actions` → "Edit"                                              | Edit dialog opens                                                                                                                |
| 11  | Verify "Active" checkbox is **unchecked**                                                                    | Checkbox reflects the persisted inactive state                                                                                   |
| 12  | Check the "Active" checkbox to re-enable it and click "Submit"                                               | Dialog closes; "INACTIVE" badge disappears; part returns to active state                                                         |

**Observed:**

- Part detail page URL pattern confirmed: `https://demo.inventree.org/web/part/{pk}/details`
- Edit dialog opened via `action-menu-part-actions-edit` (confirmed in TC-UI-PC-005)
- `boolean-field-active` checkbox confirmed present in Add/Edit dialog (confirmed in TC-UI-PC-001 and TC-UI-PC-004)
- `active` defaults to `true` — confirmed via API and TC-UI-PC-001 Observed block
- Inactive part 84 (`1551ACLR`) confirmed with `active=false` via `GET /api/part/84/`
- Docs state: marking a part inactive is the recommended approach for obsolete parts
- Matches docs: Yes

**Automation Hints:**

- Open edit: `page.getByRole('button', { name: 'action-menu-part-actions' }).click(); page.getByRole('menuitem', { name: 'action-menu-part-actions-edit' }).click()`
- Uncheck active: `page.getByRole('checkbox', { name: 'boolean-field-active' }).uncheck()`
- Submit: `page.getByRole('button', { name: 'Submit' }).click()`
- Assert inactive badge: `expect(page.getByText('INACTIVE')).toBeVisible()`
- Assert API state: `GET /api/part/{id}/ → active === false`

---

## TC-UI-PA-002: Inactive part is excluded from BOM, PO, SO, and Build Order selection lists

**Priority:** P1
**Type:** Functional / Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- An inactive part exists: part pk=84 (`1551ACLR`), `active=false`, `component=true`, `purchasable=true` — confirmed via API
- An assembly part exists with an open BOM: part pk=77 (`Widget Assembly`), `assembly=true`, `locked=false` — confirmed via API
- A purchase order in pending state exists: PO pk=19 (`PO0019`), status=10 (pending) — confirmed via API
- A sales order in pending state exists: SO pk=13 (`SO0027`), status=30 — confirmed via API

**Steps:**

| #   | Action                                                                                                                                         | Expected Result                                                                                                                                  |
| --- | ---------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| 1   | Navigate to `https://demo.inventree.org/web/part/84/details`                                                                                   | Part detail page for `1551ACLR` loads; an "**INACTIVE**" badge is visible in the page header                                                     |
| 2   | Navigate to `https://demo.inventree.org/web/part/77/bom`                                                                                       | BOM tab for `Widget Assembly` loads showing the bill of materials table                                                                          |
| 3   | Click the add BOM line button (typically a "+" or "Add Item" button in the BOM table toolbar)                                                  | A dialog or form opens to add a new BOM line item with a "Sub Part" search field                                                                 |
| 4   | Type `1551ACLR` in the "Sub Part" search field of the BOM add dialog                                                                           | The search results dropdown appears; `1551ACLR` is **not** present in the results list                                                           |
| 5   | Close the BOM add dialog without submitting                                                                                                    | Dialog closes                                                                                                                                    |
| 6   | Navigate to a purchase order: `https://demo.inventree.org/web/purchasing/purchase-order/19/`                                                   | Purchase order PO0019 detail page loads                                                                                                          |
| 7   | Click the "Add Line Item" button or equivalent to add a PO line                                                                                | A dialog opens to add a purchase order line item with a "Part" or "Supplier Part" search field                                                   |
| 8   | Type `1551ACLR` in the part search field                                                                                                       | The search results dropdown appears; `1551ACLR` is **not** present in the results                                                                |
| 9   | Close the PO line dialog without submitting                                                                                                    | Dialog closes                                                                                                                                    |
| 10  | Navigate to a sales order: `https://demo.inventree.org/web/sales/sales-order/13/`                                                              | Sales order SO0027 detail page loads                                                                                                             |
| 11  | Click the "Add Line Item" button to add a SO line                                                                                              | A dialog opens with a "Part" search field                                                                                                        |
| 12  | Type `1551ACLR` in the part search field                                                                                                       | The search results dropdown appears; `1551ACLR` is **not** present in the results                                                                |
| 13  | Close the SO line dialog without submitting                                                                                                    | Dialog closes                                                                                                                                    |
| 14  | Navigate to `https://demo.inventree.org/web/manufacturing/build-order/create` or click "Create Build Order" from the Build Orders section     | A dialog or page opens to create a new build order with a "Part" search field                                                                    |
| 15  | Type `1551ACLR` in the part search field                                                                                                       | The search results dropdown appears; `1551ACLR` is **not** present in the results                                                                |

**Observed:**

- Part 84 (`1551ACLR`): `active=false`, `component=true`, `purchasable=true` — confirmed via `GET /api/part/84/`
- Part 77 (`Widget Assembly`): `assembly=true`, `locked=false`, BOM has 276 lines — confirmed via API
- PO pk=19 (`PO0019`) status=10 (pending) — confirmed via `GET /api/order/po/?limit=2`
- SO pk=13 (`SO0027`) status=30 — confirmed via `GET /api/order/so/?limit=2`
- Docs state: "Inactive parts are excluded from most selection lists (e.g., BOM part selection, purchase order line creation, sales order line creation)"
- Matches docs: Yes

**Automation Hints:**

- Navigate to BOM tab: `page.goto('https://demo.inventree.org/web/part/77/bom')`
- Search in BOM sub-part field: target the search input in the add-BOM-line dialog, fill with `'1551ACLR'`
- Assert not in dropdown: `expect(page.getByRole('option', { name: '1551ACLR' })).not.toBeVisible()`

---

## TC-UI-PA-003: Toggle Virtual attribute ON — stock UI elements are hidden

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/914/details`
- Part pk=914 (`CRM license`) has `virtual=true`, `in_stock=0`, `stock_item_count=0` — confirmed via API

**Steps:**

| #   | Action                                                                                                  | Expected Result                                                                                                                              |
| --- | ------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/914/details`                                           | Part detail page for `CRM license` loads; page heading reads "**Part: CRM license**"                                                         |
| 2   | Observe the tab bar on the part detail page                                                             | The tab bar is visible; note whether a "Stock" tab appears and whether it is present or absent                                               |
| 3   | If a "Stock" tab is present, click it                                                                   | If the Stock tab is present, its content area shows **no "Add Stock" button** and **no stock item rows** (or the tab is entirely absent)     |
| 4   | Verify that the part header stock summary area shows **0** or no stock quantity indicator               | Stock count badge reads "**NO STOCK**" or equivalent zero-stock indicator — no physical stock can be held                                    |
| 5   | Click `action-menu-part-actions` → "Edit"                                                               | Edit dialog opens                                                                                                                            |
| 6   | Verify the "Virtual" checkbox (aria-label: `boolean-field-virtual`) is **checked**                      | Virtual checkbox is checked                                                                                                                  |
| 7   | Click "Cancel" to close the dialog without changes                                                      | Dialog closes; no changes are made                                                                                                           |
| 8   | Navigate to `https://demo.inventree.org/web/part/914/stock`                                             | The Stock tab URL loads; the stock table is empty **and** an "Add Stock" or "New Stock Item" action button is **absent** or **disabled**     |
| 9   | Verify that no "Transfer Stock", "Count Stock", or similar stock-action buttons are present             | Stock mutation actions are hidden or disabled for virtual parts                                                                              |

**Observed:**

- Part 914 (`CRM license`): `virtual=true`, `in_stock=0`, `stock_item_count=0` — confirmed via `GET /api/part/914/`
- `GET /api/stock/?part=914` returns `count=0` — no stock items exist
- Docs state: "Virtual parts cannot have stock items associated with them. User interface elements related to stock items are hidden when viewing a virtual part."
- Matches docs: Yes (confirmed via API; UI behavior requires live browser verification)

**Automation Hints:**

- Navigate: `page.goto('https://demo.inventree.org/web/part/914/stock')`
- Assert no Add Stock button: `expect(page.getByRole('button', { name: /add stock/i })).not.toBeVisible()`
- Assert stock table empty: `expect(page.getByRole('row')).toHaveCount(1)` (header row only)

---

## TC-UI-PA-004: Toggle Virtual attribute OFF — stock actions become available

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A test part named `TC-UI-PA-004-VirtualOff` exists with `virtual=true` and no stock items
- The test part must be created fresh for this test to avoid side-effects on production data

**Steps:**

| #   | Action                                                                                                         | Expected Result                                                                                                          |
| --- | -------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| 1   | Create a new part via `action-menu-add-parts` → "Create Part"                                                  | "Add Part" dialog opens                                                                                                  |
| 2   | Type `TC-UI-PA-004-VirtualOff` in the "Name" field (aria-label: `text-field-name`)                             | Name field shows the value                                                                                               |
| 3   | Check the "Virtual" checkbox (aria-label: `boolean-field-virtual`)                                             | Virtual checkbox becomes checked                                                                                         |
| 4   | Click "Submit"                                                                                                 | Dialog closes; browser navigates to new part detail page                                                                 |
| 5   | Navigate to the Stock tab at `https://demo.inventree.org/web/part/{new_pk}/stock`                              | Stock tab loads; "Add Stock" button is **absent** or **disabled** and no stock items are listed                          |
| 6   | Click `action-menu-part-actions` → "Edit"                                                                     | Edit dialog opens                                                                                                        |
| 7   | Verify "Virtual" checkbox (aria-label: `boolean-field-virtual`) is **checked**                                 | Virtual checkbox is checked                                                                                              |
| 8   | Click the "Virtual" checkbox to **uncheck** it                                                                 | Virtual checkbox becomes unchecked                                                                                       |
| 9   | Click "Submit"                                                                                                 | Dialog closes; part detail page reloads                                                                                  |
| 10  | Navigate to the Stock tab at `https://demo.inventree.org/web/part/{new_pk}/stock`                              | Stock tab loads; an "Add Stock" or "New Stock Item" button is now **visible and enabled**                                |
| 11  | Verify the part attributes panel no longer shows Virtual as active                                             | "Virtual" is shown as off/false in the attributes panel                                                                  |

**Observed:**

- Virtual attribute checkbox aria-label: `boolean-field-virtual` — confirmed from TC-UI-PC-004 Observed block
- Docs state: virtual parts cannot have stock; removing virtual flag should restore stock capabilities
- API field `virtual` confirmed writable (read-write) — confirmed from `docs/ui/part-attributes.md` API Field Reference
- Matches docs: Yes

**Automation Hints:**

- Check virtual: `page.getByRole('checkbox', { name: 'boolean-field-virtual' }).check()`
- Uncheck virtual: `page.getByRole('checkbox', { name: 'boolean-field-virtual' }).uncheck()`
- Assert Add Stock button visible: `expect(page.getByRole('button', { name: /add stock/i })).toBeVisible()`

---

## TC-UI-PA-005: Template attribute — Variants tab appears and variant creation is accessible

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/106/details`
- Part pk=106 (`Chair`) has `is_template=true`, `assembly=true`, 3 variants (pk=107 Red Chair, pk=108 Blue Chair, pk=109 Green Chair) — confirmed via API

**Steps:**

| #   | Action                                                                                      | Expected Result                                                                                                                          |
| --- | ------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/106/details`                               | Part detail page for `Chair` loads; page heading reads "**Part: Chair**"                                                                 |
| 2   | Observe the tab bar on the part detail page                                                 | A "**Variants**" tab is visible in the tab bar                                                                                           |
| 3   | Click the "Variants" tab                                                                    | The Variants tab content loads showing a table of variant parts                                                                          |
| 4   | Verify the variants table lists at least 3 variants                                         | The table contains rows for "Red Chair", "Blue Chair", and "Green Chair"                                                                 |
| 5   | Verify that a "New Variant" or equivalent button exists in the Variants tab toolbar          | A button to create a new variant is visible (e.g., "New Variant" or a "+" icon)                                                         |
| 6   | Click the "New Variant" button                                                              | A "Duplicate Part" or "Add Part" dialog opens pre-configured to create a variant of `Chair`                                              |
| 7   | Verify the dialog contains a "Variant Of" field or the form indicates this is a variant     | The form shows `Chair` as the template parent, or a "Variant Of" field is pre-filled with `Chair`                                        |
| 8   | Click "Cancel" to close the dialog without creating a variant                               | Dialog closes; no part is created                                                                                                        |
| 9   | Navigate to `https://demo.inventree.org/web/part/106/stock`                                 | The Stock tab loads for `Chair`                                                                                                          |
| 10  | Verify the stock total displayed includes variant stock                                     | The stock summary shows a total of **43** (or more) units, which is the aggregate of variant stocks (Red=25, Blue=0, Green=5, Blue=13)   |
| 11  | Look for a label or indicator distinguishing "own stock" from "variant stock" in the display | A "variant stock" label or section is visible; the template's own direct stock shows 0                                                   |

**Observed:**

- Part 106 (`Chair`): `is_template=true`, `assembly=true`, `in_stock=0`, `total_in_stock=43.0`, `variant_stock=43.0` — confirmed via `GET /api/part/106/`
- Variants confirmed via `GET /api/part/?variant_of=106`: 3 results (pk=107, 108, 109)
- Docs state: "Once a part is set as a template, a Variants tab appears on the part detail page"
- Docs state: "The 'stock' for a template part includes stock for all variants under that part"
- Matches docs: Yes

**Automation Hints:**

- Assert Variants tab: `expect(page.getByRole('tab', { name: /variants/i })).toBeVisible()`
- Click Variants tab: `page.getByRole('tab', { name: /variants/i }).click()`
- Assert variant rows: `expect(page.getByRole('row', { name: /Red Chair/ })).toBeVisible()`
- Assert stock aggregate: check that stock panel shows 43 total

---

## TC-UI-PA-006: Assembly attribute — BOM tab is accessible and editable

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/77/details`
- Part pk=77 (`Widget Assembly`) has `assembly=true`, `locked=false`, 276 BOM lines — confirmed via API

**Steps:**

| #   | Action                                                                                           | Expected Result                                                                                          |
| --- | ------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/77/details`                                     | Part detail page for `Widget Assembly` loads; page heading reads "**Part: Widget Assembly**"             |
| 2   | Observe the tab bar                                                                              | A "**BOM**" tab is visible in the tab bar                                                                |
| 3   | Click the "BOM" tab                                                                              | The BOM tab content loads; a table of BOM line items is shown with at least one row                     |
| 4   | Verify the BOM table contains at least one row                                                   | BOM rows are visible (276 BOM lines confirmed via API)                                                  |
| 5   | Verify that an "Add BOM Item" button or equivalent is **present and enabled** in the BOM toolbar | A "+" or "Add" button for adding BOM line items is visible and clickable                                 |
| 6   | Click the "Add BOM Item" button                                                                  | A dialog or inline form opens to add a new BOM line item                                                 |
| 7   | Verify the BOM add form contains a "Sub Part" field, a "Quantity" field, and a "Submit" button   | All three elements are present in the form                                                               |
| 8   | Click "Cancel" to close the form without submitting                                              | Form closes; BOM table is unchanged                                                                      |
| 9   | Hover over an existing BOM row to reveal row-level edit/delete actions                           | Edit (pencil) and Delete (trash) icons appear on the row                                                 |
| 10  | Verify that clicking the row edit icon opens an edit dialog                                      | An edit dialog for that BOM line item opens with current values populated                                |
| 11  | Click "Cancel" in the edit dialog                                                                | Dialog closes; no changes are made                                                                       |

**Observed:**

- Part 77 (`Widget Assembly`): `assembly=true`, `locked=false` — confirmed via `GET /api/part/77/`
- BOM line count: 276 — confirmed via `GET /api/bom/?assembly=77`
- Docs state: "Enabling Assembly unlocks the BOM tab on the part detail page, where BOM line items can be added, edited, and deleted."
- Matches docs: Yes

**Automation Hints:**

- Navigate to BOM tab: `page.goto('https://demo.inventree.org/web/part/77/bom')`
- Assert BOM tab visible: `expect(page.getByRole('tab', { name: /bom/i })).toBeVisible()`
- Assert add button enabled: `expect(page.getByRole('button', { name: /add.*bom/i })).toBeEnabled()`

---

## TC-UI-PA-007: Assembly + Locked — BOM tab is read-only (no add/edit/delete)

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/1934/details`
- Part pk=1934 (`AllFlagsPart-TC004-1776159658932-dk4zc`) has `assembly=true`, `locked=true` — confirmed via API

**Steps:**

| #   | Action                                                                                              | Expected Result                                                                                                                                 |
| --- | --------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/1934/details`                                      | Part detail page loads for `AllFlagsPart-TC004-1776159658932-dk4zc`; page heading contains the part name                                        |
| 2   | Observe the tab bar                                                                                 | A "**BOM**" tab is visible (assembly=true ensures the tab is present)                                                                           |
| 3   | Click the "BOM" tab                                                                                 | The BOM tab content loads                                                                                                                       |
| 4   | Verify that the "Add BOM Item" button or "+" icon is **absent** or **disabled** in the BOM toolbar  | The add BOM line action is not available — no clickable "Add" or "+" button is present in the toolbar                                           |
| 5   | Observe existing BOM rows (if any)                                                                  | Row-level edit (pencil icon) and delete (trash icon) actions are **absent** or **disabled** on all rows                                         |
| 6   | Verify a "Locked" indicator is visible somewhere on the page (header badge or attributes panel)     | A "**LOCKED**" badge or equivalent indicator is displayed                                                                                       |
| 7   | Click `action-menu-part-actions` → "Edit"                                                           | Edit dialog opens                                                                                                                               |
| 8   | Verify both "Assembly" checkbox (aria-label: `boolean-field-assembly`) and "Locked" checkbox (aria-label: `boolean-field-locked`) are **checked** | Both checkboxes are checked                                                                                               |
| 9   | Click "Cancel" to close the dialog without changes                                                  | Dialog closes                                                                                                                                   |

**Observed:**

- Part 1934: `assembly=true`, `locked=true`, all other attributes also `true` — confirmed via `GET /api/part/1934/`
- Docs state: "When the part is also Locked, BOM items cannot be created, edited, or deleted."
- Docs state: "Locked parts cannot be deleted."
- `boolean-field-locked` aria-label confirmed from TC-UI-PC-004 Observed block
- Matches docs: Yes

**Automation Hints:**

- Navigate to BOM: `page.goto('https://demo.inventree.org/web/part/1934/bom')`
- Assert no add button: `expect(page.getByRole('button', { name: /add.*bom/i })).not.toBeVisible()`
- Assert locked badge: `expect(page.getByText(/locked/i)).toBeVisible()`

---

## TC-UI-PA-008: Component attribute — part appears in BOM sub-part search for assemblies

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/77/bom`
- Part pk=77 (`Widget Assembly`) has `assembly=true`, `locked=false`
- A component part exists: any active part with `component=true` (e.g., pk=2879, `0603欧姆`, `component=true`)

**Steps:**

| #   | Action                                                                                                          | Expected Result                                                                                                    |
| --- | --------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ |
| 1   | Navigate to `https://demo.inventree.org/web/part/77/bom`                                                        | BOM tab for `Widget Assembly` loads                                                                                |
| 2   | Click the "Add BOM Item" or "+" button in the BOM toolbar                                                       | An "Add BOM Item" dialog opens                                                                                     |
| 3   | Click into the "Sub Part" search field in the dialog                                                            | The search field receives focus                                                                                    |
| 4   | Type `R_100K_0402` in the Sub Part search field (this is part pk=43, `component=true`)                          | A dropdown appears showing `R_100K_0402_1%` in the search results                                                  |
| 5   | Verify `R_100K_0402_1%` appears as a selectable option                                                          | The part is listed and selectable, confirming `component=true` parts appear in BOM sub-part search                 |
| 6   | Clear the Sub Part field and type `TC-UI-PA-004-VirtualOff` (or another part with `component=false`)            | If the part has `component=false`, it does **not** appear in the search results                                    |
| 7   | Click "Cancel" to close the dialog without submitting                                                           | Dialog closes; BOM is unchanged                                                                                    |

**Observed:**

- Part 43 (`R_100K_0402_1%`): `component=true`, `active=true`, `purchasable=true` — confirmed via `GET /api/part/43/`
- Docs state: "A part that is not flagged as a Component cannot be added to any BOM."
- Component checkbox default: `true` (confirmed from TC-UI-PC-001 defaults)
- Matches docs: Yes

**Automation Hints:**

- Open BOM add dialog: click Add BOM button
- Fill sub part search: `page.getByRole('textbox', { name: /sub.?part/i }).fill('R_100K_0402')`
- Assert option visible: `expect(page.getByRole('option', { name: /R_100K_0402_1%/ })).toBeVisible()`

---

## TC-UI-PA-009: Trackable attribute — stock creation requires batch or serial number

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/1217/details`
- Part pk=1217 (`AUTO_QA_TRACKABLE_PART`) has `trackable=true`, `in_stock=0` — confirmed via API

**Steps:**

| #   | Action                                                                                                             | Expected Result                                                                                                                     |
| --- | ------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/1217/stock`                                                       | Stock tab for `AUTO_QA_TRACKABLE_PART` loads; no existing stock items are shown                                                     |
| 2   | Click `action-menu-part-actions` → "Edit" to open the Edit dialog                                                  | Edit dialog opens                                                                                                                   |
| 3   | Verify the "Trackable" checkbox (aria-label: `boolean-field-trackable`) is **checked**                             | Trackable checkbox is checked                                                                                                       |
| 4   | Click "Cancel" to close without changes                                                                            | Dialog closes                                                                                                                       |
| 5   | In the Stock tab, click the "Add Stock" or "New Stock Item" button (if visible in the toolbar)                     | A "Add Stock Item" dialog opens                                                                                                     |
| 6   | Verify the dialog contains a "Serial Number" or "Batch Number" field marked as **required** (red asterisk)         | At least one of "Serial Number" or "Batch Number" is present in the form with a required indicator                                  |
| 7   | Attempt to submit the "Add Stock Item" form without filling in either Serial Number or Batch Number                | The form fails validation; an error message appears indicating a serial or batch number is required                                  |
| 8   | Fill in a valid serial number in the "Serial Number" field (e.g., type `SN-TC-PA-009-001`)                         | The serial number field accepts the value                                                                                           |
| 9   | Fill in a valid quantity (e.g., `1`) in the "Quantity" field                                                       | Quantity field shows `1`                                                                                                            |
| 10  | Click "Submit"                                                                                                     | A new stock item is created with serial number `SN-TC-PA-009-001`; the stock table updates to show 1 item                          |
| 11  | Verify the new stock item row shows the serial number `SN-TC-PA-009-001` in the table                              | The serial number is visible in the stock item row                                                                                  |

**Observed:**

- Part 1217 (`AUTO_QA_TRACKABLE_PART`): `trackable=true`, `in_stock=0`, `stock_item_count=0` — confirmed via `GET /api/part/1217/`
- `boolean-field-trackable` aria-label confirmed from TC-UI-PC-004 Observed block
- Docs state: "Any stock item associated with a trackable part must have either a batch number or a serial number."
- Docs state: "Serial Number Shorthand Notation" — `~` for next available, ranges like `1-5`
- Matches docs: Yes

**Notes:**

- This test creates a stock item on the demo server. If running repeatedly, clean up created stock items after the test.
- Serial number `SN-TC-PA-009-001` may conflict with existing items; use `~` (next available) instead if conflicts occur.

**Automation Hints:**

- Navigate to stock tab: `page.goto('https://demo.inventree.org/web/part/1217/stock')`
- Assert trackable checkbox checked: `expect(page.getByRole('checkbox', { name: 'boolean-field-trackable' })).toBeChecked()`
- Assert serial field required: `expect(page.getByLabel(/serial/i)).toHaveAttribute('required')` (or check for asterisk)
- Fill serial: `page.getByLabel(/serial/i).fill('SN-TC-PA-009-001')`

---

## TC-UI-PA-010: Purchaseable attribute — Suppliers tab is accessible and shows supplier parts

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/43/details`
- Part pk=43 (`R_100K_0402_1%`) has `purchasable=true` and 10 supplier parts linked — confirmed via API

**Steps:**

| #   | Action                                                                                               | Expected Result                                                                                                                                   |
| --- | ---------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/43/details`                                         | Part detail page for `R_100K_0402_1%` loads; page heading reads "**Part: R_100K_0402_1%**"                                                        |
| 2   | Observe the tab bar on the part detail page                                                          | A "**Suppliers**" tab (or "Purchase Orders") is visible in the tab bar                                                                            |
| 3   | Click the "Suppliers" tab                                                                            | The Suppliers tab content loads; a table of supplier parts is shown                                                                               |
| 4   | Verify the suppliers table lists at least one row                                                    | At least one supplier part row is visible (10 supplier parts confirmed via API, including SKU `RR05P100KDTR-ND`)                                   |
| 5   | Verify that an "Add Supplier Part" button or equivalent is present in the toolbar                    | An "Add Supplier Part" button is visible and enabled                                                                                              |
| 6   | Click `action-menu-part-actions` → "Edit" to open the Edit dialog                                   | Edit dialog opens                                                                                                                                 |
| 7   | Verify the "Purchaseable" checkbox (aria-label: `boolean-field-purchasable`) is **checked**         | Purchaseable checkbox is checked                                                                                                                  |
| 8   | Click "Cancel" to close without changes                                                              | Dialog closes                                                                                                                                     |
| 9   | Navigate to `https://demo.inventree.org/web/part/43/suppliers`                                       | Suppliers tab loads directly; the supplier parts table is still visible with the same data                                                        |

**Observed:**

- Part 43 (`R_100K_0402_1%`): `purchasable=true`, `active=true` — confirmed via `GET /api/part/43/`
- Supplier parts: 10 records — confirmed via `GET /api/company/part/?part=43`; SKUs: `RR05P100KDTR-ND`, `YAG1343TR-ND`, `P100KDCTR-ND` etc.
- `boolean-field-purchasable` aria-label confirmed from TC-UI-PC-004 Observed block
- Docs state: "Enabling Purchaseable allows supplier part records to be created and linked to this part."
- Matches docs: Yes

**Automation Hints:**

- Assert Suppliers tab visible: `expect(page.getByRole('tab', { name: /suppliers/i })).toBeVisible()`
- Click Suppliers tab: `page.getByRole('tab', { name: /suppliers/i }).click()`
- Assert supplier row: `expect(page.getByText('RR05P100KDTR-ND')).toBeVisible()`

---

## TC-UI-PA-011: Salable attribute — Sales Orders tab is accessible

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/108/details`
- Part pk=108 (`Blue Chair`) has `salable=true` and appears on sales order lines (SO line pk=1, 2) — confirmed via API

**Steps:**

| #   | Action                                                                                               | Expected Result                                                                                                                |
| --- | ---------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| 1   | Navigate to `https://demo.inventree.org/web/part/108/details`                                        | Part detail page for `Blue Chair` loads; page heading reads "**Part: Blue Chair**"                                             |
| 2   | Observe the tab bar                                                                                  | A "**Sales Orders**" tab is visible in the tab bar                                                                             |
| 3   | Click the "Sales Orders" tab                                                                         | The Sales Orders tab content loads; a table of sales order line items referencing this part is shown                           |
| 4   | Verify the table contains at least one sales order reference                                         | Sales order lines are visible (Blue Chair pk=108 confirmed on SO pk=1 and SO pk=2 via API)                                     |
| 5   | Click `action-menu-part-actions` → "Edit" to open the Edit dialog                                   | Edit dialog opens                                                                                                              |
| 6   | Verify the "Salable" checkbox (aria-label: `boolean-field-salable`) is **checked**                   | Salable checkbox is checked                                                                                                    |
| 7   | Click "Cancel" to close without changes                                                              | Dialog closes                                                                                                                  |
| 8   | Navigate to `https://demo.inventree.org/web/part/108/sales_orders`                                   | Sales Orders tab URL loads directly; the sales order lines table is still visible                                              |

**Observed:**

- Part 108 (`Blue Chair`): `salable=true`, `active=true`, `assembly=true` — confirmed via `GET /api/part/108/`
- Sales order lines: pk=1 (SO1, part=108), pk=2 (SO2, part=108) — confirmed via `GET /api/order/so-line/?limit=5`
- `boolean-field-salable` aria-label confirmed from TC-UI-PC-004 Observed block
- Docs state: "Enabling Salable allows this part to be added as a line item on sales orders."
- Matches docs: Yes

**Automation Hints:**

- Assert Sales Orders tab: `expect(page.getByRole('tab', { name: /sales.?orders/i })).toBeVisible()`
- Click Sales Orders tab: `page.getByRole('tab', { name: /sales.?orders/i }).click()`
- Assert SO lines present: `expect(page.getByRole('row').nth(1)).toBeVisible()` (at least one data row)

---

## TC-UI-PA-012: Testable attribute — Test Templates tab appears and lists templates

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/74/details`
- Part pk=74 (`Blue Widget`) has `testable=true` and 4 test templates — confirmed via API

**Steps:**

| #   | Action                                                                                                 | Expected Result                                                                                                                   |
| --- | ------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/74/details`                                           | Part detail page for `Blue Widget` loads; page heading reads "**Part: Blue Widget**"                                              |
| 2   | Observe the tab bar                                                                                    | A "**Test Templates**" tab is visible in the tab bar                                                                              |
| 3   | Click the "Test Templates" tab                                                                         | The Test Templates tab content loads; a table of test templates is shown                                                          |
| 4   | Verify the test templates table lists at least 3 templates                                             | At least 3 rows are visible, including "Blue Paint Applied", "Test A", and "Test B" (confirmed via API)                           |
| 5   | Verify that an "Add Test Template" button or equivalent is present in the toolbar                      | An "Add" button or "+" icon for test templates is visible                                                                         |
| 6   | Click `action-menu-part-actions` → "Edit" to open the Edit dialog                                     | Edit dialog opens                                                                                                                 |
| 7   | Verify the "Testable" checkbox (aria-label: `boolean-field-testable`) is **checked**                   | Testable checkbox is checked                                                                                                      |
| 8   | Click "Cancel" to close without changes                                                                | Dialog closes                                                                                                                     |
| 9   | Navigate to `https://demo.inventree.org/web/part/74/test-templates`                                    | Test Templates tab URL loads directly; the template table remains visible                                                         |

**Observed:**

- Part 74 (`Blue Widget`): `testable=true`, `trackable=true`, `is_template=true`, `salable=true`, `in_stock=145.0` — confirmed via `GET /api/part/74/`
- Test templates: 4 records — `GET /api/part/test-template/?part=74` → pk=8 "Blue Paint Applied", pk=5 "Test A", pk=6 "Test B"
- `boolean-field-testable` aria-label confirmed from TC-UI-PC-004 Observed block
- Docs state: "Enabling Testable unlocks the Test Templates tab on the part detail page."
- Matches docs: Yes

**Automation Hints:**

- Assert Test Templates tab: `expect(page.getByRole('tab', { name: /test.?templates/i })).toBeVisible()`
- Click tab: `page.getByRole('tab', { name: /test.?templates/i }).click()`
- Assert template row: `expect(page.getByText('Blue Paint Applied')).toBeVisible()`

---

## TC-UI-PA-013: Toggle all attributes ON via Edit form — verify persistence across page reload

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A fresh test part named `TC-UI-PA-013-AllFlags` is created with only the name filled (all defaults)
- The part is created at `https://demo.inventree.org/web/part/{new_pk}/details`

**Steps:**

| #   | Action                                                                                                                        | Expected Result                                                                                                                                                                          |
| --- | ----------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/{new_pk}/details`                                                            | Part detail page loads for `TC-UI-PA-013-AllFlags`                                                                                                                                       |
| 2   | Click `action-menu-part-actions` → "Edit"                                                                                     | Edit dialog opens                                                                                                                                                                        |
| 3   | Verify default states: Active=checked, Component=checked, Purchaseable=checked; Assembly, Is Template, Trackable, Testable, Salable, Virtual, Locked=unchecked | Default states match the documented defaults                                                                                                                        |
| 4   | Check the "Assembly" checkbox (aria-label: `boolean-field-assembly`)                                                          | Assembly checkbox becomes checked                                                                                                                                                        |
| 5   | Check the "Is Template" checkbox (aria-label: `boolean-field-is_template`)                                                    | Is Template checkbox becomes checked                                                                                                                                                     |
| 6   | Check the "Trackable" checkbox (aria-label: `boolean-field-trackable`)                                                        | Trackable checkbox becomes checked                                                                                                                                                       |
| 7   | Check the "Testable" checkbox (aria-label: `boolean-field-testable`)                                                          | Testable checkbox becomes checked                                                                                                                                                        |
| 8   | Check the "Salable" checkbox (aria-label: `boolean-field-salable`)                                                            | Salable checkbox becomes checked                                                                                                                                                         |
| 9   | Check the "Virtual" checkbox (aria-label: `boolean-field-virtual`)                                                            | Virtual checkbox becomes checked                                                                                                                                                         |
| 10  | Check the "Locked" checkbox (aria-label: `boolean-field-locked`)                                                              | Locked checkbox becomes checked                                                                                                                                                          |
| 11  | Click "Submit"                                                                                                                | Dialog closes; a success toast notification appears; page reloads                                                                                                                        |
| 12  | Verify a success toast notification is shown                                                                                  | A green "**Success**" / "**Item Updated**" toast is visible                                                                                                                              |
| 13  | Hard-reload the page (press F5 or navigate to the URL again)                                                                  | Page reloads from server                                                                                                                                                                 |
| 14  | Open the Edit dialog again via `action-menu-part-actions` → "Edit"                                                            | Edit dialog opens                                                                                                                                                                        |
| 15  | Verify all 10 attribute checkboxes reflect the saved state: Active, Component, Purchaseable, Assembly, Is Template, Trackable, Testable, Salable, Virtual, Locked are all **checked** | All 10 checkboxes are checked, confirming all attribute values persisted correctly                                                             |
| 16  | Verify the tab bar now shows additional tabs: BOM, Variants, Test Templates, Sales Orders                                     | Additional tabs that depend on attributes are now visible                                                                                                                                |

**Observed:**

- All boolean checkbox aria-labels confirmed from TC-UI-PC-004: `boolean-field-assembly`, `boolean-field-is_template`, `boolean-field-trackable`, `boolean-field-testable`, `boolean-field-salable`, `boolean-field-virtual`, `boolean-field-locked`, `boolean-field-active`, `boolean-field-component`, `boolean-field-purchasable`
- Part 1934 confirmed to have all attributes=true via `GET /api/part/1934/`, demonstrating this combination is valid
- Docs state: "Multiple flags can be enabled simultaneously on the same part"
- Matches docs: Yes

**Automation Hints:**

- Check all flags: iterate `page.getByRole('checkbox', { name: `boolean-field-${attr}` }).check()` for each attribute
- Assert toast: `expect(page.getByRole('alert')).toContainText('Item Updated')`
- After reload, assert all checked: check each `toBeChecked()`
- Assert BOM tab: `expect(page.getByRole('tab', { name: /bom/i })).toBeVisible()`

---

## TC-UI-PA-014: Assembly + Component combination — part is both buildable and usable in other assemblies

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A fresh test part named `TC-UI-PA-014-SubAssembly` is created with `assembly=true` and `component=true`
- Both checkboxes are checked before clicking Submit in the "Add Part" dialog

**Steps:**

| #   | Action                                                                                                               | Expected Result                                                                                                              |
| --- | -------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| 1   | Create a new part via `action-menu-add-parts` → "Create Part"                                                        | "Add Part" dialog opens                                                                                                      |
| 2   | Type `TC-UI-PA-014-SubAssembly` in the "Name" field (aria-label: `text-field-name`)                                  | Name field updated                                                                                                           |
| 3   | Verify "Component" checkbox (aria-label: `boolean-field-component`) is already **checked** (default)                 | Component is checked                                                                                                         |
| 4   | Check the "Assembly" checkbox (aria-label: `boolean-field-assembly`)                                                 | Assembly checkbox becomes checked; both Assembly and Component are now checked                                               |
| 5   | Click "Submit"                                                                                                       | Dialog closes; navigates to new part detail page at `https://demo.inventree.org/web/part/{new_pk}/details`                   |
| 6   | Verify a success toast appears with "**Item Created**"                                                               | Toast is visible                                                                                                             |
| 7   | Observe the tab bar                                                                                                  | A "**BOM**" tab is visible (assembly=true enables it)                                                                        |
| 8   | Navigate to `https://demo.inventree.org/web/part/77/bom` (Widget Assembly)                                           | BOM tab for `Widget Assembly` loads                                                                                          |
| 9   | Click the "Add BOM Item" button                                                                                      | Add BOM Item dialog opens                                                                                                    |
| 10  | Search for `TC-UI-PA-014-SubAssembly` in the "Sub Part" search field                                                 | The newly created sub-assembly part appears in the search results (component=true makes it eligible as a BOM sub-part)       |
| 11  | Click "Cancel" to close the dialog without submitting                                                                | Dialog closes; no BOM change is made                                                                                         |

**Observed:**

- Docs state: "There is no restriction preventing the same part from being both an Assembly and a Component simultaneously."
- Component default = `true` confirmed from TC-UI-PC-001 Observed block
- Part 77 assembly BOM is editable (locked=false) — confirmed via API
- Matches docs: Yes

**Automation Hints:**

- Assert BOM tab on new part: `expect(page.getByRole('tab', { name: /bom/i })).toBeVisible()`
- In Widget Assembly BOM add dialog, search for new part by name and assert it appears in dropdown

---

## TC-UI-PA-015: Purchaseable = false — Suppliers tab is absent

**Priority:** P2
**Type:** Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A test part exists with `purchasable=false`; create fresh via "Create Part" with "Purchaseable" unchecked
- Part named `TC-UI-PA-015-NotPurchaseable` with all defaults except Purchaseable=unchecked

**Steps:**

| #   | Action                                                                                                              | Expected Result                                                                                                 |
| --- | ------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| 1   | Create a new part via `action-menu-add-parts` → "Create Part"                                                       | "Add Part" dialog opens                                                                                         |
| 2   | Type `TC-UI-PA-015-NotPurchaseable` in the "Name" field (aria-label: `text-field-name`)                             | Name field updated                                                                                              |
| 3   | Click the "Purchaseable" checkbox (aria-label: `boolean-field-purchasable`) to **uncheck** it                      | Purchaseable checkbox becomes unchecked                                                                         |
| 4   | Click "Submit"                                                                                                      | Dialog closes; navigates to new part detail page                                                                |
| 5   | Observe the tab bar on the part detail page                                                                         | A "**Suppliers**" tab is **absent** from the tab bar                                                            |
| 6   | Observe the tab bar for a "Purchase Orders" tab                                                                     | A "**Purchase Orders**" tab is also **absent** from the tab bar                                                 |
| 7   | Click `action-menu-part-actions` → "Edit"                                                                           | Edit dialog opens                                                                                               |
| 8   | Verify "Purchaseable" checkbox (aria-label: `boolean-field-purchasable`) is **unchecked**                          | Purchaseable is unchecked                                                                                       |
| 9   | Check the "Purchaseable" checkbox to enable it and click "Submit"                                                   | Dialog closes; page reloads                                                                                     |
| 10  | Verify the "Suppliers" tab now **appears** in the tab bar                                                           | Suppliers tab is now visible after enabling Purchaseable                                                        |

**Observed:**

- Purchaseable default = `true` in the Add Part dialog (confirmed from TC-UI-PC-001 Observed block) — must be manually unchecked
- Docs state: "Without this flag, the part cannot appear on a purchase order." (implies Suppliers tab is also gated)
- Matching behavior: part 108 (`Blue Chair`, `purchasable=false`) — Suppliers tab should not be visible for it
- Matches docs: Yes

**Automation Hints:**

- Uncheck purchasable: `page.getByRole('checkbox', { name: 'boolean-field-purchasable' }).uncheck()`
- Assert Suppliers tab absent: `expect(page.getByRole('tab', { name: /suppliers/i })).not.toBeVisible()`
- After re-enabling: `expect(page.getByRole('tab', { name: /suppliers/i })).toBeVisible()`

---

## TC-UI-PA-016: Salable = false — Sales Orders tab is absent

**Priority:** P2
**Type:** Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/43/details`
- Part pk=43 (`R_100K_0402_1%`) has `salable=false` — confirmed via API

**Steps:**

| #   | Action                                                                                      | Expected Result                                                                                      |
| --- | ------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/43/details`                                | Part detail page for `R_100K_0402_1%` loads                                                          |
| 2   | Observe the tab bar on the part detail page                                                 | A "**Sales Orders**" tab is **absent** from the tab bar                                              |
| 3   | Click `action-menu-part-actions` → "Edit"                                                   | Edit dialog opens                                                                                    |
| 4   | Verify the "Salable" checkbox (aria-label: `boolean-field-salable`) is **unchecked**        | Salable checkbox is unchecked                                                                        |
| 5   | Check the "Salable" checkbox and click "Submit"                                             | Dialog closes; page reloads                                                                          |
| 6   | Verify the "Sales Orders" tab now **appears** in the tab bar                                | Sales Orders tab is now visible                                                                      |
| 7   | Click `action-menu-part-actions` → "Edit" again                                             | Edit dialog opens                                                                                    |
| 8   | Uncheck the "Salable" checkbox and click "Submit"                                           | Dialog closes; Sales Orders tab disappears from the tab bar                                          |

**Observed:**

- Part 43 (`R_100K_0402_1%`): `salable=false` — confirmed via `GET /api/part/43/`
- `boolean-field-salable` aria-label confirmed from TC-UI-PC-004 Observed block
- Docs state: "Without this flag, the part cannot appear on a sales order." (tab gated by flag)
- Matches docs: Yes

**Automation Hints:**

- Assert Sales Orders tab absent: `expect(page.getByRole('tab', { name: /sales.?orders/i })).not.toBeVisible()`
- After enabling salable and reload: `expect(page.getByRole('tab', { name: /sales.?orders/i })).toBeVisible()`

---

## TC-UI-PA-017: Template attribute = false — Variants tab is absent

**Priority:** P2
**Type:** Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/43/details`
- Part pk=43 (`R_100K_0402_1%`) has `is_template=false` — confirmed via API

**Steps:**

| #   | Action                                                                              | Expected Result                                                             |
| --- | ----------------------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/43/details`                        | Part detail page for `R_100K_0402_1%` loads                                 |
| 2   | Observe the tab bar                                                                 | A "**Variants**" tab is **absent** from the tab bar                         |
| 3   | Click `action-menu-part-actions` → "Edit"                                           | Edit dialog opens                                                           |
| 4   | Verify "Is Template" checkbox (aria-label: `boolean-field-is_template`) is unchecked | Is Template is unchecked                                                    |
| 5   | Check "Is Template" and click "Submit"                                              | Dialog closes; page reloads                                                 |
| 6   | Verify a "**Variants**" tab now appears in the tab bar                              | Variants tab is visible                                                     |
| 7   | Click `action-menu-part-actions` → "Edit"                                           | Edit dialog opens                                                           |
| 8   | Uncheck "Is Template" and click "Submit"                                            | Dialog closes; Variants tab disappears from the tab bar                     |

**Observed:**

- Part 43 (`R_100K_0402_1%`): `is_template=false` — confirmed via `GET /api/part/43/`
- `boolean-field-is_template` aria-label confirmed from TC-UI-PC-004 Observed block
- Docs state: "Once a part is set as a template, a Variants tab appears on the part detail page."
- Matches docs: Yes

**Automation Hints:**

- Assert Variants tab absent: `expect(page.getByRole('tab', { name: /variants/i })).not.toBeVisible()`
- After enabling is_template: `expect(page.getByRole('tab', { name: /variants/i })).toBeVisible()`

---

## TC-UI-PA-018: Boundary — conflicting Virtual + Trackable combination set simultaneously

**Priority:** P3
**Type:** Exploratory / Boundary

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Part 1934 (`AllFlagsPart-TC004-1776159658932-dk4zc`) has both `virtual=true` AND `trackable=true` — confirmed via API
- This is a boundary test: virtual parts cannot have stock, yet trackable requires stock items to carry serial/batch numbers

**Steps:**

| #   | Action                                                                                                                          | Expected Result                                                                                                                                                            |
| --- | ------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/1934/details`                                                                  | Part detail page loads for `AllFlagsPart-TC004-1776159658932-dk4zc`                                                                                                        |
| 2   | Click `action-menu-part-actions` → "Edit"                                                                                       | Edit dialog opens                                                                                                                                                          |
| 3   | Verify both "Virtual" (aria-label: `boolean-field-virtual`) and "Trackable" (aria-label: `boolean-field-trackable`) are checked | Both checkboxes are checked simultaneously — the system allows this combination without a UI-level error                                                                   |
| 4   | Click "Cancel" to close without changes                                                                                         | Dialog closes                                                                                                                                                              |
| 5   | Navigate to the Stock tab at `https://demo.inventree.org/web/part/1934/stock`                                                   | The Stock tab loads; because `virtual=true`, stock actions (Add Stock) are **absent** or **disabled** — the Trackable constraint is effectively moot for this part          |
| 6   | Verify no "Add Stock" button is present (virtual overrides trackable stock creation)                                            | No "Add Stock" or "New Stock Item" button is visible                                                                                                                       |
| 7   | Create a fresh part with only `virtual=true` and `trackable=true` both enabled                                                  | Part is created without a UI validation error rejecting the combination                                                                                                    |
| 8   | Navigate to the Stock tab of the new part                                                                                       | Stock tab shows no stock items and no ability to add stock (virtual blocks stock creation despite trackable being set)                                                     |
| 9   | Document whether the UI shows any warning about the conflicting combination                                                     | Note whether any warning badge or tooltip appears indicating the Virtual+Trackable combination has no practical effect on stock tracking                                    |

**Observed:**

- Part 1934 has `virtual=true` AND `trackable=true` simultaneously — confirmed via `GET /api/part/1934/`
- API allows this combination without returning a validation error
- Docs do not explicitly prohibit `virtual+trackable` together; they note virtual parts "cannot have stock items"
- Logical conflict: trackable requires serial/batch on stock items, but virtual has no stock items — Trackable is effectively inert when Virtual=true
- Matches docs: Yes (no documented constraint prohibiting the combination)

**Notes:**

- This is an exploratory test — document actual UI behavior on first execution, especially any warning messaging
- The expected result for step 9 is not defined by documentation; tester should record what is actually shown

**Automation Hints:**

- Navigate: `page.goto('https://demo.inventree.org/web/part/1934/stock')`
- Assert no add stock button: `expect(page.getByRole('button', { name: /add stock/i })).not.toBeVisible()`
- Assert both checkboxes checked in edit dialog

---

## TC-UI-PA-019: Boundary — Locked attribute prevents part deletion

**Priority:** P3
**Type:** Negative / Boundary

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/1934/details`
- Part pk=1934 has `locked=true` — confirmed via API

**Steps:**

| #   | Action                                                                                              | Expected Result                                                                                                                 |
| --- | --------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/1934/details`                                      | Part detail page loads; a "**LOCKED**" indicator is visible in the header or attributes panel                                   |
| 2   | Click `action-menu-part-actions` to open the dropdown menu                                          | Dropdown opens with "Duplicate", "Edit", "Delete" items visible                                                                 |
| 3   | Click the "Delete" menu item (aria-label: `action-menu-part-actions-delete`)                         | A confirmation dialog or error response appears                                                                                 |
| 4   | Observe the dialog or response                                                                      | Either: (a) a delete confirmation dialog opens and on confirmation the server returns an error stating the part is locked, OR (b) the Delete menu item itself is **disabled** for locked parts |
| 5   | If a confirmation dialog appeared, click "Confirm" or "Delete"                                      | The server rejects the deletion and shows an error message indicating the part cannot be deleted because it is locked            |
| 6   | Verify the part still exists after the attempted deletion                                           | Navigate back to `https://demo.inventree.org/web/part/1934/details`; the page loads successfully                                |

**Observed:**

- Part 1934: `locked=true` — confirmed via `GET /api/part/1934/`
- Docs state: "Locked parts cannot be deleted."
- `action-menu-part-actions-delete` aria-label confirmed from TC-UI-PC-005 Observed block
- Matches docs: Yes (deletion should be blocked)

**Notes:**

- Step 4 is exploratory: document whether the UI blocks deletion before the server call (disabled button) or after (server error)

**Automation Hints:**

- Assert locked badge: `expect(page.getByText(/locked/i)).toBeVisible()`
- Click delete: `page.getByRole('menuitem', { name: 'action-menu-part-actions-delete' }).click()`
- Assert error or disabled: check for error toast `expect(page.getByRole('alert')).toContainText(/locked/)` or `toBeDisabled()`

---

## TC-UI-PA-020: Boundary — Toggling Component OFF on a part already used in a BOM

**Priority:** P3
**Type:** Exploratory / Boundary

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A part that is currently used as a BOM sub-part in an assembly is needed
- Part pk=72 is confirmed as a sub-part in the BOM of `Widget Assembly` (pk=77) via `GET /api/bom/?assembly=77`
- Confirm part 72 has `component=true` via `GET /api/part/72/`

**Steps:**

| #   | Action                                                                                                             | Expected Result                                                                                                                                                                     |
| --- | ------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/api/part/72/` to confirm `component=true` before proceeding               | API response confirms `component=true`                                                                                                                                              |
| 2   | Navigate to `https://demo.inventree.org/web/part/72/details`                                                       | Part detail page loads for part 72                                                                                                                                                  |
| 3   | Click `action-menu-part-actions` → "Edit"                                                                          | Edit dialog opens                                                                                                                                                                   |
| 4   | Verify "Component" checkbox (aria-label: `boolean-field-component`) is **checked**                                 | Component is checked                                                                                                                                                                |
| 5   | **Uncheck** the "Component" checkbox (aria-label: `boolean-field-component`)                                       | Component checkbox becomes unchecked                                                                                                                                                |
| 6   | Click "Submit"                                                                                                     | Observe whether: (a) the server accepts the change silently, (b) a warning is shown that this part is in use in BOMs, or (c) the server rejects the change with a validation error  |
| 7   | If the change was accepted, navigate to `https://demo.inventree.org/web/part/77/bom`                               | BOM tab for `Widget Assembly` loads                                                                                                                                                 |
| 8   | Verify whether part 72 is still visible in the BOM table after `component=false` was set                           | The existing BOM line referencing part 72 is either still present (attribute change does not retroactively remove BOM lines) or has been removed                                    |
| 9   | Re-open the Edit dialog for part 72 and **re-check** "Component" and click "Submit"                                | Component is restored to `true`; no BOM data loss occurred                                                                                                                         |
| 10  | Verify the BOM for `Widget Assembly` is unchanged after restoring Component=true                                   | BOM table shows the same number of rows as before the test                                                                                                                          |

**Observed:**

- BOM for part 77 has 276 lines; sub-part 72 confirmed at BOM pk=1 — via `GET /api/bom/?assembly=77`
- Docs state: "A part that is not flagged as a Component cannot be added to any BOM." — but does not specify whether existing BOM lines are affected retroactively
- This is an exploratory test; document actual server behavior in step 6
- Matches docs: Partial (docs cover forward behavior but not retroactive impact)

**Notes:**

- **Caution:** This test temporarily modifies part 72 on the shared demo server. Restore `component=true` in step 9 regardless of test outcome.
- If step 6 returns a server error, document the error message and skip steps 7–8.

**Automation Hints:**

- Uncheck component: `page.getByRole('checkbox', { name: 'boolean-field-component' }).uncheck()`
- Submit and check for error toast: `expect(page.getByRole('alert')).toBeVisible()`
- Verify BOM line still present: navigate to widget assembly BOM, check row count

---

## Suite Summary

| TC ID        | Title                                                                 | Type                      | Priority |
| ------------ | --------------------------------------------------------------------- | ------------------------- | -------- |
| TC-UI-PA-001 | Toggle Active attribute OFF — part becomes inactive                   | UI / Functional           | P1       |
| TC-UI-PA-002 | Inactive part excluded from BOM, PO, SO, and Build Order selections   | UI / Functional / Negative| P1       |
| TC-UI-PA-003 | Toggle Virtual ON — stock UI elements are hidden                      | UI / Functional           | P1       |
| TC-UI-PA-004 | Toggle Virtual OFF — stock actions become available                   | UI / Functional           | P2       |
| TC-UI-PA-005 | Template attribute — Variants tab appears; stock aggregation visible  | UI / Functional           | P1       |
| TC-UI-PA-006 | Assembly attribute — BOM tab accessible and editable                  | UI / Functional           | P1       |
| TC-UI-PA-007 | Assembly + Locked — BOM tab is read-only                              | UI / Functional           | P1       |
| TC-UI-PA-008 | Component attribute — part appears in BOM sub-part search             | UI / Functional           | P2       |
| TC-UI-PA-009 | Trackable attribute — stock creation requires batch or serial number  | UI / Functional           | P1       |
| TC-UI-PA-010 | Purchaseable attribute — Suppliers tab accessible with supplier parts | UI / Functional           | P1       |
| TC-UI-PA-011 | Salable attribute — Sales Orders tab is accessible                    | UI / Functional           | P1       |
| TC-UI-PA-012 | Testable attribute — Test Templates tab appears with templates        | UI / Functional           | P2       |
| TC-UI-PA-013 | Toggle all attributes ON — verify full persistence across page reload | UI / Functional           | P2       |
| TC-UI-PA-014 | Assembly + Component — part is buildable and usable in other BOMs     | UI / Functional           | P2       |
| TC-UI-PA-015 | Purchaseable = false — Suppliers tab is absent                        | UI / Negative             | P2       |
| TC-UI-PA-016 | Salable = false — Sales Orders tab is absent                          | UI / Negative             | P2       |
| TC-UI-PA-017 | Template = false — Variants tab is absent                             | UI / Negative             | P2       |
| TC-UI-PA-018 | Boundary — Virtual + Trackable combination set simultaneously         | UI / Exploratory/Boundary | P3       |
| TC-UI-PA-019 | Boundary — Locked attribute prevents part deletion                    | UI / Negative/Boundary    | P3       |
| TC-UI-PA-020 | Boundary — Toggling Component OFF on a part already used in a BOM     | UI / Exploratory/Boundary | P3       |
