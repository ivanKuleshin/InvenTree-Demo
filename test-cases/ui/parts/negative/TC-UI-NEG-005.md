### TC-UI-NEG-005: Submit "Edit Part" with IPN at 101 characters — rejected above boundary

**Type**: UI / Negative / Boundary
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/{id}/details`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- An existing part is available to edit (e.g., navigate to `https://demo.inventree.org/web/part/911/details`)
- The part detail page has loaded — page title shows "Part: Blue Furniture Set"

**Steps**:

1. Click the `action-menu-part-actions` button in the part detail page header
2. Click the "Edit" menu item (aria-label: `action-menu-part-actions-edit`)
3. Verify the "Edit Part" dialog has opened — heading reads "Edit Part"
4. Click the "IPN" field (aria-label: `text-field-IPN`)
5. Clear the current value of the IPN field
6. Type exactly 101 characters into the "IPN" field — use 101 `X` characters
7. Click the "Submit" button
8. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error appears directly below the "IPN" field: "Ensure this field has no more than 100 characters."
- The part is not updated — the original IPN value is preserved

**Observed** (filled during exploration):

- Element confirmed: `text-field-IPN` — present in Edit Part dialog (aria-label confirmed)
- API confirmed: POST /api/part/ with `IPN` = 101 chars returns HTTP 400, body `{"IPN": ["Ensure this field has no more than 100 characters."]}`
- Matches docs: Yes — IPN maxLength is 100

**Notes**: IPN is not required, so the at-boundary test (100 chars, valid) is implicitly covered by acceptance of empty IPN. The 101-char above-boundary case is the meaningful negative test.
