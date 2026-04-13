---
name: API Schema Documentation Files
description: Tracks which API schema documentation files have been created and what they cover
type: project
---

API schema documentation is derived from the live InvenTree demo at `https://demo.inventree.org/api/schema/` (OpenAPI 3.0.3, API version 479, ~945KB). The docs site schema YAML files are generated at build time and not committed to the repo.

## Created Files

| File | Coverage |
|------|----------|
| `docs/api/part-api-schema.md` | 25 path groups (68 operations) for `/api/part/*`; 37 component schemas; auth schemes; OAuth2 scopes |

**Why:** The canonical docs page at `https://docs.inventree.org/en/stable/api/schema/part/` uses a MkDocs `[OAD(...)]` plugin directive to render OpenAPI YAML at build time. That YAML is not in the repo. The live demo API endpoint is the authoritative source.

**How to apply:** When creating API schema docs for any other InvenTree component, fetch `https://demo.inventree.org/api/schema/`, parse the YAML locally (`/tmp/inventree_schema.yml`), filter paths by prefix (e.g., `/api/stock`), extract referenced schemas, then generate the MD from the structured data. The schema file is large — always save to /tmp first then parse with Python.
