# Quick Tips and Best Practices

## Before You Start

- **Check existing coverage** — Browse `test-cases/ui/` and `test-cases/api/` to see what's already covered
- **Understand the test ID pattern** — Use the right format to avoid duplicates and ensure searchability
- **Know the demo credentials** — Username: `admin`, Password: `inventree` (never embed in files)
- **Check test case format** — Reference the tc-template.md files before writing

## During Exploration

### Logging and Documentation

- **Log everything** — Create a `-ui-manual-action-log.md` or `-api-manual-log.md` file while exploring
- **Record exact labels** — Copy exact UI field labels, button text, validation messages (not paraphrases)
- **Record request/response** — For API tests, capture actual JSON payloads and responses
- **Note timestamps** — Include date/time or API version info for context
- **Screenshot step references** — For UI, describe what page or form state you're in

### What to Record

**For UI exploration**:
- Exact page title
- Exact field labels (copy from UI)
- Exact button labels
- Placeholder text
- Required field indicators
- Error messages (exact text)
- Navigation paths (from home to target page)
- Form validation behavior

**For API exploration**:
- Full request: method, URL, headers, body
- Full response: status code, headers, body (prettified)
- Response field types (string, integer, boolean, array, object)
- Required vs optional fields
- Error responses (4xx, 5xx) with exact error messages
- Pagination details (limit, offset, total count)
- Field constraints (max length, regex patterns, enums)

## Writing Test Cases

### Quality Practices

- **Evidence-based** — Everything in test cases must be grounded in actual exploration (not assumptions)
- **Specific, not generic** — "Navigate to Part Creation page" is too vague; specify the exact URL or menu path
- **Exact UI text** — Copy field labels and button text exactly as they appear; don't paraphrase
- **Self-documenting** — Test case should be understandable without reading the exploration logs
- **Traceability** — Cross-reference related test cases and link to automation implementations

### Structure Tips

- **Preconditions first** — List all setup requirements before test steps
- **Atomic steps** — Each step should be a single action (click, type, request, etc.)
- **Expected result clarity** — Include exact messages, URLs, or response fields that should be present
- **Observed section depth** — Include enough detail that an automation engineer can implement the test without re-exploring

### Naming and Organization

- **Consistent IDs** — Follow the pattern strictly (e.g., `TC_UI_PC_001`, not `TC_UI_PC_1` or `TC_UI_Parts_001`)
- **File name = TC_ID** — Make it easy to locate tests by ID
- **Group by feature** — Keep related tests in the same folder
- **Archive superseded tests** — Move old tests to `archive/` subfolder instead of deleting

## Managing Mutating Flows

**Mutating flows are dangerous in a shared demo environment** — they modify data.

- **Flag with [MUTATING]** — Mark create, update, delete, and import operations
- **Plan before exploring** — Get user confirmation before testing mutating flows
- **Use test data wisely** — Create parts with distinctive IPN values (e.g., `TEST-001-MANUAL-20260422`)
- **Avoid pollution** — Don't create dozens of test parts; be surgical
- **Consider cleanup** — For long-running tests, plan for data cleanup or use fixture data

## When to Use Each Skill

### Use `/ui-manual-testing` for:
- Exploring page layouts and navigation
- Testing form submissions and validation
- Checking UI error messages
- Testing role-based access (admin vs read-only)
- Part detail view, category views, import flows

### Use `/api-manual-testing` for:
- Testing CRUD endpoints
- Field-level validation (required, max length, types)
- Testing filtering and pagination
- Testing error responses (4xx, 5xx)
- Testing authorization and access control
- Testing relational constraints

### Use `/requirements-researcher` for:
- Creating documentation snapshots
- Extracting API schema details
- Learning about features before exploration
- Creating reference material for other agents

## Troubleshooting

### Test Case Not Found
- Check the `test-cases/` folder for the exact ID
- Use `test-case-organization.md` to understand the folder structure
- Archive test cases may be in a subfolder

### API Response Differs from Docs
- **This is gold** — Document the actual behavior in Observed section
- This discrepancy is exactly what manual testing should catch
- Include the difference in the test case for automation engineers

### Demo Data Changed
- The demo may reset periodically or be modified
- If preconditions are no longer met (e.g., "assume category exists"), update or mark the test as affected
- Consider creating self-contained tests that create their own data

### Authentication Issues
- Demo credentials: `admin` / `inventree`
- UI tests use HTTP Basic auth (embedded in URL or browser login)
- API tests may use Bearer tokens or HTTP Basic
- Always note the auth method in Preconditions

## Pro Tips

- **Batch exploration** — Plan to explore multiple related features in one session to minimize back-and-forth
- **Cross-reference** — Link related test cases (e.g., "See TC_UI_PC_001 for part creation flow")
- **Version annotations** — Note the InvenTree version or API version tested
- **Review before submit** — Have another team member review test cases for clarity before handing off to automation
- **Keep logs** — Archive exploration logs in test case folders for future reference