import { type Locator, type Page } from "@playwright/test";
import { BasePage } from "@framework/core/BasePage";
import { PartDetailTabBar } from "@framework/components/parts/PartDetailTabBar";
import { CreatePartModal } from "@framework/pages/parts/CreatePartModal";
import { EditPartModal } from "@framework/pages/parts/EditPartModal";
import { PartDetailsPanel } from "@framework/components/parts/tabs/PartDetailsPanel";
import { StockPanel } from "@framework/components/parts/tabs/StockPanel";
import { AllocationsPanel } from "@framework/components/parts/tabs/AllocationsPanel";
import { UsedInPanel } from "@framework/components/parts/tabs/UsedInPanel";
import { PartPricingPanel } from "@framework/components/parts/tabs/PartPricingPanel";
import { SuppliersPanel } from "@framework/components/parts/tabs/SuppliersPanel";
import { PurchaseOrdersPanel } from "@framework/components/parts/tabs/PurchaseOrdersPanel";
import { StockHistoryPanel } from "@framework/components/parts/tabs/StockHistoryPanel";
import { RelatedPartsPanel } from "@framework/components/parts/tabs/RelatedPartsPanel";
import { ParametersPanel } from "@framework/components/parts/tabs/ParametersPanel";
import { AttachmentsPanel } from "@framework/components/parts/tabs/AttachmentsPanel";
import { NotesPanel } from "@framework/components/parts/tabs/NotesPanel";

export class PartsDetailViewPage extends BasePage {
  readonly url = /\/web\/part\/\d+\/details/;

  readonly tabBar: PartDetailTabBar;
  readonly partDetailsPanel: PartDetailsPanel;
  readonly stockPanel: StockPanel;
  readonly allocationsPanel: AllocationsPanel;
  readonly usedInPanel: UsedInPanel;
  readonly partPricingPanel: PartPricingPanel;
  readonly suppliersPanel: SuppliersPanel;
  readonly purchaseOrdersPanel: PurchaseOrdersPanel;
  readonly stockHistoryPanel: StockHistoryPanel;
  readonly relatedPartsPanel: RelatedPartsPanel;
  readonly parametersPanel: ParametersPanel;
  readonly attachmentsPanel: AttachmentsPanel;
  readonly notesPanel: NotesPanel;

  constructor(page: Page) {
    super(page);
    this.tabBar = new PartDetailTabBar(page);
    this.partDetailsPanel = new PartDetailsPanel(page);
    this.stockPanel = new StockPanel(page);
    this.allocationsPanel = new AllocationsPanel(page);
    this.usedInPanel = new UsedInPanel(page);
    this.partPricingPanel = new PartPricingPanel(page);
    this.suppliersPanel = new SuppliersPanel(page);
    this.purchaseOrdersPanel = new PurchaseOrdersPanel(page);
    this.stockHistoryPanel = new StockHistoryPanel(page);
    this.relatedPartsPanel = new RelatedPartsPanel(page);
    this.parametersPanel = new ParametersPanel(page);
    this.attachmentsPanel = new AttachmentsPanel(page);
    this.notesPanel = new NotesPanel(page);
  }

  override async navigate(partId?: number | string): Promise<void> {
    if (partId == null) {
      throw new Error(
        "PartsDetailViewPage.navigate() requires a partId argument.",
      );
    }
    await this.navigateTo(`/web/part/${partId}/details`);
  }

  // ── Header ────────────────────────────────────────────────────────────────

  get partTitle(): Locator {
    return this.page.getByText(/^Part:\s/);
  }

  get partDescription(): Locator {
    return this.page.getByText(/^Part:\s/).locator("xpath=../p[last()]");
  }

  get inStockBadge(): Locator {
    return this.page.getByText(/^In Stock:/);
  }

  get noStockBadge(): Locator {
    return this.page.getByText("No Stock");
  }

  get successToast(): Locator {
    return this.page.getByText("Item Created");
  }

  // ── Actions menu ──────────────────────────────────────────────────────────

  get actionsMenuButton(): Locator {
    return this.page.locator('button[aria-label="action-menu-part-actions"]');
  }

  private get duplicateMenuItem(): Locator {
    return this.page.locator(
      'button[aria-label="action-menu-part-actions-duplicate"]',
    );
  }

  private get editMenuItem(): Locator {
    return this.page.locator(
      'button[aria-label="action-menu-part-actions-edit"]',
    );
  }

  async openDuplicateModal(): Promise<CreatePartModal> {
    await this.actionsMenuButton.click();
    await this.duplicateMenuItem.click();
    const modal = new CreatePartModal(this.page);
    await modal.waitForVisible();
    return modal;
  }

  async openEditModal(): Promise<EditPartModal> {
    await this.actionsMenuButton.click();
    await this.editMenuItem.click();
    const modal = new EditPartModal(this.page);
    await modal.waitForVisible();
    return modal;
  }

  currentPartId(): string {
    const match = this.page.url().match(/\/web\/part\/(\d+)\//);
    return match ? match[1] : "";
  }

  // ── Header queries ────────────────────────────────────────────────────────

  async getPartName(): Promise<string> {
    const text = (await this.partTitle.textContent()) ?? "";
    return text.replace(/^Part:\s*/, "");
  }

  async getDescription(): Promise<string> {
    return (await this.partDescription.textContent()) ?? "";
  }

  async getInStockCount(): Promise<number> {
    const text = (await this.inStockBadge.textContent()) ?? "";
    const match = text.match(/In Stock:\s*(\d+)/);
    return match ? parseInt(match[1], 10) : 0;
  }

  // ── Load ──────────────────────────────────────────────────────────────────

  override async waitForLoad(): Promise<void> {
    await this.partTitle.waitFor({ state: "visible", timeout: 30_000 });
    this.assertCurrentUrl();
  }
}
