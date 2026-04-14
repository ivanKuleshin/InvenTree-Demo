---
source: demo.inventree.org + docs.inventree.org
component: ui
topic: Part Creation - Manual Entry (Add Part Form)
fetched: 2026-04-14
---

# Part Creation — Manual Entry (Add Part Form)

> **Source**: https://demo.inventree.org/web/part/category/index/parts (live demo, observed 2026-04-14)
> Additional reference: https://docs.inventree.org/en/stable/part/

## Table of Contents

1. [Accessing the Add Part Dialog](#accessing-the-add-part-dialog)
2. [Form Title and Layout](#form-title-and-layout)
3. [Form Fields — Text and Number Inputs](#form-fields--text-and-number-inputs)
4. [Form Fields — Relational Selectors](#form-fields--relational-selectors)
5. [Boolean Toggle Fields](#boolean-toggle-fields)
6. [Initial Stock Section](#initial-stock-section)
7. [Form Controls and Buttons](#form-controls-and-buttons)
8. [Form Validation](#form-validation)
9. [Default Values Summary](#default-values-summary)

---

## Accessing the Add Part Dialog

The "Add Part" dialog is accessed from the Parts list page via a dropdown action menu:

1. Navigate to **Parts** in the top navigation bar.
2. The page lands on `/web/part/category/index/parts` (Parts tab within the top-level category).
3. In the toolbar above the parts table, click the button with `aria-label="action-menu-add-parts"` (a "+" / add icon button).
4. A dropdown menu appears with two items:
   - **Create Part** (`aria-label="action-menu-add-parts-create-part"`) — opens the manual entry dialog.
   - **Import from File** (`aria-label="action-menu-add-parts-import-from-file"`) — opens the CSV import wizard.
5. Click **Create Part** to open the Add Part modal dialog.

> **[IMAGE DESCRIPTION]**: The Parts list page toolbar, showing a table of parts with columns: Part, IPN, Revision, Units, Description, Category, Total Stock. Above the table there is a row of icon buttons including "action-menu-add-parts" (a plus icon). When clicked, a small dropdown appears with two menu items: "Create Part" and "Import from File".

---

## Form Title and Layout

- **Dialog title**: `Add Part`
- **Dialog type**: Modal (Mantine Modal component), rendered as a centered overlay on the page.
- The form scrolls vertically; all fields are presented in a single column layout.
- The form is organized into three logical sections (not visually separated by headers):
  1. **Part identification fields** — Category through Link
  2. **Stock and administrative fields** — Default Location, Default Expiry, Minimum Stock, Responsible
  3. **Boolean attribute toggles** — Component through Copy Category Parameters
  4. **Initial Stock sub-section** — expandable/collapsible via the "Initial Stock" accordion/toggle button

---

## Form Fields — Text and Number Inputs

All text inputs use Mantine TextInput components. None of these fields display a visible placeholder text (placeholder is empty); the field label and description are shown above/below the input instead.

| Field Label | Required | Input Type | Description | Constraints |
|---|---|---|---|---|
| **Name** | Yes (`*`) | text | Part name | Max 100 characters; must be non-empty |
| **IPN** | No | text | Internal Part Number | Max 100 characters; default: empty string |
| **Description** | No | text | Part description (optional) | Max 250 characters |
| **Revision** | No | text | Part revision or version number | Max 100 characters; default: empty string |
| **Keywords** | No | text | Part keywords to improve visibility in search results | Max 250 characters |
| **Units** | No | text | Units of measure for this part | Max 20 characters |
| **Link** | No | url | Link to external URL | Max 2000 characters; input type=`url` |
| **Default Expiry** | No | text (numeric) | Expiry time (in days) for stock items of this part | Default: `0`; integer; min: 0; max: 2147483647 |
| **Minimum Stock** | No | text (numeric) | Minimum allowed stock level | Default: `0`; decimal number |

> **Note:** The `Name` field is the only field marked required in the UI (shown with `*` after the label). The `Initial Stock Quantity` field is also required (see Initial Stock section below).

---

## Form Fields — Relational Selectors

These fields use React-Select (searchable dropdown/combobox) components. Each has a "Search..." placeholder within the dropdown input and shows a pre-selected empty option by default.

| Field Label | Description | Behavior |
|---|---|---|
| **Category** | Part category | Searchable dropdown; selects from existing part categories; no category is pre-selected (top-level / unassigned). Label: "Part category". |
| **Revision Of** | Is this part a revision of another part? | Searchable dropdown; links to an existing Part record. |
| **Variant Of** | Is this part a variant of another part? | Searchable dropdown; links to an existing Part record. |
| **Default Location** | Where is this item normally stored? | Searchable dropdown; selects a stock location. |
| **Responsible** | Person/group responsible for this part | Searchable dropdown; selects from users or groups. |

All relational selectors:
- Support free-text search to filter options.
- Display the matched entity name.
- Are optional unless otherwise noted.
- Have empty default (nothing pre-selected).

---

## Boolean Toggle Fields

Boolean fields are rendered as Mantine Switch (toggle) components — visually a sliding toggle rather than a plain checkbox. Each has a label (the field name) and a description (the tooltip/help text). The `aria-describedby` attribute uses the pattern `boolean-field-<fieldname>`.

| Field Label | Description | Default State | API Field Name |
|---|---|---|---|
| **Component** | Can this part be used to build other parts? | **ON** (checked = `true`) | `component` |
| **Assembly** | Can this part be built from other parts? | OFF (checked = `false`) | `assembly` |
| **Is Template** | Is this part a template part? | OFF (checked = `false`) | `is_template` |
| **Testable** | Can this part have test results recorded against it? | OFF (checked = `false`) | `testable` |
| **Trackable** | Does this part have tracking for unique items? | OFF (checked = `false`) | `trackable` |
| **Purchaseable** | Can this part be purchased from external suppliers? | **ON** (checked = `true`) | `purchaseable` |
| **Salable** | Can this part be sold to customers? | OFF (checked = `false`) | `salable` |
| **Virtual** | Is this a virtual part, such as a software product or license? | OFF (checked = `false`) | `virtual` |
| **Locked** | Locked parts cannot be edited | OFF (checked = `false`) | `locked` |
| **Active** | Is this part active? | **ON** (checked = `true`) | `active` |
| **Copy Category Parameters** | Copy parameter templates from selected part category | **ON** (checked = `true`) | `copy_category_parameters` |

> **Note:** The default states observed above match the API schema defaults: `component=true`, `purchaseable=true`, `active=true`. All others default to `false`. The `copy_category_parameters` field is write-only in the API and controls whether the part inherits parameter templates from its category on creation.

> **[IMAGE DESCRIPTION]**: The boolean toggle section of the Add Part form showing 11 rows of Mantine Switch components. Each row has the toggle on the left, the field name in bold to the right of the toggle, and a description text below. "Component", "Purchaseable", "Active", and "Copy Category Parameters" have their toggles in the ON (blue/filled) position. The remaining toggles (Assembly, Is Template, Testable, Trackable, Salable, Virtual, Locked) are in the OFF (grey/empty) position.

---

## Initial Stock Section

Below the boolean toggles there is a collapsible accordion section labeled **"Initial Stock"**. When collapsed, only the section header button is visible. When expanded, two additional fields appear:

| Field Label | Required | Input Type | Description |
|---|---|---|---|
| **Initial Stock Quantity** | Yes (`*`) | text (numeric) | Specify initial stock quantity for this Part. If quantity is zero, no stock is added. Default: `0`. |
| **Initial Stock Location** | No | React-Select (searchable) | Specify initial stock location for this Part. |

> **Note:** The `Initial Stock Quantity` field is marked required (`*`) and has a default value of `0`. When the quantity is `0`, no stock item is created. The corresponding API fields are `initial_stock.quantity` and `initial_stock.location` (write-only nested objects).

---

## Form Controls and Buttons

At the bottom of the dialog:

| Button / Control | Type | Behavior |
|---|---|---|
| **Cancel** | button | Closes the dialog without saving |
| **Submit** | button | Submits the form; triggers client-side validation first |
| **Keep form open** | Toggle (checkbox) | When ON, the dialog remains open after a successful submission so another part can be added immediately. Default: OFF. |

The "Keep form open" control label: "Keep form open after submitting".

---

## Form Validation

### Client-side Validation (on Submit)

- When **Submit** is clicked with the **Name** field empty, the form does not submit.
- A red error banner appears at the top of the form content area: **"Form Error — Errors exist for one or more form fields"**.
- Below the **Name** field, an inline error message appears: **"This field is required."**
- The form remains open; no API request is made.

### Server-side Validation

- If the API rejects the submission (e.g., duplicate IPN, invalid URL format), error messages are returned and displayed inline below the relevant field.

### URL Field Validation

- The **Link** field has `type="url"` and will reject non-URL values at the browser level.

---

## Default Values Summary

| Field | Default Value |
|---|---|
| Name | (empty, required) |
| IPN | (empty) |
| Description | (empty) |
| Revision | (empty) |
| Keywords | (empty) |
| Units | (empty) |
| Link | (empty) |
| Category | (none selected) |
| Revision Of | (none) |
| Variant Of | (none) |
| Default Location | (none) |
| Default Expiry | `0` |
| Minimum Stock | `0` |
| Responsible | (none) |
| Component | ON (true) |
| Assembly | OFF (false) |
| Is Template | OFF (false) |
| Testable | OFF (false) |
| Trackable | OFF (false) |
| Purchaseable | ON (true) |
| Salable | OFF (false) |
| Virtual | OFF (false) |
| Locked | OFF (false) |
| Active | ON (true) |
| Copy Category Parameters | ON (true) |
| Initial Stock Quantity | `0` |
| Initial Stock Location | (none) |
| Keep form open | OFF (false) |
