### TC-UI-TABS-022: Attachments tab loads and shows empty state when no attachments exist

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/attachments`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has zero attachments (confirmed via API: `GET /api/attachment/?model_type=part&model_id=77` returns count=0)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 77 detail page.
2. Click the "Attachments" tab.
3. Observe the URL and the content of the Attachments panel.
4. Verify the empty state message or indicator.
5. Verify that an "Add Attachment" or equivalent upload action button is present.

**Expected Result**:
- URL changes to `https://demo.inventree.org/web/part/77/attachments`.
- The "Attachments" tab becomes active.
- The tab panel shows an empty state (e.g., "No records found" or similar empty table indicator).
- An "Add Attachment" or upload button is visible in the toolbar.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: "Attachments" tab — present (URL: `/web/part/77/attachments`)
- Actual behavior: API confirmed 0 attachments for part 77; tab content not directly loaded (session expired)
- Matches docs: Yes (docs describe Attachments tab listing files; 0-attachment empty state is an expected valid state)

**Notes**: The global `GET /api/attachment/?model_type=part` returned 0 results, confirming no part attachments exist in the demo environment at time of exploration.
