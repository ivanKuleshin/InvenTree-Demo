import { type Locator, type Page } from "@playwright/test";

/**
 * Abstract base class for all page objects and components.
 * Holds a reference to the Playwright Page and provides
 * a shared locator helper used by subclasses.
 */
export abstract class ElementHolder {
  readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  /**
   * Shorthand for page.locator() scoped to an optional root.
   * Subclasses that have a root locator (components) override this.
   */
  protected locator(selector: string): Locator {
    return this.page.locator(selector);
  }

  /**
   * Wait for the network to be idle — useful after navigation or heavy interactions.
   */
  protected async waitForNetworkIdle(): Promise<void> {
    await this.page.waitForLoadState("networkidle");
  }
}
