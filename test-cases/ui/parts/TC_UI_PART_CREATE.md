# TC_UI_PART_CREATE — Part Creation UI Test Suite

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
| **Observed**         | Data confirmed from live UI exploration       |
| **Automation Hints** | Selectors and assertions for automation       |

---

## TC-UI-PC-001: Create part via manual entry — required fields only (happy path)

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The "Parts" tab in the left panel is selected

**Steps:**

| #   | Action                                                                              | Expected Result                                                                                                                                         |
| --- | ----------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Click the `action-menu-add-parts` button (green "+" icon in the table toolbar)      | A dropdown menu appears with two items: "Create Part" and "Import from File"                                                                            |
| 2   | Click the "Create Part" menu item                                                   | The "Add Part" modal dialog opens                                                                                                                       |
| 3   | Verify the dialog title                                                             | Dialog heading reads "**Add Part**"                                                                                                                     |
| 4   | Verify that the "Name" field is marked as required                                  | The "Name" label shows a red asterisk ("Name \*")                                                                                                       |
| 5   | Verify that "Category" field placeholder text reads "Search..."                     | The Category combobox placeholder is "Search..."                                                                                                        |
| 6   | Type `TC-UI-PC-001-MinimalPart` in the "Name" field (aria-label: `text-field-name`) | The field value updates to "TC-UI-PC-001-MinimalPart"                                                                                                   |
| 7   | Leave all other fields at their default values                                      | IPN, Description, Revision fields are empty; Component, Purchaseable, Active, Copy Category Parameters checkboxes are checked; all others are unchecked |
| 8   | Click the "Submit" button                                                           | The dialog closes; the browser navigates to the new part detail page                                                                                    |
| 9   | Verify the page URL                                                                 | URL matches pattern `https://demo.inventree.org/web/part/{id}/details`                                                                                  |
| 10  | Verify the page heading                                                             | Heading reads "**Part: TC-UI-PC-001-MinimalPart**"                                                                                                      |
| 11  | Verify the success notification                                                     | A green toast notification appears with heading "**Success**" and body "**Item Created**"                                                               |
| 12  | Verify the stock badge                                                              | An orange "**NO STOCK**" badge is visible in the part header                                                                                            |

**Observed:**

- Page title seen: "InvenTree Demo Server | Parts"
- "Add Part" dialog confirmed present and opened via `action-menu-add-parts` → "Create Part"
- Name field aria-label: `text-field-name`; type: text; required marker: red asterisk on label
- Default checked booleans confirmed: `boolean-field-component` (true), `boolean-field-purchaseable` (true), `boolean-field-active` (true), `boolean-field-copy_category_parameters` (true)
- On submit with name only: redirected to `/web/part/1375/details`; toast "Success — Item Created" confirmed visible
- Matches docs: Yes (name is the only user-required field)

**Automation Hints:**

- Click add menu: `page.getByRole('button', { name: 'action-menu-add-parts' }).click()`
- Click create: `page.getByRole('menuitem', { name: 'action-menu-add-parts-create-part' }).click()`
- Fill name: `page.getByRole('textbox', { name: 'text-field-name' }).fill('TC-UI-PC-001-MinimalPart')`
- Submit: `page.getByRole('button', { name: 'Submit' }).click()`
- Assert URL: `expect(page).toHaveURL(/\/web\/part\/\d+\/details/)`
- Assert heading: `expect(page.getByRole('heading')).toContainText('TC-UI-PC-001-MinimalPart')`
- Assert toast: `expect(page.getByRole('alert')).toContainText('Item Created')`

---

## TC-UI-PC-002: Create part with all optional text fields populated

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- "Parts" tab selected

**Steps:**

