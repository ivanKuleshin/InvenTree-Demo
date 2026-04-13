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

**Why:** User requested full investigation of the Part Views module; related sub-topics were documented as separate files per the documentation rules.

**How to apply:** When a user or agent asks about any part-related topic, check these files before fetching from the web. The `part-views.md` file is the primary entry point for understanding the Part Detail View UI tabs and fields.
