---
name: InvenTree Modules Inventory
description: Top-level module/domain inventory file mapping every InvenTree functional area to its API base path and local-doc coverage status
type: project
---

`docs/api/inventree-modules.md` is the authoritative coverage map of InvenTree top-level modules. Created 2026-04-16 from the live OpenAPI schema at `https://demo.inventree.org/api/schema/` (API version 479, 264 paths) and the docs.inventree.org navigation.

**Why:** The Test Automation Framework currently covers only the Parts domain. A single-page inventory was needed for coverage planning so future agents/users can see at a glance which domains have local snapshots and which are gaps.

**How to apply:** When asked about a module not in the Parts domain (Stock, Build, PO/SO/RO, Companies, Reports, Plugins, etc.), check this file first to see whether docs already exist or if a fetch-and-document cycle is required. Refresh the inventory if the API version changes or new top-level paths appear under `/api/` in the live schema.

Confirmed gap modules (no local docs as of 2026-04-16): Stock, Build/Manufacturing, BOM lines, Purchase Orders, Sales Orders, Return Orders, Companies/Suppliers/Manufacturers/Customers, Reports, Labels, Attachments, Barcodes, Plugins/Machines, Importer, Settings, Notifications, News, Background Tasks, Data Output, Currency, Project Codes, Webhooks, Email, Error Reports, System/Admin/Flags, Generators, Locate, Search, Part Parameters (template + value endpoints).
