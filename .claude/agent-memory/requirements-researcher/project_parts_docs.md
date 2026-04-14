---
name: Parts Documentation Files Created
description: Tracks all Markdown files created under docs/parts/ and what each covers
type: project
---

All six parts documentation files were created on 2026-04-13 under `docs/parts/` and rebuilt on 2026-04-13 with corrected `source:` frontmatter pointing to docs.inventree.org URLs.

- `docs/parts/part-overview.md` — Part definition, Part Categories, all boolean Part Attributes (Virtual, Template, Assembly, Component, Testable, Trackable, Purchaseable, Salable), Locked Parts, Active Parts, Units of Measure, Part Images, Part Import. Source: `https://docs.inventree.org/en/stable/part/`.
- `docs/parts/part-views.md` — Part Detail View page: category breadcrumb, part detail fields (IPN, Name, Description, Revision, Keywords, External Link, Creation Date, Units), all 14 tabs (Stock, BOM, Build Orders, Allocations, Suppliers, Purchase Orders, Sales Orders, Stock History, Test Templates, Related Parts, Parameters, Attachments, Notes, Variants). Source: `https://docs.inventree.org/en/stable/part/views/`.
- `docs/parts/part-template.md` — Template parts and variant management: purpose, serial number uniqueness across variants, stock aggregation, how to enable Template flag, how to create a Variant. Source: `https://docs.inventree.org/en/stable/part/template/`.
- `docs/parts/part-trackable.md` — Trackable parts: stock tracking requirements, serial number input syntax (single, list, range, tilde/next, start+, start+length), build order constraints. Source: `https://docs.inventree.org/en/stable/part/trackable/`.
- `docs/parts/part-virtual.md` — Virtual (non-physical) parts: definition, use cases (licenses/labor/process steps), behavior in Stock Items (hidden), BOM (cost included, not allocated), Build Orders (hidden from required parts), Sales Orders (included, not stock-allocated). Source: `https://docs.inventree.org/en/stable/part/virtual/`.
- `docs/parts/part-test-templates.md` — Test template definitions: cascade to variants, all parameters (Name, Key, Description, Required, Requires Value, Requires Attachment, Enabled), test key generation rules with examples, test results linkage. Source: `https://docs.inventree.org/en/stable/part/test/`.

- `docs/ui/part-attributes.md` — Comprehensive reference for all 10 boolean toggle attributes (Active, Virtual, Template/is_template, Assembly, Component, Trackable, Testable, Purchaseable, Salable, Locked): UI labels, API field names, defaults, per-feature-area behavior, and a full dependency/interaction matrix. Created 2026-04-14. Source: `https://docs.inventree.org/en/stable/part/part/`.

**Why:** User requested full investigation of the Part Views module; related sub-topics were documented as separate files per the documentation rules. A dedicated UI-focused attributes file was created 2026-04-14 to serve the ui-manual-testing agent.
- `docs/parts/part-categories.md` — Category hierarchy (parent/child/structural), category fields schema, tree structure, pathstring, category display/navigation in UI, cascade filtering, user-configurable filters, category parameter templates, full API reference for all `/api/part/category/` endpoints (list, create, detail, update, partial-update, delete, tree, parameters CRUD), and all related schemas (Category, CategoryTree, CategoryParameterTemplate, PaginatedCategoryList). Source: `https://docs.inventree.org/en/stable/part/` and live API schema.
- `docs/parts/part-parametric-tables.md` — Parameters concept (parameter templates, template attributes, create template/parameter workflow), parametric tables (parametric view toggle, sort by parameter, filter by parameter, multiple filters, multiple filters on same parameter, unit-aware filtering, removing filters, filter operators), parameter units (incompatible units, unit sorting), selection lists. Source: `https://docs.inventree.org/en/stable/concepts/parameters/`.

**Why:** User requested full investigation of the Part Views module; related sub-topics were documented as separate files per the documentation rules. Category and parametric table docs added 2026-04-14 per explicit request.

**How to apply:** When a user or agent asks about any part-related topic, check these files before fetching from the web. The `part-views.md` file is the primary entry point for understanding the Part Detail View UI tabs and fields. For attribute toggle behavior specifically, use `docs/ui/part-attributes.md`.
**How to apply:** When a user or agent asks about any part-related topic, check these files before fetching from the web. The `part-views.md` file is the primary entry point for understanding the Part Detail View UI tabs and fields. For categories specifically use `part-categories.md`; for parametric filtering use `part-parametric-tables.md`.
