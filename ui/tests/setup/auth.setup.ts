/**
 * Authentication setup tests.
 *
 * Playwright runs this "setup" project before the main test projects.
 * Each test logs in as one role and saves the browser storage state to
 * .auth/<role>.json so that regular tests can reuse authenticated sessions
 * without repeating the login flow.
 *
 * Referenced in playwright.config.ts via:
 *   projects: [{ name: 'setup', testMatch: this file }]
 */
import { test as setup } from "@playwright/test";
import { LoginPage } from "../../framework/pages/auth/LoginPage";
import { STORAGE_STATE_BY_ROLE } from "../../config/storageState";
import { getUser, ALL_ROLES } from "../../config/users";

for (const role of ALL_ROLES) {
  setup(`authenticate: ${role}`, async ({ page }) => {
    const user = getUser(role);
    const loginPage = new LoginPage(page);

    await loginPage.navigate();
    await loginPage.login(user.username, user.password);
    await loginPage.waitForLoginSuccess();

    await page.context().storageState({ path: STORAGE_STATE_BY_ROLE[role] });
  });
}
