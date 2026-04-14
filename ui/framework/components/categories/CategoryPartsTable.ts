import { expect, type Locator, type Page } from "@playwright/test";
import { BaseTable } from "@framework/core/BaseTable";

export class CategoryPartsTable extends BaseTable {
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

  get filterButton(): Locator {
    return this.page.locator('button[aria-label="table-select-filters"]');
  }

  get standardViewButton(): Locator {
    return this.page.locator('//button[@aria-label="segmented-icon-control-table"]//ancestor::label');
  }

  get parametricViewButton(): Locator {
    return this.page.locator('//button[@aria-label="segmented-icon-control-parametric"]//ancestor::label');
  }

  async switchToParametricView(): Promise<void> {
    await this.parametricViewButton.click();
    await this.page.waitForLoadState("networkidle");
  }

  async switchToStandardView(): Promise<void> {
    await this.standardViewButton.click();
    await this.page.waitForLoadState("networkidle");
  }

  get paginationText(): Locator {
    return this.page.getByText(/\d+ - \d+ \/ \d+/).first();
  }

  getTotalCount(): Locator {
    return this.paginationText;
  }

  async getTotalCountValue(): Promise<number> {
    const text = (await this.paginationText.textContent()) ?? "";
    const match = text.match(/\/\s*(\d+)/);
    return match ? parseInt(match[1], 10) : 0;
  }

  get prevPageButton(): Locator {
    return this.page.getByRole("button", { name: "Previous page" });
  }

  get nextPageButton(): Locator {
    return this.page.getByRole("button", { name: "Next page" });
  }

  get emptyStateText(): Locator {
    return this.page.getByText("No records found");
  }

  getSortButtonForColumn(columnName: string): Locator {
    return this.page
      .locator("//th[contains(.," + columnName + ")]")
      .getByRole("button")
      .first();
  }

  getSortedState(columnName: string): Locator {
    return this.getSortButtonForColumn(columnName).locator('//previous-sibling::div')
  }


  getParameterFilterIcon(paramName: string): Locator {
    const escaped = paramName.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
    return this.page
      .locator("th")
      .filter({ hasText: new RegExp(escaped) })
      .getByRole("button")
      .last();
  }

  getParamFilterOperatorInput(paramName: string): Locator {
    return this.page.locator(`//input[@aria-label="filter-${paramName}-operator"]`).first();
  }
  
  getParamFilterValueInput(paramName: string): Locator {
    return this.page.locator(`[aria-label="filter-${paramName}"]`).first();
  }

  async applyParameterFilter(
    paramName: string,
    operator: string,
    value: string,
  ): Promise<void> {
    await this.getParameterFilterIcon(paramName).click();
    const operatorInput = this.getParamFilterOperatorInput(paramName);
    await operatorInput.click();
    await this.page.locator(`//div[@data-combobox-option and .="${operator}"]`).click();
    const valueInput = this.getParamFilterValueInput(paramName);
    await valueInput.fill(value);
    await valueInput.press("Enter");
    await this.page.waitForLoadState("networkidle");
  }

  getFilterRemoveButton(paramName: string): Locator {
    return this.page
      .locator(`[aria-label="filter-${paramName}-operator"]`)
      .locator("..")
      .getByRole("button");
  }

  async assertVisible(): Promise<void> {
    await expect(this.root).toBeVisible();
  }

  async clickSortColumn(columnName: string): Promise<void> {
    await this.getSortButtonForColumn(columnName).click();
    await this.waitForNetworkIdle();
  }

  async clickNextPage(): Promise<void> {
    await this.nextPageButton.click();
    await this.waitForNetworkIdle();
  }

  async clickPrevPage(): Promise<void> {
    await this.prevPageButton.click();
    await this.waitForNetworkIdle();
  }

  async getFirstRowFirstCellText(): Promise<string> {
    return this.getRowByIdx(0).locator("td").first().innerText();
  }

  async assertFirstNCellsContainText(n: number, text: string): Promise<void> {
    const rowCount = await this.rows.count();
    for (let i = 0; i < Math.min(rowCount, n); i++) {
      const cellText = await this.rows.nth(i).locator("td").first().innerText();
      expect(cellText).toContain(text);
    }
  }

  async assertPaginationRange(pattern: RegExp): Promise<void> {
    await expect(this.page.getByText(pattern)).toBeVisible({ timeout: 10_000 });
  }

  get filterDrawer(): Locator {
    return this.page.getByRole("dialog", { name: "Table Filters" });
  }

  async assertFilterDrawerVisible(): Promise<void> {
    await expect(this.filterDrawer).toBeVisible({ timeout: 10_000 });
  }

  get addFilterButton(): Locator {
    return this.page.getByRole("button", { name: "Add Filter" });
  }

  async assertAddFilterButtonVisible(): Promise<void> {
    await expect(this.addFilterButton).toBeVisible();
  }

  async clickAddFilterButton(): Promise<void> {
    await this.addFilterButton.click();
  }

  get filterSubFormCancelButton(): Locator {
    return this.page.getByRole("button", { name: "Cancel" });
  }

  async assertFilterSubFormVisible(): Promise<void> {
    await expect(this.filterSubFormCancelButton).toBeVisible({ timeout: 10_000 });
  }

  async clickFilterSubFormCancel(): Promise<void> {
    await this.filterSubFormCancelButton.click();
  }

  async assertFilterSubFormHidden(): Promise<void> {
    await expect(this.filterSubFormCancelButton).not.toBeVisible();
  }

 

  async assertOperatorDropdownOptions(paramName: string, operators: string[]): Promise<void> {
    await this.getParamFilterOperatorInput(paramName).click();
    for (const op of operators) {
      await expect(this.page.getByRole("option", { name: op, exact: true })).toBeVisible({ timeout: 5_000 });
    }
  }

  async removeParameterFilter(paramName: string): Promise<void> {
    await this.getParameterFilterIcon(paramName).click();
    await expect(this.getParamFilterOperatorInput(paramName)).toBeVisible({ timeout: 10_000 });
    await this.getFilterRemoveButton(paramName).click();
    await this.waitForNetworkIdle();
  }
}
