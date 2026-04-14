### TC-UI-TABS-016: Variants tab is visible on a template part and lists variant parts

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/variants`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has `is_template=true` and 4 variant parts (confirmed via API)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 77 detail page.
2. Verify that the "Variants" tab is present in the tab bar.
3. Click the "Variants" tab.
4. Observe the URL and the content of the Variants panel.
5. Count the number of variant rows displayed in the table.

**Expected Result**:
- The "Variants" tab is visible because part 77 is a template part.
- URL changes to `https://demo.inventree.org/web/part/77/variants`.
- A table of variant parts is displayed showing 4 variant rows.
- Each variant row shows the part name, IPN, and description.
- A "New Variant" action button is present in the toolbar.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: "Variants" tab — present (URL: `/web/part/77/variants`)
- Actual behavior: Tab confirmed present; variant count confirmed to be 4 via API (`/api/part/?variant_of=77`)
- Matches docs: Yes (docs state Variants tab visible only when part is a Template Part)

**Notes**: UI tab label is "Variants". Docs describe the same. No divergence. The 4 variants exist as part objects with `variant_of=77`.
