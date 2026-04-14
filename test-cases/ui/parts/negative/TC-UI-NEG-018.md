### TC-UI-NEG-018: Submit "Add Part" with revision_of set but revision code left blank — error on Revision field

**Type**: UI / Negative / Relational
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/index/parts`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- An existing non-template part is available to use as the "Revision Of" target (e.g., part named "Blue Furniture Set", pk=911, which is not a template)
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The "Parts" tab panel is active

**Steps**:

1. Click the `action-menu-add-parts` button in the parts table toolbar
2. Click the "Create Part" menu item (aria-label: `action-menu-add-parts-create-part`)
3. Verify the "Add Part" dialog has opened
4. Type `TC-NEG-018-RevTest` into the "Name" field (aria-label: `text-field-name`)
5. Locate the "Revision Of" field (aria-label: `related-field-revision_of`)
6. Click the "Revision Of" combobox and type `Blue Furniture` in the search box
7. Select "Blue Furniture Set" from the dropdown results
8. Leave the "Revision" field (aria-label: `text-field-revision`) empty
9. Click the "Submit" button
10. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error appears below the "Revision" field (aria-label: `text-field-revision`): "Revision code must be specified for a part marked as a revision"
- No part is created

**Observed** (filled during exploration):

- Element confirmed: `related-field-revision_of` — present in Edit Part dialog (and Add Part dialog has same fields)
- Element confirmed: `text-field-revision` — present in Edit Part dialog
- API confirmed: POST /api/part/ with `revision_of: 911` and no `revision` field returns HTTP 400, `{"revision": ["Revision code must be specified for a part marked as a revision"]}`
- Matches docs: Yes — revision code is required when revision_of is set

**Notes**: The `revision_of` combobox searches active parts. Once a "Revision Of" parent is selected, the "Revision" text field becomes effectively required — the system mandates a unique revision code to distinguish this part from other revisions of the same parent.
