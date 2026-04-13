# API Manual Test Case Template

```markdown
### TC-<SUITE_PREFIX>-<NNN>: <Title>

**Type**: API
**Priority**: P1 (smoke) | P2 (regression) | P3 (edge case)
**Endpoint**: `<METHOD> /api/<path>/`
**Preconditions**: <resource state required before this call>

**Request**:
- Method: `<GET|POST|PATCH|DELETE>`
- URL: `/api/<path>/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body (if applicable):
  ```json
  { "<field>": "<value>" }
  ```

**Expected Result**: <what should happen per spec>

**Observed** (filled during probe):
- Status: `<HTTP status code>`
- Response snippet:
  ```json
  { "<field>": "<observed value>" }
  ```
- Matches spec: Yes | No | [ASSUMED — not probed]

**Notes**: <any divergence from docs, unexpected fields, or environment-specific behavior>
```

## Priority guide

| Priority | When to use |
|---|---|
| P1 | Happy path — core CRUD, 200/201 responses |
| P2 | Validation — 400 on bad payload, 404 on missing resource |
| P3 | Edge case — 401 unauth, duplicate conflict, boundary values |
