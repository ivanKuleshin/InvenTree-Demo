### TC-UI-NEG-007: Submit "Edit Part" with unrecognized unit string — rejected with invalid unit error

**Type**: UI / Negative
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/{id}/details`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/911/details`
- The part detail page has loaded — page title shows "Part: Blue Furniture Set"

**Steps**:

1. Click the `action-menu-part-actions` button in the part detail page header
2. Click the "Edit" menu item (aria-label: `action-menu-part-actions-edit`)
3. Verify the "Edit Part" dialog has opened
4. Locate the "Units" field (aria-label: `text-field-units`)
5. Clear the current value of the Units field
6. Type the value `boxes` into the "Units" field
7. Click the "Submit" button
8. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error appears directly below the "Units" field: "Invalid physical unit"
- The part is not updated
- No unit change is persisted

**Observed** (filled during exploration):

- Element confirmed: `text-field-units` — present in Edit Part dialog
- Error confirmed in Edit Part dialog submission with value `"boxes"`: inline error `"Invalid physical unit"` appears below the Units field
- API confirmed: POST /api/part/ with `units: "boxes"` returns HTTP 400, body `{"units": ["Invalid physical unit"]}`
- Matches docs: Yes — unrecognized Pint unit strings are rejected server-side

**Notes**: The Units field has no dropdown — it is a plain text input. Validation is entirely server-side; no client-side pre-validation of Pint unit strings occurs before submission.
