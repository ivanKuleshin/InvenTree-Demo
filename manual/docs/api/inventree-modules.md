---
source: https://docs.inventree.org/en/stable/ + https://demo.inventree.org/api/schema/
component: inventree-platform
topic: top-level module/domain inventory
fetched: 2026-04-16
---

# InvenTree Top-Level Modules / Domains

This document is a structured inventory of the top-level functional domains of the InvenTree application, intended as a coverage-planning map for the Test Automation Framework. Module names and API base paths were verified against the live OpenAPI schema at `https://demo.inventree.org/api/schema/` (API version 479, 264 paths). Documentation section names were taken from the official MkDocs navigation. Local-doc coverage refers to files already present under `/docs/`.

## Modules

| Module | Description | Key Entities | API Base Path | Local Docs Coverage |
| --- | --- | --- | --- | --- |
| Parts | Catalog of physical and virtual parts; central domain in InvenTree. | Part, PartThumb, PartRequirements, PartSerialNumber, PartTestTemplate, PartRelation, PartStocktake | `/api/part/` (25 paths) | yes — [docs/parts/](parts/), [docs/api/part-api/](part-api/), [docs/api/endpoints/](endpoints/), [docs/api/schemas/](schemas/), [docs/ui/part-attributes.md](../ui/part-attributes.md), [docs/ui/part-negative-boundary-scenarios.md](../ui/part-negative-boundary-scenarios.md) |
| Part Categories | Hierarchical category tree for organizing parts; supports parametric templates. | PartCategory, CategoryTree, CategoryParameterTemplate | `/api/part/category/`, `/api/part/category/tree/`, `/api/part/category/parameters/` (under `/api/part/`) | yes — [docs/parts/part-categories.md](parts/part-categories.md) |
| Part Parameters | Parameter templates and parameter values attached to parts; powers parametric tables and selection lists. | PartParameter, PartParameterTemplate, SelectionList | `/api/part/parameter/`, `/api/parameter/` (4 paths) | partial — [docs/parts/part-parametric-tables.md](parts/part-parametric-tables.md) |
| Part Pricing | Per-part cost rollup and price breaks (internal + sale). | PartPricing, PartInternalPriceBreak, PartSalePriceBreak | `/api/part/{id}/pricing/`, `/api/part/internal-price/`, `/api/part/sale-price/` | yes (schemas + endpoints) — [docs/api/schemas/part-pricing.md](schemas/part-pricing.md), [docs/api/endpoints/part-pricing-GET.md](endpoints/part-pricing-GET.md) |
| Part Test Templates | Test definitions that cascade to part variants and gate stock-test results. | PartTestTemplate | `/api/part/test-template/` | yes — [docs/parts/part-test-templates.md](parts/part-test-templates.md), [docs/api/endpoints/part-test-template-list-GET.md](endpoints/part-test-template-list-GET.md) |
| Bill of Materials (BOM) | Assembly composition: lines linking assembly parts to component parts. | BomItem, BomItemSubstitute | `/api/bom/` (5 paths), `/api/part/{id}/bom-validate/`, `/api/part/{id}/bom-copy/` | partial — only `part-bom-validate-*` and `part-bom-copy-POST` endpoints; no `/api/bom/` line endpoints; no domain MD |
| Stock | Stock items: physical instances of parts with quantity, location, status, batch, serial. | StockItem, StockItemTracking, StockItemTestResult | `/api/stock/` (26 paths) | yes — [docs/stock/overview.md](stock/overview.md), [docs/stock/stock-items.md](stock/stock-items.md) |
| Stock Locations | Hierarchical storage locations and location types. | StockLocation, StockLocationType | `/api/stock/location/`, `/api/stock/location-type/` | yes — [docs/stock/stock-locations.md](stock/stock-locations.md) |
| Stock Adjustments | Stock-mutating actions: add, remove, count, transfer, change-status, assign, merge. | StockAdjustment | `/api/stock/add/`, `/api/stock/remove/`, `/api/stock/count/`, `/api/stock/transfer/`, `/api/stock/change_status/`, `/api/stock/assign/`, `/api/stock/merge/` | yes — [docs/stock/stock-adjustments.md](stock/stock-adjustments.md) |
| Build Orders (Manufacturing) | Production orders consuming BOM components and producing assembly stock. | Build, BuildLine, BuildItem, BuildOutput | `/api/build/` (19 paths) — incl. `/{id}/allocate/`, `/{id}/complete/`, `/{id}/cancel/`, `/{id}/issue/`, `/{id}/finish-output/` | no |
| Purchase Orders | Procurement orders to suppliers. | PurchaseOrder, PurchaseOrderLineItem, PurchaseOrderExtraLine | `/api/order/po/` + `/api/order/po-line/` + `/api/order/po-extra-line/` (~12 paths under `/api/order/`) | no |
| Sales Orders | Customer-facing orders: line items, allocations, shipments. | SalesOrder, SalesOrderLineItem, SalesOrderShipment, SalesOrderAllocation, SalesOrderExtraLine | `/api/order/so/`, `/api/order/so-line/`, `/api/order/so-allocation/`, `/api/order/so/shipment/`, `/api/order/so-extra-line/` | no |
| Return Orders | Customer return / RMA workflow. | ReturnOrder, ReturnOrderLineItem, ReturnOrderExtraLine | `/api/order/ro/`, `/api/order/ro-line/`, `/api/order/ro-extra-line/` | no |
| Companies | Generic company records — flagged as supplier, manufacturer, or customer. | Company, Contact, Address | `/api/company/` (12 paths) — incl. `/contact/`, `/address/` | no |
| Suppliers | Supplier-side company records and supplier-part linkage with pack/pricing data. | SupplierPart, SupplierPriceBreak | `/api/company/part/` (SupplierPart), `/api/supplier/` (3 paths) | no |
| Manufacturers | Manufacturer-side company records and manufacturer-part linkage. | ManufacturerPart, ManufacturerPartParameter | `/api/company/part/manufacturer/` | no |
| Customers | Customer-side company records (Company with `is_customer=true`). | Company (customer flag) | `/api/company/` | no |
| Users & Permissions | Users, groups, roles, ownership, profiles, current user. | User, Group, Owner, UserProfile, Role | `/api/user/` (15 paths) — incl. `/me/`, `/group/`, `/owner/`, `/profile/`, `/roles/` | partial — [docs/auth/login-requirements.md](../auth/login-requirements.md), [docs/auth/demo-credentials.md](../auth/demo-credentials.md) |
| Reports | Report templates, snippets, assets, and rendering. | ReportTemplate, ReportSnippet, ReportAsset | `/api/report/` (7 paths) — incl. `/template/`, `/snippet/`, `/asset/`, `/print/` | no |
| Labels | Label templates and label printing. | LabelTemplate | `/api/label/` (3 paths) — `/template/`, `/print/` | no |
| Attachments | Generic file attachments for any model via content-type linkage. | Attachment | `/api/attachment/` (2 paths); `/api/contenttype/` (3 paths) | no |
| Barcodes | Barcode scanning, assignment, and lookup. | BarcodeScan, BarcodeAssignment | `/api/barcode/` (9 paths) | no |
| Plugins | Plugin discovery, install, activate, settings, UI features. | PluginConfig, PluginSetting, Machine | `/api/plugins/` (14 paths); `/api/machine/` (8 paths) | no |
| Importer | Bulk data import sessions for any supported model. | ImportSession, ImportColumn, ImportRow | `/api/importer/` (9 paths) | no |
| Settings | Global and per-user configuration values. | GlobalSetting, UserSetting | `/api/settings/` (4 paths) — `/global/`, `/user/` | no |
| Notifications & News | In-app messages and news feed. | NotificationMessage, NewsItem | `/api/notifications/` (3 paths); `/api/news/` (2 paths) | no |
| Background Tasks | Async job queue inspection and triggering. | TaskResult | `/api/background-task/` (5 paths); `/api/action/` | no |
| Data Output / Export | Data export sessions and downloads. | DataOutput | `/api/data-output/` (2 paths) | no |
| Generic Status & Metadata | Status enums, metadata, generic lookups. | StatusList, Metadata | `/api/generic/` (4 paths); `/api/metadata/` (2 paths) | no |
| Currency / Pricing Util | Exchange rates and currency conversion. | ExchangeRate | `/api/currency/` (2 paths) | no |
| Project Codes | Project-code tagging applied to orders and other records. | ProjectCode | `/api/project-code/` (2 paths) | no |
| Units of Measure | Pint-based physical units used by parts, parameters, supplier parts. | (settings + part `units` field) | `/api/units/` (3 paths) | yes — [docs/ui/units-of-measure.md](../ui/units-of-measure.md) |
| Selection Lists | Reusable enumerated value lists for parameters. | SelectionList, SelectionListEntry | `/api/selection/` (4 paths) | partial — mentioned in [docs/parts/part-parametric-tables.md](parts/part-parametric-tables.md) |
| Search | Cross-domain search endpoint. | (search query) | `/api/search/` | no |
| Locate / Locator | Plugin-driven physical-locate workflow. | LocateRequest | `/api/locate/` | no |
| Email | Outgoing email log / templates. | EmailMessage | `/api/email/` | no |
| Webhooks | Inbound webhook receiver. | WebhookEndpoint | `/api/webhook/` | no |
| Error Reports | Server-side error log inspection. | ErrorReport | `/api/error-report/` (2 paths) | no |
| System / Admin / Health | Version, license, system info, admin actions, feature flags, icons. | SystemInfo, License, FeatureFlag, Icon | `/api/version/`, `/api/version-text/`, `/api/license/`, `/api/system/`, `/api/system-internal/`, `/api/admin/` (5), `/api/flags/` (2), `/api/icons/`, `/api/notes-image-upload/` | no |
| Generators | Server-side generators for default values (e.g., serial numbers, batch codes). | (generator response) | `/api/generate/` (2 paths) | no |

