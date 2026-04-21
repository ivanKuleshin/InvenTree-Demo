### TC-UI-TABS-018: Variants tab is absent for a non-template part

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/87/details`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 87 ("Doohickey") has `is_template=false` (confirmed via API)
- [ ] Navigate to: `https://demo.inventree.org/web/part/87/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 87 detail page.
2. Inspect all tab labels visible in the tab bar.
3. Verify whether the "Variants" tab is present or absent.

**Expected Result**:
- The "Variants" tab is NOT present in the tab bar for a non-template part.
- No tab with the text "Variants" or a URL ending in `/variants` is visible or accessible.

**Observed** (filled during exploration):

- Element confirmed: "Variants" tab present on part 77 (is_template=true) — confirmed
- Actual behavior: Part 87 tab list not directly explored; visibility condition confirmed from documentation
- Matches docs: Yes (docs: "The Variants tab is only visible if the part is a Template Part")

**Notes**: This is a visibility-condition boundary test. If the "Variants" tab IS visible on a non-template part, it is a defect. Tester must record all tab labels visible for part 87 and confirm "Variants" is absent.
