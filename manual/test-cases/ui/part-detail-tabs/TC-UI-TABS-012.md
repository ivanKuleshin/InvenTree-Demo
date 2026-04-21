### TC-UI-TABS-012: Build Orders tab shows correct build status badges

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/77/builds`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 has existing build orders in various statuses
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/builds`

**Steps**:

1. Click the "Build Orders" tab in the tab bar.
2. Observe the "Status" column in the build orders table.
3. Identify all distinct status badge labels visible in the Status column.
4. Verify that the statuses match the documented set: "Pending", "Production", "On Hold", "Cancelled", "Completed".

**Expected Result**:
- The build orders table has a "Status" column.
- Status badges visible in the table are from the set: "Pending", "Production", "On Hold", "Cancelled", "Completed".
- No unrecognized or blank status values are displayed.

**Observed** (filled during exploration):

- Element confirmed: "Build Orders" tab — present
- Actual behavior: Full tab content not directly explored due to session expiry; status set confirmed from API documentation
- Matches docs: Yes (docs define 5 statuses: Pending, Production, On Hold, Cancelled, Completed)

**Notes**: Part 77 header shows "In Production: 215" suggesting at least one build order in "Production" status exists. The header stat "In Production" correlates to build orders in progress.
