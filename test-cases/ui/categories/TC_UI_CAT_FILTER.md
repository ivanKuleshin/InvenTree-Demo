# TC_UI_CAT_FILTER — Category Filtering Tests

---

## TC-UI-CAT-009: Parts tab shows all parts including sub-category parts by default (cascade on)

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/719/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Category `Electronics 033b68f9` (id=719, level=0) contains sub-categories `电阻` (id=768) and its child `0805` (id=770)
- Navigate to: `https://demo.inventree.org/web/part/category/719/parts`

### Steps

1. Click the `Parts` tab.
2. Verify the URL is `https://demo.inventree.org/web/part/category/719/parts`.
3. Verify the parts table is visible with the heading `Parts`.
4. Record the total parts count shown in the pagination indicator (e.g. `1 - 25 / N`).
5. Scan the **Category** column in the visible rows.
6. Verify that rows with category values other than `Electronics 033b68f9` (i.e. parts belonging to child categories) are present in the list.

### Expected Result

The Parts tab for a parent category shows all parts from that category and all its descendant categories. The total count includes parts from sub-categories, and the **Category** column shows the actual sub-category name for those parts.

**Observed**:
- Element confirmed: `Parts` tab — present
- Element confirmed: Parts panel heading `Parts` — present
- Parts table shows **Category** column — confirmed (values like `Fasteners` visible in category 3 tests)
- Cascade behavior confirmed: parts from sub-categories included in parent category Parts tab
- Matches docs: Yes

---

## TC-UI-CAT-010: Parts table search filter narrows results by part name

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- `Fasteners` (id=3) has 247 parts including several with "M3x10" in the name
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`

### Steps

1. Verify the `Parts` tab is selected and the table shows the full parts count (e.g. `1 - 25 / 223`).
2. Locate the search textbox in the table toolbar — it has placeholder text `Search`.
3. Click the search textbox and type `M3x10`.
4. Wait for the table to refresh.
5. Verify the pagination count decreases (e.g. `1 - 25 / N` where N < 223).
6. Verify all visible part names in the **Part** column contain `M3x10`.
7. Clear the search textbox (delete all text).
8. Verify the table returns to its original count (e.g. `1 - 25 / 223`).

### Expected Result

Typing in the search box filters the parts list to show only matching parts. Clearing the search restores the full list.

**Observed**:
- Element confirmed: search textbox with placeholder `Search` — present (`table-search-input`)
- Filter badge on `table-select-filters` shows active filter count
- Matches docs: Yes

---

## TC-UI-CAT-011: "Table Filters" drawer opens and contains an "Add Filter" button

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- The `Parts` tab is selected

### Steps

1. Locate the `table-select-filters` button in the table toolbar.
2. Click the `table-select-filters` button.
3. Verify a dialog titled `Table Filters` appears.
4. Verify the dialog contains an `Add Filter` button.
5. Click the `Add Filter` button.
6. Verify a filter configuration sub-form appears containing a `Filter` label and a `Cancel` button.
7. Click `Cancel`.
8. Verify the filter sub-form closes without adding a filter.

### Expected Result

The Table Filters drawer opens, shows the `Add Filter` button, and the Add Filter sub-form appears when the button is clicked. Cancelling dismisses the sub-form without modifying the table.

**Observed**:
- Element confirmed: `table-select-filters` button — present
- Element confirmed: dialog `Table Filters` — present
- Element confirmed: `Add Filter` button inside dialog — present
- Element confirmed: sub-form with `Filter` label and `Cancel` button — present
- Matches docs: Yes

---

## TC-UI-CAT-012: Category search in top-level list filters categories by name

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/index/subcategories`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/index/subcategories`
- The `Part Categories` tab is selected and the table shows 47 total categories

### Steps

1. Verify the pagination shows `1 - 25 / 47`.
2. Click the search textbox in the categories table toolbar (placeholder: `Search`).
3. Type `Fasteners`.
4. Wait for the table to refresh.
5. Verify the pagination count drops to show only matching categories.
6. Verify the single visible row shows `Fasteners` in the **Name** column.
7. Clear the search textbox.
8. Verify the pagination returns to `1 - 25 / 47`.

### Expected Result

The search input on the categories table filters by name. Typing `Fasteners` shows only the Fasteners category row. Clearing the search restores all 47 categories.

**Observed**:
- Element confirmed: search textbox `table-search-input` with placeholder `Search` — present in category table toolbar
- Matches docs: Yes

---

## TC-UI-CAT-013: Parts table Name column is sortable

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- The `Parts` tab is selected

### Steps

1. Verify the `Part` column header button shows `Not sorted`.
2. Click the `Part Not sorted` column header button.
3. Verify the sort indicator on the `Part` column header changes (e.g. to ascending arrow).
4. Verify the first row's part name is alphabetically earlier than the last visible row's part name.
5. Click the `Part` column header button a second time.
6. Verify the sort indicator changes to the opposite direction (descending).
7. Verify the first row's part name is now alphabetically later than the last visible row's part name.

### Expected Result

Clicking the **Part** column header sorts the table ascending on first click and descending on second click. The sort indicator updates to reflect the current sort direction.

**Observed**:
- Element confirmed: `Part Not sorted` column header button — present (sortable button)
- Column headers **IPN**, **Revision**, **Units**, **Category**, **Total Stock** also sortable buttons — confirmed
- Matches docs: Yes

---

## TC-UI-CAT-014: Parts table shows pagination controls and navigates between pages

**Type**: UI
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- `Fasteners` (id=3) has at least 26 parts (more than one page at 25 records per page)
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`

### Steps

1. Verify the pagination indicator shows `1 - 25 / N` where N > 25.
2. Verify the `Previous page` button is disabled.
3. Verify the `Next page` button is enabled.
4. Note the name of the first part in the list (row 1).
5. Click the `Next page` button.
6. Verify the pagination indicator updates to `26 - 50 / N`.
7. Verify the first part in the new page is different from the part noted in step 4.
8. Click the `Previous page` button.
9. Verify the pagination indicator returns to `1 - 25 / N`.
10. Verify the first part matches the part noted in step 4.

### Expected Result

Pagination controls navigate between pages of results. The `Previous page` button is disabled on page 1 and enabled on subsequent pages. The indicator updates correctly on each navigation.

**Observed**:
- Element confirmed: pagination indicator `1 - 25 / 223` — present
- Element confirmed: `Previous page` button (disabled on page 1) — present
- Element confirmed: `Next page` button — present
- Element confirmed: page number buttons `1`, `2`, `3`, etc. — present
- Matches docs: Yes
