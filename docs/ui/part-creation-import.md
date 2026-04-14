---
source: demo.inventree.org + docs.inventree.org
component: ui
topic: Part Creation - Import from File (CSV Import Wizard)
fetched: 2026-04-14
---

# Part Creation — Import from File (CSV Import Wizard)

> **Source**: https://demo.inventree.org/web/part/category/index/parts (live demo, observed 2026-04-14)
> Additional reference: https://docs.inventree.org/en/stable/settings/import/ and https://docs.inventree.org/en/stable/part/

## Table of Contents

1. [Accessing the Import Wizard](#accessing-the-import-wizard)
2. [Wizard Overview and Navigation](#wizard-overview-and-navigation)
3. [Step 1 — Upload File](#step-1--upload-file)
4. [Step 2 — Map Columns](#step-2--map-columns)
5. [Step 3 — Import Rows](#step-3--import-rows)
6. [Step 4 — Process Data](#step-4--process-data)
7. [Step 5 — Complete Import](#step-5--complete-import)
8. [Supported File Formats](#supported-file-formats)
9. [Importable Fields (Column Mapping Table)](#importable-fields-column-mapping-table)
10. [Validation and Error Handling](#validation-and-error-handling)

---

## Accessing the Import Wizard

1. Navigate to **Parts** in the top navigation bar (URL: `/web/part/category/index/parts`).
2. In the table toolbar, click the add-parts menu button (`aria-label="action-menu-add-parts"`).
3. In the dropdown, click **Import from File** (`aria-label="action-menu-add-parts-import-from-file"`).
4. The Import Parts modal dialog opens.

> **Note:** The import feature must be enabled in the Part Settings for the "Import from File" option to appear. Staff-member or admin permissions are required to perform data imports.

---

## Wizard Overview and Navigation

The import is a **5-step wizard**. A Mantine Stepper component at the top of the dialog shows all steps with their completion status. Completed steps show their step number; active and future steps show their names.

The five steps in order:

| Step # | Step Name | Description |
|---|---|---|
| 1 | **Upload File** | Select and upload the data file |
| 2 | **Map Columns** | Map imported file columns to database fields |
| 3 | **Import Rows** | Preview data rows; select rows to import |
| 4 | **Process Data** | Server processes the selected rows |
| 5 | **Complete Import** | Confirmation that import is complete |

The stepper buttons at the top are clickable for already-completed steps, allowing the user to navigate back. The dialog title throughout is **"Importing Data"** (shown after file upload; the first dialog title is "Import Parts").

> **[IMAGE DESCRIPTION]**: The top of the import wizard modal showing a horizontal Mantine Stepper with 5 steps. The completed steps show a checkmark icon with step number. The active step is highlighted. Steps are labeled: "Upload File", "Map Columns", "Import Rows", "Process Data", "Complete Import". The step numbers 4 and 5 are prefixed before the step name for Process Data and Complete Import when those steps are not yet complete.

---

## Step 1 — Upload File

**Dialog title**: `Import Parts`

### Field

| Field Label | Required | Input Type | Description |
|---|---|---|---|
| **Data File** | Yes (`*`) | File upload | Data file to import |

### UI Elements

- A button labeled **"Select file to upload"** (`aria-label="file-field-data_file"`) acts as the file chooser trigger. Clicking it opens the OS file picker.
- The underlying `<input type="file">` element accepts multiple formats (see [Supported File Formats](#supported-file-formats)).
- Once a file is selected, the filename is displayed next to the button (e.g., `test_import.csv`).

### Buttons

| Button | Behavior |
|---|---|
| **Cancel** | Closes the wizard without importing |
| **Submit** | Uploads the file and advances to Step 2 (Map Columns) |

### Validation

- If **Submit** is clicked without selecting a file, the form shows an error: `"This field is required."` (within a "Form Error" banner: "Errors exist for one or more form fields").
- If an unsupported file format is uploaded (e.g., `.txt`), the form shows: **"File extension "txt" is not allowed. Allowed extensions are: csv, xlsx, tsv."** and **"Unsupported data file format"** below the file field.

---

## Step 2 — Map Columns

**Dialog title / stepper label**: `Map Columns`

This step presents a table where each database field is listed as a row and the user maps it to a column from the uploaded file.

### Table Structure

The mapping table has four columns:

| Table Column | Description |
|---|---|
| **Database Field** | The InvenTree part field name (e.g., "Name", "IPN", "Description"). Required fields are marked with `*`. |
| **Field Description** | A brief description of what the field represents. |
| **Imported Column** | A Mantine Select dropdown that maps the database field to a column from the uploaded file. Options are the column header names from the CSV/XLSX plus "Ignore this field". InvenTree auto-detects matches by name similarity. |
| **Default Value** | A free-text input (or relational selector for relational fields) allowing a default value to be set for the field if the mapped column is empty or the field is not mapped. Boolean fields show a checkbox for the default. Relational fields show a searchable dropdown. |

### Behavior

- InvenTree **automatically pre-maps** file columns to database fields based on name matching (case-insensitive). For example, a CSV column named `name` is auto-mapped to the **Name** database field; `IPN` maps to **IPN**; `description` maps to **Description**.
- Unmapped fields default to **"Ignore this field"** in the dropdown.
- The user can manually adjust any mapping by clicking the dropdown for the **Imported Column** cell and selecting a different file column or "Ignore this field".
- Default values for boolean fields are pre-populated from InvenTree system defaults (e.g., `component = true`, `purchaseable = true`, `active = true`).
- Relational fields (Category, Default Location, Revision Of, Variant Of, Responsible) show a searchable dropdown for the default value.

### Button

| Button | Behavior |
|---|---|
| **Accept Column Mapping** | Confirms the mapping and advances to Step 3 (Import Rows). The server then loads the file data into the import session. |

> **[IMAGE DESCRIPTION]**: The column mapping step showing a table with rows for each database field. The first column shows the field name (e.g., "Name *", "Active", "Assembly", "Category", "Component", etc.). The second column shows the field description. The third column shows a Mantine Select dropdown — for auto-detected matches the dropdown shows the matched CSV column name (e.g., "name", "IPN", "description"); for unmatched fields it shows "Ignore this field". The fourth column shows either a text input for the default value (showing "0" for numeric fields), a checkbox for booleans, or a search dropdown for relational fields.

---

## Step 3 — Import Rows

**Dialog title / stepper label**: `Import Rows`

This step shows a paginated data table of all rows loaded from the uploaded file. The user selects which rows to import.

### Table Structure

The row preview table shows one row per imported file row. Columns match the database fields that were mapped in Step 2. Example columns for a CSV with `name`, `IPN`, `description` columns:

| Table Column | Content |
|---|---|
| Row | Row index number (0-based) |
| Description | Value from mapped description column |
| IPN | Value from mapped IPN column |
| Name | Value from mapped name column |

### Row Selection

- A checkbox column on the left of the table allows selecting individual rows.
- A **"Select all records"** checkbox in the table header (`aria-label="Select all records"`) selects all rows on the current page.
- Individual row checkboxes are labeled `"Select record N"` where N is the 1-based row index.

### Table Toolbar Buttons

| Button / Control | `aria-label` | Description |
|---|---|---|
| Delete selected | `action-button-delete-selected-records` | Removes selected rows from the import session (disabled when no rows selected) |
| Import selected rows | `action-button-import-selected-rows` | Imports the selected rows into the database and advances to Step 4 (disabled when no rows selected) |
| Refresh | `table-refresh` | Refreshes the row data |
| Select columns | `table-select-columns` | Opens column visibility selector |
| Filter | `table-select-filters` | Opens row filter panel |
| Row action menu | `row-action-menu-N` | Per-row context menu for individual row actions |

### Pagination

Standard table pagination controls: records-per-page selector (default 25), page navigation buttons (Previous page, numbered pages, Next page).

> **[IMAGE DESCRIPTION]**: The "Import Rows" step showing a data table with column headers: Row, Description, IPN, Name. One data row is visible showing row index 0, with the description, IPN, and name values from the uploaded CSV. A checkbox column is on the far left. Above the table are toolbar icon buttons for delete-selected, import-selected-rows, refresh, select-columns, and filters. The import-selected-rows and delete-selected buttons appear greyed out (disabled) because no rows are selected yet. At the bottom is a standard pagination control showing "1 - 1 / 1 Records per page 25".

---

## Step 4 — Process Data

**Dialog title / stepper label**: `Process Data`

After clicking **Import selected rows** in Step 3, the server processes the data. A progress indicator may show: e.g., **"0 / 1"** during processing (rows processed / total rows).

### Row Status Indicators

Each row displays a status during/after processing:

| Status | Meaning |
|---|---|
| **Pending** | Row contains no errors and is ready to be imported |
| **Error** | Row contains an error that must be corrected before import |
| **Imported** | Row has been successfully imported into the database |

### Processing Notes

- The import process is handled by InvenTree's background worker process.
- For large files, the user can navigate away and return later; the import session persists on the server.
- Each row must be selected and confirmed before actual database insertion.
- Any errors detected are displayed inline on the relevant row.

---

## Step 5 — Complete Import

**Dialog title / stepper label**: `Complete Import`

When all selected rows have been processed successfully, the wizard advances to the final confirmation step.

### UI Elements

- A success message: **"Import Complete"** (heading)
- Confirmation text: **"Data has been imported successfully"**

### Button

| Button | Behavior |
|---|---|
| **Close** | Closes the wizard dialog; the imported parts are now in the database |

> **[IMAGE DESCRIPTION]**: The final step of the import wizard showing a large checkmark or success icon, the text "Import Complete" as a heading, the subtitle "Data has been imported successfully", and a "Close" button at the bottom of the dialog.

---

## Supported File Formats

The file upload field accepts the following formats (validated on Submit):

| Extension | Format |
|---|---|
| `.csv` | Comma-separated values |
| `.xlsx` | Excel workbook |
| `.tsv` | Tab-separated values |

Attempting to upload any other format (e.g., `.txt`, `.json`, `.pdf`) produces the error:

> `File extension "<ext>" is not allowed. Allowed extensions are: csv, xlsx, tsv.`
> `Unsupported data file format`

---

## Importable Fields (Column Mapping Table)

The following database fields are available for mapping in the column mapping step. This list was observed from the live demo during the import of a Parts CSV file:

| Database Field | Required | Field Description | Field Type |
|---|---|---|---|
| **Name** | Yes (`*`) | Part name | Text |
| Active | No | Is this part active? | Boolean |
| Assembly | No | Can this part be built from other parts? | Boolean |
| Category | No | Part category | Relational (searchable) |
| Component | No | Can this part be used to build other parts? | Boolean |
| Default Expiry | No | Expiry time (in days) for stock items of this part | Numeric (integer) |
| Default Location | No | Where is this item normally stored? | Relational (searchable) |
| Description | No | Part description (optional) | Text |
| Remote Image | No | URL of remote image file | URL text |
| Existing Image | No | Filename of an existing part image | Text |
| IPN | No | Internal Part Number | Text |
| Is Template | No | Is this part a template part? | Boolean |
| Keywords | No | Part keywords to improve visibility in search results | Text |
| Link | No | Link to external URL | URL text |
| Locked | No | Locked parts cannot be edited | Boolean |
| Minimum Stock | No | Minimum allowed stock level | Numeric (decimal) |
| Notes | No | Markdown notes (optional) | Text (Markdown) |
| Purchaseable | No | Can this part be purchased from external suppliers? | Boolean |
| Revision | No | Part revision or version number | Text |
| Revision Of | No | Is this part a revision of another part? | Relational (searchable) |
| Salable | No | Can this part be sold to customers? | Boolean |
| Testable | No | Can this part have test results recorded against it? | Boolean |
| Trackable | No | Does this part have tracking for unique items? | Boolean |
| Units | No | Units of measure for this part | Text |
| Variant Of | No | Is this part a variant of another part? | Relational (searchable) |
| Virtual | No | Is this a virtual part, such as a software product or license? | Boolean |
| Responsible | No | Responsible | Relational (searchable) |

> **Note:** Only **Name** is required. All other fields are optional and can be either mapped to a file column or left as "Ignore this field" (optionally with a default value).

---

## Validation and Error Handling

### Step 1 File Upload Errors

| Scenario | Error Message |
|---|---|
| No file selected, Submit clicked | "This field is required." (inline below field) + "Form Error — Errors exist for one or more form fields" (banner) |
| Unsupported file extension (e.g., `.txt`) | `File extension "txt" is not allowed. Allowed extensions are: csv, xlsx, tsv.` + `Unsupported data file format` |

### Step 2 Column Mapping Notes

- InvenTree auto-maps columns by name; mismatches must be manually corrected.
- The user must verify that required fields (Name) are mapped or have a default value, otherwise import rows may fail.
- If the `ID` field is included in the file, it can be used to **update existing records** rather than create new ones (admin-only feature from the Admin Center; not available from the Parts table import wizard directly).

### Step 3 — Row-Level Errors

- Rows with validation errors show an **Error** status badge.
- The row remains in the session; the user may edit the default values in Step 2 and re-upload, or delete the problematic row.

### General Notes

- Imports from the Parts table context are creation-only; updating existing records requires using the Admin Center import session with "Update Existing Records" enabled.
- Multi-level imports are not supported: related models (e.g., categories) must already exist in the database before importing parts that reference them.
- Import sessions persist on the server; the user can navigate away and return later.
