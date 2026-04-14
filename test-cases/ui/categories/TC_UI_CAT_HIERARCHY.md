# TC_UI_CAT_HIERARCHY — Category Hierarchy Navigation Tests

---

## TC-UI-CAT-001: Top-level category list loads with expected columns and data

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/index/subcategories`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/index/subcategories`

### Steps

1. Verify the page title reads `InvenTree Demo Server | Parts`.
2. Verify the breadcrumb shows `Parts` as the sole entry with a link to `/web/part`.
3. Verify the `Part Categories` tab is selected.
4. Verify the categories table is visible with the following column headers: **Name**, **Description**, **Path**, **Parts**.
5. Verify the pagination indicator shows a count greater than zero (e.g. `1 - 25 / 47`).
6. Verify that the category rows include at least: `Fasteners`, `Electronics 033b68f9`, `Furniture`.
7. Verify each row in the **Name** column is clickable.

### Expected Result

The page loads successfully. The `Part Categories` tab is selected and the categories table is visible with columns **Name**, **Description**, **Path**, **Parts**. At least 1 page of categories is shown and each category name is a clickable link.

**Observed**:
- Page title seen: `InvenTree Demo Server | Parts`
- Element confirmed: `Part Categories` tab — present
- Element confirmed: table columns Name, Description, Path, Parts — present
- Element confirmed: `Fasteners`, `Electronics 033b68f9`, `Furniture` rows — present
- Pagination: `1 - 25 / 47` — present
- Actual behavior: Category list loaded as expected
- Matches docs: Yes

---

## TC-UI-CAT-002: Navigating into a child category opens its dedicated page

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/index/subcategories`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/index/subcategories`
- The `Fasteners` category exists and is visible in the table

### Steps

1. Click the `Fasteners` cell in the **Name** column of the categories table.
2. Verify the URL changes to `https://demo.inventree.org/web/part/category/3/subcategories`.
3. Verify the page title reads `InvenTree Demo Server | Part Category`.
4. Verify the breadcrumb shows two segments: `Parts` (link to `/web/part`) and `Fasteners` (current).
5. Verify the category subtitle shows `Screws / nuts / bolts / etc` below the `Part Category` heading.
6. Verify the following tabs are visible: **Category Details**, **Subcategories**, **Parts**, **Stock Items**, **Category Parameters**.

### Expected Result

Clicking a category name navigates to that category's dedicated page. The breadcrumb reflects the new location with two segments, the subtitle shows the category description, and all five tabs are present.

**Observed**:
- Page title seen: `InvenTree Demo Server | Part Category`
- URL after click: `https://demo.inventree.org/web/part/category/3/subcategories`
- Element confirmed: breadcrumb `Parts > Fasteners` — present
- Element confirmed: subtitle `Screws / nuts / bolts / etc` — present
- Element confirmed: tabs Category Details, Subcategories, Parts, Stock Items, Category Parameters — all present
- Matches docs: Yes

---

## TC-UI-CAT-003: Category Details tab shows all metadata fields

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/3/details`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Navigate to: `https://demo.inventree.org/web/part/category/3/details`

### Steps

1. Verify the `Category Details` tab is selected.
2. Verify the **Name** field shows `Fasteners`.
3. Verify the **Path** field shows `Fasteners`.
4. Verify the **Description** field shows `Screws / nuts / bolts / etc`.
5. Verify the **Subscribed** field is present and shows `NO`.
6. Verify the **Parts** field shows a numeric count (e.g. `247`).
7. Verify the **Structural** field shows `NO`.

### Expected Result

All metadata fields are visible in the Category Details panel with correct values for the Fasteners category.

**Observed**:
- Page title seen: `InvenTree Demo Server | Part Category`
- Element confirmed: Name = `Fasteners` — present
- Element confirmed: Path = `Fasteners` — present
- Element confirmed: Description = `Screws / nuts / bolts / etc` — present
- Element confirmed: Structural = `NO` — present
- Element confirmed: Parts = `247` — present
- Matches docs: Yes

---

## TC-UI-CAT-004: Three-level nested category shows full pathstring and multi-segment breadcrumb

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/770/details`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Category hierarchy exists: `Electronics 033b68f9` (id=719) → `电阻` (id=768) → `0805` (id=770)
- Navigate to: `https://demo.inventree.org/web/part/category/770/details`

### Steps

1. Verify the page title reads `InvenTree Demo Server | Part Category`.
2. Verify the breadcrumb shows four segments in order: `Parts` › `Electronics 033b68f9` › `电阻` › `0805`.
3. Verify each breadcrumb segment (except the last) is a clickable link.
4. Verify the **Path** field in the Category Details panel shows `Electronics 033b68f9/电阻/0805`.
5. Verify the **Parent Category** field shows `电阻` with a link.
6. Click the `电阻` breadcrumb segment.
7. Verify the URL changes to `https://demo.inventree.org/web/part/category/768/`.

### Expected Result

