# TC-UI-PC-010: "Keep form open" toggle — create multiple parts in sequence without closing dialog

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/index/parts`

**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The "Parts" tab is selected

**Steps**:

| # | Action | Expected Result |
|---|--------|-----------------|
| 1 | Click the `action-menu-add-parts` button in the table toolbar | A dropdown menu appears with "Create Part" and "Import from File" |
| 2 | Click the "**Create Part**" menu item (`aria-label="action-menu-add-parts-create-part"`) | The "**Add Part**" modal dialog opens |
| 3 | Scroll to the bottom of the dialog and verify the "**Keep form open**" checkbox state | The checkbox labeled "**Keep form open**" with description "Keep form open after submitting" is unchecked (default: OFF) |
| 4 | Click the "**Keep form open**" checkbox to enable it | The checkbox becomes checked |
| 5 | Verify the checkbox is now checked | Checkbox state is `checked=true` |
| 6 | Type `TC-UI-PC-010-PartA` in the "**Name**" field (`aria-label="text-field-name"`) | The Name field shows "TC-UI-PC-010-PartA" |
| 7 | Click the "**Submit**" button | A success toast notification "**Success**" / "**Item Created**" appears; the dialog **remains open** (does not close) |
| 8 | Verify the "**Add Part**" dialog is still visible | The modal dialog with title "**Add Part**" is still displayed on screen |
| 9 | Verify the "**Name**" field is cleared/reset for a new entry | The Name field is empty (or reset to its default empty state) |
| 10 | Verify the "**Keep form open**" checkbox is still checked | The checkbox remains checked |
| 11 | Type `TC-UI-PC-010-PartB` in the "**Name**" field | The Name field shows "TC-UI-PC-010-PartB" |
| 12 | Click the "**Submit**" button | A success toast notification appears; the dialog remains open again |
| 13 | Verify "TC-UI-PC-010-PartB" was created (success toast visible) | Toast shows "**Success**" / "**Item Created**" |
| 14 | Click the "**Keep form open**" checkbox to uncheck it | The checkbox becomes unchecked |
| 15 | Type `TC-UI-PC-010-PartC` in the "**Name**" field | The Name field shows "TC-UI-PC-010-PartC" |
| 16 | Click the "**Submit**" button | The dialog closes and the browser navigates to the new part's detail page |
| 17 | Verify the URL matches `https://demo.inventree.org/web/part/{id}/details` | URL contains `/web/part/` followed by a numeric ID and `/details` |
| 18 | Navigate to the Parts list and search for `TC-UI-PC-010-PartA` | Part "TC-UI-PC-010-PartA" appears in the search results |
| 19 | Search for `TC-UI-PC-010-PartB` | Part "TC-UI-PC-010-PartB" appears in the search results |

**Expected Result**: With "Keep form open" enabled, each successful submission creates a part and resets the form without closing the dialog. Unchecking "Keep form open" before the final submission causes the dialog to close and navigate to the new part's detail page after submission.

**Observed** (filled during exploration):

- Page confirmed: `https://demo.inventree.org/web/part/category/index/parts`, title "InvenTree Demo Server | Parts"
- "Keep form open" checkbox confirmed in dialog: no `aria-label`, id `mantine-4a229vqyl` (dynamic Mantine ID), parent text "Keep form openKeep form open after submitting"
- Default state: `checked=false` (unchecked)
- The checkbox must be located by its label text "Keep form open" as it has no stable aria-label
- Matches docs: Yes — label text "Keep form open", description "Keep form open after submitting", default OFF

**Notes**: The "Keep form open" checkbox has no `aria-label` attribute. Automation agents should locate it via `page.getByText('Keep form open').locator('..').getByRole('checkbox')` or by label text association.
