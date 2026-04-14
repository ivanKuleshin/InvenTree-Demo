# TC_UI_UNITS — Units of Measure UI Test Suite

**Application:** InvenTree Demo
**URL:** https://demo.inventree.org
**User under test:** `admin` / `inventree` (Display name: Adam Administrator)
**Approach:** Functional + Negative + Boundary
**Created:** 2026-04-14

---

## Template Legend

| Field                | Description                                   |
| -------------------- | --------------------------------------------- |
| **ID**               | Unique test case identifier                   |
| **Priority**         | P1 (smoke) / P2 (regression) / P3 (edge case) |
| **Type**             | Functional / Negative / Boundary              |
| **Preconditions**    | State required before execution               |
| **Steps**            | Numbered actions with expected results        |
| **Observed**         | Data confirmed from live API exploration      |
| **Automation Hints** | Selectors and assertions for automation       |

---

## TC-UI-UNIT-001: Assign a valid SI unit (metres) to a new part at creation

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The parts list page is visible

**Steps:**

| #   | Action                                                                                                                  | Expected Result                                                                                          |
| --- | ----------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------- |
| 1   | Click the `action-menu-add-parts` button (green "+" icon in the table toolbar)                                          | A dropdown menu appears with "Create Part" and "Import from File" items                                  |
| 2   | Click the "Create Part" menu item                                                                                       | The "Add Part" modal dialog opens                                                                        |
| 3   | Type `TC-UI-UNIT-001-Wire` in the "Name" field (aria-label: `text-field-name`)                                          | The field value updates to "TC-UI-UNIT-001-Wire"                                                         |
| 4   | Locate the "Units" field (aria-label: `text-field-units`) in the dialog                                                 | The "Units" text field is visible and empty                                                              |
| 5   | Type `metres` in the "Units" field                                                                                      | The field value updates to "metres"                                                                      |
| 6   | Click the "Submit" button                                                                                               | The dialog closes; the browser navigates to the new part detail page                                     |
| 7   | Verify the page URL matches `https://demo.inventree.org/web/part/{id}/details`                                          | URL contains `/web/part/` followed by a numeric ID and `/details`                                        |
| 8   | Verify the page heading reads "Part: TC-UI-UNIT-001-Wire"                                                               | Heading is visible with the correct part name                                                            |
| 9   | Verify a green toast notification appears                                                                               | A toast with heading "**Success**" and body "**Item Created**" is visible                                |
| 10  | Locate the part details panel and find the "Units" field value                                                          | The Units field displays `metres`                                                                        |
| 11  | Send `GET /api/part/{id}/` with the new part's numeric ID                                                               | The response body contains `"units": "metres"`                                                           |

**Observed:**

- Units field aria-label confirmed: `text-field-units` (confirmed from TC-UI-PC-002 form DOM)
- API OPTIONS for `POST /api/part/`: `units` field — type: string, max_length: 20, allow_blank: true, label: "Units"
- Valid SI unit `metres` accepted by API: `POST /api/part/ {"units":"metres"}` → 201, units="metres" (pk=3924)
- The details panel on the part page renders the `units` field value from the API response
- Matches docs: Yes — docs state "metres" is a valid Pint unit for the `units` field

**Automation Hints:**

- Open create dialog: `page.getByRole('button', { name: 'action-menu-add-parts' }).click(); page.getByRole('menuitem', { name: 'action-menu-add-parts-create-part' }).click()`
- Fill name: `page.getByRole('textbox', { name: 'text-field-name' }).fill('TC-UI-UNIT-001-Wire')`
- Fill units: `page.getByRole('textbox', { name: 'text-field-units' }).fill('metres')`
- Submit: `page.getByRole('button', { name: 'Submit' }).click()`
- Assert URL: `expect(page).toHaveURL(/\/web\/part\/\d+\/details/)`
- Assert toast: `expect(page.getByRole('alert')).toContainText('Item Created')`
- Assert API: `GET /api/part/{id}/ → units === "metres"`

---

## TC-UI-UNIT-002: Assign a valid dimensionless unit (piece) to a new part

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The parts list page is visible

**Steps:**

