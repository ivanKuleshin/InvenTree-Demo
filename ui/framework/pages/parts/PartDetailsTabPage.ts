import { type Locator, type Page } from "@playwright/test";
import { BasePage } from "@framework/core/BasePage";
import { PartDetailTabBar } from "@framework/components/parts/PartDetailTabBar";
import { CreatePartModal } from "@framework/pages/parts/CreatePartModal";
import { EditPartModal } from "@framework/pages/parts/EditPartModal";
import { StockTab } from "@framework/components/parts/tabs/StockTab";
import { AllocationsTab } from "@framework/components/parts/tabs/AllocationsTab";
import { UsedInTab } from "@framework/components/parts/tabs/UsedInTab";
import { PartPricingTab } from "@framework/components/parts/tabs/PartPricingTab";
import { SuppliersTab } from "@framework/components/parts/tabs/SuppliersTab";
import { PurchaseOrdersTab } from "@framework/components/parts/tabs/PurchaseOrdersTab";
import { StockHistoryTab } from "@framework/components/parts/tabs/StockHistoryTab";
import { RelatedPartsTab } from "@framework/components/parts/tabs/RelatedPartsTab";
import { ParametersTab } from "@framework/components/parts/tabs/ParametersTab";
import { AttachmentsTab } from "@framework/components/parts/tabs/AttachmentsTab";
import { NotesTab } from "@framework/components/parts/tabs/NotesTab";

export class PartDetailsTabPage extends BasePage {
  readonly url = /\/web\/part\/\d+\/details/;

  readonly tabBar: PartDetailTabBar;
  readonly stockTab: StockTab;
  readonly allocationsTab: AllocationsTab;
  readonly usedInTab: UsedInTab;
  readonly partPricingTab: PartPricingTab;
  readonly suppliersTab: SuppliersTab;
  readonly purchaseOrdersTab: PurchaseOrdersTab;
  readonly stockHistoryTab: StockHistoryTab;
  readonly relatedPartsTab: RelatedPartsTab;
  readonly parametersTab: ParametersTab;
  readonly attachmentsTab: AttachmentsTab;
  readonly notesTab: NotesTab;

  constructor(page: Page) {
    super(page);
    this.tabBar = new PartDetailTabBar(page);
    this.stockTab = new StockTab(page);
    this.allocationsTab = new AllocationsTab(page);
    this.usedInTab = new UsedInTab(page);
    this.partPricingTab = new PartPricingTab(page);
    this.suppliersTab = new SuppliersTab(page);
    this.purchaseOrdersTab = new PurchaseOrdersTab(page);
    this.stockHistoryTab = new StockHistoryTab(page);
    this.relatedPartsTab = new RelatedPartsTab(page);
    this.parametersTab = new ParametersTab(page);
    this.attachmentsTab = new AttachmentsTab(page);
    this.notesTab = new NotesTab(page);
  }

  override async navigate(partId?: number | string): Promise<void> {
    if (partId == null) {
      throw new Error(
        "PartDetailsTabPage.navigate() requires a partId argument.",
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

  // ── Part Details tab ──────────────────────────────────────────────────────

  fieldValue(text: string): Locator {
    return this.page
      .getByRole("tabpanel", { name: "Part Details" })
      .getByText(text);
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
