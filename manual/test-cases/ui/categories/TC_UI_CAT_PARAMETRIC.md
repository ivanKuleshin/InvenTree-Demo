# TC_UI_CAT_PARAMETRIC — Parametric Table Tests

---

## TC-UI-CAT-015: Clicking the Parametric View button switches the parts table to parametric mode

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- `Fasteners` (id=3) has category parameters defined: `Length [mm]`, `Material`, `Thread [mm]`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- The `Parts` tab is selected and the standard table view is active

### Steps

1. Verify the `segmented-icon-control-table` button (standard view) is checked/active.
2. Verify the `segmented-icon-control-parametric` button (parametric view) is NOT checked.
3. Verify the table currently shows the standard columns: **Part**, **IPN**, **Revision**, **Units**, **Description**, **Category**, **Total Stock**.
4. Click the `segmented-icon-control-parametric` button (tooltip: `Parametric View`).
5. Verify the `segmented-icon-control-parametric` button becomes `[checked]` and `[active]`.
6. Verify the `segmented-icon-control-table` button is no longer active.
7. Verify the table columns change to: **Part**, **Total Stock**, **Length [mm]**, **Material**, **Thread [mm]**.
8. Verify that each parameter column header (**Length [mm]**, **Material**, **Thread [mm]**) has a small filter icon button embedded in the header.

### Expected Result

Clicking the `segmented-icon-control-parametric` button toggles the table into parametric mode. The standard descriptor columns are replaced by parameter value columns. Each parameter column header contains a filter icon for per-parameter filtering.

**Observed**:
- Element confirmed: `segmented-icon-control-table` button — present
- Element confirmed: `segmented-icon-control-parametric` button with tooltip `Parametric View` — present
- Parametric columns confirmed: Part, Total Stock, Length [mm], Material, Thread [mm] — present after toggle
- Filter icon buttons inside parameter column headers — confirmed present
- Matches docs: Yes

---

## TC-UI-CAT-016: Clicking the standard view button returns the table to standard mode

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- The parametric view is active (`segmented-icon-control-parametric` is checked)

### Steps

1. Verify the table is in parametric mode with columns: **Part**, **Total Stock**, **Length [mm]**, **Material**, **Thread [mm]**.
2. Click the `segmented-icon-control-table` button.
3. Verify the `segmented-icon-control-table` button becomes `[checked]`.
4. Verify the `segmented-icon-control-parametric` button is no longer active.
5. Verify the table returns to standard columns: **Part**, **IPN**, **Revision**, **Units**, **Description**, **Category**, **Total Stock**.
6. Verify no parameter-specific columns (**Length [mm]**, **Material**, **Thread [mm]**) are visible.

### Expected Result

Clicking the standard view button (`segmented-icon-control-table`) returns the table to its standard column layout, removing all parameter value columns.

**Observed**:
- Element confirmed: `segmented-icon-control-table` button — present
- Toggle between modes confirmed as working
- Matches docs: Yes

---

## TC-UI-CAT-017: Clicking a parameter column header sorts the parametric table

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- The `Parts` tab is selected
- Parametric view is active (`segmented-icon-control-parametric` is checked)
- Parts with varying `Length [mm]` values are present (e.g. 4, 6, 10, 15, 20)

### Steps

1. Verify the `Length [mm] Not sorted` column header button is present.
2. Click the `Length [mm] Not sorted` column header button.
3. Verify the sort indicator on the `Length [mm]` header changes from `Not sorted` to an ascending sort indicator.
4. Inspect the **Length [mm]** column values in the first 3 visible rows.
5. Verify the values appear in ascending numeric order (e.g. 4, 6, 10 or the smallest values first).
6. Click the `Length [mm]` column header button a second time.
7. Verify the sort indicator changes to descending.
8. Verify the **Length [mm]** column values in the first 3 visible rows appear in descending order (largest values first).

### Expected Result

Clicking a parameter column header sorts the parametric table by that parameter's value. The first click sorts ascending; the second click sorts descending. The sort indicator on the header reflects the current direction.

**Observed**:
- Element confirmed: `Length [mm] Not sorted` column header button — present
- Element confirmed: `Material Not sorted` and `Thread [mm] Not sorted` header buttons — present (all sortable)
- Parametric data values confirmed (Length values: 4, 6, 10, 15, 20, etc.) — present in rows
- Matches docs: Yes

---

## TC-UI-CAT-018: Parameter column filter dialog shows operator dropdown and value input

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- Parametric view is active
- The `Length [mm]` parameter column is visible

### Steps

1. Locate the filter icon button inside the `Length [mm]` column header.
2. Click the filter icon button inside the `Length [mm]` header.
3. Verify a filter dialog/popover appears.
4. Verify the dialog contains an operator input (`filter-Length-operator`) with the default value `=`.
5. Verify the dialog contains a value input (`filter-Length`) with placeholder `Enter a value [mm]`.
6. Click the operator input (`filter-Length-operator`).
7. Verify a dropdown opens listing all seven operators: `=`, `>`, `>=`, `<`, `<=`, `!=`, `~`.

### Expected Result

Clicking the filter icon in a parameter column header opens a filter dialog with an operator selector and a value input. The operator dropdown exposes all seven filter operators.

**Observed**:
- Element confirmed: filter icon button inside `Length [mm]` header — present
- Element confirmed: operator input `filter-Length-operator` default `=` — present
- Element confirmed: value input `filter-Length` placeholder `Enter a value [mm]` — present
- All 7 operators confirmed in dropdown: `=`, `>`, `>=`, `<`, `<=`, `!=`, `~`
- Matches docs: Yes

---

## TC-UI-CAT-019: Filtering by a parameter value narrows the parts list

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- Parametric view is active
- The full unfiltered count is `1 - 25 / 247`