The three-level hierarchy is fully reflected in the breadcrumb (4 segments) and the pathstring uses `/` as separator. Clicking an ancestor breadcrumb link navigates to that ancestor category page.

**Observed**:
- Page title seen: `InvenTree Demo Server | Part Category`
- Element confirmed: breadcrumb `Parts > Electronics 033b68f9 > 电阻 > 0805` — present
- Element confirmed: Path = `Electronics 033b68f9/电阻/0805` — present
- Element confirmed: Parent Category = `电阻` — present
- Breadcrumb ancestor links confirmed clickable (href present)
- Matches docs: Yes

---

## TC-UI-CAT-005: Structural category shows Structural = YES and contains no directly assigned parts

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/24/details`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- `Category 1` (id=24) is a structural category with 2 sub-categories and 0 directly assigned parts
- Navigate to: `https://demo.inventree.org/web/part/category/24/details`

### Steps

1. Verify the `Category Details` tab is selected.
2. Verify the **Structural** field shows `YES`.
3. Verify the **Subcategories** field shows `2`.
4. Click the `Parts` tab.
5. Verify the URL changes to `https://demo.inventree.org/web/part/category/24/parts`.
6. Verify the parts table body shows `No records found`.

### Expected Result

A structural category displays `Structural = YES` in its details. Its Parts tab shows no directly assigned parts, confirming that structural categories cannot hold parts directly.

**Observed**:
- Page title seen: `InvenTree Demo Server | Part Category`
- Element confirmed: Structural = `YES` — present
- Element confirmed: Subcategories = `2` — present
- Parts tab body: `No records found` — confirmed
- Matches docs: Yes

---

## TC-UI-CAT-006: Breadcrumb link navigates up to parent category

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/770/details`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Category 770 (`0805`) is a child of category 768 (`电阻`) which is a child of 719 (`Electronics 033b68f9`)
- Navigate to: `https://demo.inventree.org/web/part/category/770/details`

### Steps

1. Verify the breadcrumb shows: `Parts > Electronics 033b68f9 > 电阻 > 0805`.
2. Click the `电阻` link in the breadcrumb.
3. Verify the URL changes to `https://demo.inventree.org/web/part/category/768/`.
4. Verify the page heading area shows `电阻` as the current category name.
5. Verify the breadcrumb now shows: `Parts > Electronics 033b68f9 > 电阻`.
6. Click the `Electronics 033b68f9` link in the breadcrumb.
7. Verify the URL changes to `https://demo.inventree.org/web/part/category/719/`.
8. Verify the breadcrumb now shows: `Parts > Electronics 033b68f9`.

### Expected Result

Each breadcrumb ancestor link navigates up one level in the hierarchy. After each navigation, the breadcrumb updates to reflect the new current category level.

**Observed**:
- Element confirmed: breadcrumb ancestor links present and contain correct `/web/part/category/{id}/` hrefs
- Navigation up hierarchy confirmed via href inspection
- Matches docs: Yes

---

## TC-UI-CAT-007: Sub-categories tab lists direct children of a parent category

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/719/subcategories`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- Category `Electronics 033b68f9` (id=719) has 2 direct sub-categories
- Navigate to: `https://demo.inventree.org/web/part/category/719/subcategories`

### Steps

1. Click the `Subcategories` tab.
2. Verify the URL is `https://demo.inventree.org/web/part/category/719/subcategories`.
3. Verify the subcategories table is visible with the same columns as the top-level list: **Name**, **Description**, **Path**, **Parts**.
4. Verify the table shows exactly the 2 direct child categories of `Electronics 033b68f9`.
5. Verify each sub-category row in the **Name** column is clickable.
6. Click one sub-category name to navigate into it.
7. Verify the breadcrumb gains a new segment for the child category.

### Expected Result

The Subcategories tab shows only the direct children of the current category. Each child is a clickable link. Clicking a child updates the breadcrumb with a new level.

**Observed**:
- Element confirmed: `Subcategories` tab — present on category pages
- Column structure confirmed: Name, Description, Path, Parts
- Child category rows confirmed clickable (cell elements have `cursor=pointer`)
- Matches docs: Yes

---

## TC-UI-CAT-008: Leaf-node category shows empty Subcategories tab

**Type**: UI
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/category/3/subcategories`
**Preconditions**:
- [AUTH REQUIRED] Logged in as `admin`
- `Fasteners` (id=3) has no sub-categories
- Navigate to: `https://demo.inventree.org/web/part/category/3/subcategories`

### Steps

1. Click the `Subcategories` tab.
2. Verify the URL is `https://demo.inventree.org/web/part/category/3/subcategories`.
3. Verify the subcategories table body shows `No records found`.
4. Verify no child category rows are visible.

### Expected Result

A leaf-node category (no children) shows an empty Subcategories table with the text `No records found`.

**Observed**:
- Element confirmed: `Subcategories` tab — present
- Subcategories table body: `No records found` — confirmed
- Matches docs: Yes
