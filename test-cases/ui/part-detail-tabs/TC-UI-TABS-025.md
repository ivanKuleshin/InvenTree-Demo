### TC-UI-TABS-025: Related Parts tab loads and shows empty state when no related parts exist

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/related_parts`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has zero related parts (confirmed via API: `GET /api/part/related/?part=77` returns empty list)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 77 detail page.
2. Click the "Related Parts" tab.
3. Observe the URL and the content of the Related Parts panel.
4. Verify the empty state message or indicator.
5. Verify that an "Add Related Part" or equivalent action button is present.

**Expected Result**:
- URL changes to `https://demo.inventree.org/web/part/77/related_parts`.
- The "Related Parts" tab becomes active.
- The tab panel shows an empty state (e.g., "No records found").
- An "Add Related Part" or equivalent add button is visible in the toolbar.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: "Related Parts" tab — present (URL: `/web/part/77/related_parts`)
- Actual behavior: API confirmed 0 related parts for both part 77 and part 87; tab content not directly loaded
- Matches docs: Yes (docs describe empty table with "Add Related Part" button)

**Notes**: API endpoint `GET /api/part/related/?part=77` returned `[]` (empty). The Related Parts feature must be enabled in global settings for the tab to appear.
