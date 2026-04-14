import { test as base } from "@playwright/test";
import { PartCategoriesPage } from "@framework/pages/categories/PartCategoriesPage";
import { PartCategoryDetailPage } from "@framework/pages/categories/PartCategoryDetailPage";

type CategoriesFixtures = {
  categoriesPage: PartCategoriesPage;
  categoryDetailPage: PartCategoryDetailPage;
};

export const test = base.extend<CategoriesFixtures>({
  categoriesPage: async ({ page }, use) => {
    await use(new PartCategoriesPage(page));
  },
  categoryDetailPage: async ({ page }, use) => {
    await use(new PartCategoryDetailPage(page));
  },
});

export { expect } from "@playwright/test";