| #   | Action                                                                                                           | Expected Result                                       |
| --- | ---------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------- |
| 1   | Click `action-menu-add-parts` button                                                                             | Dropdown opens                                        |
| 2   | Click "Create Part"                                                                                              | "Add Part" dialog opens                               |
| 3   | Type `TC-UI-PC-002-FullFieldsPart` in the "Name" field (aria-label: `text-field-name`)                           | Name field shows the value                            |
| 4   | Type `IPN-TC002` in the "IPN" field (aria-label: `text-field-IPN`)                                               | IPN field shows "IPN-TC002"                           |
| 5   | Type `A detailed description for TC-UI-PC-002` in the "Description" field (aria-label: `text-field-description`) | Description field shows the text                      |
| 6   | Type `Rev-A` in the "Revision" field (aria-label: `text-field-revision`)                                         | Revision field shows "Rev-A"                          |
| 7   | Type `resistor capacitor electronics` in the "Keywords" field (aria-label: `text-field-keywords`)                | Keywords field shows the value                        |
| 8   | Type `pcs` in the "Units" field (aria-label: `text-field-units`)                                                 | Units field shows "pcs"                               |
| 9   | Type `https://example.com/datasheet.pdf` in the "Link" field (aria-label: `text-field-link`)                     | Link field shows the URL                              |
| 10  | Type `30` in the "Default Expiry" field (aria-label: `number-field-default_expiry`)                              | Field shows "30"                                      |
| 11  | Type `10` in the "Minimum Stock" field (aria-label: `number-field-minimum_stock`)                                | Field shows "10"                                      |
| 12  | Click "Submit"                                                                                                   | Dialog closes; navigates to new part detail page      |
| 13  | Verify page heading                                                                                              | Heading reads "**Part: TC-UI-PC-002-FullFieldsPart**" |
| 14  | Click the "Part Details" tab in the left panel                                                                   | Part Details content loads                            |
| 15  | Verify that the IPN "IPN-TC002" is displayed in the Part Details panel                                           | IPN value is visible                                  |
| 16  | Verify the description "A detailed description for TC-UI-PC-002" is displayed                                    | Description is visible                                |

**Observed:**

- All text field aria-labels confirmed from JS extraction of dialog DOM
- Fields confirmed: `text-field-IPN`, `text-field-description`, `text-field-revision`, `text-field-keywords`, `text-field-units`, `text-field-link`, `number-field-default_expiry` (default 0), `number-field-minimum_stock` (default 0)
- Matches docs: Yes

**Automation Hints:**

- Fill each field using `page.getByRole('textbox', { name: '{aria-label}' }).fill('{value}')`
- Assert heading: `expect(page.getByText('Part: TC-UI-PC-002-FullFieldsPart')).toBeVisible()`

---

## TC-UI-PC-003: Create part assigned to a specific category

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- At least one part category exists (confirmed: "Connectors" category visible in parts list)

**Steps:**

| #   | Action                                                                                        | Expected Result                                                           |
| --- | --------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------- |
| 1   | Click `action-menu-add-parts` → "Create Part"                                                 | "Add Part" dialog opens                                                   |
| 2   | Click the "Category" combobox (aria-label: `related-field-category`, placeholder "Search...") | The combobox receives focus and a dropdown or search input becomes active |
| 3   | Type `Electronics` in the Category search field                                               | A dropdown list appears showing matching category names                   |
| 4   | Select the first matching category from the dropdown                                          | The Category field shows the selected category name                       |
| 5   | Type `TC-UI-PC-003-CatPart` in the "Name" field                                               | Name field updates                                                        |
| 6   | Click "Submit"                                                                                | Dialog closes; navigates to new part detail page                          |
| 7   | Verify the page heading                                                                       | Heading reads "**Part: TC-UI-PC-003-CatPart**"                            |
| 8   | Verify the category breadcrumb or detail shows the assigned category                          | The selected category name is visible in the part details                 |

**Observed:**

- Category field confirmed as combobox with `related-field-category` aria-label
- Placeholder text: "Search..."
- Demo has categories including "Connectors" (confirmed in parts table column)
- Matches docs: Yes (category is optional FK field)

**Automation Hints:**

- Click category: `page.getByRole('textbox', { name: 'related-field-category' }).click()`
- Type to search: `page.getByRole('textbox', { name: 'related-field-category' }).fill('Electronics')`
- Select option: `page.getByRole('option').first().click()`
- Assert category in detail: check for category name in the part detail panel

---

