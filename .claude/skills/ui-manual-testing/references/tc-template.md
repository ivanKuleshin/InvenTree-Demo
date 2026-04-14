# UI Manual Test Case Template

```markdown
### TC-<SUITE_PREFIX>-<NNN>: <Title>

**Type**: UI
**Priority**: P1 (smoke) | P2 (regression) | P3 (edge case)
**Page URL**: `https://demo.inventree.org/<path>/`
**Preconditions**: <auth state, navigation state, existing data required>
  - [ ] Logged in as `admin` [AUTH REQUIRED]
  - [ ] Navigate to: `<URL>`

**Steps**:
1. <Verb> the "<exact label>" <element type> — e.g., Click the "New Part" button
2. <Verb> "<exact field label>" field with `<value>`
3. ...

**Expected Result**: <observable visual outcome — what the user sees>

**Observed** (filled during exploration):
- Page title seen: `<title>`
- Element confirmed: "<exact label>" — present | missing | [ASSUMED — page not fetched]
- Actual behavior: <what happened or what was visible>
- Matches docs: Yes | No | [ASSUMED — not explored]

**Notes**: <divergence from docs, missing elements, or environment-specific behavior>
```

## Traceability prefix guide

| Prefix | Suite |
|---|---|
| `TC-CRE-` | Part creation (manual entry + import) |
| `TC-TAB-` | Part detail tabs |
| `TC-CAT-` | Part categories |
| `TC-ATR-` | Part attributes |
| `TC-UOM-` | Units of measure |
| `TC-REV-` | Part revisions |
| `TC-NEG-` | Negative / boundary scenarios |

## Priority guide

| Priority | When to use |
|---|---|
| P1 | Happy path — page loads, form submits, data appears |
| P2 | Validation — required field errors, invalid input messages |
| P3 | Edge case — empty states, permission boundaries, large data |
```
