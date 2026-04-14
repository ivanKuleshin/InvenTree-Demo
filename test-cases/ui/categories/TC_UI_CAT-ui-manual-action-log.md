# UI Manual Action Log — Part Categories

**Date**: 2026-04-14
**Base URL**: https://demo.inventree.org
**Auth**: admin (logged in via session)

---

## Session 1 — Category Index Page

**URL visited**: `https://demo.inventree.org/web/part/category/index/subcategories`
**Page title**: InvenTree Demo Server | Parts

### Observations

- Top-level breadcrumb: `Parts` (link to `/web/part`)
- Heading: `Parts`
- Three tabs visible at index level:
  - `Category Details` (tab, link to `.../details`)
  - `Part Categories` (tab, link to `.../subcategories`)
  - `Parts` (tab, link to `.../parts`)
- "Category Details" tab content: `Top level part category`
- "Part Categories" tab shows a table with columns: **Name**, **Description**, **Path**, **Parts**
- Table toolbar buttons: `action-button-add-part-category`, search textbox (placeholder: `Search`), `table-refresh`, `table-select-columns`, `table-select-filters`, `table-export-data`
- Pagination: `1 - 25 / 47` categories total; 2 pages
- Sample top-level categories visible:
  - `Category 1` — "Part category, level 2" — 2 parts
  - `Fasteners` — "Screws / nuts / bolts / etc" — 247 parts
  - `Electronics 033b68f9` — "Electronic components" — 10 parts
  - `Food` — (no description) — 0 parts
  - `Furniture` — "Furniture and associated things" — 24 parts

---

## Session 2 — Fasteners Category (id=3)

**URL visited**: `https://demo.inventree.org/web/part/category/3/subcategories`
**Page title**: InvenTree Demo Server | Part Category

### Observations

- Breadcrumb: `Parts > Fasteners`
  - "Parts" link: `/web/part`
  - "Fasteners" link: `/web/part/category/3/`
- Category subtitle: `Part Category` / `Screws / nuts / bolts / etc`
- Action buttons in header: `action-button-open-in-admin-interface`, `action-button-subscribe-to-notifications`, `action-menu-category-actions`
- Five tabs:
  - `Category Details`
  - `Subcategories` (selected by default on this URL)
  - `Parts`
  - `Stock Items`
  - `Category Parameters`
- "Subcategories" tab: table shows "No records found" — Fasteners has no sub-categories
- Category Details content (via `/web/part/category/3/details`):
  - Name: `Fasteners`
  - Path: `Fasteners`
  - Description: `Screws / nuts / bolts / etc`
  - Subscribed: `NO`
  - Parts: `247`
  - Structural: `NO`

---

## Session 3 — Fasteners Parts Tab + Parametric View

**URL visited**: `https://demo.inventree.org/web/part/category/3/parts`
**Page title**: InvenTree Demo Server | Part Category

### Observations

- "Parts" tab content:
  - Panel heading: `Parts`
  - **View toggle radiogroup** (two buttons):
    - `segmented-icon-control-table` (standard table view, default)
    - `segmented-icon-control-parametric` (parametric view)
  - Tooltip on parametric button: `Parametric View`
- Standard table toolbar: search (`Search` placeholder), `table-refresh`, `table-select-columns`, `table-select-filters` (with badge showing `1` active filter), `table-export-data`
- Standard table columns: **Part**, **IPN**, **Revision**, **Units**, **Description**, **Category**, **Total Stock**
- Pagination in standard view: `1 - 25 / 223`

### Parametric View (after clicking `segmented-icon-control-parametric`)

- `segmented-icon-control-parametric` button becomes `[active]` / `[checked]`
- Table changes to parametric layout
- Parametric table columns: **Part**, **Total Stock**, **Length [mm]**, **Material**, **Thread [mm]**
- Each parameter column header (Length, Material, Thread) has a small filter icon button embedded in it
- Pagination in parametric view: `1 - 25 / 247` (all parts, no filter applied yet)
- Sample data rows:
  - M2x4 SHCS: Length=4, Material=-, Thread=2
  - M3x10 FHS-ALL: Length=10, Material=Alloy, Thread=3
  - M3x10 FHS-PLA: Length=10, Material=Plastic, Thread=3
  - M3x10 Torx: Length=10, Material=-, Thread=3

