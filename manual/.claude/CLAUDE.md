# CLAUDE.md

This file provides guidance to Claude Code when working with code in this repository.

All detailed references are stored in `.claude/memory-references/` and should be loaded dynamically when needed.

## Quick Reference

- [Project Overview](memory-references/project-overview.md) — What this folder does, repository structure, key links
- [Common Workflows](memory-references/common-workflows.md) — How to use the ui-manual-testing, api-manual-testing, and
  requirements-researcher tools
- [Test Case Organization](memory-references/test-case-organization.md) — Directory structure, test ID patterns,
  coverage areas
- [Agents and Skills](memory-references/agents-and-skills.md) — Available agents, skill descriptions, 4-phase workflows
- [Test Case Format](memory-references/test-case-format.md) — Markdown structure, template references, quality checklist
- [Quick Tips](memory-references/quick-tips.md) — Best practices, dos and don'ts, troubleshooting

## Key Concepts at a Glance

From [common-workflows.md](memory-references/common-workflows.md):

- **UI exploration** — Use `/ui-manual-testing` to explore pages, record UI elements, create TC_UI_* test cases
- **API exploration** — Use `/api-manual-testing` to test endpoints, record responses, create TC_AP* test cases
- **Documentation** — Use `/requirements-researcher` to load docs and create snapshots in `docs/api/`

From [test-case-organization.md](memory-references/test-case-organization.md):

- **UI tests** — `test-cases/ui/{suite}/TC_UI_<AREA>_<NUMBER>.md` (e.g., `TC_UI_PC_001.md` = Part Creation)
- **API tests** — `test-cases/api/{suite}/TC_AP<TYPE>_<NUMBER>.md` (e.g., `TC_APCRUD_001.md` = Part CRUD)

From [quick-tips.md](memory-references/quick-tips.md):

- **Always log exploration** — Create `-ui-manual-action-log.md` or `-api-manual-log.md` during exploration for evidence
- **Evidence matters** — Test cases must include **Observed** sections grounded in real exploration
- **Mutating flows are risky** — Flag create/edit/delete operations with `[MUTATING]` and get confirmation before
  testing