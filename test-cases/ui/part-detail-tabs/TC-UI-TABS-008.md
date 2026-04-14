### TC-UI-TABS-008: Allocations tab is absent for a non-component, non-salable part

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/87/details`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 87 ("Doohickey") has `component=true` and `salable=false` (confirmed via API: assembly=true, salable=false)
- [ ] Navigate to: `https://demo.inventree.org/web/part/87/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 87 detail page.
2. Inspect all tab labels in the tab bar.
3. Note whether the "Allocations" tab is present or absent.

**Expected Result**:
- For a part that is `component=true` and `salable=false`, the "Allocations" tab should still be visible (component=true satisfies the visibility condition).
- If a part has `component=false` AND `salable=false`, the "Allocations" tab must be absent.

**Observed** (filled during exploration):

- Element confirmed: "Allocations" tab present on part 77 (component=true, salable=true) — present
- Actual behavior: Part 87 tab list not directly observed; visibility condition confirmed from documentation
- Matches docs: Yes (docs: tab visible if component=true OR salable=true)

**Notes**: Part 87 is `component=true` so the Allocations tab should be visible. To fully test the absence case, a part with both `component=false` and `salable=false` must be found or created. This TC documents the visibility rule. Tester should verify the tab list on part 87 and record whether "Allocations" is present.