### Parameter Column Filter Dialog (Length [mm])

- Opened by clicking the filter icon button inside the "Length [mm]" column header
- Filter dialog contains:
  - Operator dropdown: `textbox "filter-Length-operator"` — default value `=`
  - Value input: `textbox "filter-Length"` — placeholder: `Enter a value [mm]`
- Operator options (listbox `filter-Length-operator`):
  - `=` (selected by default)
  - `>`
  - `>=`
  - `<`
  - `<=`
  - `!=`
  - `~`

### Parameter Filter Applied (Length = 10)

- Entered `10` in value input, pressed Enter
- Pagination changed from `1 - 25 / 247` to `1 - 25 / 49`
- Filter badge on `table-select-filters` button incremented

### Standard Table Filters Panel (table-select-filters)

- Dialog title: `Table Filters`
- Close button: `filter-drawer-close`
- Body: `Add Filter` button
- Clicking "Add Filter" opens a sub-form with `Filter` label and `Cancel` button

---

## Session 4 — Category 1 — Structural Category (id=24)

**URL visited**: `https://demo.inventree.org/web/part/category/24/details`
**Page title**: InvenTree Demo Server | Part Category

### Observations

- Breadcrumb: `Parts > Category 1`
- Category Details panel shows:
  - Name: `Category 1`
  - Path: `Category 1`
  - Description: `Part category, level 2`
  - Subscribed: `NO`
  - Parts: `2`
  - Subcategories: `2`
  - **Structural: `YES`**
- Parts tab at `/web/part/category/24/parts`: table shows **"No records found"** — structural categories cannot directly hold parts

---

## Session 5 — Deep Hierarchy (id=770, level=2)

**URL visited**: `https://demo.inventree.org/web/part/category/770/details`
**Page title**: InvenTree Demo Server | Part Category

### Observations

- Breadcrumb: `Parts > Electronics 033b68f9 > 电阻 > 0805`
  - "Electronics 033b68f9" link: `/web/part/category/719/`
  - "电阻" link: `/web/part/category/768/`
  - "0805" link: `/web/part/category/770/`
- Category Details panel shows:
  - Name: `0805`
  - **Path: `Electronics 033b68f9/电阻/0805`** (slash-separated pathstring)
  - Description: `0805电阻`
  - Parent Category: `电阻` (link)
  - Subscribed: `NO`
  - Parts: `0`
  - Structural: `NO`
  - Default location: `Storage Room A`

---

## Key UI Element Labels (Confirmed)

| Element | Exact Label / Ref |
|---|---|
| Top-level categories route | `/web/part/category/index/subcategories` |
| Category page route | `/web/part/category/{id}/details` |
| Tab — Category Details | `Category Details` |
| Tab — Subcategories | `Subcategories` |
| Tab — Parts | `Parts` |
| Tab — Stock Items | `Stock Items` |
| Tab — Category Parameters | `Category Parameters` |
| Parametric view toggle | `segmented-icon-control-parametric` button, tooltip: `Parametric View` |
| Standard view toggle | `segmented-icon-control-table` button |
| Filters drawer button | `table-select-filters` |
| Filters drawer title | `Table Filters` |
| Add filter button | `Add Filter` |
| Length filter operator input | `filter-Length-operator` |
| Length filter value input | `filter-Length` (placeholder: `Enter a value [mm]`) |
| Structural field (details) | `Structural` — value: `YES` or `NO` |
| Path field (details) | `Path` — value: pathstring e.g. `Electronics 033b68f9/电阻/0805` |
| Parent Category field (details) | `Parent Category` |
| Breadcrumb separator | `>` |
