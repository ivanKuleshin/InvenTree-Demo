### TC-UI-TABS-027: Remove a related part relationship from the Related Parts tab [MUTATING]

**Type**: UI
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/77/related_parts`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 has at least one related part (add one using TC-UI-TABS-026 first if none exist)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/related_parts`
- [ ] The "Related Parts" tab is active and at least one related part row is visible

**Steps**:

1. Locate the related part row in the Related Parts table.
2. Find the delete/remove action icon on the row (typically a trash icon or X icon).
3. Click the delete/remove action icon.
4. Observe that a confirmation dialog or prompt appears.
5. Click the "Confirm" or "Delete" button.
6. Observe the Related Parts table after confirmation.
7. Navigate to the previously related part's "Related Parts" tab and verify the relationship is also gone there.

**Expected Result**:
- Clicking the delete/remove icon opens a confirmation prompt.
- After confirming, the dialog closes and the table refreshes.
- The removed related part row is no longer present in the table.
- The relationship is removed bidirectionally: the other part's "Related Parts" tab also no longer shows part 77.
- Neither part is deleted — only the association between them is removed.

**Observed** (filled during exploration):

- Element confirmed: "Related Parts" tab — present
- Actual behavior: Remove flow not directly exercised; delete icon inferred from docs ("Each row has a remove/delete action icon")
- Matches docs: Yes (docs: "Removing a related part relationship does not delete either part — it only removes the association")

**Notes**: This TC should be run after TC-UI-TABS-026. The key assertion is that removing the association from one side also removes it from the other side (bidirectional relationship).
