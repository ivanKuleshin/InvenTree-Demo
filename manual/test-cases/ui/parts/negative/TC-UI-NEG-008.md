### TC-UI-NEG-008: Submit "Edit Part" with uppercase unit "KG" — rejected due to case sensitivity

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
4. Locate the "Units" field (aria-label: `text-field-units`)
5. Clear the current value of the Units field
6. Type the value `KG` (uppercase) into the "Units" field
7. Click the "Submit" button
8. Observe the dialog state and any error messages
9. Without closing the dialog, clear the "Units" field and type `kg` (lowercase)
10. Click the "Submit" button again
11. Observe the result

**Expected Result**:

- **Step 7–8 (KG uppercase):** Dialog remains open; inline error below "Units" field shows "Invalid physical unit"; part is not updated
- **Step 9–11 (kg lowercase):** Dialog closes successfully; toast notification "Success — Item Saved" appears; the part's unit is updated to `kg`

**Observed** (filled during exploration):

- Error confirmed in Edit Part dialog: entering `"KG"` and submitting shows `"Invalid physical unit"` below the Units field
- API confirmed: POST with `units: "KG"` returns HTTP 400, `{"units": ["Invalid physical unit"]}`
- API confirmed: POST with `units: "kg"` returns HTTP 201 — valid lowercase accepted
- Matches docs: Yes — Pint unit strings are case-sensitive; `kg` valid, `KG` invalid

**Notes**: This test covers the case-sensitivity rule in one TC by testing both the failing and the passing value. The lowercase `kg` submission in step 10 is a mutating action that updates the demo part's units — restore the original value afterwards if needed.
