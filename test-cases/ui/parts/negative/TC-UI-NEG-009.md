### TC-UI-NEG-009: Submit "Edit Part" with Units field exceeding 20 characters — rejected with two errors

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
4. Locate the "Units" field (aria-label: `text-field-units`)
5. Clear the current value of the Units field
6. Type exactly 21 characters into the "Units" field — use 21 `k` characters: `kkkkkkkkkkkkkkkkkkkkk`
7. Click the "Submit" button
8. Observe the dialog state and the number of error messages shown below the "Units" field

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- Two inline error messages appear below the "Units" field (in order):
  1. "Invalid physical unit"
  2. "Ensure this field has no more than 20 characters."
- The part is not updated

**Observed** (filled during exploration):

- API confirmed: POST /api/part/ with `units` = 21-char string returns HTTP 400, body `{"units": ["Invalid physical unit", "Ensure this field has no more than 20 characters."]}` — two error strings in the array
- Matches docs: Yes — both the Pint validation and the maxLength constraint are applied independently and both errors are returned simultaneously

**Notes**: The two errors are returned in a single response; both appear as separate inline messages under the Units field in the UI. Order may vary between renders.
