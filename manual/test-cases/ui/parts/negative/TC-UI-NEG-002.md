### TC-UI-NEG-002: Submit "Add Part" with Name at exactly 100 characters — accepted at boundary

**Type**: UI / Boundary
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/index/parts`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The "Parts" tab panel is active

**Steps**:

1. Click the `action-menu-add-parts` button in the parts table toolbar
2. Click the "Create Part" menu item (aria-label: `action-menu-add-parts-create-part`)
3. Verify the "Add Part" dialog has opened
4. Click the "Name" field (aria-label: `text-field-name`)
5. Type exactly 100 characters into the "Name" field — use the value: `TC-NEG-002-` followed by 89 `A` characters (total = 100 characters)
6. Verify the field contains exactly 100 characters before submitting
7. Leave all other fields at their default values
8. Click the "Submit" button
9. Observe the result

**Expected Result**:

- The dialog closes successfully
- The browser navigates to the new part detail page at a URL matching `https://demo.inventree.org/web/part/{id}/details`
- A green toast notification appears with heading "Success" and body "Item Created"
- The part detail page heading contains the 100-character name
- No validation error is shown — 100 characters is the valid maximum for the Name field

**Observed** (filled during exploration):

- Element confirmed: `text-field-name` — present in Add Part dialog
- API confirmed: POST /api/part/ with `name` = 100 characters returns HTTP 201 (accepted at boundary)
- Matches docs: Yes — field maxLength is 100; at-boundary value is valid

**Notes**: [MUTATING] — this TC creates a part on the demo server. Clean up the created part after the test run by navigating to the part detail page and using "Delete" from the part actions menu (the part must be set to inactive first).
