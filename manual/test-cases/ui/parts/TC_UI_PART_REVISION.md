# TC_UI_PART_REVISION — Part Revisions UI Test Suite

**Application:** InvenTree Demo
**URL:** https://demo.inventree.org
**User under test:** `admin` / `inventree` (Display name: Adam Administrator)
**Approach:** Functional + Exploratory + Negative
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

## TC-UI-REV-001: Create a revision from an existing part (happy path)

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A base part named `TC-UI-REV-001-BasePart` exists and is active (assembly=true, is_template=false); note its part ID
- Navigate to `https://demo.inventree.org/web/part/{id}/details` for that part
- `PART_ENABLE_REVISION` is enabled in System Settings → Parts (verified at `https://demo.inventree.org/web/settings/system/parts`)

**Steps:**

| #   | Action                                                                                                                                | Expected Result                                                                                                                                      |
| --- | ------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/{id}/details` for `TC-UI-REV-001-BasePart`                                          | Part detail page loads; page title reads "**Part: {IPN} \| TC-UI-REV-001-BasePart**"; "Select Part Revision" section is absent                      |
| 2   | Click the `action-menu-part-actions` button (three-dot icon in the part header)                                                       | A dropdown menu opens with items: "Duplicate", "Edit", "Delete"                                                                                      |
| 3   | Click the "Duplicate" menu item (`action-menu-part-actions-duplicate`)                                                                | The "Add Part" modal dialog opens pre-populated with all fields from the source part                                                                  |
| 4   | Verify the dialog title                                                                                                               | Dialog heading reads "**Add Part**"                                                                                                                  |
| 5   | Verify the "Revision Of" field (`related-field-revision_of`) is visible and currently empty or not pre-filled                        | The "Revision Of" combobox is present and shows "Search..." placeholder (not pre-filled from the source part)                                        |
| 6   | In the "Revision Of" field, type `TC-UI-REV-001-BasePart` and select it from the dropdown                                            | The "Revision Of" field displays `TC-UI-REV-001-BasePart` with its IPN and thumbnail                                                                |
| 7   | Clear the "Revision" field (`text-field-revision`) and type `B`                                                                       | The "Revision" field value is `B`                                                                                                                    |
| 8   | Verify the "Name" field (`text-field-name`) still contains `TC-UI-REV-001-BasePart`                                                  | Name field value is `TC-UI-REV-001-BasePart` (inherited from source)                                                                                |
| 9   | Click the "Submit" button                                                                                                             | The dialog closes; browser redirects to the new part detail page at `https://demo.inventree.org/web/part/{new-id}/details`                           |
| 10  | Verify the page title of the new revision                                                                                             | Page title reads "**Part: {IPN} \| TC-UI-REV-001-BasePart \| B**" (revision code appended)                                                          |
| 11  | Verify the Part Details table shows "Revision of" row                                                                                 | The "Revision of" row is visible in the Part Details table; its value is a link reading the original part's display string (e.g., `{IPN} \| TC-UI-REV-001-BasePart`) |
| 12  | Verify the Part Details table shows "Revision" row with value `B`                                                                    | The "Revision" row shows `B`                                                                                                                         |
| 13  | Navigate back to the original part's detail page                                                                                     | Page loads for `TC-UI-REV-001-BasePart`                                                                                                              |
| 14  | Verify the "Select Part Revision" panel now appears on the original part's detail page                                               | A "**Select Part Revision**" section is visible in the Part Details panel with a `part-revision-select` combobox listing at least one revision entry |

**Observed:**