| #   | Action                                                                                                 | Expected Result                                                                                    |
| --- | ------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------- |
| 1   | Click the `action-menu-add-parts` button                                                               | Dropdown opens with "Create Part" and "Import from File"                                           |
| 2   | Click "Create Part"                                                                                    | "Add Part" modal dialog opens                                                                      |
| 3   | Type `TC-UI-UNIT-002-PiecePart` in the "Name" field (aria-label: `text-field-name`)                   | Name field shows "TC-UI-UNIT-002-PiecePart"                                                        |
| 4   | Type `piece` in the "Units" field (aria-label: `text-field-units`)                                     | The field value updates to "piece"                                                                 |
| 5   | Click "Submit"                                                                                         | Dialog closes; browser navigates to the new part detail page                                       |
| 6   | Verify page heading reads "Part: TC-UI-UNIT-002-PiecePart"                                             | Heading is correct                                                                                 |
| 7   | Verify a success toast appears with body "Item Created"                                                | Green toast notification is visible                                                                |
| 8   | Locate the Units field in the part details panel                                                       | The Units field displays `piece`                                                                   |
| 9   | Send `GET /api/part/{id}/` for the new part                                                            | Response contains `"units": "piece"`                                                               |
| 10  | Repeat steps 1–9 substituting `each` in step 4 and `TC-UI-UNIT-002-EachPart` in step 3               | Part is created with `units="each"` and the detail panel shows `each`                              |
| 11  | Repeat steps 1–9 substituting `dozen` in step 4 and `TC-UI-UNIT-002-DozenPart` in step 3             | Part is created with `units="dozen"` and the detail panel shows `dozen`                            |

**Observed:**

- `piece`, `each`, `dozen` confirmed accepted by live API:
  - `POST /api/part/ {"units":"piece"}` → 201, pk=3877
  - `POST /api/part/ {"units":"each"}` → 201, pk=3878
  - `POST /api/part/ {"units":"dozen"}` → 201, pk=3879
- Docs list built-in dimensionless units: `piece`, `each`, `dozen`, `hundred`, `thousand`
- Matches docs: Yes

**Automation Hints:**

- Parameterize steps 1–9 with values: `["piece", "each", "dozen"]`
- Assert API response `units` field equals the exact string entered
- Assert UI detail panel contains the unit string

---

## TC-UI-UNIT-003: Leave the Units field blank — part defaults to dimensionless (pcs)

**Priority:** P1
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`

**Steps:**

| #   | Action                                                                                    | Expected Result                                                                     |
| --- | ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- |
| 1   | Click the `action-menu-add-parts` button                                                  | Dropdown opens                                                                      |
| 2   | Click "Create Part"                                                                       | "Add Part" dialog opens                                                             |
| 3   | Type `TC-UI-UNIT-003-BlankUnit` in the "Name" field (aria-label: `text-field-name`)      | Name field shows the value                                                          |
| 4   | Verify the "Units" field (aria-label: `text-field-units`) is empty                        | The Units field contains no value and shows placeholder text or is empty            |
| 5   | Leave the "Units" field empty without typing anything                                     | Units field remains empty                                                           |
| 6   | Click "Submit"                                                                            | Dialog closes; browser navigates to the new part detail page                        |
| 7   | Verify page heading reads "Part: TC-UI-UNIT-003-BlankUnit"                                | Heading is correct                                                                  |
| 8   | Verify success toast with body "Item Created" is visible                                  | Green toast notification appears                                                    |
| 9   | Locate the Units field in the part details panel                                           | The Units field is either empty or displays a default value indicating "pcs"        |
| 10  | Send `GET /api/part/{id}/` for the new part                                               | Response contains `"units": ""` (blank string, not null)                            |

**Observed:**

- `POST /api/part/ {"units":""}` → 201, pk=3876, units="" (blank string returned)
- API OPTIONS: `units` allow_blank=true, allow_null=true — both blank and null are permitted
- Docs: "When the `units` field is left blank or not provided, the part is tracked as dimensionless 'pieces' (pcs)"
- Matches docs: Yes — blank unit is valid; the UI description label states "The default is 'pcs'"

**Automation Hints:**

- Confirm Units input is empty: `expect(page.getByRole('textbox', { name: 'text-field-units' })).toHaveValue('')`
- After creation, assert API: `GET /api/part/{id}/ → units === ""`
- Assert UI detail panel shows empty or "pcs" label text

---

## TC-UI-UNIT-004: Entering an invalid/unrecognized unit string is rejected with an error

**Priority:** P1
**Type:** Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`

