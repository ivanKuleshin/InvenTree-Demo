---
name: InvenTree Docs Site Returns 403
description: Official docs.inventree.org blocks all WebFetch requests with HTTP 403; use GitHub raw content as fallback
type: feedback
---

Use `https://raw.githubusercontent.com/inventree/InvenTree/master/docs/docs/` instead of `https://docs.inventree.org/en/stable/` for fetching InvenTree documentation.

**Why:** Every attempted fetch to `docs.inventree.org/en/stable/part/`, `/en/stable/part/part/`, and `/en/stable/part/views/` returned 403 Forbidden. GitHub raw content is a reliable authoritative alternative.

**How to apply:** When any InvenTree docs URL returns 403, immediately pivot to the GitHub raw content path. The mapping is: `docs.inventree.org/en/stable/{section}/{page}/` → `raw.githubusercontent.com/inventree/InvenTree/master/docs/docs/{section}/{page}.md`. Also note that the index page of a section is at `{section}/index.md`, not `{section}/{section}.md` (e.g., `part/index.md` not `part/part.md`).
