### TC-UI-NEG-006: Submit "Edit Part" with Description at 251 characters — rejected above boundary

**Type**: UI / Negative / Boundary
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/{id}/details`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/911/details`
- The part detail page has loaded — page title shows "Part: Blue Furniture Set"

**Steps**:

1. Click the `action-menu-part-actions` button in the part detail page header
2. Click the "Edit" menu item (aria-label: `action-menu-part-actions-edit`)
3. Verify the "Edit Part" dialog has opened
4. Click the "Description" field (aria-label: `text-field-description`)
5. Clear the current value of the Description field
6. Type exactly 251 characters into the "Description" field — use 251 `D` characters
7. Click the "Submit" button
8. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error appears directly below the "Description" field: "Ensure this field has no more than 250 characters."
- The part is not updated

**Observed** (filled during exploration):

- Element confirmed: `text-field-description` — present in Edit Part dialog
- API confirmed: POST /api/part/ with `description` = 251 chars returns HTTP 400, body `{"description": ["Ensure this field has no more than 250 characters."]}`
- Matches docs: Yes — description maxLength is 250

**Notes**: At-boundary value (250 chars) is accepted; 251 chars is the first invalid length.
