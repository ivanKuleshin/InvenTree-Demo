/**
 * Test fixtures — page object injection only.
 *
 * Authentication is NOT handled here.
 * Tests set the storageState themselves:
 *
 *   import { test, expect } from '../fixtures';
 *   import { STORAGE_STATE } from '../config/storageState';
 *
 *   test.use({ storageState: STORAGE_STATE.ALLACCESS });
 *
 *   test('can view parts', async ({ partsListPage }) => {
 *     await partsListPage.navigate();
 *     ...
 *   });
 */
import { test as base, expect } from "@playwright/test";
import { LoginPage } from "../framework/pages/auth/LoginPage";
import { PartsListPage } from "../framework/pages/parts/PartsListPage";
import { PartDetailPage } from "../framework/pages/parts/PartDetailPage";
import { NavigationBar } from "../framework/components/NavigationBar";

interface PageFixtures {
  loginPage: LoginPage;
  partsListPage: PartsListPage;
  partDetailPage: PartDetailPage;
  navigationBar: NavigationBar;
}

export const test = base.extend<PageFixtures>({
  loginPage: async ({ page }, use) => {
    await use(new LoginPage(page));
  },

  partsListPage: async ({ page }, use) => {
    await use(new PartsListPage(page));
  },

  partDetailPage: async ({ page }, use) => {
    await use(new PartDetailPage(page));
  },

  navigationBar: async ({ page }, use) => {
    await use(new NavigationBar(page));
  },
});

export { expect };
export type { PageFixtures };
