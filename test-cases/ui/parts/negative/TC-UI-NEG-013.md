### TC-UI-NEG-013: Default Expiry at integer maximum (2147483647) accepted; one above rejected

**Type**: UI / Boundary
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
4. Locate the "Default Expiry" field (aria-label: `number-field-default_expiry`)
5. Clear the current value of the "Default Expiry" field
6. Type the value `2147483647` into the "Default Expiry" field
7. Click the "Submit" button
8. Observe the result — if the dialog closes, the maximum value was accepted
9. Re-open the "Edit Part" dialog via `action-menu-part-actions` → "Edit"
10. Locate the "Default Expiry" field and clear its value
11. Type the value `2147483648` into the "Default Expiry" field
12. Click the "Submit" button
13. Observe the dialog state and any error messages

**Expected Result**:

- **Steps 6–8 (2147483647):** Dialog closes; toast "Success — Item Saved" appears; part is updated with expiry = 2147483647
- **Steps 11–13 (2147483648):** Dialog remains open; inline error below "Default Expiry": "Ensure this value is less than or equal to 2147483647."; part is not updated

**Observed** (filled during exploration):

- Element confirmed: `number-field-default_expiry` — present in Edit Part dialog
- API confirmed: POST with `default_expiry: 2147483647` returns HTTP 201 — at-boundary max accepted
- API confirmed: POST with `default_expiry: 2147483648` returns HTTP 400, `{"default_expiry": ["Ensure this value is less than or equal to 2147483647."]}`
- Matches docs: Yes — maximum is `2^31 - 1` (Django IntegerField max)

**Notes**: [MUTATING] — Step 7 updates the demo part's default_expiry to 2147483647. Restore the original value (0) by re-editing after the test. The number input in the browser UI may render this large value in scientific notation or truncate it — the tester should verify the typed value is correctly passed to the server.
