### TC-UI-TABS-021: Revision selector is absent for a part with no revisions

**Type**: UI
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/77/details`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has `revision_count=0` (confirmed via API) and `is_template=true`
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/details`

**Steps**:

1. Load the Part 77 detail page at `https://demo.inventree.org/web/part/77/details`.
2. Observe the area at the top of the part detail page (above or near the tab bar) for any revision selector element.
3. Verify that no "Select Part Revision" dropdown or revision selector is visible.

**Expected Result**:
- No revision selector dropdown is visible on the page.
- The area above the tab bar contains only the part header info (name, description, stats) with no revision dropdown.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Actual behavior: Part 77 detail page explored — no revision selector observed in the accessibility snapshot. Part header showed: "Part: WID-TEMPLATE | Widget Assembly", stats, action buttons. No "Select Part Revision" element present.
- Matches docs: Yes (docs: "The revision selector is only visible when multiple revisions exist for the part")

**Notes**: Part 77 (template part) has revision_count=0 and is_template=true. Template parts cannot have revisions per documentation. The revision selector should therefore be absent.
