import { type Locator, type Page } from "@playwright/test";
import { BasePage } from "@framework/core/BasePage";
import { BaseTable } from "@framework/core/BaseTable";
import { CreatePartModal } from "./CreatePartModal";
import { PartsTableRow } from "./PartsTableRow";

// ── Parts table ───────────────────────────────────────────────────────────────

class PartsTable extends BaseTable {
  constructor(page: Page) {
    super(page, "table");
  }

  get searchInput(): Locator {
    return this.page.locator('input[aria-label="table-search-input"]');
  }

  async search(query: string): Promise<void> {
    await this.searchInput.fill(query);
    await this.page.waitForLoadState("networkidle");
  }

  async clearSearch(): Promise<void> {
    await this.searchInput.clear();
    await this.page.waitForLoadState("networkidle");
  }

  /**
   * Return a {@link PartsTableRow} for the first row whose cells contain
   * the given part name.
   */
  getRow(partName: string): PartsTableRow {
    return new PartsTableRow(this.page, this.getRowByText(partName));
  }

  async waitForResults(): Promise<void> {
    await this.waitForRows();
  }
}

// ── Parts page ────────────────────────────────────────────────────────────────

/**
 * Parts list page scoped to a specific category.
 * URL: /part/category/index/parts
 */
export class PartsPage extends BasePage {
  readonly url = "/part/category/index/parts";

  readonly table: PartsTable;

  constructor(page: Page) {
    super(page);
    this.table = new PartsTable(page);
  }

  // ── Toolbar locators ──────────────────────────────────────────────────────

  /** The "Add Parts" split-button that opens the add-parts action menu. */
  get addPartsButton(): Locator {
    return this.page.locator('button[aria-label="action-menu-add-parts"]');
  }

  /** The "Create Part" menu item inside the add-parts dropdown. */
  private get createPartMenuItem(): Locator {
    return this.page.locator(
      'button[aria-label="action-menu-add-parts-create-part"]',
    );
  }

  // ── Actions ───────────────────────────────────────────────────────────────

  /**
   * Open the Add Parts menu and click "Create Part".
   * Returns the {@link CreatePartModal} once it is visible.
   */
  async openCreatePartModal(): Promise<CreatePartModal> {
    await this.addPartsButton.click();
    await this.createPartMenuItem.click();
    const modal = new CreatePartModal(this.page);
    await modal.waitForVisible();
    return modal;
  }

  /**
   * Search for a part by name and return the matching table row.
   * Waits for the table to refresh before returning.
   *
   * @param partName Exact or partial part name to search for
   */
  async findPart(partName: string): Promise<PartsTableRow> {
    await this.table.search(partName);
    await this.table.waitForResults();
    return this.table.getRow(partName);
  }

  // ── Load ──────────────────────────────────────────────────────────────────

  override async waitForLoad(): Promise<void> {
    await this.page.waitForLoadState("domcontentloaded");
    this.assertCurrentUrl();
    await this.page
      .locator('table, [role="table"], text=No parts found')
      .first()
      .waitFor({ state: "visible", timeout: 15_000 })
      .catch(() => undefined);
  }
}
