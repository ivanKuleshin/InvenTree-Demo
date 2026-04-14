### TC-UI-TABS-005: Validate BOM from the Bill of Materials tab [MUTATING]

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/87/bom`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 87 ("Doohickey") has `assembly=true` and at least one BOM item
- [ ] Navigate to: `https://demo.inventree.org/web/part/87/bom`
- [ ] The "Bill of Materials" tab is active and BOM items are visible

**Steps**:

1. Locate the `bom-validation-info` button or BOM validation status indicator at the top of the BOM table.
2. Observe the current validation status displayed (e.g., "Validated" or "Not Validated" badge/indicator).
3. Click the `action-button-validate-bom` button in the toolbar.
4. If a confirmation dialog appears, click the confirm button.
5. Observe the BOM validation status indicator after the action completes.

**Expected Result**:
- A BOM validation status indicator is visible at the top of the BOM table.
- Clicking `action-button-validate-bom` triggers a validation action (may show a confirmation dialog).
- After validation completes, the status indicator updates to show a "Validated" or green checkmark state.
- No error toast or notification indicating failure.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: `action-button-validate-bom` — present
- Element confirmed: `bom-validation-info` — present
- Actual behavior: Both buttons confirmed present on BOM tab toolbar; validation dialog and result state not directly exercised
- Matches docs: Yes (docs describe "BOM Validation Status" indicator and "Validate BOM" button)

**Notes**: Docs describe two visual states — "Validated" (green checkmark) and "Not Validated" (orange/red with "Validate BOM" button). The `bom-validation-info` button likely shows the current checksum or status details. Tester should record the exact label text of the validation status indicator on first run.
