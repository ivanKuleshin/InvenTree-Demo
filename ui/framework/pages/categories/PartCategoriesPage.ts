import { expect, type Locator, type Page } from "@playwright/test";
import { BasePage } from "@framework/core/BasePage";
import { CategoryTable } from "@framework/components/categories/CategoryTable";

export class PartCategoriesPage extends BasePage {
  readonly url = "/web/part/category/index/subcategories";

  readonly categoriesTable: CategoryTable;

  constructor(page: Page) {
    super(page);
    this.categoriesTable = new CategoryTable(page);
  }

  get partCategoriesTab(): Locator {
    return this.page.getByRole("tab", { name: "Part Categories", exact: true });
  }

  get breadcrumbRoot(): Locator {
    return this.page.locator("//div[contains(@class, 'Breadcrumbs-root')]");
  }

  getBreadcrumbLink(name: string): Locator {
    return this.breadcrumbRoot.getByRole("link", { name, exact: true });
  }

  async assertPageTitle(): Promise<void> {
    await expect(this.page).toHaveTitle(/InvenTree Demo Server \| Parts/);
  }

  override async waitForLoad(): Promise<void> {
    await this.page.waitForLoadState("domcontentloaded");
    await this.assertCurrentUrl();
    await this.partCategoriesTab.waitFor({ state: "visible", timeout: 30_000 });
  }
}
