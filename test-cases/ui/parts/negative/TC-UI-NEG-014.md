### TC-UI-NEG-014: Submit "Edit Part" with Minimum Stock set to -1 — rejected below minimum

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
4. Scroll down in the dialog to locate the "Minimum Stock" field (aria-label: `number-field-minimum_stock`) — it shows the current value (default: `0`)
5. Clear the current value of the "Minimum Stock" field
6. Type the value `-1` into the "Minimum Stock" field
7. Click the "Submit" button
8. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error appears directly below the "Minimum Stock" field: "Ensure this value is greater than or equal to 0."
- The part is not updated

**Observed** (filled during exploration):

- Element confirmed: `number-field-minimum_stock` — present in Edit Part dialog
- Error confirmed in Edit Part dialog submission with value `-1`: `"Ensure this value is greater than or equal to 0."` appears below the Minimum Stock field
- API confirmed: POST /api/part/ with `minimum_stock: -1` returns HTTP 400, `{"minimum_stock": ["Ensure this value is greater than or equal to 0."]}`
- Divergence from docs: Documentation states "no schema-level minimum" for minimum_stock — however the server DOES enforce a minimum of 0. The OpenAPI schema minimum property is absent but Django model validation rejects negative values.
- Matches docs: No — docs state minimum_stock has no documented minimum; actual behavior rejects negative values

**Notes**: This is a documentation divergence — the OpenAPI schema does not list a `minimum` constraint for `minimum_stock`, but the Django model-level validator enforces `>= 0`. The UI error message is identical to the one for `default_expiry < 0`.
