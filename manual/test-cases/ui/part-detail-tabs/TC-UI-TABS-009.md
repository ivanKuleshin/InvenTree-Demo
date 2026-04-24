### TC-UI-TABS-009: Allocations tab shows reserved vs. consumed stock lifecycle state

**Type**: UI
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/77/allocations`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has active allocations (header confirms "Allocated: 5")
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/allocations`

**Steps**:

1. Click the "Allocations" tab in the `panel-tabs-part` tab bar.
2. Observe the table of allocations and note the column headers.
3. Identify at least one row showing an allocation linked to a Build Order or Sales Order.
4. Click on the linked Build Order or Sales Order reference in the allocation row.
5. Observe that the navigation leads to the corresponding order detail page.

**Expected Result**:
- The allocations table displays rows with allocation details.
- Each allocation row references a specific Build Order or Sales Order.
- Clicking an order reference navigates to that order's detail page.
- Allocated items are shown as reserved (not yet consumed from inventory).

**Observed** (filled during exploration):

- Element confirmed: "Allocations" tab — present on part 77
- Element confirmed: Header badge "Allocated: 5" — visible in part detail header
- Actual behavior: Full Allocations tab content not directly observed; existence of 5 allocated units confirmed from part header
- Matches docs: Yes (docs describe allocation showing build orders and sales orders)

**Notes**: The part header stats confirm "Allocated: 5" and "Required: 277" exist. The tab content must be explored to confirm exact column headers and link behavior. This TC covers the allocation lifecycle visibility and navigation flow.
