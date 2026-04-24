# Test Case Format

## Overview

Both UI and API test cases use a consistent Markdown structure with the following sections:

- **Title** — Test case ID and description
- **Preconditions** — System state, authentication, data required
- **Steps** — Exact numbered steps to execute
- **Expected Result** — What should happen, exact UI text or API response
- **Observed** — (From manual testing) Actual behavior, field labels, response structures

## Template Location

- **UI Template**: `.claude/skills/ui-manual-testing/references/tc-template.md`
- **API Template**: `.claude/skills/api-manual-testing/references/tc-template.md`

**Always reference the template files for the authoritative format.**

## Key Sections Explained

### Title

```markdown
# TC_UI_PC_001: Create Part with Basic Details

or

# TC_APCRUD_001: Create Part via POST /api/part/
```

Format: `# {TC_ID}: {Brief Description}`

### Preconditions

Describes the system state and setup required:

```markdown
## Preconditions

- Admin user logged in
- At least one active Part Category exists
- Demo data populated
```

For API tests:
```markdown
## Preconditions

- Authentication: Bearer token or HTTP Basic (admin/inventree)
- Category with id=1 exists
- Server is running at https://demo.inventree.org
```

### Steps

Numbered steps to execute. For UI:

```markdown
## Steps

1. Navigate to Part Creation page (Parts → New Part)
2. Enter "TEST-001" in IPN field
3. Enter "Test Part" in Name field
4. Click "Create" button
```

For API:

```markdown
## Steps

1. Prepare POST request to /api/part/
2. Include headers: Content-Type: application/json, Authorization: Bearer {token}
3. Send JSON payload: { "ipn": "TEST-001", "name": "Test Part", "category": 1 }
4. Verify response status code
```

### Expected Result

What should happen (success case):

```markdown
## Expected Result

- Part is created successfully
- User is redirected to Part detail page
- Message displays: "Part created successfully"
- IPN field shows "TEST-001"
- Name field shows "Test Part"
```

For API:

```markdown
## Expected Result

- HTTP Status: 201 Created
- Response includes: { "id": <auto_increment>, "ipn": "TEST-001", "name": "Test Part", "category": 1 }
- All required fields are present in response
```

### Observed

**Only in manual test cases** — Actual behavior from live exploration:

```markdown
## Observed

**UI Elements**:
- IPN field label: "Inventory Part Number (IPN)"
- Name field label: "Part Name"
- Create button label: "Create Part"

**Behavior**:
- Form validates on submit
- Success message: "Part created successfully"
- Redirect URL: /part/{id}/
```

For API:

```markdown
## Observed

**Request**:
```json
POST /api/part/
Content-Type: application/json

{
  "ipn": "TEST-001",
  "name": "Test Part",
  "category": 1
}
```

**Response (201)**:
```json
{
  "id": 42,
  "ipn": "TEST-001",
  "name": "Test Part",
  "category": 1,
  "description": "",
  "active": true,
  "virtual": false,
  ...
}
```
```

## Naming Conventions

### File Names

Use the test case ID exactly as the file name:

```
TC_UI_PC_001.md
TC_UI_AUTH_001.md
TC_APCRUD_001.md
TC_APCATPARAM_002.md
```

### Title in File

The title (first level heading) should include both the ID and description:

```markdown
# TC_UI_PC_001: Create Part with Basic Details
```

## Optional Sections

Test cases may include additional sections as needed:

- **Notes** — Special considerations, known limitations
- **Related Test Cases** — Links to dependent or related tests
- **Test Data** — Specific values or scenarios
- **Edge Cases** — Boundary conditions tested

## Quality Checklist

When writing test cases:

- ✓ Title includes both TC_ID and description
- ✓ Preconditions are clear and complete
- ✓ Steps are numbered and specific
- ✓ Expected Result includes exact UI text or API response format
- ✓ Observed section is grounded in real exploration (with evidence)
- ✓ File name matches TC_ID
- ✓ No hardcoded credentials in test data
- ✓ Mutating flows clearly marked as [MUTATING]
- ✓ Clear separation between precondition setup and actual test steps