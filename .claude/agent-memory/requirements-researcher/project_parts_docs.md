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

Additional tab-specific docs created on 2026-04-14 under `docs/parts/`:

- `docs/parts/part-stock-tab.md` — Stock tab: stock item concept, attributes, status codes (OK/Attention/Damaged/Destroyed/Lost/Rejected/Quarantined), export/new item/stock actions functions, stock history, tracking events, location management, traceability.
- `docs/parts/part-bom-tab.md` — BOM tab: line item properties, consumable/optional/substitute/inherited line items, BOM creation (manual + import), multi-level BOMs, validation/checksum, required quantity calculation formula.
- `docs/parts/part-allocated-tab.md` — Allocated tab: visibility conditions, allocation concept, untracked vs tracked stock, auto/manual/row-level allocation methods, consumption lifecycle.
- `docs/parts/part-build-orders-tab.md` — Build Orders tab: build order concept, status codes (Pending/Production/On Hold/Cancelled/Completed), parameters, outputs, stock management, lifecycle, calendar/table views, config settings.
- `docs/parts/part-parameters-tab.md` — Parameters tab: parameter templates, fields (Name/Description/Units/Model Type/Value Choices), instances, parametric tables, unit-aware filtering, filter operators, selection lists.
- `docs/parts/part-variants-tab.md` — Variants tab: template part concept, purposes, serial number uniqueness, stock aggregation, how to enable Template flag, how to create a variant, BOM inheritance, test template cascading.
- `docs/parts/part-revisions-tab.md` — Revisions tab: revision purpose, revisions-are-parts principle, Revision/Revision Of fields, restrictions (no circular refs, unique codes, no template revisions), global settings, creation steps, revision navigation dropdown.
- `docs/parts/part-attachments-tab.md` — Attachments tab: attachment concept, purpose, UI (upload/download/delete), supported object types.
- `docs/parts/part-related-parts-tab.md` — Related Parts tab: concept, use cases, UI, feature enable/disable setting, add/remove operations.
- `docs/parts/part-test-templates-tab.md` — Test Templates tab: visibility (testable parts only), cascading to variants, all template parameters, test key generation with examples table, test result fields, multiple results handling, automated test integration.

**Why:** User requested full investigation of the Part Views module; related sub-topics were documented as separate files per the documentation rules.

**How to apply:** When a user or agent asks about any part-related topic, check these files before fetching from the web. The `part-views.md` file is the primary entry point for understanding the Part Detail View UI tabs and fields. For tab-specific detail, use the `part-*-tab.md` files.