- Action menu button: `action-menu-part-actions` — confirmed present on part detail page
- "Duplicate" menu item: `action-menu-part-actions-duplicate`, label "Duplicate" — confirmed present
- Dialog title: "Add Part" (not "Duplicate Part" as docs describe) — confirmed via live exploration on part 917
- "Revision Of" field: `related-field-revision_of`, type combobox, label "Revision Of", description "Is this part a revision of another part?" — confirmed in dialog
- "Revision" field: `text-field-revision`, type text, label "Revision", description "Part revision or version number" — confirmed in dialog
- "Select Part Revision" section: label "Select Part Revision", combobox `part-revision-select` — confirmed on part 917 (which has 3 revisions: C2, C3, C4)
- "Select Part Revision" section is absent on parts with no revisions — confirmed on part 1921 (APIPart-01ad2779)
- Matches docs: Mostly yes; docs show dialog as "Duplicate Part" but actual title is "Add Part"

**Automation Hints:**

- Open menu: `page.getByRole('button', { name: 'action-menu-part-actions' }).click()`
- Click duplicate: `page.getByRole('menuitem', { name: 'action-menu-part-actions-duplicate' }).click()`
- Fill revision_of: `page.getByRole('combobox', { name: 'related-field-revision_of' }).fill('TC-UI-REV-001-BasePart')`
- Fill revision: `page.getByRole('textbox', { name: 'text-field-revision' }).fill('B')`
- Submit: `page.getByRole('button', { name: 'Submit' }).click()`
- Assert redirect: `expect(page).toHaveURL(/\/web\/part\/\d+\/details/)`
- Assert revision row: `expect(page.getByRole('row', { name: /Revision/ })).toContainText('B')`
- Assert revision-of row: `expect(page.getByRole('row', { name: /Revision of/ })).toContainText('TC-UI-REV-001-BasePart')`

---

## TC-UI-REV-002: Create a revision with a duplicate revision code (validation error)

**Priority:** P2
**Type:** Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A base part `TC-UI-REV-002-BasePart` exists with one revision already created (revision code `A`)
- `PART_ENABLE_REVISION` is enabled

**Steps:**

| #   | Action                                                                                                                                            | Expected Result                                                                                                     |
| --- | ------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/{id}/details` for `TC-UI-REV-002-BasePart`                                                      | Part detail page loads                                                                                              |
| 2   | Click the `action-menu-part-actions` button                                                                                                       | Dropdown menu opens                                                                                                 |
| 3   | Click the "Duplicate" menu item (`action-menu-part-actions-duplicate`)                                                                            | "Add Part" dialog opens                                                                                             |
| 4   | In the "Revision Of" field (`related-field-revision_of`), search for and select `TC-UI-REV-002-BasePart`                                         | "Revision Of" field is populated with the base part                                                                 |
| 5   | In the "Revision" field (`text-field-revision`), type `A` (the code already used by the existing revision)                                       | The "Revision" field value is `A`                                                                                   |
| 6   | Click the "Submit" button                                                                                                                         | The dialog does **not** close; a validation error message appears near the "Revision" field or as a form-level error |
| 7   | Verify the error message content                                                                                                                  | An error is displayed indicating the revision code `A` already exists for this part (duplicate revision not allowed) |
| 8   | Change the "Revision" field value to `B` (a unique code) and click "Submit" again                                                                | The dialog closes; a new revision part is created; browser navigates to the new part detail page                    |

**Observed:**

- "Revision" field: `text-field-revision` — confirmed in "Add Part" dialog
- "Revision Of" field: `related-field-revision_of` — confirmed in "Add Part" dialog
- Revision uniqueness constraint documented: "A part cannot have two revisions with the same revision number"
- Error message exact text not confirmed via live submission (destructive — would create data); expected behavior inferred from API validation layer
- Matches docs: Yes (uniqueness constraint documented)

**Automation Hints:**

- Fill duplicate revision: `page.getByRole('textbox', { name: 'text-field-revision' }).fill('A')`
- Assert error visible: `expect(page.getByRole('dialog')).toContainText(/already exists|duplicate|unique/i)`
- Assert dialog still open: `expect(page.getByRole('dialog', { name: 'Add Part' })).toBeVisible()`

---

## TC-UI-REV-003: Create a revision when PART_ENABLE_REVISION is disabled (feature hidden)

**Priority:** P2
**Type:** Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/settings/system/parts`
- The "Part Revisions" setting (`toggle-setting-PART_ENABLE_REVISION`) is currently enabled (default)
- A base part `TC-UI-REV-003-BasePart` exists and is active

