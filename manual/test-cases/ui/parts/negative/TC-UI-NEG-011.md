### TC-UI-NEG-011: Submit "Edit Part" with javascript: scheme in Link field — rejected as invalid URL

**Type**: UI / Negative / Security
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
6. Type the value `javascript:alert(1)` into the "Link" field
7. Click the "Submit" button
8. Observe the dialog state and any error messages

**Expected Result**:

- The dialog remains open
- A red alert banner appears: heading "Form Error", body "Errors exist for one or more form fields"
- An inline error appears directly below the "Link" field: "Enter a valid URL."
- The `javascript:` scheme is rejected — no XSS payload is stored or executed
- The part is not updated

**Observed** (filled during exploration):

- API confirmed: POST /api/part/ with `link: "javascript:alert(1)"` returns HTTP 400, `{"link": ["Enter a valid URL."]}`
- Django's URL validator rejects non-http/https/ftp schemes
- Matches docs: Yes — link field has `format: uri` and Django URL validator enforces http/https schemes

**Notes**: This test verifies that the server-side URL validator rejects JavaScript pseudo-URLs that could be used for XSS if rendered as clickable links in the UI. The rejection is consistent with Django's built-in `URLValidator`.
