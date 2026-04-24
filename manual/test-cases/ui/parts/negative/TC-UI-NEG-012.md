### TC-UI-NEG-012: Submit "Edit Part" with Default Expiry set to -1 — rejected below minimum

**Type**: UI / Negative / Boundary
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/{id}/details`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/911/details`
- The part detail page has loaded — page title shows "Part: Blue Furniture Set"

**Steps**:

1. Click the `action-menu-part-actions` button in the part detail page header
2. Click the "Edit" menu item (aria-label: `action-menu-part-actions-edit`)
3. Verify the "Edit Part" dialog has opened
4. Locate the "Default Expiry" field (aria-label: `number-field-default_expiry`) — it shows the current value (default: `0`)
5. Clear the current value of the "Default Expiry" field
6. Type the value `-1` into the "Default Expiry" field
7. Click the "Submit" button
8. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error appears directly below the "Default Expiry" field: "Ensure this value is greater than or equal to 0."
- The part is not updated — the expiry value remains at its previous setting

**Observed** (filled during exploration):

- Element confirmed: `number-field-default_expiry` — present in Edit Part dialog, type number/spinbutton
- Error confirmed in Edit Part dialog submission with value `-1`: `"Ensure this value is greater than or equal to 0."` appears below the Default Expiry field
- API confirmed: POST /api/part/ with `default_expiry: -1` returns HTTP 400, `{"default_expiry": ["Ensure this value is greater than or equal to 0."]}`
- Matches docs: Yes — minimum schema value for default_expiry is 0

**Notes**: The minimum valid value is `0` (meaning no expiry / not set). Any negative integer is rejected.
