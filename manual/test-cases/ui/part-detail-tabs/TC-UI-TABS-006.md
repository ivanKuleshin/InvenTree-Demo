### TC-UI-TABS-006: Bill of Materials tab is absent for a non-assembly part

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/82/details`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 82 ("1551ABK") must have `assembly=false` (confirmed via API)
- [ ] Navigate to: `https://demo.inventree.org/web/part/82/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 82 detail page.
2. Inspect all tab labels visible in the tab bar.
3. Verify whether the "Bill of Materials" tab is present or absent.

**Expected Result**:
- The "Bill of Materials" tab is NOT present in the tab bar for a non-assembly part.
- No BOM tab link is visible with text "Bill of Materials" or URL ending in `/bom`.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly` (part 77 tabs explored)
- Element confirmed: "Bill of Materials" tab present on assembly part 77 — confirmed
- Actual behavior: Part 82 tab list not directly observed; visibility condition confirmed via documentation and API (assembly=false)
- Matches docs: Yes (docs state "The BOM tab is only visible if the Part is designated as an assembly")

**Notes**: This is a visibility-condition test. The expected absence must be verified by checking every tab label in the `panel-tabs-part` tablist on part 82's detail page. If the tab IS present for a non-assembly part, this is a defect.
