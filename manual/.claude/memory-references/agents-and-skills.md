# Key Agents and Skills

## Agents (`.claude/agents/`)

### manual-qa-agent

**Role**: Senior QA engineer with exploratory testing mindset

**Purpose**: The core agentic persona for manual testing workflows. This agent:
- Uses a senior QA engineer perspective to understand features deeply
- Performs ad-hoc and exploratory testing (not just scripted scenarios)
- Observes real application behavior and creates evidence-based test cases
- Combines documentation knowledge with hands-on exploration

**Used by**: ui-manual-testing and api-manual-testing skills

**File**: `.claude/agents/manual-qa-agent.md`

### requirements-researcher

**Role**: Documentation analyst and knowledge extractor

**Purpose**: Loads InvenTree documentation and creates reusable snapshots

**Capabilities**:
- Queries official InvenTree documentation
- Extracts API endpoint details, schemas, and feature descriptions
- Creates Markdown snapshots for agent reuse
- Organizes documentation by topic

**Output**: Saved to `docs/api/` for use by other agents and skills

**File**: `.claude/agents/requirements-researcher.md`

## Skills (`.claude/skills/`)

### ui-manual-testing

**Invocation**: `/ui-manual-testing [feature area]`

**Purpose**: Explore the InvenTree web UI and create grounded test cases

**Workflow** (4 phases):

1. **Load Knowledge**
   - Reads existing docs from `docs/api/`
   - Loads InvenTree's official UI documentation
   - Understands feature requirements and constraints

2. **Plan Coverage**
   - Creates a coverage plan table (pages, flows, auth requirements)
   - Flags mutating flows (create/edit/delete) with `[MUTATING]`
   - Asks for user confirmation before exploration

3. **Explore**
   - Uses Playwright MCP to fetch live pages from https://demo.inventree.org
   - Records all UI elements: labels, field names, buttons, placeholders, validation messages
   - Logs steps in a `-ui-manual-action-log.md` file for reference
   - Tests form submissions and navigations

4. **Document**
   - Writes test cases to `test-cases/ui/{suite}/` using the tc-template format
   - Includes **Observed** section with actual UI behavior and element labels
   - Names files by test ID: `TC_UI_<AREA>_<NUMBER>.md`

**Test Case Format**: See `.claude/skills/ui-manual-testing/references/tc-template.md`

**Template Reference**: `.claude/skills/ui-manual-testing/references/tc-template.md`

### api-manual-testing

**Invocation**: `/api-manual-testing [endpoint or feature area]`

**Purpose**: Explore the InvenTree REST API and create grounded test cases

**Workflow** (4 phases):

1. **Load Knowledge**
   - Reads API docs from `docs/api/`
   - Understands endpoint schemas, query parameters, response structures
   - Identifies required fields, field constraints, and error scenarios

2. **Plan Coverage**
   - Creates a coverage plan (endpoints, HTTP methods, scenarios)
   - Flags validation cases, edge cases, and error scenarios
   - Asks for user confirmation before testing

3. **Explore**
   - Makes live HTTP requests to the demo API (https://demo.inventree.org/api/)
   - Records response structures, status codes, field types
   - Tests error scenarios: missing fields, invalid values, unauthorized access
   - Logs requests and responses in a `-api-manual-log.md` file

4. **Document**
   - Writes test cases to `test-cases/api/{suite}/` using the tc-template format
   - Includes **Observed** section with actual API responses and field definitions
   - Names files by test ID: `TC_AP<TYPE>_<NUMBER>.md`

**Test Case Format**: See `.claude/skills/api-manual-testing/references/tc-template.md`

**Template Reference**: `.claude/skills/api-manual-testing/references/tc-template.md`

## Skill Phase Patterns

Both ui-manual-testing and api-manual-testing follow the same 4-phase pattern:

```
Phase 0: Load Knowledge
   ↓
Phase 1: Plan & Confirm
   ↓
Phase 2: Explore & Log
   ↓
Phase 3: Document
   ↓
Phase 4: Summary & Handoff
```

This structure ensures:
- User alignment before exploration
- Complete evidence logging for traceability
- High-quality test cases grounded in real behavior
- Clear handoff to automation teams