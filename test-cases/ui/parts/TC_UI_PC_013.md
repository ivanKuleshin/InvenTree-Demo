# TC-UI-PC-013: Import parts — invalid Link URL in a CSV row produces row-level Error status

**Type**: UI
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/category/index/parts`

**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The "Parts" tab is selected
- A CSV file `tc013_bad_rows.csv` is prepared locally with the following exact content:
  ```
  name,description,link
  TC-PC-013-ValidRow,Valid part with no link,
  TC-PC-013-BadLink,Part with invalid link,not-a-url
  TC-PC-013-GoodLink,Part with valid link,https://example.com/datasheet.pdf
  ```

**Steps**:

| # | Action | Expected Result |
|---|--------|-----------------|
| 1 | Click the `action-menu-add-parts` button and then "**Import from File**" (`aria-label="action-menu-add-parts-import-from-file"`) | The "**Import Parts**" dialog opens at Step 1 |
| 2 | Click "**Select file to upload**" and select `tc013_bad_rows.csv` | The filename appears in the Data File field |
| 3 | Click "**Submit**" | The dialog advances to Step 2 — Map Columns |
| 4 | Verify "**Name \***" is auto-mapped to the `name` CSV column | The mapping shows `name` in the Imported Column dropdown for the "Name" row |
| 5 | Verify "**Description**" is auto-mapped to the `description` CSV column | The mapping shows `description` for "Description" |
| 6 | Verify "**Link**" is auto-mapped to the `link` CSV column | The mapping shows `link` for "Link" |
| 7 | Click "**Accept Column Mapping**" | The dialog advances to Step 3 — Import Rows; a table with 3 rows loads |
| 8 | Click the "**Select all records**" checkbox to select all 3 rows | All 3 row checkboxes are checked |
| 9 | Click the "**Import selected rows**" button (`aria-label="action-button-import-selected-rows"`) | The dialog advances to Step 4 — Process Data |
| 10 | Wait for processing to complete (up to 10 seconds) | Row statuses are updated |
| 11 | Verify row 1 (TC-PC-013-ValidRow, no link) shows "**Imported**" status | Row 1 has a green "Imported" indicator |
| 12 | Verify row 2 (TC-PC-013-BadLink, link=`not-a-url`) shows "**Error**" status | Row 2 has a red "Error" indicator; an error message related to the invalid URL is shown inline on the row |
| 13 | Verify row 3 (TC-PC-013-GoodLink, link=`https://example.com/datasheet.pdf`) shows "**Imported**" status | Row 3 has a green "Imported" indicator |
| 14 | Locate row 2 in the table and click its row action menu button (`aria-label="row-action-menu-1"`) | A context menu appears for row 2 |
| 15 | Select the delete/remove option from the row context menu | Row 2 is removed from the import session |
| 16 | Verify only rows 1 and 3 remain and both show "**Imported**" | The session shows 2 imported rows, no error rows |
| 17 | Verify the dialog advances to Step 5 — Complete Import | "**Import Complete**" heading and "**Data has been imported successfully**" text are shown |
| 18 | Click "**Close**" | The dialog closes |
| 19 | In the Parts table, search for `TC-PC-013-ValidRow` | The part appears in the Parts list |
| 20 | Search for `TC-PC-013-GoodLink` | The part appears in the Parts list |
| 21 | Search for `TC-PC-013-BadLink` | No part with this name appears (it was deleted from the import session) |

**Expected Result**: Rows with invalid data (invalid URL in the Link field) are assigned "Error" status and are not imported. Valid rows are imported successfully. The user can remove error rows from the session and complete the import for the valid rows. Row-level errors do not block import of other rows.

**Observed** (filled during exploration):

- Row status values confirmed from docs: "Pending" (ready, no errors), "Error" (validation failure), "Imported" (successfully written to DB)
- Row action menu button pattern confirmed from docs: `aria-label="row-action-menu-N"` where N is 0-based index
- Delete selected button: `aria-label="action-button-delete-selected-records"` (disabled when no rows selected)
- Import selected rows button: `aria-label="action-button-import-selected-rows"` (disabled when no rows selected)
- API confirmed Link field type: `url`, max_length 2000, allow_blank true — invalid URL values would fail server-side validation
- Step 2–5 UI flow validated via API session (session pk=29 created successfully with auto-mapping); row-level error display requires live import execution
- Matches docs: Yes — row-level Error status, per-row action menu, and delete-from-session behavior all documented

**Notes**: Steps 11–13 (row Error/Imported status) and steps 14–16 (row action menu and deletion) are partially exploratory — the exact error message text on row 2 and the precise labels of the row context menu items must be confirmed on first execution. The import session persists on the server; if the wizard is closed mid-way the session remains and can be accessed again.
