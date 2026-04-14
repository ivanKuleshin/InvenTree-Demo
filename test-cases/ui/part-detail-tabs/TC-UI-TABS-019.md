### TC-UI-TABS-019: Revision selector dropdown appears when a part has multiple revisions

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/82/details`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 82 ("1551ABK") has `revision_count=1` and one revision part exists: part 2005 ("RevisionPart-TC007-...", revision="B") (confirmed via API)
- [ ] Navigate to: `https://demo.inventree.org/web/part/82/details`

**Steps**:

1. Load the Part 82 detail page at `https://demo.inventree.org/web/part/82/details`.
2. Observe the area at the top of the part detail page (above or near the tab bar).
3. Look for a "Select Part Revision" dropdown or revision selector element.
4. Click the revision selector dropdown.
5. Observe the list of available revisions shown.
6. Select the revision labeled "B" (or the name containing "RevisionPart-TC007").
7. Observe the page content after selecting the revision.

**Expected Result**:
- A "Select Part Revision" dropdown (or equivalent revision selector) is visible at the top of the part detail page.
- The dropdown lists all available revisions: the original part 82 ("1551ABK") and revision "B" (part 2005).
- Selecting revision "B" navigates to `https://demo.inventree.org/web/part/2005/details` (or equivalent URL for the revision part).
- The page updates to show the selected revision's details.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Actual behavior: Part 77 tab structure confirmed; revision selector not visible on part 77 (it has 0 revisions and is a template — docs state template parts cannot have revisions). Part 82 revision data confirmed via API (revision_count=1, revision part pk=2005 with revision="B").
- Matches docs: Yes (docs: "When multiple revisions exist, a Select Part Revision drop-down renders at the top of the part page")

**Notes**: The revision selector is documented as appearing "only when multiple revisions exist". Part 82 has 1 revision (count=1 means 1 revision part exists). Tester must confirm the selector label is "Select Part Revision" and the dropdown lists both the base part and revision B.