## TC-UI-PC-004: Boolean attribute toggles in the "Add Part" form

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin`
- "Add Part" dialog is open (via `action-menu-add-parts` → "Create Part")

**Steps:**

| #   | Action                                                                                                       | Expected Result                                                                                                                                                                |
| --- | ------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 1   | Open the "Add Part" dialog                                                                                   | Dialog is visible                                                                                                                                                              |
| 2   | Verify the default state of all boolean checkboxes                                                           | Component: checked; Purchaseable: checked; Active: checked; Copy Category Parameters: checked; Assembly, Is Template, Testable, Trackable, Salable, Virtual, Locked: unchecked |
| 3   | Click the "Assembly" checkbox (aria-label: `boolean-field-assembly`)                                         | Assembly checkbox becomes checked                                                                                                                                              |
| 4   | Click the "Is Template" checkbox (aria-label: `boolean-field-is_template`)                                   | Is Template checkbox becomes checked                                                                                                                                           |
| 5   | Click the "Testable" checkbox (aria-label: `boolean-field-testable`)                                         | Testable checkbox becomes checked                                                                                                                                              |
| 6   | Click the "Trackable" checkbox (aria-label: `boolean-field-trackable`)                                       | Trackable checkbox becomes checked                                                                                                                                             |
| 7   | Click the "Salable" checkbox (aria-label: `boolean-field-salable`)                                           | Salable checkbox becomes checked                                                                                                                                               |
| 8   | Click the "Virtual" checkbox (aria-label: `boolean-field-virtual`)                                           | Virtual checkbox becomes checked                                                                                                                                               |
| 9   | Click the "Component" checkbox (aria-label: `boolean-field-component`) to uncheck it                         | Component checkbox becomes unchecked                                                                                                                                           |
| 10  | Type `TC-UI-PC-004-BoolPart` in the "Name" field                                                             | Name field updates                                                                                                                                                             |
| 11  | Click "Submit"                                                                                               | Dialog closes; navigates to new part detail page                                                                                                                               |
| 12  | Verify the page heading                                                                                      | Heading reads "**Part: TC-UI-PC-004-BoolPart**"                                                                                                                                |
| 13  | Click `action-menu-part-actions` → "Edit" to re-open the edit form                                           | Edit form opens                                                                                                                                                                |
| 14  | Verify that Assembly, Is Template, Testable, Trackable, Salable, Virtual are checked; Component is unchecked | Attribute states match what was set at creation                                                                                                                                |

**Observed:**

- All boolean checkboxes confirmed present in dialog via JS extraction
- Confirmed defaults: component=true, purchaseable=true, active=true, copy_category_parameters=true; all others=false
- Labels confirmed from `querySelectorAll('label')`: "Component", "Assembly", "Is Template", "Testable", "Trackable", "Purchaseable", "Salable", "Virtual", "Locked", "Active"
- Part actions menu confirmed: "Duplicate", "Edit", "Delete" items with aria-labels `action-menu-part-actions-duplicate`, `action-menu-part-actions-edit`, `action-menu-part-actions-delete`
- Matches docs: Yes

**Automation Hints:**

- Check Assembly: `page.getByRole('checkbox', { name: 'boolean-field-assembly' }).check()`
- Uncheck Component: `page.getByRole('checkbox', { name: 'boolean-field-component' }).uncheck()`
- Assert checked state: `expect(page.getByRole('checkbox', { name: 'boolean-field-assembly' })).toBeChecked()`

---

## TC-UI-PC-005: Duplicate an existing part

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin`
- An existing part is available (e.g., part created in TC-UI-PC-001 at `/web/part/1375/details`, or any other valid part)
- Navigate to the part detail page: `https://demo.inventree.org/web/part/{id}/details`

**Steps:**