**Steps:**

| #   | Action                                                                                           | Expected Result                                                                                                    |
| --- | ------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------ |
| 1   | Click the `action-menu-add-parts` button                                                         | Dropdown opens                                                                                                     |
| 2   | Click "Create Part"                                                                              | "Add Part" dialog opens                                                                                            |
| 3   | Type `TC-UI-UNIT-004-InvalidUnit` in the "Name" field (aria-label: `text-field-name`)           | Name field shows the value                                                                                         |
| 4   | Type `INVALIDUNIT123` in the "Units" field (aria-label: `text-field-units`)                     | The field value updates to "INVALIDUNIT123"                                                                        |
| 5   | Click "Submit"                                                                                   | The dialog does **not** close; the form remains open                                                               |
| 6   | Verify an inline error message appears below the "Units" field                                   | An error message reading "**Invalid physical unit**" appears beneath the Units input field                         |
| 7   | Verify no part was created (no navigation away from dialog)                                      | The browser URL has not changed; the "Add Part" dialog is still open                                               |
| 8   | Clear the "Units" field and type `xyz_not_a_real_unit` as a second invalid value                | The field updates to "xyz_not_a_real_unit"                                                                         |
| 9   | Click "Submit" again                                                                             | Form remains open; "Invalid physical unit" error is shown again                                                    |
| 10  | Clear the "Units" field and leave it blank, then click "Submit"                                  | Dialog closes; part is created successfully (blank unit is valid)                                                  |

**Observed:**

- `POST /api/part/ {"units":"INVALIDUNIT123"}` → 400, response: `{"units":["Invalid physical unit"]}`
- API rejects the value before the record is persisted; the UI surfaces this as an inline field error
- The error message text from the API is exactly: `"Invalid physical unit"`
- The form stays open on validation failure — consistent with other InvenTree dialog flows
- Matches docs: Yes — "Invalid/unrecognized unit string: Rejected by the API with a validation error"

**Automation Hints:**

- Fill invalid units: `page.getByRole('textbox', { name: 'text-field-units' }).fill('INVALIDUNIT123')`
- Submit: `page.getByRole('button', { name: 'Submit' }).click()`
- Assert error visible: `expect(page.getByText('Invalid physical unit')).toBeVisible()`
- Assert dialog still open: `expect(page.getByRole('dialog')).toBeVisible()`
- Assert URL unchanged: `expect(page).toHaveURL(/\/web\/part\/category\/index\/parts/)`

---

## TC-UI-UNIT-005: Case-sensitivity validation — uppercase KG is rejected, lowercase kg is accepted

**Priority:** P2
**Type:** Negative / Boundary

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`

**Steps:**

| #   | Action                                                                                        | Expected Result                                                                                              |
| --- | --------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| 1   | Click `action-menu-add-parts` → "Create Part"                                                 | "Add Part" dialog opens                                                                                      |
| 2   | Type `TC-UI-UNIT-005-UppercaseKG` in the "Name" field (aria-label: `text-field-name`)        | Name field shows the value                                                                                   |
| 3   | Type `KG` (uppercase) in the "Units" field (aria-label: `text-field-units`)                  | The field value updates to "KG"                                                                              |
| 4   | Click "Submit"                                                                                | The dialog does **not** close; form remains open                                                             |
| 5   | Verify an inline error appears below the Units field                                          | Error message "**Invalid physical unit**" is displayed — `KG` is not recognized by Pint                     |
| 6   | Clear the "Units" field and type `kg` (lowercase)                                             | The field value updates to "kg"                                                                              |
| 7   | Click "Submit"                                                                                | Dialog closes; browser navigates to the new part detail page                                                 |
| 8   | Verify page heading reads "Part: TC-UI-UNIT-005-UppercaseKG"                                 | Heading is correct                                                                                           |
| 9   | Verify success toast with body "Item Created" is visible                                      | Green toast notification appears                                                                             |
| 10  | Send `GET /api/part/{id}/` for the new part                                                   | Response contains `"units": "kg"` (lowercase preserved exactly as entered)                                   |

**Observed:**

- `POST /api/part/ {"units":"KG"}` → 400, `{"units":["Invalid physical unit"]}` — uppercase rejected
- `POST /api/part/ {"units":"kg"}` → 201, pk=3875, units="kg" — lowercase accepted
- Docs explicitly state: "kg is valid; KG is not valid. Unit strings are case sensitive per Pint rules."
- Matches docs: Yes

**Automation Hints:**

- Fill uppercase: `page.getByRole('textbox', { name: 'text-field-units' }).fill('KG')`
- Assert error: `expect(page.getByText('Invalid physical unit')).toBeVisible()`
- Clear and fill lowercase: `.clear(); .fill('kg')`
- Assert success: `expect(page).toHaveURL(/\/web\/part\/\d+\/details/)`
- Assert API: `GET /api/part/{id}/ → units === "kg"`

---

## TC-UI-UNIT-006: Edit and change the unit on an existing part

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/896/details`
- Part pk=896 (`Silicon Wire 10AWG Black`) exists with `units='m'`, `purchaseable=true`

