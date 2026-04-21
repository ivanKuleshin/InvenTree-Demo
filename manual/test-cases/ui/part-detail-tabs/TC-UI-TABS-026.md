### TC-UI-TABS-026: Add a related part from the Related Parts tab [MUTATING]

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/77/related_parts`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") and Part 87 ("Doohickey") both exist
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/related_parts`
- [ ] The "Related Parts" tab is active

**Steps**:

1. Locate the "Add Related Part" or equivalent add action button in the Related Parts tab toolbar.
2. Click the "Add Related Part" button.
3. Observe that a part selector dialog or form opens.
4. In the part selector, search for and select "Doohickey" (Part 87).
5. Confirm the selection (click "Save" or "Submit" or equivalent confirm button).
6. Observe the Related Parts table after the dialog closes.

**Expected Result**:
- An "Add Related Part" dialog opens with a part selector input.
- Searching for "Doohickey" returns Part 87 in the selector results.
- After selecting Part 87 and confirming, the dialog closes.
- The Related Parts table refreshes and shows "Doohickey" as a related part row.
- The relationship is bidirectional: navigating to Part 87's "Related Parts" tab also shows "Widget Assembly" as a related part.

**Observed** (filled during exploration):

- Element confirmed: "Related Parts" tab — present
- Actual behavior: Tab presence confirmed; "Add Related Part" dialog not directly exercised (session expired)
- Matches docs: Yes (docs describe: "Click Add Related Part button → Select part in selector → Confirm")

**Notes**: Post-test cleanup: remove the related part relationship between part 77 and part 87 after this TC. Docs confirm the relationship is bidirectional — removing it from either part's view removes it from both.
