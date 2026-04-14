### TC-UI-TABS-029: Add a new Test Template from the Test Templates tab [MUTATING]

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/87/test_templates`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 87 ("Doohickey") has `testable=true` and exactly 3 test templates: "Checksum", "Color", "Firmware" (confirmed via API)
- [ ] Navigate to: `https://demo.inventree.org/web/part/87/test_templates`
- [ ] The "Test Templates" tab is active

**Steps**:

1. Locate the "Add Test Template" or equivalent add action button in the Test Templates tab toolbar.
2. Click the "Add Test Template" button.
3. Observe that a form dialog opens.
4. Locate the "Test Name" field and enter `Visual Inspection TC-029`.
5. Locate the "Description" field and enter `Visual check for physical damage`.
6. Locate the "Required" toggle/checkbox and set it to enabled (checked/on).
7. Verify that the "Test Key" field auto-populates based on the name (expected: `visualinspectiontc029` or similar).
8. Leave "Requires Value" and "Requires Attachment" unchecked.
9. Click the "Save" or "Submit" button.
10. Observe the test templates table after the dialog closes.

**Expected Result**:
- A form dialog opens with fields: "Test Name", "Description", "Required" (boolean), "Requires Value" (boolean), "Requires Attachment" (boolean), "Enabled" (boolean).
- The "Test Key" field is either auto-generated (read-only) or auto-fills based on the name.
- After submission, the dialog closes and the table refreshes.
- A new row appears with test name "Visual Inspection TC-029", required=true, a generated test key.
- Total test template count for part 87 becomes 4.

**Observed** (filled during exploration):

- Element confirmed: "Test Templates" tab — present
- Actual behavior: Tab present; add dialog not directly exercised (session expired); test template fields confirmed via API schema
- Matches docs: Yes (docs describe Test Template fields: Test Name, Description, Required, Requires Value, Requires Attachment, Enabled; key is auto-generated from name)

**Notes**: Post-test cleanup: delete the "Visual Inspection TC-029" test template. Docs warn: "Deleting a test template will also delete any associated test results." Since this is a new template with no results, deletion is safe. Docs also state the test key is a "simplified lowercase alphanumeric" string auto-generated from the test name.
