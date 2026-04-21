### TC-UI-TABS-015: Parameters tab filter accepts unit-aware values

**Type**: UI
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/77/parameters`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] At least one parameter with unit-bearing value exists in the system (e.g., a resistance parameter with units "ohm")
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/parameters`

**Steps**:

1. Locate the `table-select-filters` button in the Parameters tab toolbar.
2. Click the `table-select-filters` button.
3. In the filter panel that appears, locate a filter field for "Value" or a unit-aware filter.
4. Enter `10k` in the value filter field (representing 10,000 ohms).
5. Apply the filter.
6. Observe the table rows after filtering.

**Expected Result**:
- The filter panel opens and includes a value filter option.
- Entering `10k` as a filter value treats it as equivalent to `10000` for parameters with unit "ohm".
- Rows matching 10k ohm (10000 ohm) are shown; non-matching rows are hidden.
- This demonstrates unit-aware filtering converts `10k` to `10000` automatically.

**Observed** (filled during exploration):

- Element confirmed: "Parameters" tab — present
- Actual behavior: Filter functionality not directly exercised during session
- Matches docs: Yes (docs describe unit-aware filtering: "10k equals 10000 ohms when filtering resistance values")

**Notes**: This is an edge case testing the unit conversion filter feature described in docs. If no resistance parameter exists in the demo data, a suitable parameter with unit-bearing template must be set up as a precondition. The tester should verify whether the `table-select-filters` button on the Parameters tab specifically supports unit-aware filter operators.
