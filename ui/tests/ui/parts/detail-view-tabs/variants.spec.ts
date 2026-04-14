import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_WIDGET_ASSEMBLY = 77;
const PART_DOOHICKEY = 87;

test.describe("TC_UI_PART_DETAIL_TABS", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-TABS-016: Variants tab is visible on a template part and lists variant parts", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 detail page", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
    });

    await test.step("THEN the Variants tab is present in the tab bar", async () => {
      await expect(
        partDetailPage.tabBar.root.getByRole("tab", {
          name: "Variants",
          exact: true,
        }),
      ).toBeVisible();
    });

    await test.step("WHEN user clicks the Variants tab", async () => {
      await partDetailPage.tabBar.chooseTab("Variants");
    });

    await test.step("THEN URL updates to the variants path", async () => {
      await expect(page).toHaveURL(/\/part\/77\/(?:details\/)?variants/, {
        timeout: 15_000,
      });
    });

    await test.step("AND the variants table shows at least 4 variant rows", async () => {
      const tabPanel = page.getByRole("tabpanel", { name: "Variants" });
      await expect(tabPanel).toBeVisible();
      await expect(tabPanel.getByRole("row").nth(4)).toBeVisible({
        timeout: 20_000,
      });
    });
  });

  test("TC-UI-TABS-018: Variants tab is absent for a non-template part", async ({
    partDetailPage,
  }) => {
    await test.step("GIVEN user navigates to Part 87 detail page", async () => {
      await partDetailPage.navigate(PART_DOOHICKEY);
    });

    await test.step("THEN the Variants tab is NOT present in the tab bar", async () => {
      await expect(
        partDetailPage.tabBar.root.getByRole("tab", {
          name: "Variants",
          exact: true,
        }),
      ).not.toBeVisible({ timeout: 5_000 });
    });
  });
});
