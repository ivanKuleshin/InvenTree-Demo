import { expect, type Locator, type Page } from "@playwright/test";
import { BasePage } from "@framework/core/BasePage";
import { CategoryDetailTabBar } from "@framework/components/categories/CategoryDetailTabBar";
import { CategoryTable } from "@framework/components/categories/CategoryTable";
import { CategoryPartsTable } from "@framework/components/categories/CategoryPartsTable";

export class PartCategoryDetailPage extends BasePage {
  readonly url = /\/web\/part\/category\/\d+\//;

  readonly tabBar: CategoryDetailTabBar;
  readonly subcategoriesTable: CategoryTable;
  readonly partsTable: CategoryPartsTable;

  constructor(page: Page) {
    super(page);
    this.tabBar = new CategoryDetailTabBar(page);
    this.subcategoriesTable = new CategoryTable(page);
    this.partsTable = new CategoryPartsTable(page);
  }

  override async navigate(
    categoryId?: number | string,
    tab: "details" | "subcategories" | "parts" | "stock" | "parameters" = "details",
  ): Promise<void> {
    if (categoryId == null) {
      throw new Error("PartCategoryDetailPage.navigate() requires a categoryId argument.");
    }
    await this.navigateTo(`/web/part/category/${categoryId}/${tab}`);
  }

  get breadcrumbRoot(): Locator {
    return this.page.locator("//div[contains(@class, 'Breadcrumbs-root')]");
  }

  getBreadcrumbLink(name: string): Locator {
    return this.breadcrumbRoot.locator("a").filter({ hasText: name });
  }

  async assertBreadcrumbs(expected: string[]): Promise<void> {
    const links = this.breadcrumbRoot.locator("a");
    for (let i = 0; i < expected.length; i++) {
      await expect(links.nth(i)).toHaveText(expected[i]);
    }
  }

  get categoryDetailsPanel(): Locator {
    return this.page.getByRole("tabpanel", { name: "Category Details" });
  }

  getDetailFieldValue(text: string): Locator {
    return this.categoryDetailsPanel.getByText(text);
  }

  get categorySubtitle(): Locator {
    return this.page.locator("h6, p").filter({ hasText: /\S+/ }).first();
  }

  currentCategoryId(): string {
    const match = this.page.url().match(/\/web\/part\/category\/(\d+)\//);
    return match ? match[1] : "";
  }

  async assertPageTitle(): Promise<void> {
    await expect(this.page).toHaveTitle(/InvenTree Demo Server \| Part Category/);
  }

  async assertUrl(categoryId: number | string, tab?: string): Promise<void> {
    const segment = tab ? `${categoryId}/${tab}` : `${categoryId}/`;
    await expect(this.page).toHaveURL(
      new RegExp(`/web/part/category/${segment}`)
    );
  }

  async assertCategoryDetailUrlPattern(): Promise<void> {
    await expect(this.page).toHaveURL(/\/web\/part\/category\/\d+\//);
  }

  async assertDescriptionText(text: string): Promise<void> {
    await expect(this.page.getByText(text).first()).toBeVisible();
  }

  async assertBreadcrumbSegmentVisible(text: string): Promise<void> {
    await expect(this.breadcrumbRoot.getByText(text, { exact: false })).toBeVisible();
  }

  async assertPartsCountVisible(count: number): Promise<void> {
    await expect(this.categoryDetailsPanel.getByText(String(count))).toBeVisible();
  }

  override async waitForLoad(): Promise<void> {
    await this.page.waitForLoadState("domcontentloaded");
    this.assertCurrentUrl();
  }
}