**Steps:**

| #   | Action                                                                                                        | Expected Result                                                                                              |
| --- | ------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| 1   | Navigate to `https://demo.inventree.org/web/part/896/details`                                                 | Part detail page loads; heading reads "**Part: Silicon Wire 10AWG Black**"                                   |
| 2   | Locate the part details panel; confirm the Units field shows `m`                                              | The Units field value is visible as `m`                                                                      |
| 3   | Click the `action-menu-part-actions` button (three-dot menu in the part header)                               | Dropdown opens with "Duplicate", "Edit", "Delete" items                                                      |
| 4   | Click "Edit" (aria-label: `action-menu-part-actions-edit`)                                                    | The "Edit Part" dialog opens with current field values populated                                             |
| 5   | Verify the "Units" field (aria-label: `text-field-units`) shows the current value `m`                         | Units field contains `m`                                                                                     |
| 6   | Clear the "Units" field and type `cm`                                                                          | The field updates to `cm`                                                                                    |
| 7   | Click "Submit"                                                                                                 | Dialog closes; the part detail page reloads                                                                  |
| 8   | Locate the Units field in the part details panel                                                               | The Units field now shows `cm`                                                                               |
| 9   | Send `GET /api/part/896/` to confirm the persisted value                                                       | Response contains `"units": "cm"`                                                                            |
| 10  | Re-open the Edit dialog and restore the original value by clearing the field and typing `m`, then click Submit | Dialog closes; Units field in the details panel returns to `m`; `GET /api/part/896/` confirms `"units": "m"` |

**Observed:**

- `PATCH /api/part/3875/ {"units":"metres"}` → 200, units="metres" — unit change via API succeeds
- Edit dialog opens via `action-menu-part-actions-edit` (same flow as TC-UI-PA-001)
- `text-field-units` is a text input in the Edit dialog (same as the Add dialog)
- Docs note: "Changing the unit on a part that already has associated supplier parts may require reviewing compatibility — the system does not automatically migrate existing records"
- Matches docs: Yes

**Automation Hints:**

- Navigate: `page.goto('https://demo.inventree.org/web/part/896/details')`
- Open edit: `page.getByRole('button', { name: 'action-menu-part-actions' }).click(); page.getByRole('menuitem', { name: 'action-menu-part-actions-edit' }).click()`
- Clear and fill: `page.getByRole('textbox', { name: 'text-field-units' }).clear(); .fill('cm')`
- Submit: `page.getByRole('button', { name: 'Submit' }).click()`
- Assert panel: `expect(page.getByText('cm')).toBeVisible()` (within the details panel)
- Assert API: `GET /api/part/896/ → units === "cm"`

---