## Coverage Gaps

The following modules exist in InvenTree but have **no local documentation snapshot** under `/docs/`:

1. **Build Orders / Manufacturing** — entire `/api/build/` surface, including allocate, complete, cancel, issue, finish-output flows.
2. **Bill of Materials (BOM)** — `/api/bom/` line endpoints and substitutes; only the part-level validate and copy endpoints are captured.
3. **Purchase Orders** — `/api/order/po/` and line/extra-line/state-transition endpoints.
4. **Sales Orders** — `/api/order/so/`, allocations, shipments, line items.
5. **Return Orders** — `/api/order/ro/` and lines.
6. **Companies / Suppliers / Manufacturers / Customers** — `/api/company/`, `/api/supplier/`, contacts, addresses, supplier-part, manufacturer-part.
7. **Reports & Labels** — templates, snippets, assets, printing.
8. **Attachments** — generic attachment endpoints (`/api/attachment/`, `/api/contenttype/`).
9. **Barcodes** — scan/assign/unassign/order-line endpoints.
10. **Plugins & Machines** — install, activate, settings, ui-features, machine drivers.
11. **Importer** — bulk-import sessions used across modules.
12. **Settings** — global and per-user settings APIs.
13. **Users & Permissions** — full `/api/user/` surface (groups, owners, roles, profile, me); only auth/credentials are captured today.
14. **Notifications, News, Background Tasks, Data Output, Currency, Project Codes, Webhooks, Email, Error Reports, System / Admin / Flags / Icons / Generate / Locate / Search** — all support/admin domains.
15. **Part Parameters** — only parametric-table UI behaviour is documented; the parameter-template and parameter-value endpoints are not yet captured.

Local coverage today spans the **Parts** domain (Part, Part Categories, Part Pricing, Part Test Templates, Part Stocktake, Part Thumbs, Part Relations, BOM-validate/copy, Internal/Sale price breaks) and the **Stock** domain (StockItem, StockLocation, StockLocationType, StockItemTracking, StockItemTestResult, Stock Adjustments). Every other top-level module is a documentation gap.