| #   | Action                                                                               | Expected Result                                                                          |
| --- | ------------------------------------------------------------------------------------ | ---------------------------------------------------------------------------------------- |
| 1   | Click the `action-menu-part-actions` button (three-dot menu icon in the part header) | A dropdown menu opens with three items: "Duplicate", "Edit", "Delete"                    |
| 2   | Click the "Duplicate" menu item (aria-label: `action-menu-part-actions-duplicate`)   | The "Add Part" dialog opens with the Name field pre-filled with the original part's name |
| 3   | Verify the dialog title                                                              | Dialog heading reads "**Add Part**"                                                      |
| 4   | Verify the Name field is pre-filled                                                  | The "Name" field (aria-label: `text-field-name`) contains the original part's name       |
| 5   | Verify additional copy checkboxes are present at the bottom of the form              | Three checkboxes visible: "Copy Image", "Copy Notes", "Copy Parameters"                  |
| 6   | Clear the "Name" field and type `TC-UI-PC-005-DuplicatePart`                         | Name field shows the new value                                                           |
| 7   | Check the "Copy Notes" checkbox if not already checked                               | Copy Notes is checked                                                                    |
| 8   | Check the "Copy Parameters" checkbox                                                 | Copy Parameters is checked                                                               |
| 9   | Click "Submit"                                                                       | Dialog closes; navigates to the new duplicate part's detail page                         |
| 10  | Verify the page heading                                                              | Heading reads "**Part: TC-UI-PC-005-DuplicatePart**"                                     |
| 11  | Verify the success notification                                                      | Green toast shows "**Success**" / "**Item Created**"                                     |
| 12  | Verify the new part URL is different from the original                               | URL contains a different part ID than the source part                                    |

**Observed:**

- Part actions menu confirmed with three items: "Duplicate" (`action-menu-part-actions-duplicate`), "Edit" (`action-menu-part-actions-edit`), "Delete" (`action-menu-part-actions-delete`)
- Duplicate dialog title: "Add Part" (same as Create dialog)
- Name field pre-filled with original name confirmed via screenshot
- Additional checkboxes confirmed from JS extraction: "Copy Image" (copy image from original part), "Copy Notes" (copy notes from original part), "Copy Parameters" (copy parameter data from original part)
- Note: "Copy BOM" and "Copy Tests" mentioned in API docs are NOT present in the UI form
- Matches docs: Partially (copy_bom and copy_tests not surfaced in UI)

**Automation Hints:**

- Click part actions: `page.getByRole('button', { name: 'action-menu-part-actions' }).click()`
- Click duplicate: `page.getByRole('menuitem', { name: 'action-menu-part-actions-duplicate' }).click()`
- Assert name pre-filled: `expect(page.getByRole('textbox', { name: 'text-field-name' })).toHaveValue('{original_name}')`
- Assert copy checkboxes present: `expect(page.getByRole('checkbox', { name: 'boolean-field-copy_image' })).toBeVisible()`

---

## TC-UI-PC-006: Validation errors — missing required field and invalid input

**Priority:** P2
**Type:** Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`

**Steps:**

| #   | Action                                                                                              | Expected Result                                                                                                |
| --- | --------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------- |
| 1   | Click `action-menu-add-parts` → "Create Part"                                                       | "Add Part" dialog opens                                                                                        |
| 2   | Leave the "Name" field empty                                                                        | Name field is empty                                                                                            |
| 3   | Click "Submit"                                                                                      | Dialog remains open; validation errors appear                                                                  |
| 4   | Verify a red error banner appears at the top of the form                                            | Banner with heading "**Form Error**" and body "**Errors exist for one or more form fields**" is visible        |
| 5   | Verify the "Name" field is highlighted with an error state                                          | Name field border turns red                                                                                    |
| 6   | Verify an inline error message appears below the "Name" field                                       | Text "**This field is required.**" is shown directly below the Name input                                      |
| 7   | Type a single space ` ` in the "Name" field                                                         | Field shows a space character                                                                                  |
| 8   | Click "Submit"                                                                                      | Observe whether the server accepts a whitespace-only name (edge case — document actual behavior)               |
| 9   | Clear the "Name" field and type a 101-character string (exceeding the 100-char limit)               | Field accepts the input                                                                                        |
| 10  | Click "Submit"                                                                                      | Observe whether the server rejects the oversized name; if rejected, an error message appears on the Name field |
| 11  | Type `https://not-a-valid-url` in the "Link" field (aria-label: `text-field-link`) and a valid name | Field accepts the input                                                                                        |
| 12  | Click "Submit"                                                                                      | Observe whether the invalid URL format is rejected with an error on the Link field                             |

