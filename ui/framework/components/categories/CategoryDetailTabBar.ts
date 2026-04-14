import { expect, type Locator, type Page } from "@playwright/test";
import { BaseComponent } from "@framework/core/BaseComponent";

export type CategoryDetailTab =
  | "Category Details"
  | "Subcategories"
  | "Parts"
  | "Stock Items"
  | "Category Parameters";

export class CategoryDetailTabBar extends BaseComponent {
  constructor(page: Page) {
    super(page, page.getByRole("tablist", { name: "panel-tabs-partcategory" }));
  }

  async chooseTab(tab: CategoryDetailTab): Promise<void> {
    await this.root.getByRole("tab", { name: tab, exact: true }).click();
    await this.page.waitForLoadState("networkidle");
  }

  getTab(tab: CategoryDetailTab): Locator {
    return this.root.getByRole("tab", { name: tab, exact: true });
  }

  async activeTabName(): Promise<string | null> {
    return this.root.getByRole("tab", { selected: true }).textContent();
  }

  async assertTabsVisible(tabs: CategoryDetailTab[]): Promise<void> {
    for (const tab of tabs) {
      await expect(this.getTab(tab)).toBeVisible();
    }
  }
}
