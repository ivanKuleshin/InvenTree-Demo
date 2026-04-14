### TC-UI-TABS-014: Add a parameter to a part from the Parameters tab [MUTATING]

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/77/parameters`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] At least one parameter template exists in the system (e.g., accessible from admin settings)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/parameters`
- [ ] The "Parameters" tab is active

**Steps**:

1. Locate the "Add Parameter" or equivalent add action button in the toolbar of the Parameters tab.
2. Click the add parameter button.
3. Observe that a dialog or form opens.
4. In the dialog, locate the "Template" or "Parameter Template" selector and select an available parameter template.
5. Locate the "Value" field and enter `100`.
6. Click the "Save" or "Submit" button.
7. Observe the parameters table after the dialog closes.

**Expected Result**:
- An "Add Parameter" dialog opens containing a "Template" selector and a "Value" field.
- After selecting a template and entering a value, clicking "Save" closes the dialog.
- The parameters table refreshes and shows the new parameter row with the selected template name and value "100".

**Observed** (filled during exploration):

- Element confirmed: "Parameters" tab — present
- Actual behavior: Tab presence confirmed; dialog labels not directly observed
- Matches docs: Yes (docs describe adding parameters via the "Parameters" tab)

**Notes**: Docs describe parameters as having a Template reference and a value. Units are defined by the template, not entered during instance creation. The selector may display a list of template names defined in admin settings.
