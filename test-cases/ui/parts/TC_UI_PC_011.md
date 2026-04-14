# TC-UI-PC-011: Import parts — unsupported file format rejected at Step 1

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/index/parts`

**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The "Parts" tab is selected
- A plain text file named `tc011_unsupported.txt` is prepared locally with any content (e.g., `name,description`)

**Steps**:

| # | Action | Expected Result |
|---|--------|-----------------|
| 1 | Click the `action-menu-add-parts` button in the table toolbar | A dropdown menu appears with "Create Part" and "Import from File" |
| 2 | Click the "**Import from File**" menu item (`aria-label="action-menu-add-parts-import-from-file"`) | The "**Import Parts**" modal dialog opens |
| 3 | Verify the dialog title | Dialog heading reads "**Import Parts**" |
| 4 | Verify the single visible field | A field labeled "**Data File \***" (required, marked with `*`) is shown with description "Data file to import" |
| 5 | Verify the upload trigger button label | A button labeled "**Select file to upload**" (`aria-label="file-field-data_file"`) is visible |
| 6 | Click the "**Submit**" button without selecting any file | The dialog shows a "**Form Error**" banner: "**Errors exist for one or more form fields**" and an inline error below the Data File field: "**No file was submitted.**" |
| 7 | Click the "**Select file to upload**" button and select `tc011_unsupported.txt` from the OS file picker | The filename `tc011_unsupported.txt` appears in the Data File field |
| 8 | Click the "**Submit**" button | The dialog shows a "**Form Error**" banner: "**Errors exist for one or more form fields**" |
| 9 | Verify the first inline error message below the Data File field | Error text reads: `File extension "txt" is not allowed. Allowed extensions are: csv, xlsx, tsv.` |
| 10 | Verify the second inline error message below the Data File field | Error text reads: `Unsupported data file format` |
| 11 | Verify the dialog remains on Step 1 (Upload File) | The dialog does not advance to Step 2 (Map Columns); the "**Select file to upload**" button and "Data File" field remain visible |
| 12 | Click the "**Cancel**" button | The dialog closes without creating an import session |

**Expected Result**: Submitting a `.txt` file displays two specific inline error messages and keeps the dialog on the Step 1 upload screen. The import session is not created. Supported formats are `csv`, `xlsx`, and `tsv` only.

**Observed** (filled during exploration):

- Page confirmed: `https://demo.inventree.org/web/part/category/index/parts`, title "InvenTree Demo Server | Parts"
- "Import Parts" dialog title confirmed
- Data File field confirmed: label "Data File \*", description "Data file to import", upload button text "Select file to upload", `aria-label="file-field-data_file"` on the button
- No-file submit error confirmed: banner "Form Error — Errors exist for one or more form fields" + inline "**No file was submitted.**" (divergence from docs: docs say "This field is required." but actual UI shows "No file was submitted.")
- Unsupported format errors confirmed with `tc011_unsupported.txt`: `File extension "txt" is not allowed. Allowed extensions are: csv, xlsx, tsv.` and `Unsupported data file format` (two separate messages under the file field)
- Matches docs: Partially — error messages for unsupported format match docs exactly; no-file error text differs ("No file was submitted." vs docs "This field is required.")

**Notes**: Divergence from documentation — when no file is selected, the actual error message is "**No file was submitted.**", not "This field is required." as stated in the docs. The unsupported-format error messages match exactly.
