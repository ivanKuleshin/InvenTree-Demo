### TC-UI-NEG-004: Submit "Add Part" with whitespace-only Name — rejected as blank

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
5. Type three space characters into the "Name" field — the field visually appears non-empty
6. Leave all other fields at their default values
7. Click the "Submit" button
8. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error appears below the "Name" field: "This field may not be blank."
- No part is created — the server strips whitespace and treats the value as blank

**Observed** (filled during exploration):

- API confirmed: POST /api/part/ with `name: "   "` (3 spaces) returns HTTP 400, body `{"name": ["This field may not be blank."]}`
- Same error as empty string — server normalizes whitespace-only strings to blank
- Matches docs: Partially — documentation notes this behavior as "must be confirmed"; confirmed as rejected

**Notes**: This is a security/data-quality boundary: the field appears non-empty to the user but is treated as blank by the server. The UI may not have client-side whitespace validation.