**Steps:**

| #   | Action                                                                                                                                 | Expected Result                                                                                                                                    |
| --- | -------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/settings/system/parts`                                                                    | System Settings page loads; "Parts" tab is selected                                                                                                |
| 2   | Locate the "Part Revisions" setting (description: "Enable revision field for Part")                                                    | The setting row is visible with a toggle switch (`toggle-setting-PART_ENABLE_REVISION`) in the enabled/checked state                               |
| 3   | Click the `toggle-setting-PART_ENABLE_REVISION` switch to disable it                                                                   | The switch moves to the off/unchecked state; a success notification confirms the setting was saved                                                  |
| 4   | Navigate to `https://demo.inventree.org/web/part/{id}/details` for `TC-UI-REV-003-BasePart`                                           | Part detail page loads                                                                                                                             |
| 5   | Click the `action-menu-part-actions` button and click "Duplicate" (`action-menu-part-actions-duplicate`)                               | "Add Part" dialog opens                                                                                                                            |
| 6   | Verify the "Revision" field (`text-field-revision`) is absent from the dialog                                                          | The "Revision" text input is not present in the form when `PART_ENABLE_REVISION` is disabled                                                       |
| 7   | Verify the "Revision Of" field (`related-field-revision_of`) is absent from the dialog                                                 | The "Revision Of" combobox is not present in the form                                                                                              |
| 8   | Close the dialog                                                                                                                        | Dialog closes                                                                                                                                      |
| 9   | Navigate back to `https://demo.inventree.org/web/settings/system/parts` and re-enable the "Part Revisions" setting                    | Switch returns to enabled state; setting is saved                                                                                                  |
| 10  | Navigate back to the part detail page and re-open the "Duplicate" dialog                                                               | "Revision" and "Revision Of" fields are visible again in the "Add Part" dialog                                                                     |

**Observed:**

- Setting label: "**Part Revisions**", description: "Enable revision field for Part" — confirmed via live exploration at `/web/settings/system/parts`
- Toggle switch aria-label: `toggle-setting-PART_ENABLE_REVISION` — confirmed present on settings page
- "Revision" field aria-label: `text-field-revision` — confirmed in "Add Part" dialog
- "Revision Of" field aria-label: `related-field-revision_of` — confirmed in "Add Part" dialog
- Exact behavior when disabled (fields hidden vs. read-only) was not destructively tested; expected based on documented behavior
- Matches docs: Yes (`PART_ENABLE_REVISION` = False hides revision fields)

**Automation Hints:**

- Toggle setting: `page.getByRole('switch', { name: 'toggle-setting-PART_ENABLE_REVISION' }).click()`
- Assert revision field absent: `expect(page.getByRole('textbox', { name: 'text-field-revision' })).not.toBeVisible()`
- Assert revision_of field absent: `expect(page.getByRole('combobox', { name: 'related-field-revision_of' })).not.toBeVisible()`

---

## TC-UI-REV-004: Navigate between revisions using the revision drop-down

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Part 917 ("Encabulator", IPN "ENCAB") exists with three revisions: C2 (part 918), C3 (part 919), C4 (part 920)
- `PART_ENABLE_REVISION` is enabled

**Steps:**

