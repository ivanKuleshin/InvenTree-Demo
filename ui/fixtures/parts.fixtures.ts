import { test as base } from "@playwright/test";
import { PartsPage } from "@framework/pages/parts/PartsPage";
import { PartsDetailViewPage } from "@framework/pages/parts/PartsDetailViewPage";

type PartsFixtures = {
  partsPage: PartsPage;
  partDetailPage: PartsDetailViewPage;
};

export const test = base.extend<PartsFixtures>({
  partsPage: async ({ page }, use) => {
    await use(new PartsPage(page));
  },
  partDetailPage: async ({ page }, use) => {
    await use(new PartsDetailViewPage(page));
  },
});

export { expect } from "@playwright/test";