## TC-UI-UNIT-007: View Physical Units in System Settings

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`

**Steps:**

| #   | Action                                                                                                     | Expected Result                                                                                                |
| --- | ---------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------- |
| 1   | Click the settings icon or navigate to `https://demo.inventree.org/web/settings/`                         | The Settings page loads                                                                                        |
| 2   | Locate the "System Settings" section in the left navigation or tab strip                                   | A "System Settings" category or tab is visible                                                                 |
| 3   | Click on the "Physical Units" tab or sub-section                                                           | The Physical Units panel expands or loads                                                                      |
| 4   | Verify the Physical Units section is visible and contains a list or table of custom unit definitions       | A panel titled "Physical Units" (or equivalent) is shown with any existing custom unit rows                    |
| 5   | Verify a button or link to create a new custom unit is visible                                             | A "New Unit", "Add Unit", or "+" button is present in the Physical Units panel toolbar                         |
| 6   | Click the "New Unit" (or equivalent) button                                                                | A dialog or inline form opens to define a new custom unit with fields: Name, Definition, and Description       |
| 7   | Verify the form contains at least a "Name" field and a "Definition" field                                  | Input fields for unit name and definition value are visible                                                    |
| 8   | Close the dialog without submitting                                                                        | Dialog closes without changes                                                                                  |

**Observed:**

- Settings global API endpoint: `GET /api/settings/global/` returns PARAMETER_ENFORCE_UNITS (pk=134) — a relevant setting
- Docs state: "UI location: System Settings → Physical Units tab → Create new unit"
- Docs state: "Custom unit creation requires staff or admin permissions"
- Docs state: "To create a dimensionless custom unit enter the literal value '1' as the unit definition"
- No dedicated REST endpoint for custom unit listing was found (`/api/part/units/` returns 404)
- Matches docs: Yes (UI path confirmed from documentation)

**Automation Hints:**

- Navigate: `page.goto('https://demo.inventree.org/web/settings/')`
- Click Physical Units tab: `page.getByRole('tab', { name: /Physical Units/i }).click()` or equivalent settings nav
- Assert panel visible: `expect(page.getByText('Physical Units')).toBeVisible()`
- Assert create button: `expect(page.getByRole('button', { name: /New Unit|Add Unit/i })).toBeVisible()`

---

## TC-UI-UNIT-008: Supplier part with incompatible unit is rejected with a conversion error

**Priority:** P1
**Type:** Negative

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/896/suppliers`
- Part pk=896 (`Silicon Wire 10AWG Black`) has `units='m'` (length) and `purchaseable=true`
- Supplier pk=42 (`Wirey`) exists and is linked to part 896

**Steps:**

| #   | Action                                                                                                                    | Expected Result                                                                                                             |
| --- | ------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/896/suppliers`                                                           | The Suppliers tab loads showing existing supplier part rows for part 896                                                    |
| 2   | Verify the existing supplier parts table shows rows including "WIRE.BLK.10AWG" for supplier "Wirey"                      | At least one supplier part row is visible                                                                                   |
| 3   | Click the "Add Supplier Part" button or "+" icon in the Suppliers tab toolbar                                             | A dialog opens to create a new supplier part                                                                                |
| 4   | Select or type `Wirey` in the "Supplier" field                                                                            | "Wirey" is selected as the supplier                                                                                         |
| 5   | Type `TC-UI-UNIT-008-INCOMPAT` in the "SKU" field                                                                         | SKU field shows the value                                                                                                   |
| 6   | Type `5 kg` in the "Pack Quantity" field                                                                                  | The field updates to "5 kg"                                                                                                 |
| 7   | Click "Submit"                                                                                                            | The dialog does **not** close; form remains open                                                                            |
| 8   | Verify an inline error message appears below the "Pack Quantity" field                                                    | Error message reads "**Could not convert 5 kg to m**" (or similar incompatibility wording)                                 |
| 9   | Verify no new supplier part row appears in the Suppliers table                                                            | The supplier parts table is unchanged                                                                                       |
| 10  | Clear the "Pack Quantity" field and type `100 feet` (a compatible length unit)                                             | The field updates to "100 feet"                                                                                             |
| 11  | Click "Submit"                                                                                                            | Dialog closes; a new supplier part row appears in the table with Pack Quantity "100 feet"                                   |
| 12  | Verify the success notification                                                                                           | A green toast notification with "Item Created" (or equivalent) is visible                                                   |

**Observed:**

