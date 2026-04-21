---
name: InvenTree Research Toolkit (Python)
description: Stdlib Python CLI that retrieves OpenAPI schema data, rendered docs pages, raw Markdown, and local-docs inventory for the researcher agent
type: reference
---

# InvenTree Research Toolkit

Script: `.claude/agent-memory/requirements-researcher/scripts/inventree_research.py`

Stdlib-only Python 3 CLI covering the data-retrieval request types this agent handles. Run any subcommand via `Bash` and capture stdout. Schema JSON is cached to `/tmp/inventree-openapi-schema.json` so repeated calls in one session are cheap.

## Subcommands

| Command | Purpose | Key flags |
| --- | --- | --- |
| `schema-info` | Version, path count, schema count | `--refresh` |
| `endpoints` | List endpoints filtered by path prefix | `--prefix /api/stock/` |
| `endpoint` | Full OpenAPI operation for one path+method (JSON) | `--path /api/stock/ --method get` |
| `schema-list` | List component schema names, regex filter optional | `--pattern "^Stock"` |
| `schema-show` | Dump a named component schema (JSON) | `--name StockItem` |
| `docs-web` | Fetch rendered `docs.inventree.org` page, GitHub fallback | `--path "part/"` |
| `docs-raw` | Raw `.md` from `raw.githubusercontent.com` (last resort) | `--path "part/index.md"` |
| `local-scan` | List `.md` files already present under `docs/{component}/` | `--component parts` |
| `demo` | Extract text from `inventree.org/demo.html` | — |

## Typical researcher flow for a new module

```bash
# 1. Check existing local coverage
python3 .claude/agent-memory/requirements-researcher/scripts/inventree_research.py local-scan --component stock

# 2. Enumerate API surface
python3 .claude/agent-memory/requirements-researcher/scripts/inventree_research.py endpoints --prefix /api/stock/

# 3. Drill into one endpoint
python3 .claude/agent-memory/requirements-researcher/scripts/inventree_research.py endpoint --path /api/stock/ --method post

# 4. Grab schemas that show up in those endpoints
python3 .claude/agent-memory/requirements-researcher/scripts/inventree_research.py schema-show --name StockItem

# 5. Fetch the rendered narrative docs
python3 .claude/agent-memory/requirements-researcher/scripts/inventree_research.py docs-web --path "stock/"
```

## Notes

- `docs-web` tries `https://docs.inventree.org/en/stable/<path>` first (200 when the page exists) and falls back to `https://github.com/inventree/InvenTree/blob/master/docs/docs/<path-or-path/index.md>`. URL layout on the docs site has moved around (e.g., Build content now lives under `manufacturing/build/`) — supply the actual current URL path, not a guess.
- `docs-raw` goes straight to `raw.githubusercontent.com`. Use it only when `docs-web` fails or when diffing rendered vs source.
- The schema endpoint returns **YAML** by default but serves JSON with `Accept: application/json`; the script requests JSON so no PyYAML needed.
- SSL trust: the script probes `certifi` → `/etc/ssl/cert.pem` → default context. Works on macOS Python.org installs where the default context has no CA bundle.
- HTML-to-text extraction scopes to `<article>` (falls back to `<main>`, then whole document) to avoid MkDocs Material's 40+ sidebar `<nav>` blocks swallowing the body. Images are replaced with `[IMAGE: alt (src)]` markers the researcher can turn into `[IMAGE DESCRIPTION]` blocks per the agent's image-handling rule.

## Why: verification done on 2026-04-16

- `schema-info` → `API v479, 264 paths, 374 schemas` (matches inventree-modules.md)
- `endpoints --prefix /api/build/` → returned 32 endpoints including the full build-lifecycle action set (allocate, auto-allocate, cancel, complete, consume, finish, hold, issue, scrap-outputs, unallocate)
- `schema-show --name Build` → returned the Build serializer field map
- `docs-web --path part/` → 30 lines of article content with H1/H2 hierarchy and image markers
- `docs-raw --path part/index.md` → raw front-matter + article markdown
- `demo` → parsed credentials table from inventree.org/demo.html
- `local-scan --component parts` → 9 MD files, sizes in bytes
