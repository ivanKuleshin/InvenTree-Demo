# Project Overview

## What is This Folder?

This folder is the **manual test case documentation hub** for InvenTree. It documents the application's behavior through evidence-based, exploratory testing using agentic workflows.

## Structure

- **`docs/`** — Snapshots of InvenTree API endpoints, schemas, and documentation (auto-generated from live exploration)
- **`test-cases/`** — Manually created test cases for UI and API (organized by feature area)
- **`.claude/`** — Custom agents and skills for test case creation and documentation

## Overall Workflow

```
Explore InvenTree demo (UI/API)
    ↓
Create evidence-based manual test cases
    ↓
Hand off to automation teams (../ui/ and ../api/)
    ↓
Implement as automated tests with 1:1 traceability
```

## Key Links

- **Demo**: [https://demo.inventree.org](https://demo.inventree.org)
- **InvenTree Official Docs**: [https://docs.inventree.org](https://docs.inventree.org)
- **Part Attributes**: [https://docs.inventree.org/en/stable/part/#part-attributes](https://docs.inventree.org/en/stable/part/#part-attributes)
- **API Reference**: [https://docs.inventree.org/en/stable/api/](https://docs.inventree.org/en/stable/api/)

## Demo Credentials

- **URL**: https://demo.inventree.org
- **Username**: admin
- **Password**: inventree

(Demo credentials — never embed in test case files)

## Repository Context

This folder is part of the **InvenTree Test Automation Framework**:

- **`../manual/`** (this folder) — Manual test case documentation
- **`../ui/`** — Playwright + TypeScript automation (implements manual test cases)
- **`../api/`** — Java + Maven automation (implements manual test cases)

Goal: Maintain 1:1 traceability between manual test cases and automated implementations.