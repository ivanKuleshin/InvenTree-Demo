# TC-UI-PC-009: Initial Stock accordion — create part with initial stock quantity and location

**Type**: UI
**Priority**: P2
**Page URL**: `https://demo.inventree.org/web/part/category/index/parts`

**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Navigate to `https://demo.inventree.org/web/part/category/index/parts`
- The "Parts" tab is selected
- At least one stock location exists in the system (confirmed: demo has stock locations)

**Steps**:

| # | Action | Expected Result |
|---|--------|-----------------|
| 1 | Click the `action-menu-add-parts` button (green "+" icon in the table toolbar) | A dropdown menu appears with two items: "Create Part" and "Import from File" |
| 2 | Click the "Create Part" menu item (`aria-label="action-menu-add-parts-create-part"`) | The "Add Part" modal dialog opens with title "**Add Part**" |
| 3 | Scroll to the bottom of the dialog and locate the "**Initial Stock**" section header button | A button labeled "**Initial Stock**" is visible with `aria-expanded="true"` — the section is expanded by default |
| 4 | Verify the "**Initial Stock Quantity \***" field is visible with default value `0` | The field (aria-label: `number-field-initial_stock.quantity`) is visible, marked required (`*`), and shows value `0` |
| 5 | Verify the "**Initial Stock Location**" field is visible with placeholder "Search..." | The field (aria-label: `related-field-initial_stock.location`) is visible and empty |
| 6 | Click the "**Initial Stock**" button to collapse the section | The section collapses: `aria-expanded` changes to `"false"`, the panel becomes hidden (`aria-hidden="true"`, `display: none`) |
| 7 | Click the "**Initial Stock**" button again to re-expand the section | The section expands: `aria-expanded` changes to `"true"`, the "Initial Stock Quantity" and "Initial Stock Location" fields are visible again |
| 8 | Type `TC-UI-PC-009-StockPart` in the "**Name**" field (`aria-label="text-field-name"`) | The Name field shows "TC-UI-PC-009-StockPart" |
| 9 | Clear the "**Initial Stock Quantity**" field and type `5` | The field shows `5` |
| 10 | Click the "**Initial Stock Location**" combobox (`aria-label="related-field-initial_stock.location"`) and type `stores` in the search field | A dropdown list appears showing stock location options matching "stores" |
| 11 | Select the first matching stock location from the dropdown | The "Initial Stock Location" field displays the selected location name |
| 12 | Click the "**Submit**" button | The dialog closes; the browser navigates to the new part detail page |
| 13 | Verify the URL matches `https://demo.inventree.org/web/part/{id}/details` | URL contains `/web/part/` followed by a numeric ID and `/details` |
| 14 | Verify the page heading | Heading reads "**Part: TC-UI-PC-009-StockPart**" |
| 15 | Verify the success toast notification | A green notification with heading "**Success**" and body "**Item Created**" is visible |
| 16 | Verify the stock badge in the part header | The stock badge shows a numeric value (e.g., "5") rather than "**NO STOCK**", confirming initial stock was created |

**Expected Result**: The part is created with the specified initial stock quantity allocated to the selected location. The part detail page shows stock greater than zero.

**Observed** (filled during exploration):

- Page title confirmed: "InvenTree Demo Server | Parts"
- "Add Part" dialog confirmed open via `action-menu-add-parts` → "Create Part"
- "Initial Stock" accordion button confirmed: `aria-expanded="true"` by default (section is open on dialog load)
- Accordion control ID pattern: `mantine-*-control-OpenByDefault`; panel ID pattern: `mantine-*-panel-OpenByDefault`
- When collapsed: `aria-expanded="false"`, panel `aria-hidden="true"`, `display: none`, `height: 0px`
- "Initial Stock Quantity" field: `aria-label="number-field-initial_stock.quantity"`, `name="initial_stock.quantity"`, type=`decimal`, required=`true`, default value=`0`
- "Initial Stock Location" field: `aria-label="related-field-initial_stock.location"`, React-Select combobox, placeholder "Search..."
- Matches docs: Yes — accordion open by default, quantity required, location optional

**Notes**: When `Initial Stock Quantity` is `0`, no stock item is created (documented behavior). This TC sets quantity to `5` to verify actual stock creation.