| #   | Action                                                                                                                                             | Expected Result                                                                                                                                  |
| --- | -------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| 1   | Navigate to `https://demo.inventree.org/web/part/917/details`                                                                                     | Part detail page for "Encabulator" (revision C) loads; page title contains "ENCAB \| Encabulator \| C"                                          |
| 2   | Verify the "Select Part Revision" section is visible in the Part Details panel                                                                     | A section labelled "**Select Part Revision**" is visible, containing a combobox (`part-revision-select`) and a thumbnail of the current revision |
| 3   | Verify the currently selected revision in the combobox                                                                                             | The combobox or the thumbnail area shows the current part as "ENCAB \| Encabulator \| C"                                                        |
| 4   | Click the `part-revision-select` combobox to open the revision list                                                                                | A dropdown opens listing all revisions for this part family (C, C2, C3, C4 or similar set)                                                       |
| 5   | Select revision "C2" from the dropdown                                                                                                             | The browser navigates to `https://demo.inventree.org/web/part/918/details` (or equivalent URL for the C2 revision)                              |
| 6   | Verify the page title reflects the selected revision                                                                                               | Page title reads "**Part: ENCAB \| Encabulator \| C2**"                                                                                         |
| 7   | Verify the "Select Part Revision" section is still visible on the C2 revision page                                                                 | "Select Part Revision" section is present; combobox shows "ENCAB \| Encabulator \| C2" as the current selection                                 |
| 8   | Click the `part-revision-select` combobox and select revision "C4"                                                                                 | Browser navigates to part 920 detail page                                                                                                        |
| 9   | Verify the page title                                                                                                                               | Page title reads "**Part: ENCAB \| Encabulator \| C4**"                                                                                         |

**Observed:**

- "Select Part Revision" section confirmed present on part 917 (has 3 revisions)
- Combobox aria-label: `part-revision-select` — confirmed
- Section heading text: "Select Part Revision" — confirmed
- Thumbnail img alt: "Thumbnail" — confirmed inside the revision selector widget
- "Select Part Revision" section is absent on parts with no revisions (part 1921 confirmed absent)
- Revisions of part 917 confirmed via API: C2 (id=918), C3 (id=919), C4 (id=920); parent revision is "C" at id=917
- Part 917 detail URL: `https://demo.inventree.org/web/part/917/details` — confirmed
- Matches docs: Yes

**Automation Hints:**

- Assert selector present: `expect(page.getByText('Select Part Revision')).toBeVisible()`
- Open combobox: `page.locator('[aria-label="part-revision-select"]').click()`
- Select option: `page.getByRole('option', { name: /C2/ }).click()`
- Assert URL change: `expect(page).toHaveURL(/\/web\/part\/918\/details/)`
- Assert title: `expect(page).toHaveTitle(/Encabulator \| C2/)`

---

## TC-UI-REV-005: Verify revision inherits parent data correctly

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A base part `TC-UI-REV-005-BasePart` exists with: Category = "Widgets", Description = "Test description for revision inheritance", IPN = "TC-REV-005", Units = "kg"
- `PART_ENABLE_REVISION` is enabled

**Steps:**

