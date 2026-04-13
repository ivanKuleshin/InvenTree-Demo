import { type Locator, type Page } from "@playwright/test";
import { ElementHolder } from "./ElementHolder";

/**
 * Base class for reusable UI components (modals, panels, navigation bars, etc.).
 * Scopes all locator queries to a root element, preventing cross-component leakage.
 */
export abstract class BaseComponent extends ElementHolder {
  readonly root: Locator;

  constructor(page: Page, rootSelector: string | Locator) {
    super(page);
    this.root =
      typeof rootSelector === "string"
        ? page.locator(rootSelector)
        : rootSelector;
  }

  /**
   * All locator queries inside a component are scoped to the root element.
   */
  protected override locator(selector: string): Locator {
    return this.root.locator(selector);
  }

  /** Wait for the component's root element to be visible */
  async waitForVisible(): Promise<void> {
    await this.root.waitFor({ state: "visible" });
  }

  /** Wait for the component's root element to be hidden */
  async waitForHidden(): Promise<void> {
    await this.root.waitFor({ state: "hidden" });
  }

  /** Whether the component's root element is currently visible */
  async isVisible(): Promise<boolean> {
    return this.root.isVisible();
  }
}
