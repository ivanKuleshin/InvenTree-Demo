import { test as base } from "@playwright/test";
import { PartsPage } from "@framework/pages/parts/PartsPage";
import { PartDetailsTabPage } from "@framework/pages/parts/PartDetailsTabPage";

type PartsFixtures = {
  partsPage: PartsPage;
  partDetailPage: PartDetailsTabPage;
};

export const test = base.extend<PartsFixtures>({
  partsPage: async ({ page }, use) => {
    await use(new PartsPage(page));
  },
  partDetailPage: async ({ page }, use) => {
    await use(new PartDetailsTabPage(page));
  },
});

export { expect } from "@playwright/test";
