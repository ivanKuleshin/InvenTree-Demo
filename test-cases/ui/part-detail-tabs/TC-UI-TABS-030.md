### TC-UI-TABS-030: Test Templates tab is absent for a non-testable part

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/82/details`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 82 ("1551ABK") must have `testable=false` (assembly=false, component=true, trackable=false — not testable based on its API fields)
- [ ] Navigate to: `https://demo.inventree.org/web/part/82/details`

**Steps**:

1. Load the Part 82 detail page at `https://demo.inventree.org/web/part/82/details`.
2. Wait for the page to fully load and the tab bar to render.
3. Locate the tab bar labeled `panel-tabs-part`.
4. Inspect all visible tab labels.
5. Verify that the "Test Templates" tab is absent from the tab bar.

**Expected Result**:
- The "Test Templates" tab is NOT present in the tab bar.
- No tab with the text "Test Templates" is visible.

**Observed** (filled during exploration):

- Element confirmed: "Test Templates" tab present on part 77 (testable=true) — confirmed
- Actual behavior: Part 82 tab list not directly observed; visibility condition from documentation confirmed
- Matches docs: Yes (docs: "The Test Templates tab is only visible if the Part is designated as testable")

**Notes**: Tester must verify that part 82's "Testable Part" attribute in the Part Details tab shows "No". If it shows "Yes", then the Test Templates tab SHOULD be present, and this TC's expected outcome would need revision.