| #   | Action                                                                                                                                          | Expected Result                                                                                                            |
| --- | ----------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/{id}/details` for `TC-UI-REV-005-BasePart`                                                    | Part detail page loads                                                                                                     |
| 2   | Click `action-menu-part-actions` → "Duplicate" (`action-menu-part-actions-duplicate`)                                                           | "Add Part" dialog opens with all fields pre-populated from the source part                                                 |
| 3   | Verify the "Name" field (`text-field-name`) value                                                                                               | "Name" field contains `TC-UI-REV-005-BasePart`                                                                            |
| 4   | Verify the "Description" field (`text-field-description`) value                                                                                 | "Description" field contains "Test description for revision inheritance"                                                   |
| 5   | Verify the "IPN" field (`text-field-IPN`) value                                                                                                 | "IPN" field contains "TC-REV-005"                                                                                          |
| 6   | Verify the "Units" field (`text-field-units`) value                                                                                             | "Units" field contains "kg"                                                                                                |
| 7   | Verify the "Category" field (`related-field-category`) value                                                                                    | "Category" field shows "Widgets"                                                                                           |
| 8   | Set "Revision Of" (`related-field-revision_of`) to `TC-UI-REV-005-BasePart`                                                                    | "Revision Of" field is populated                                                                                           |
| 9   | Set "Revision" (`text-field-revision`) to `B`                                                                                                   | "Revision" field value is `B`                                                                                              |
| 10  | Click "Submit"                                                                                                                                  | Dialog closes; browser navigates to the new revision part page                                                             |
| 11  | Verify the Part Details table on the new revision shows Name = `TC-UI-REV-005-BasePart`                                                         | "Name" row in Part Details table reads `TC-UI-REV-005-BasePart`                                                           |
| 12  | Verify the Part Details table shows Category = "Widgets"                                                                                        | "Category" row reads "Widgets" with a link to the Widgets category                                                         |
| 13  | Verify the Part Details table shows Description = "Test description for revision inheritance"                                                    | "Description" row reads "Test description for revision inheritance"                                                        |
| 14  | Verify the Part Details table shows Revision = `B`                                                                                              | "Revision" row reads `B`                                                                                                   |
| 15  | Verify the Part Details table shows "Revision of" pointing to `TC-UI-REV-005-BasePart`                                                          | "Revision of" row shows a link to the base part                                                                            |

**Observed:**

- "Add Part" dialog pre-populates all source fields when opened via "Duplicate" — confirmed on part 917 (Name=Encabulator, IPN=ENCAB, Description pre-filled, Category=Widgets, Assembly=checked, Salable=checked)
- Field aria-labels confirmed: `text-field-name`, `text-field-IPN`, `text-field-description`, `text-field-units`, `related-field-category`
- Part Details table rows confirmed: "Name", "IPN", "Description", "Revision", "Revision of", "Category" — all present on part 917 detail page
- Matches docs: Yes (revision is itself a Part and inherits all fields)

**Automation Hints:**

- Assert pre-filled name: `expect(page.getByRole('textbox', { name: 'text-field-name' })).toHaveValue('TC-UI-REV-005-BasePart')`
- Assert category pre-filled: `expect(page.getByRole('dialog')).toContainText('Widgets')`
- After submit, assert name row: `expect(page.getByRole('row', { name: /^Name/ })).toContainText('TC-UI-REV-005-BasePart')`

---

## TC-UI-REV-006: Set a revision as active (promotion analog)

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A revision part exists that is currently inactive (active = false); use part 918 (revision C2) or create a dedicated inactive revision `TC-UI-REV-006-RevB` with active=false
- Navigate to that revision's detail page

**Steps:**

| #   | Action                                                                                                                                        | Expected Result                                                                                                                     |
| --- | --------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/{id}/details` for the inactive revision part                                                 | Part detail page loads; an "**INACTIVE**" badge or indicator is visible in the part header                                          |
| 2   | Verify the "Active" row in the Part Details attributes table                                                                                   | The "Active" row shows "No"                                                                                                         |
| 3   | Click the `action-menu-part-actions` button                                                                                                   | Dropdown menu opens                                                                                                                 |
| 4   | Click the "Edit" menu item (`action-menu-part-actions-edit`)                                                                                  | The "Edit Part" dialog opens                                                                                                        |
| 5   | Locate the "Active" switch (`boolean-field-active`) in the dialog                                                                             | The "Active" switch is unchecked (aria-checked = "false"); its label reads "Active" and description reads "Is this part active?"    |
| 6   | Click the "Active" switch to enable it (check it)                                                                                             | The switch moves to the checked state                                                                                                |
| 7   | Click the "Submit" button                                                                                                                     | Dialog closes; part detail page reloads or updates                                                                                  |
| 8   | Verify the "INACTIVE" badge is no longer visible in the part header                                                                           | The "INACTIVE" badge has disappeared from the part header                                                                           |
| 9   | Verify the "Active" row in the Part Details attributes table                                                                                   | The "Active" row now shows "Yes"                                                                                                    |
| 10  | Re-open the "Edit Part" dialog                                                                                                                 | Dialog opens                                                                                                                        |
| 11  | Verify the "Active" switch (`boolean-field-active`) state                                                                                     | The switch is checked, confirming the active=true state is persisted                                                                |

