import { expect, type Locator, type Page } from "@playwright/test";
import { BaseTable } from "@framework/core/BaseTable";

export class CategoryTable extends BaseTable {
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

  get paginationText(): Locator {
    return this.page.getByText(/\d+ - \d+ \/ \d+/).first();
  }

  async getTotalCount(): Promise<number> {
    const text = (await this.paginationText.textContent()) ?? "";
    const match = text.match(/\/\s*(\d+)/);
    return match ? parseInt(match[1], 10) : 0;
  }

  get emptyStateText(): Locator {
    return this.page.getByText("No records found");
  }

  getCategoryNameCell(name: string): Locator {
    return this.getRowByText(name).locator("td").first();
  }

  async clickCategory(name: string): Promise<void> {
    await this.getRowByText(name).click();
  }

 
  async clickFirstCategory(): Promise<string> {
    const row = this.getRowByIdx(0);
    const nameCell = this.getCellInRow(row, 1);
    const name = (await nameCell.innerText()).trim();
    await nameCell.click();
    return name;
  }
}