**Observed:**

- Empty-submit validation confirmed: red "Form Error" banner + red Name field border + "This field is required." inline message
- Validation is client-side (triggered before any network request)
- Matches docs: Yes (name is required; link has URI format validation per schema)

**Automation Hints:**

- Assert error banner: `expect(page.getByRole('alert', { name: 'Form Error' })).toBeVisible()`
- Assert inline error: `expect(page.getByText('This field is required.')).toBeVisible()`
- Assert Name field error state: `expect(page.getByRole('textbox', { name: 'text-field-name' })).toHaveAttribute('data-error', 'true')` (Mantine uses `data-error` attribute)

---

## TC-UI-PC-007: Import parts from CSV — happy path

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- A valid CSV file is prepared with the following content and saved as `tc007-import-parts.csv`:
  ```
  name,description,IPN
  TC-IMPORT-001,First imported part,IMP-001
  TC-IMPORT-002,Second imported part,IMP-002
  TC-IMPORT-003,Third imported part,IMP-003
  ```

**Steps:**

| #   | Action                                                                          | Expected Result                                                                                                                                                    |
| --- | ------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 1   | Click the `action-menu-add-parts` button                                        | Dropdown opens                                                                                                                                                     |
| 2   | Click "Import from File" (aria-label: `action-menu-add-parts-import-from-file`) | The "Import Parts" dialog opens                                                                                                                                    |
| 3   | Verify the dialog title                                                         | Dialog heading reads "**Import Parts**"                                                                                                                            |
| 4   | Verify the "Data File" field is required                                        | Label shows "**Data File \***" (with red asterisk)                                                                                                                 |
| 5   | Click the "Select file to upload" area                                          | The system file picker opens                                                                                                                                       |
| 6   | Select the prepared `tc007-import-parts.csv` file                               | The selected filename appears in the Data File field                                                                                                               |
| 7   | Click "Submit"                                                                  | The dialog closes; the import session is created and progresses to the field mapping step                                                                          |
| 8   | Verify the field mapping screen loads                                           | A two-column layout is shown: left column lists CSV column headers (name, description, IPN); right column shows dropdown selectors for mapping to InvenTree fields |
| 9   | Verify that "name" is automatically mapped to the "Name" InvenTree field        | The "name" column is pre-mapped to the Name field                                                                                                                  |
| 10  | Verify that "description" is automatically mapped to "Description"              | The "description" column is pre-mapped                                                                                                                             |
| 11  | Verify that "IPN" is automatically mapped to "IPN"                              | The "IPN" column is pre-mapped                                                                                                                                     |
| 12  | Click "Submit" or "Confirm Mappings" to proceed                                 | The field mapping is saved; the wizard advances to the data processing step                                                                                        |
| 13  | Verify the process data screen shows a table with 3 rows                        | Each of the 3 CSV rows is shown as a table row                                                                                                                     |
| 14  | Verify each row has a "Pending" status indicator                                | All rows show the Pending state (no red error indicators)                                                                                                          |
| 15  | Click "Import All" or individually import each row                              | Each row transitions to "Imported" status                                                                                                                          |
| 16  | Verify that all 3 rows show "Imported" status                                   | All rows are green / marked as Imported                                                                                                                            |
| 17  | Close or complete the import session                                            | Session is closed; parts are now in the database                                                                                                                   |
| 18  | Navigate to the Parts list and search for "TC-IMPORT-001"                       | The part appears in the list                                                                                                                                       |

**Observed:**

- "Import Parts" dialog confirmed open via `action-menu-add-parts` → "Import from File"
- Dialog title: "Import Parts"
- Single visible field: "Data File \*" (required, type=file, label "Data file to import")
- Only "Select file to upload" area and Cancel/Submit buttons in Step 1
- Subsequent steps (field mapping, row review) require actual file upload — not explored without a real file
- Matches docs: Yes (step 1 is file upload only)

