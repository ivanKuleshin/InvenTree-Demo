import { type Locator, type Page } from "@playwright/test";
import { BasePage } from "@framework/core/BasePage";

/** Available tabs on the Part detail page */
export type PartTab =
  | "stock"
  | "bom"
  | "allocated"
  | "build-orders"
  | "parameters"
  | "variants"
  | "revisions"
  | "attachments"
  | "related-parts"
  | "test-templates";

/**
 * Part detail page — /part/<id>/
 *
 * `url` is a RegExp because the path contains a dynamic segment.
 * Use `navigate(partId)` or `navigateTo('/part/42/')` explicitly.
 */
export class PartDetailPage extends BasePage {
  readonly url = /\/part\/\d+\//;

  constructor(page: Page) {
    super(page);
  }

  /**
   * Navigate to the detail page of a specific part.
   */
  override async navigate(partId?: number | string): Promise<void> {
    if (partId == null) {
      throw new Error("PartDetailPage.navigate() requires a partId argument.");
    }
    await this.navigateTo(`/part/${partId}/`);
  }

  // ── Header / breadcrumb ───────────────────────────────────────────────────

  get partName(): Locator {
    return this.locator(
      'h1, h2, [data-testid="part-name"], .part-name',
    ).first();
  }

  get breadcrumb(): Locator {
    return this.page.locator("//div[contains(@class, 'Breadcrumbs-root')]");
  }

  get editButton(): Locator {
    return this.locator(
      'button:has-text("Edit"), [aria-label*="edit" i]',
    ).first();
  }

  get deleteButton(): Locator {
    return this.locator(
      'button:has-text("Delete"), [aria-label*="delete" i]',
    ).first();
  }

  // ── Attribute chips ───────────────────────────────────────────────────────

  get activeChip(): Locator {
    return this.locator(
      '[data-testid="part-active"], .part-active, :text("Active")',
    ).first();
  }

  get assemblyChip(): Locator {
    return this.locator(
      '[data-testid="part-assembly"], :text("Assembly")',
    ).first();
  }

  get templateChip(): Locator {
    return this.locator(
      '[data-testid="part-template"], :text("Template")',
    ).first();
  }

  get componentChip(): Locator {
    return this.locator(
      '[data-testid="part-component"], :text("Component")',
    ).first();
  }

  get trackableChip(): Locator {
    return this.locator(
      '[data-testid="part-trackable"], :text("Trackable")',
    ).first();
  }

  get purchasableChip(): Locator {
    return this.locator(
      '[data-testid="part-purchasable"], :text("Purchasable")',
    ).first();
  }

  get salableChip(): Locator {
    return this.locator(
      '[data-testid="part-salable"], :text("Salable")',
    ).first();
  }

  get virtualChip(): Locator {
    return this.locator(
      '[data-testid="part-virtual"], :text("Virtual")',
    ).first();
  }

  // ── Tabs ─────────────────────────────────────────────────────────────────

  async openTab(tab: PartTab): Promise<void> {
    await this.locator(`[role="tab"]:has-text("${TAB_LABELS[tab]}")`).click();
    await this.page.waitForLoadState("networkidle");
  }

  // ── Queries ──────────────────────────────────────────────────────────────

  async getPartNameText(): Promise<string> {
    return this.partName.innerText();
  }

  async isEditButtonVisible(): Promise<boolean> {
    return this.editButton.isVisible();
  }

  async isDeleteButtonVisible(): Promise<boolean> {
    return this.deleteButton.isVisible();
  }

  override async waitForLoad(): Promise<void> {
    await this.page.waitForLoadState("domcontentloaded");
    this.assertCurrentUrl();
    await this.partName.waitFor({ state: "visible", timeout: 15_000 });
  }
}

const TAB_LABELS: Record<PartTab, string> = {
  stock: "Stock",
  bom: "BOM",
  allocated: "Allocated",
  "build-orders": "Build Orders",
  parameters: "Parameters",
  variants: "Variants",
  revisions: "Revisions",
  attachments: "Attachments",
  "related-parts": "Related Parts",
  "test-templates": "Test Templates",
};
