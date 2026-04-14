import { expect, type Page } from "@playwright/test";
import { ElementHolder } from "./ElementHolder";

/**
 * Base class for all Page Objects.
 *
 * Subclasses declare `url` as either:
 *   - a relative string path  ("/part/") → used for navigation AND includes-match validation
 *   - a RegExp                (/\/part\/\d+\//) → used for pattern-match validation only;
 *     subclasses must override `navigate()` and supply an explicit path.
 */
export abstract class BasePage extends ElementHolder {
  /**
   * The URL (or pattern) that identifies this page.
   *   string  → relative path used for `goto()` and `includes` URL validation
   *   RegExp  → pattern tested against the full URL after load
   */
  abstract readonly url: string | RegExp;

  constructor(page: Page) {
    super(page);
  }

  /**
   * Navigate to this page using `this.url`.
   * Only works when `url` is a string — throws if it is a RegExp.
   * Override in subclasses that need dynamic paths.
   */
  async navigate(): Promise<void> {
    if (typeof this.url !== "string") {
      throw new Error(
        `${this.constructor.name}: \`url\` is a RegExp — override navigate() and supply an explicit path.`,
      );
    }
    await this.page.goto(this.url);
    await this.waitForLoad();
  }

  /**
   * Navigate to an explicit path and then run `waitForLoad()`.
   * Use this from subclasses whose URL is dynamic (e.g. /part/42/).
   */
  protected async navigateTo(path: string): Promise<void> {
    await this.page.goto(path);
    await this.waitForLoad();
  }

  /**
   * Wait for the page to be ready and assert that the current URL matches `this.url`.
   *
   * Validation rules:
   *   string → current URL must *include* the string (baseURL prefix is stripped automatically)
   *   RegExp → current URL must match the pattern
   *
   * Override in subclasses that need a custom ready-state signal (e.g. waiting for a table).
   * When overriding, call `super.waitForLoad()` or replicate the URL assertion.
   */
  async waitForLoad(): Promise<void> {
    await this.page.waitForLoadState("domcontentloaded");
    await this.assertCurrentUrl();
  }

  /**
   * Assert that the browser's current URL matches `this.url`.
   * Called internally by `waitForLoad()`.
   */
  async assertCurrentUrl(): Promise<void> {
    await expect(this.page).toHaveURL(this.url);
  }
}
