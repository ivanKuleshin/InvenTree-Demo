### TC-UI-TABS-020: Create a new revision of a part via the Duplicate Part action [MUTATING]

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/87/details`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 87 ("Doohickey") has `is_template=false` (non-template parts can have revisions)
- [ ] Navigate to: `https://demo.inventree.org/web/part/87/details`

**Steps**:

1. On the Part 87 detail page, locate the part actions menu button (three vertical dots, aria-label `action-menu-part-actions`) in the top-right of the part detail header.
2. Click the `action-menu-part-actions` button to open the actions dropdown menu.
3. Locate and click the "Duplicate Part" option in the dropdown menu.
4. Observe that the "Duplicate Part" form dialog opens.
5. In the form, locate the "Revision Of" field and verify it is pre-populated with part 87 ("Doohickey").
6. Locate the "Revision" field (text input) and enter `C`.
7. Change the "Name" field to `Doohickey Rev C`.
8. Click the "Submit" button.
9. Observe the result and any navigation that occurs.

**Expected Result**:
- The "Duplicate Part" dialog opens with standard part fields pre-filled from part 87.
- The "Revision Of" field references part 87 ("Doohickey").
- The "Revision" field is editable and accepts the value `C`.
- After clicking "Submit", a new part is created as a revision of part 87.
- The "Select Part Revision" dropdown becomes visible on part 87's detail page (or the page navigates to the new revision).

**Observed** (filled during exploration):

- Element confirmed: `action-menu-part-actions` button — present in part detail header
- Actual behavior: Action menu button confirmed present; "Duplicate Part" option label confirmed from docs
- Matches docs: Yes (docs: "Click on the part actions menu → Select the Duplicate Part action")

**Notes**: Docs specify: set "Revision Of" = original part, set "Revision" = unique code. Post-test cleanup: delete the "Doohickey Rev C" part created in this TC.
