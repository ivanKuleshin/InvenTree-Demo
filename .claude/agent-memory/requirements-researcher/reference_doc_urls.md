---
name: InvenTree Documentation URL Mapping
description: Maps InvenTree component names to their GitHub raw content documentation paths
type: reference
---

Base URL for all raw docs: `https://raw.githubusercontent.com/inventree/InvenTree/master/docs/docs/`

Known component paths (relative to base):

| Component | Path | Notes |
|-----------|------|-------|
| Parts (index/overview) | `part/index.md` | NOT `part/part.md` — that 404s |
| Part Views (UI detail page) | `part/views.md` | |
| Part Templates/Variants | `part/template.md` | |
| Trackable Parts | `part/trackable.md` | |
| Virtual Parts | `part/virtual.md` | |
| Part Test Templates | `part/test.md` | |
| Stock | `stock/index.md` | Not yet fetched |
| Build Orders | `build/index.md` | Not yet fetched |
| Purchase Orders | `order/purchase_order.md` | Not yet fetched |
| Sales Orders | `order/sales_order.md` | Not yet fetched |

The official site `docs.inventree.org/en/stable/` returns HTTP 403 for all paths — always use GitHub raw content.

Demo/auth info is at `https://inventree.org/demo.html` (not blocked).