**Observed:**

- "Edit Part" dialog title: "Edit Part" — confirmed via live exploration on part 918
- "Active" switch in Edit dialog: `boolean-field-active`, role=switch — confirmed present on part 918 Edit form (value="true")
- "Active" attribute row in Part Details table: label "Active", shows "Yes" / "No" — confirmed on part 917 (shows "Yes")
- "INACTIVE" badge behavior confirmed in TC-UI-PA-001 exploration (badge text "INACTIVE")
- Matches docs: Yes — active flag controls which revision is considered "current" (no dedicated promote button)

**Automation Hints:**

- Open edit: `page.getByRole('button', { name: 'action-menu-part-actions' }).click(); page.getByRole('menuitem', { name: 'action-menu-part-actions-edit' }).click()`
- Enable active: `page.getByRole('switch', { name: 'boolean-field-active' }).check()`
- Submit: `page.getByRole('button', { name: 'Submit' }).click()`
- Assert badge gone: `expect(page.getByText('INACTIVE')).not.toBeVisible()`
- Assert active row: `expect(page.getByRole('row', { name: /^Active/ })).toContainText('Yes')`

---

## TC-UI-REV-007: Set a revision as inactive (deactivation)

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A revision part exists that is currently active (active = true); use a dedicated part `TC-UI-REV-007-RevA` that is active
- Navigate to that revision's detail page

**Steps:**

| #   | Action                                                                                                                                       | Expected Result                                                                                                                    |
| --- | -------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/{id}/details` for `TC-UI-REV-007-RevA` (active revision)                                   | Part detail page loads; no "INACTIVE" badge is visible                                                                             |
| 2   | Verify the "Active" row in the Part Details attributes table shows "Yes"                                                                     | "Active" row reads "Yes"                                                                                                           |
| 3   | Click the `action-menu-part-actions` button and select "Edit" (`action-menu-part-actions-edit`)                                              | "Edit Part" dialog opens                                                                                                           |
| 4   | Verify the "Active" switch (`boolean-field-active`) is currently **checked**                                                                 | Switch is checked (aria-checked = "true")                                                                                          |
| 5   | Click the "Active" switch to **uncheck** it                                                                                                  | Switch moves to unchecked state                                                                                                    |
| 6   | Click the "Submit" button                                                                                                                    | Dialog closes; part detail page updates                                                                                            |
| 7   | Verify an "**INACTIVE**" badge appears in the part header                                                                                    | The "INACTIVE" indicator is visible in the part header area                                                                        |
| 8   | Verify the "Active" row in the Part Details attributes table shows "No"                                                                      | "Active" row reads "No"                                                                                                            |
| 9   | Navigate to `https://demo.inventree.org/api/part/{id}/` and confirm `active` field is `false`                                                | API response shows `"active": false` for this part                                                                                 |

**Observed:**

- "Active" switch in "Edit Part" dialog: `boolean-field-active` — confirmed, same switch as in "Add Part" form
- "Active" attribute table row confirmed: label "Active", values "Yes"/"No" — confirmed on part 917
- "Edit Part" dialog title: "Edit Part" — confirmed
- Matches docs: Yes — deactivation is done by toggling `active` to false; no dedicated "deactivate revision" workflow

**Automation Hints:**

- Uncheck active: `page.getByRole('switch', { name: 'boolean-field-active' }).uncheck()`
- Assert inactive badge: `expect(page.getByText('INACTIVE')).toBeVisible()`
- Assert active row: `expect(page.getByRole('row', { name: /^Active/ })).toContainText('No')`
- Verify via API: `GET /api/part/{id}/ → active === false`

---

## TC-UI-REV-008: Attempt to create a revision of a template part

