### TC-UI-TABS-024: Delete an existing attachment from the Attachments tab [MUTATING]

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/77/attachments`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] At least one attachment exists for part 77 (upload one using TC-UI-TABS-023 first if none exist)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/attachments`
- [ ] The "Attachments" tab is active and at least one attachment row is visible

**Steps**:

1. Locate an existing attachment row in the Attachments table.
2. Find the delete action icon (trash icon or similar delete button) on the attachment row.
3. Click the delete action icon for the attachment.
4. Observe that a confirmation dialog appears asking to confirm the deletion.
5. Click the "Confirm" or "Delete" button in the confirmation dialog.
6. Observe the Attachments table after confirmation.

**Expected Result**:
- Clicking the delete icon on an attachment row opens a confirmation dialog.
- After confirming deletion, the dialog closes and the Attachments table refreshes.
- The deleted attachment row is no longer present in the table.
- If it was the only attachment, the table returns to the empty state.

**Observed** (filled during exploration):

- Element confirmed: "Attachments" tab — present
- Actual behavior: Delete flow not directly exercised; delete icon presence inferred from docs description ("Each row has a download link and a delete action")
- Matches docs: Yes (docs describe delete action per row)

**Notes**: Docs explicitly show a delete action icon on each attachment row. A confirmation step is standard for destructive operations in InvenTree. Tester should confirm whether a confirmation dialog appears or whether deletion is immediate.