**Notes:**

- The field mapping step (Step 2) uses automatic column header matching — testers should verify auto-mapping is correct before confirming
- Import is non-reversible; ensure the demo server is acceptable for this mutation
- Background worker processes the file; testers may need to refresh the page if processing takes time

**Automation Hints:**

- Click import: `page.getByRole('menuitem', { name: 'action-menu-add-parts-import-from-file' }).click()`
- Assert dialog: `expect(page.getByRole('dialog')).toContainText('Import Parts')`
- Upload file: `page.getByLabel('Data File').setInputFiles('tc007-import-parts.csv')`
- Submit: `page.getByRole('button', { name: 'Submit' }).click()`

---

## TC-UI-PC-008: Import parts from CSV — error rows with inline correction

**Priority:** P3
**Type:** Negative / Exploratory

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- A CSV file is prepared with intentionally bad data saved as `tc008-bad-import.csv`:
  ```
  name,description,category
  TC-ERR-001,Valid part,
  ,Missing name is invalid,
  TC-ERR-003,Category does not exist,99999999
  ```

**Steps:**

| #   | Action                                                                                 | Expected Result                                                                              |
| --- | -------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| 1   | Click `action-menu-add-parts` → "Import from File"                                     | "Import Parts" dialog opens                                                                  |
| 2   | Upload `tc008-bad-import.csv` via the "Data File" field                                | File is selected                                                                             |
| 3   | Click "Submit"                                                                         | Import session is created; field mapping screen appears                                      |
| 4   | Map CSV "name" column to InvenTree "Name" field                                        | Mapping is set                                                                               |
| 5   | Map CSV "description" column to "Description" field                                    | Mapping is set                                                                               |
| 6   | Map CSV "category" column to "Category" field                                          | Mapping is set                                                                               |
| 7   | Confirm mappings and proceed                                                           | Data is loaded into the session; the process data screen appears with 3 rows                 |
| 8   | Verify row 2 (missing name) shows an "Error" status in red                             | Row 2 is highlighted red; error message "This field is required." or similar is shown inline |
| 9   | Verify row 3 (invalid category ID 99999999) shows an "Error" status                    | Row 3 is red; error message indicates the category ID was not found                          |
| 10  | Verify row 1 (valid row) shows "Pending" status                                        | Row 1 has no error indicator                                                                 |
| 11  | Import row 1 individually by clicking its individual import action                     | Row 1 transitions to "Imported" status                                                       |
| 12  | Correct row 2 by entering a valid name in-place                                        | The inline editing accepts the value                                                         |
| 13  | Re-process row 2                                                                       | Row 2 status changes from "Error" to "Pending"                                               |
| 14  | Import row 2                                                                           | Row 2 transitions to "Imported"                                                              |
| 15  | Delete/remove row 3 from the session (do not import the bad row)                       | Row 3 is removed from the session                                                            |
| 16  | Verify all remaining rows are in "Imported" or removed state                           | Import session can be closed                                                                 |
| 17  | Navigate to Parts list and confirm TC-ERR-001 and the corrected row 2 part are present | Both imported parts appear in the list                                                       |

**Observed:**

- Step 1 of import wizard confirmed — file upload dialog with single required field
- Row status states documented in parts-import.md: Error (red), Pending, Imported
- Error scenarios documented: missing required field → Error; invalid FK → Error; duplicate name → Error
- Inline correction capability documented in parts-import.md: "user can correct the data and re-process without restarting the session"
- Matches docs: Yes (error row behavior matches documentation)

**Notes:**

- This test requires exploring the import session's row review UI, which was not directly observed (requires a real file upload)
- Individual row import and inline editing actions are documented but selectors are to be discovered during first automation run
- Steps 12-14 (inline correction and re-process) are exploratory — document actual UI behavior on first execution

**Automation Hints:**

- After upload, check for error rows: `expect(page.locator('[data-status="error"]')).toHaveCount(2)` (exact selector TBD from live session)
- Row import action: locate per-row action button (exact aria-label TBD from live exploration)
- Assert row 1 Pending: check status indicator color or text for "Pending" state
