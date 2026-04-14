### TC-UI-NEG-017: Submit duplicate part (same name + IPN + revision) — uniqueness constraint error in banner

**Type**: UI / Negative
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/911/details`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Part pk=911 ("Blue Furniture Set") exists
- Navigate to `https://demo.inventree.org/web/part/911/details`
- The part detail page has loaded — page title reads "Part: Blue Furniture Set"

**Steps**:

1. Click the `action-menu-part-actions` button in the part detail page header
2. Click the "Duplicate" menu item (aria-label: `action-menu-part-actions-duplicate`)
3. Verify the "Add Part" dialog opens with the "Name" field pre-filled with `Blue Furniture Set`
4. Leave the Name, IPN, and Revision fields unchanged (keep the same values as the original part)
5. Click the "Submit" button
6. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears at the top of the dialog with:
  - Heading: "Form Error"
  - Body: "The fields name, IPN, revision must make a unique set."
- No inline error appears below any individual field — the error is a non-field (form-level) error only
- No new part is created

**Observed** (filled during exploration):

- Duplicate dialog confirmed: opened via `action-menu-part-actions-duplicate`, prefilled Name = `"Blue Furniture Set"`
- Banner text confirmed after submitting unchanged: `"Form ErrorThe fields name, IPN, revision must make a unique set."`
- Confirmed: no inline field-level error below Name, IPN, or Revision — the uniqueness error appears only in the top-level alert banner
- API confirmed: POST with duplicate name+IPN+revision returns HTTP 400, `{"non_field_errors": ["The fields name, IPN, revision must make a unique set."]}`
- Matches docs: Yes — combination (name, IPN, revision) must be unique

**Notes**: The duplicate uniqueness error appears as a `non_field_errors` response key, which the UI renders in the top banner without attaching it to a specific field. This is distinct from field-level errors (which appear below the specific field). To make the duplicate succeed, at least one of name, IPN, or revision must differ from the original part.
