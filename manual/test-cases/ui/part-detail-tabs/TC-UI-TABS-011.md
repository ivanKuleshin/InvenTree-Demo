### TC-UI-TABS-011: Create a new Build Order from the Build Orders tab [MUTATING]

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/builds`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has `assembly=true`
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/builds`
- [ ] The "Build Orders" tab is active

**Steps**:

1. Locate and click the action button for creating a new build order (look for a button with label containing "New Build Order" or a `+` add-type button in the toolbar).
2. Observe that a "New Build Order" dialog or form opens.
3. In the form, locate the "Reference" field and note the auto-generated reference value (or leave it as default).
4. Locate the "Quantity" field and enter `2`.
5. Locate the "Title" or "Description" field and enter `Test Build TC-011`.
6. Click the "Save" or "Submit" button.
7. Observe the build orders table after the dialog closes.

**Expected Result**:
- A "New Build Order" dialog opens with fields for Reference, Quantity, Description, and optional fields (Sales Order, Source Location, Destination Location, Start Date, Target Date, Responsible Party, Notes).
- The Reference field shows an auto-generated value matching the configured reference pattern.
- After submission, the dialog closes and the build orders table refreshes.
- A new build order row appears in the table with quantity 2 and status "Pending".

**Observed** (filled during exploration):

- Element confirmed: "Build Orders" tab — present
- Actual behavior: Full dialog not exercised; build order creation confirmed possible via API (builds exist for part 77)
- Matches docs: Yes (docs describe creating Build Orders from the Part Detail Page)

**Notes**: Docs describe Build Order parameters: Reference, Description, Quantity, Sales Order, Source Location, Destination Location, Start Date, Target Date, Responsible Party, Notes. Tester should record the exact dialog field labels on first run.
