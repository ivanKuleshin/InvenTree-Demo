### TC-UI-NEG-010: Submit "Edit Part" with invalid URL in Link field — rejected with URL format error

**Type**: UI / Negative
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
4. Locate the "Link" field (aria-label: `text-field-link`)
5. Clear the current value of the Link field
6. Type the value `not-a-url` into the "Link" field
7. Click the "Submit" button
8. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error appears directly below the "Link" field: "Enter a valid URL."
- The part is not updated

**Observed** (filled during exploration):

- Element confirmed: `text-field-link` — present in Edit Part dialog
- Error confirmed in Edit Part dialog: entering `"not-a-url"` and submitting shows `"Enter a valid URL."` below the Link field
- API confirmed: POST with `link: "not-a-url"` returns HTTP 400, `{"link": ["Enter a valid URL."]}`
- Same error returned for bare domain `"example.com"` (missing scheme) — HTTP 400, `{"link": ["Enter a valid URL."]}`
- Matches docs: Yes — link field requires a valid URI with scheme

**Notes**: Client-side validation does not check URL format before submission — the error appears only after the form is submitted to the server. A valid URL for this field requires a scheme (e.g., `https://`) and a host.
