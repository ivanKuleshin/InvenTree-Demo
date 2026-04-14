import { type Locator, type Page } from "@playwright/test";
import { BasePage } from "@framework/core/BasePage";
import { BaseTable } from "@framework/core/BaseTable";
import { CreatePartModal } from "./CreatePartModal";

/**
 * Parts table sub-component used within the Parts list page.
 */
class PartsTable extends BaseTable {
  constructor(page: Page) {
    super(
      page,
      '[data-testid="part-table-list"], .mantine-DataTable-root, table',
    );
  }

  getPartNameCell(row: Locator): Locator {
    return this.getCellInRow(row, 1);
  }

  getDescriptionCell(row: Locator): Locator {
    return this.getCellInRow(row, 2);
  }

  async getAllPartNames(): Promise<string[]> {
    return this.getColumnValues(1);
  }
}

/**
 * Parts list page — /part/
 */
export class PartsListPage extends BasePage {
  readonly url = "/part/";

  readonly table: PartsTable;

  constructor(page: Page) {
    super(page);
    this.table = new PartsTable(page);
  }

  // ── Toolbar ──────────────────────────────────────────────────────────────

  get newPartButton(): Locator {
    return this.locator(
      'button:has-text("New Part"), button:has-text("Add Part"), [aria-label*="new part" i]',
    ).first();
  }

  get searchInput(): Locator {
    return this.locator(
      'input[placeholder*="search" i], input[type="search"]',
    ).first();
  }

  get filterButton(): Locator {
    return this.locator(
      'button:has-text("Filter"), [aria-label*="filter" i]',
    ).first();
  }

  // ── Actions ──────────────────────────────────────────────────────────────

  async openCreatePartModal(): Promise<CreatePartModal> {
    await this.newPartButton.click();
    const modal = new CreatePartModal(this.page);
    await modal.waitForVisible();
    return modal;
  }

  async search(query: string): Promise<void> {
    await this.searchInput.fill(query);
    await this.page.keyboard.press("Enter");
    await this.page.waitForLoadState("networkidle");
  }

  async clearSearch(): Promise<void> {
    await this.searchInput.clear();
    await this.page.keyboard.press("Enter");
    await this.page.waitForLoadState("networkidle");
  }

  async isNewPartButtonVisible(): Promise<boolean> {
    return this.newPartButton.isVisible();
  }

  async isNewPartButtonEnabled(): Promise<boolean> {
    return this.newPartButton.isEnabled();
  }

  override async waitForLoad(): Promise<void> {
    await this.page.waitForLoadState("domcontentloaded");
    this.assertCurrentUrl();
    await this.page
      .locator(
        '[data-testid="part-table-list"], table, [role="table"], text=No parts found',
      )
      .first()
      .waitFor({ state: "visible", timeout: 15_000 })
      .catch(() => undefined);
  }
}
