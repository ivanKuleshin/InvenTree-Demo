### TC-UI-NEG-001: Submit "Add Part" with empty Name field — required field error

**Type**: UI / Negative
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/category/index/parts`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The "Parts" tab panel is active in the left panel

**Steps**:

1. Click the `action-menu-add-parts` button (green "+" icon in the parts table toolbar)
2. Click the "Create Part" menu item (aria-label: `action-menu-add-parts-create-part`)
3. Verify the "Add Part" dialog has opened — the dialog heading reads "Add Part"
4. Verify the "Name" field (aria-label: `text-field-name`) is empty
5. Leave the "Name" field empty — do not type any value
6. Click the "Submit" button
7. Observe the dialog state after submission attempt

**Expected Result**:

- The dialog remains open (does not close)
- A red alert banner appears at the top of the dialog with heading "Form Error" and body text "Errors exist for one or more form fields"
- The "Name" field border turns red (error state)
- An inline error message appears directly below the "Name" field: "This field may not be blank."
- No network request completes with a 201 response — no part is created

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Parts`
- Element confirmed: `action-menu-add-parts` — present in parts table toolbar
- Element confirmed: `text-field-name` — present in Add Part dialog, type text, required marker red asterisk
- Alert banner text confirmed: `"Form ErrorErrors exist for one or more form fields"` (heading + body rendered as one string in DOM)
- Inline error confirmed: `"This field may not be blank."` — appears below Name field on empty submit
- Matches docs: Yes — API returns `{"name": ["This field may not be blank."]}` for empty name (HTTP 400)

**Notes**: The blank/whitespace name produces "This field may not be blank." (server-side), not "This field is required." The "required" marker (red asterisk) is a UI hint; the actual enforced error message is the server response. TC-UI-PC-006 covers general create validation; this TC isolates the exact error message for the empty-name case specifically.