**Priority:** P2
**Type:** Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- A template part exists (is_template = true); use part 994 (`APIPart-2c4b576a`) or create `TC-UI-REV-008-TemplatePart` with Is Template = true
- `PART_ENABLE_REVISION` is enabled

**Steps:**

| #   | Action                                                                                                                                                   | Expected Result                                                                                                                                           |
| --- | -------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/994/details` (or the dedicated template part)                                                           | Part detail page loads; Part Details table shows "Template Part" row with value "Yes"                                                                     |
| 2   | Click the `action-menu-part-actions` button                                                                                                              | Dropdown menu opens; "Duplicate" item is present and enabled                                                                                              |
| 3   | Click "Duplicate" (`action-menu-part-actions-duplicate`)                                                                                                 | "Add Part" dialog opens; `boolean-field-is_template` switch is pre-checked (aria-checked="true")                                                          |
| 4   | In the "Revision Of" field (`related-field-revision_of`), search for and select the template part itself                                                 | The "Revision Of" field is populated with the template part                                                                                               |
| 5   | Set "Revision" field (`text-field-revision`) to `B`                                                                                                     | "Revision" field value is `B`                                                                                                                             |
| 6   | Click the "Submit" button                                                                                                                                | The dialog does **not** close; a validation error message is displayed                                                                                    |
| 7   | Verify the error message indicates template parts cannot be a revision target                                                                             | An error message is shown near the "Revision Of" field or as a form-level error, stating that template parts cannot have revisions                         |
| 8   | Clear the "Revision Of" field and click "Submit" again (without setting revision_of)                                                                     | The dialog submits successfully; a new non-revision duplicate is created (no revision relationship)                                                        |

**Observed:**

- Template part 994 ("APIPart-2c4b576a"): `is_template=true` confirmed via API
- Duplicate menu item on template part: `action-menu-part-actions-duplicate` — present and not disabled (confirmed via live exploration)
- Dialog for template part duplicate: `boolean-field-is_template` pre-checked with value "true" — confirmed via live exploration
- `related-field-revision_of` is empty (not pre-filled) when duplicating a template part — confirmed
- Docs state: "A part which is a template part cannot have revisions" — validation is expected server-side
- Exact error message text not confirmed (not submitted destructively); expected behavior from documented constraint
- Matches docs: Yes

**Automation Hints:**

- Assert is_template pre-checked: `expect(page.getByRole('switch', { name: 'boolean-field-is_template' })).toBeChecked()`
- After setting revision_of to a template part and submitting: assert dialog still open: `expect(page.getByRole('dialog', { name: 'Add Part' })).toBeVisible()`
- Assert error message: `expect(page.getByRole('dialog')).toContainText(/template|revision/i)`

---

## TC-UI-REV-009: View all revisions of a part (revision history / list)

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Part 917 ("Encabulator") has three revisions: C2 (918), C3 (919), C4 (920)
- `PART_ENABLE_REVISION` is enabled

**Steps:**

| #   | Action                                                                                                                             | Expected Result                                                                                                                               |
| --- | ---------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/917/details`                                                                     | Part detail page for "Encabulator" loads; "Select Part Revision" section is visible                                                           |
| 2   | Locate the "Select Part Revision" section in the Part Details panel                                                                | The section shows a `part-revision-select` combobox and a thumbnail card for the currently viewed revision                                    |
| 3   | Click the `part-revision-select` combobox to open the revision list                                                                | A dropdown list opens showing all available revisions for this part family                                                                    |
| 4   | Verify the dropdown lists all revisions including the current part                                                                  | The list contains at least: the current revision (C) and revisions C2, C3, C4 — each showing name, revision code, and stock info             |
| 5   | Close the dropdown without selecting                                                                                                | Dropdown closes; current page remains on part 917                                                                                             |
| 6   | Navigate to revision C2 (`https://demo.inventree.org/web/part/918/details`)                                                       | Part detail for "Encabulator C2" loads                                                                                                        |
| 7   | Verify the "Select Part Revision" section is still present on the C2 page                                                          | "Select Part Revision" section is visible; same revision list is accessible from any revision's page                                          |
| 8   | Verify the Part Details table "Revision of" row on C2 shows a link to the parent                                                   | The "Revision of" row links to `ENCAB \| Encabulator \| B` (or whichever part C2 points to) and is clickable                                  |
| 9   | Click the "Revision of" link                                                                                                       | Browser navigates to the parent part's detail page                                                                                            |