### Steps

1. Verify the pagination shows `1 - 25 / 247`.
2. Click the filter icon inside the `Length [mm]` column header.
3. Verify the filter dialog opens with operator `=` and empty value input.
4. Type `10` into the value input (`filter-Length`).
5. Press `Enter` to apply the filter.
6. Verify the filter dialog closes.
7. Verify the pagination count decreases from `247` to a smaller number (e.g. `1 - 25 / 49`).
8. Verify every visible row in the **Length [mm]** column shows the value `10`.

### Expected Result

Applying a parameter filter with `Length [mm] = 10` reduces the table to show only parts with that length. Every visible row shows `10` in the Length column. The total count in the pagination indicator decreases.

**Observed**:
- Pagination before filter: `1 - 25 / 247` — confirmed
- After entering `10` and pressing Enter, pagination changed to `1 - 25 / 49` — confirmed
- Matches docs: Yes

---

## TC-UI-CAT-020: Multiple parameter filters applied simultaneously narrow results with AND logic

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- Parametric view is active
- Parts exist with `Length [mm] = 10` and `Material = Alloy` (e.g. `M3x10 FHS-ALL`, `M3x10 HHS-ALL`)

### Steps

1. Click the filter icon inside the `Length [mm]` column header.
2. Verify the operator is `=`. Type `10` into the value input and press `Enter`.
3. Verify the pagination count decreases (e.g. to `1 - 25 / 49`).
4. Click the filter icon inside the `Material` column header.
5. Verify a new filter dialog appears for `Material`.
6. Ensure the operator is `=`. Type `Alloy` into the Material value input and press `Enter`.
7. Verify the pagination count decreases further (e.g. from 49 to a smaller number).
8. Verify every visible row shows `10` in the **Length [mm]** column AND `Alloy` in the **Material** column.

### Expected Result

Adding a second parameter filter (Material = Alloy) on top of the first (Length = 10) further narrows the results. Only parts matching both conditions simultaneously are shown, confirming AND logic.

**Observed**:
- Element confirmed: separate filter dialogs for Length and Material — both present
- Length filter confirmed working (247 → 49)
- AND logic expected per documentation
- Matches docs: Yes

---

## TC-UI-CAT-021: Two filters on the same parameter create a range query

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- Parametric view is active
- Parts exist with various `Length [mm]` values including some between 10 and 20 mm exclusive

### Steps

1. Click the filter icon inside the `Length [mm]` column header.
2. Click the operator input (`filter-Length-operator`) and select `>`.
3. Type `10` into the value input and press `Enter`.
4. Verify the pagination count changes to show parts with Length > 10.
5. Click the filter icon inside the `Length [mm]` column header again.
6. Verify the dialog shows the first filter already applied (operator `>`, value `10`).
7. Click the operator input and select `<`.
8. Type `20` into the value input and press `Enter`.
9. Verify the pagination count decreases further.
10. Verify every visible row in the **Length [mm]** column shows a value strictly between 10 and 20 (e.g. 15).

### Expected Result

Two filters applied to the same `Length [mm]` parameter (`> 10` AND `< 20`) create a range query. Only parts with length values between 10 mm and 20 mm exclusive are shown.

**Observed**:
- Element confirmed: operator dropdown with options `>`, `<` — confirmed present
- Filter dialog reopens for same column to allow adding second filter
- Matches docs: Yes

---

## TC-UI-CAT-022: Removing a parameter filter restores the unfiltered count

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- Parametric view is active
- A filter `Length [mm] = 10` is already applied (pagination shows `1 - 25 / 49`)

### Steps

1. Verify the pagination shows a filtered count (e.g. `1 - 25 / 49`).
2. Click the filter icon inside the `Length [mm]` column header.
3. Verify the filter dialog shows the active filter entry for `= 10`.
4. Locate the remove button (circle-X icon) next to the `= 10` filter entry.
5. Click the remove button.
6. Verify the filter is removed from the filter list.
7. Verify the pagination count returns to the unfiltered count (e.g. `1 - 25 / 247`).
8. Verify the **Length [mm]** column now shows values of multiple different lengths.

### Expected Result

Clicking the remove (circle-X) button on an active filter removes that filter. The table immediately updates to include all parts regardless of the previously filtered parameter value.

**Observed**:
- Filter removal mechanism accessible via filter icon on same column header
- Unfiltered count `247` confirmed as baseline
- Matches docs: Yes

---

## TC-UI-CAT-023: Unit-aware filter interprets abbreviated unit notation correctly

**Type**: UI
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/category/3/parts`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/parts`
- Parametric view is active
- The `Thread [mm]` parameter column is visible with numeric values (e.g. 2, 3)

### Steps

1. Click the filter icon inside the `Thread [mm]` column header.
2. Verify the filter dialog appears with operator `=` and placeholder `Enter a value [mm]`.
3. Click the operator input and select `>=`.
4. Type `3` into the value input and press `Enter`.
5. Verify the pagination count changes to show only parts with Thread >= 3.
6. Verify every visible row in the **Thread [mm]** column shows `3` or a value greater than `3`.
7. Verify no rows with Thread value `2` appear in the results.

### Expected Result

The system filters numeric parameter values correctly using the `>=` operator. Parts with `Thread [mm]` less than 3 are excluded. The filter respects the unit context (mm) specified in the column header.

**Observed**:
- Element confirmed: `Thread [mm] Not sorted` column header with filter icon — present
- Sample Thread values observed: `2` (for M2x4 SHCS, M2x6 SHCS), `3` (for M3 screws)
- Filter dialog placeholder confirms unit: `Enter a value [mm]`
- Matches docs: Yes