- `POST /api/company/part/ {"part":896,"supplier":3,"SKU":"TC-INCOMPAT-KG","pack_quantity":"5 kg"}` → 400: `{"pack_quantity":["Could not convert 5 kg to m"]}`
- `POST /api/company/part/ {"part":896,"supplier":3,"SKU":"TC-TEST-COMPAT-FEET","pack_quantity":"10 feet"}` → 201, pk=1085, pack_quantity="10 feet", pack_quantity_native=3.048
- Compatible units (same physical dimension as base unit) are automatically converted on save
- The API field is named `pack_quantity` (string, max_length=25); the UI label is "Pack Quantity"
- Docs state: "If an incompatible unit type is specified for a supplier part, an error is displayed and the supplier part cannot be saved"
- Docs state: "InvenTree performs automatic quantity conversion when the supplier unit differs from the base part unit"
- Matches docs: Yes

**Automation Hints:**

- Navigate: `page.goto('https://demo.inventree.org/web/part/896/suppliers')`
- Click add supplier part button (toolbar icon in suppliers table)
- Fill SKU: target field with label "SKU"
- Fill pack qty: target field with label "Pack Quantity" or aria-label equivalent
- Submit and assert error: `expect(page.getByText(/Could not convert.*to m/)).toBeVisible()`
- Change pack qty to compatible: `.clear(); .fill('100 feet')`
- Assert success toast: `expect(page.getByRole('alert')).toContainText('Item Created')`

---

## TC-UI-UNIT-009: Supplier part with compatible unit is accepted and native quantity is converted

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/896/suppliers`
- Part pk=896 (`Silicon Wire 10AWG Black`) has `units='m'` and `purchaseable=true`
- Supplier pk=3 (`Arrow`) exists

**Steps:**

| #   | Action                                                                                                             | Expected Result                                                                                                            |
| --- | ------------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/part/896/suppliers`                                                    | Suppliers tab loads with existing supplier part rows                                                                       |
| 2   | Click the "Add Supplier Part" button or "+" icon in the toolbar                                                    | The "Add Supplier Part" dialog opens                                                                                       |
| 3   | Select `Arrow` in the "Supplier" field                                                                             | "Arrow" is selected                                                                                                        |
| 4   | Type `TC-UI-UNIT-009-FEET` in the "SKU" field                                                                      | SKU field shows the value                                                                                                  |
| 5   | Type `50 feet` in the "Pack Quantity" field                                                                         | The field updates to "50 feet"                                                                                             |
| 6   | Click "Submit"                                                                                                     | Dialog closes; a new row appears in the supplier parts table                                                               |
| 7   | Verify the success notification                                                                                     | A green toast with "Item Created" is visible                                                                               |
| 8   | Locate the new `TC-UI-UNIT-009-FEET` row in the supplier parts table                                               | Row is present showing SKU "TC-UI-UNIT-009-FEET" and pack quantity "50 feet"                                               |
| 9   | Send `GET /api/company/part/?part=896&search=TC-UI-UNIT-009-FEET`                                                  | Response includes the new supplier part; `pack_quantity="50 feet"` and `pack_quantity_native` is approximately `15.24` (50 feet converted to metres) |

**Observed:**

- `POST /api/company/part/ {"part":896,"supplier":3,"SKU":"TC-TEST-COMPAT-FEET","pack_quantity":"10 feet"}` → 201
  - pack_quantity="10 feet", pack_quantity_native=3.048 (3.048 m ≈ 10 feet — confirmed correct conversion)
- Docs: "InvenTree performs automatic quantity conversion when the supplier unit differs from the base part unit"
- Matches docs: Yes

**Automation Hints:**

- Navigate: `page.goto('https://demo.inventree.org/web/part/896/suppliers')`
- Click add supplier part button
- Fill fields: Supplier="Arrow", SKU="TC-UI-UNIT-009-FEET", Pack Quantity="50 feet"
- Submit and assert toast: `expect(page.getByRole('alert')).toContainText('Item Created')`
- Assert API: `GET /api/company/part/?part=896` → find entry with SKU matching, assert `pack_quantity_native ≈ 15.24`

---

## TC-UI-UNIT-010: Unit string at the 20-character boundary is handled correctly

