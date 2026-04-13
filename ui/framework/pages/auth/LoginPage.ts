import { type Locator, type Page } from "@playwright/test";
import { BasePage } from "@framework/core/BasePage";

/**
 * InvenTree login page.
 * Used exclusively in setup tests to generate authenticated storageState files.
 *
 * The demo app redirects unauthenticated users to the login page on any route,
 * so we navigate to '/' and wait for the login form to appear.
 */
export class LoginPage extends BasePage {
  /**
   * URL pattern for validation — InvenTree's React frontend typically lands on
   * a route containing "login" after an unauthenticated redirect.
   * Override `waitForLoad()` so we wait for the form instead of doing a strict URL check.
   */
  readonly url = /login/i;

  constructor(page: Page) {
    super(page);
  }

  // ── Locators ─────────────────────────────────────────────────────────────

  get usernameInput(): Locator {
    return this.page.locator('input[aria-label="login-username"]');
  }

  get passwordInput(): Locator {
    return this.page.locator('input[aria-label="login-password"]');
  }

  get submitButton(): Locator {
    return this.page.locator('button[type="submit"]').first();
  }

  get errorMessage(): Locator {
    return this.page
      .locator('[role="alert"], .alert-danger, .error-message')
      .first();
  }

  // ── Actions ──────────────────────────────────────────────────────────────

  /**
   * Navigate to the app root — InvenTree redirects to the login page when unauthenticated.
   */
  override async navigate(): Promise<void> {
    await this.page.goto("/");
    await this.waitForLoad();
  }

  /**
   * Fill credentials and submit.
   */
  async login(username: string, password: string): Promise<void> {
    await this.usernameInput.fill(username);
    await this.passwordInput.fill(password);
    await this.submitButton.click();
  }

  /**
   * Wait until the browser has redirected away from the login page,
   * indicating a successful authentication.
   */
  async waitForLoginSuccess(): Promise<void> {
    await this.page.waitForURL(
      (url) => !url.href.toLowerCase().includes("login"),
      {
        timeout: 40000,
      },
    );
    await this.page.waitForLoadState("networkidle");
  }

  // ── Overrides ─────────────────────────────────────────────────────────────

  /**
   * Wait for the login form to be visible (instead of URL-pattern matching,
   * which is unreliable across InvenTree deployment configurations).
   */
  override async waitForLoad(): Promise<void> {
    await this.page.waitForLoadState("domcontentloaded");
    await this.usernameInput.waitFor({ state: "visible", timeout: 20_000 });
  }
}
