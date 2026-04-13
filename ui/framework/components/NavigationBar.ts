import { type Page } from "@playwright/test";
import { BaseComponent } from "@framework/core/BaseComponent";

/**
 * Reusable navigation bar component.
 * InvenTree uses a left sidebar for primary navigation.
 */
export class NavigationBar extends BaseComponent {
  constructor(page: Page) {
    // InvenTree sidebar navigation wrapper
    super(page, 'nav, [data-testid="nav-bar"], .mantine-AppShell-navbar');
  }

  async goToParts(): Promise<void> {
    await this.locator('a[href*="/part/"]').first().click();
    await this.page.waitForLoadState("domcontentloaded");
  }

  async goToStock(): Promise<void> {
    await this.locator('a[href*="/stock/"]').first().click();
    await this.page.waitForLoadState("domcontentloaded");
  }

  async goToBuild(): Promise<void> {
    await this.locator('a[href*="/build/"]').first().click();
    await this.page.waitForLoadState("domcontentloaded");
  }

  async goToPurchaseOrders(): Promise<void> {
    await this.locator('a[href*="/order/purchase-order/"]').first().click();
    await this.page.waitForLoadState("domcontentloaded");
  }

  async goToSalesOrders(): Promise<void> {
    await this.locator('a[href*="/order/sales-order/"]').first().click();
    await this.page.waitForLoadState("domcontentloaded");
  }

  async isVisible(): Promise<boolean> {
    return this.root.isVisible();
  }
}