**Priority:** P3
**Type:** Boundary

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`

**Steps:**

| #   | Action                                                                                                                           | Expected Result                                                                                                                          |
| --- | -------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Click `action-menu-add-parts` → "Create Part"                                                                                    | "Add Part" dialog opens                                                                                                                  |
| 2   | Type `TC-UI-UNIT-010-MaxLen` in the "Name" field (aria-label: `text-field-name`)                                                 | Name field shows the value                                                                                                               |
| 3   | Type a 21-character string `aaaaaaaaaaaaaaaaaaaaa` (21 'a' chars) in the "Units" field (aria-label: `text-field-units`)          | The field shows the 21-character string (or truncates to 20 if enforced by the input element's maxlength attribute)                      |
| 4   | Click "Submit"                                                                                                                   | The dialog does **not** close; form remains open                                                                                         |
| 5   | Verify an inline error appears for the Units field                                                                               | Error includes "**Ensure this field has no more than 20 characters.**" and/or "**Invalid physical unit**"                                |
| 6   | Clear the "Units" field and type a 20-character string `aaaaaaaaaaaaaaaaaaaa` (20 'a' chars)                                     | The field shows the 20-character string                                                                                                  |
| 7   | Click "Submit"                                                                                                                   | The dialog does **not** close; error "**Invalid physical unit**" is shown (20 chars accepted by length check, but not a real Pint unit)  |
| 8   | Clear the "Units" field and type `metres` (6 chars, valid unit)                                                                  | The field shows "metres"                                                                                                                 |
| 9   | Click "Submit"                                                                                                                   | Dialog closes; part is created successfully                                                                                              |
| 10  | Verify success toast and that `GET /api/part/{id}/` returns `"units": "metres"`                                                  | Part created with valid unit                                                                                                             |

**Observed:**

- `POST /api/part/ {"units":"aaaaaaaaaaaaaaaaaaaaa"}` (21 chars) → 400: `{"units":["Invalid physical unit","Ensure this field has no more than 20 characters."]}`
  - Both errors are returned simultaneously
- `POST /api/part/ {"units":"aaaaaaaaaaaaaaaaaaaa"}` (20 chars) → 400: `{"units":["Invalid physical unit"]}`
  - Only the content error is returned — length boundary (20) is within the allowed max
- The HTML input likely has `maxlength="20"` which may prevent typing beyond 20 chars in the UI
- Matches docs: Yes — "Unit string max length: 20 characters"

**Automation Hints:**

- Fill 21 chars: `page.getByRole('textbox', { name: 'text-field-units' }).fill('aaaaaaaaaaaaaaaaaaaaa')`
- Assert length error: `expect(page.getByText(/no more than 20 characters/)).toBeVisible()`
- Optionally assert maxlength attribute: `expect(input).toHaveAttribute('maxlength', '20')`

---

## TC-UI-UNIT-011: Valid volume unit (litres) assigned to a new part

**Priority:** P2
**Type:** Functional

**Preconditions:**

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`

**Steps:**

| #   | Action                                                                                             | Expected Result                                                                      |
| --- | -------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------ |
| 1   | Click `action-menu-add-parts` → "Create Part"                                                      | "Add Part" dialog opens                                                              |
| 2   | Type `TC-UI-UNIT-011-LitresPart` in the "Name" field (aria-label: `text-field-name`)              | Name field shows the value                                                           |
| 3   | Type `litres` in the "Units" field (aria-label: `text-field-units`)                               | The field value updates to "litres"                                                  |
| 4   | Click "Submit"                                                                                     | Dialog closes; browser navigates to the new part detail page                         |
| 5   | Verify page heading reads "Part: TC-UI-UNIT-011-LitresPart"                                       | Heading is correct                                                                   |
| 6   | Verify success toast with body "Item Created" is visible                                           | Green toast notification appears                                                     |
| 7   | Locate the Units field in the part details panel                                                   | Units field displays `litres`                                                        |
| 8   | Send `GET /api/part/{id}/` for the new part                                                        | Response contains `"units": "litres"`                                                |

**Observed:**

- `POST /api/part/ {"units":"litres"}` → 201, pk=3925, units="litres" — volume unit accepted
- Docs list volume units: `litres`, `ml`, `gallons` — all in the valid Pint unit set
- Matches docs: Yes

**Automation Hints:**

- Fill units: `page.getByRole('textbox', { name: 'text-field-units' }).fill('litres')`
- Assert API: `GET /api/part/{id}/ → units === "litres"`
