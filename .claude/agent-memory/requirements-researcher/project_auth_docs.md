---
name: Auth and Demo Credentials Documentation
description: Location of demo environment credentials and access details for InvenTree
type: project
---

Demo credentials are stored at `docs/auth/demo-credentials.md`, created 2026-04-13.

Demo URL: `https://demo.inventree.org`

Accounts:
- `allaccess` / `nolimits` — Full create/edit/view permissions
- `reader` / `readonly` — View-only access
- `engineer` / `partsonly` — Parts management and stock viewing
- `admin` / `inventree` — Superuser with admin functions

Database resets daily. Source: `https://inventree.org/demo.html`.

**Why:** Documented as part of initial bootstrap; needed by ui-manual-tester and api-manual-tester agents for authentication.

**How to apply:** Direct any agent asking for login credentials or demo environment info to `docs/auth/demo-credentials.md`. Do not re-fetch unless the user suspects credentials have changed.
