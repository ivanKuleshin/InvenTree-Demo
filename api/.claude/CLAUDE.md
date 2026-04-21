# CLAUDE.md

This file provides guidance to Claude Code when working with code in this repository.

All detailed references are stored in `.claude/memory-references/` and should be loaded dynamically when needed.

## Quick Reference

- [Project Overview](memory-references/project-overview.md) — Framework scope and links
- [Build Commands](memory-references/build-commands.md) — Maven commands and environment
- [Running Tests](memory-references/running-tests.md) — Test execution patterns
- [Architecture](memory-references/architecture.md) — Design, services, test structure, auth
- [Configuration](memory-references/configuration.md) — config.properties and overrides
- [Development Workflow](memory-references/development-workflow.md) — Step-by-step process
- [Coding Rules](memory-references/coding-rules.md) — Non-negotiable patterns
- [Common Issues](memory-references/common-issues.md) — Q&A troubleshooting

## Key Conventions at a Glance

From [coding-rules.md](memory-references/coding-rules.md):

- **Dynamic test data** — Never hardcode IDs or field values. Create/fetch data at runtime.
- **POJO models** — Must have `@Getter`, `@Builder`, `@JsonInclude(NON_NULL)`, `@JsonProperty`.
- **Service layer** — All API calls via service classes, not directly in tests.
- **No test interdependencies** — Tests must be independent; use `@AfterMethod` cleanup.
- **No comments** — Code must be self-documenting; only add WHY comments for non-obvious workarounds.