**Observed:**

- "Select Part Revision" section confirmed on part 917 with combobox `part-revision-select` — confirmed
- Part 917 revisions confirmed via API: C2 (918), C3 (919), C4 (920)
- "Revision of" row confirmed on part 917 (which is itself a revision): row text "Revision of", value is a clickable link to `/web/part/916/` (ENCAB | Encabulator | B)
- "Revision" row confirmed on part 917: value "C"
- Matches docs: Yes — "Select Part Revision" drop-down lists all revisions of the part family

**Automation Hints:**

- Assert revision selector present: `expect(page.getByText('Select Part Revision')).toBeVisible()`
- Assert revision_of link: `expect(page.getByRole('link', { name: /Encabulator/ })).toBeVisible()`
- Navigate via link: `page.getByRole('row', { name: /Revision of/ }).getByRole('link').click()`

---

## TC-UI-REV-010: Attempt to set a circular revision reference via Edit

**Priority:** P3
**Type:** Negative / Edge Case

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Two parts exist: Part A (`TC-UI-REV-010-PartA`) and Part B (`TC-UI-REV-010-PartB`) where Part B is already set as "revision of" Part A (revision code `B`)
- `PART_ENABLE_REVISION` is enabled

**Steps:**

| #   | Action                                                                                                                                                   | Expected Result                                                                                                                              |
| --- | -------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/{idA}/details` for `TC-UI-REV-010-PartA`                                                               | Part detail page for Part A loads; Part A has no "Revision of" (it is the root part)                                                         |
| 2   | Click `action-menu-part-actions` → "Edit" (`action-menu-part-actions-edit`)                                                                              | "Edit Part" dialog opens for Part A                                                                                                          |
| 3   | In the "Revision Of" field (`related-field-revision_of`), search for and select `TC-UI-REV-010-PartB`                                                   | The "Revision Of" field is populated with Part B (making Part A a revision of Part B, which is itself a revision of Part A — circular)       |
| 4   | Click the "Submit" button                                                                                                                                | The dialog does **not** close; a validation error message is displayed                                                                       |
| 5   | Verify the error message indicates a circular reference is not allowed                                                                                   | An error message appears near the "Revision Of" field or as a form-level error indicating that a circular revision reference is not permitted |
| 6   | Clear the "Revision Of" field and click "Submit"                                                                                                         | The dialog closes successfully without the circular reference; Part A retains its original state                                             |

**Observed:**

- "Edit Part" dialog confirmed present with `related-field-revision_of` field — confirmed on part 918
- Docs state: "A part cannot be a revision of itself. This would create a circular reference which is not allowed"
- Validation is enforced server-side via API; the UI will display the error returned from the API
- Exact error message text was not confirmed via live submission (would mutate demo data)
- Matches docs: Yes (circular reference constraint explicitly documented)

**Automation Hints:**

- Open edit dialog: `page.getByRole('button', { name: 'action-menu-part-actions' }).click(); page.getByRole('menuitem', { name: 'action-menu-part-actions-edit' }).click()`
- Set circular revision_of: `page.getByRole('combobox', { name: 'related-field-revision_of' }).fill('TC-UI-REV-010-PartB')`
- Submit and assert error: `expect(page.getByRole('dialog')).toContainText(/circular|revision/i)`
- Assert dialog remains open: `expect(page.getByRole('dialog', { name: 'Edit Part' })).toBeVisible()`
