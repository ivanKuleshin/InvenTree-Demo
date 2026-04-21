### TC-UI-NEG-003: Submit "Add Part" with Name at 101 characters — rejected above boundary

**Type**: UI / Negative / Boundary
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
5. Type exactly 101 characters into the "Name" field — use the value: `TC-NEG-003-` followed by 90 `A` characters (total = 101 characters)
6. Leave all other fields at their default values
7. Click the "Submit" button
8. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open (does not close)
- A red alert banner appears at the top of the dialog: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error message appears directly below the "Name" field: "Ensure this field has no more than 100 characters."
- No part is created — the form submission is rejected
- No navigation away from the current page occurs

**Observed** (filled during exploration):

- Element confirmed: `text-field-name` — present in Edit Part dialog (same validation applies to Add Part dialog)
- Error message confirmed via Edit Part dialog submission: `"Ensure this field has no more than 100 characters."` appears below the Name field
- API confirmed: POST /api/part/ with 101-char name returns HTTP 400, body `{"name": ["Ensure this field has no more than 100 characters."]}`
- Matches docs: Yes — maxLength 100 enforced server-side

**Notes**: The browser's HTML `maxlength` attribute may not be set on this input, allowing more than 100 characters to be typed. The constraint is enforced server-side after form submission.
