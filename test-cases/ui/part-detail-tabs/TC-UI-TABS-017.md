### TC-UI-TABS-017: Create a new variant from the Variants tab [MUTATING]

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/77/variants`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has `is_template=true`
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/variants`
- [ ] The "Variants" tab is active

**Steps**:

1. Locate the "New Variant" action button in the Variants tab toolbar.
2. Click the "New Variant" button.
3. Observe that the "Duplicate Part" form dialog opens.
4. In the dialog, observe that fields are pre-filled from the template part (Name, Description, IPN, etc.).
5. Change the "Name" field to `Widget Assembly TC-017 Test Variant`.
6. Change the "IPN" field to `WID-TC017`.
7. Click the "Submit" button.
8. Observe the result after submission.

**Expected Result**:
- Clicking "New Variant" opens a "Duplicate Part" form dialog.
- Form fields are pre-filled from the template part (Name="Widget Assembly", Description="An assembled widget", etc.).
- After modifying the Name and IPN and clicking "Submit", a new variant part is created.
- The new variant appears in the Variants tab table.
- The new variant's detail page (if navigated to) shows the "Template Part" reference pointing to part 77.

**Observed** (filled during exploration):

- Element confirmed: "Variants" tab — present
- Actual behavior: Tab present; "New Variant" button existence not directly confirmed (session expired); docs state "New Variant" button exists on the Variants tab
- Matches docs: Yes (docs describe: "Click on the New Variant button" → "Duplicate Part form will be displayed")

**Notes**: Docs specify the form name is "Duplicate Part". Post-creation cleanup is needed — delete `WID-TC017` after this test to keep the demo environment clean.
