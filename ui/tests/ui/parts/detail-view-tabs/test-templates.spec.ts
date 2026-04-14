import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_WIDGET_ASSEMBLY = 77;
const PART_1551ABK = 82;

test.describe("TC_UI_PART_DETAIL_TABS", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-TABS-028: Test Templates tab loads and shows test templates for a testable part", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 detail page", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
    });

    await test.step("THEN the Test Templates tab is present in the tab bar", async () => {
      await expect(
        partDetailPage.tabBar.root.getByRole("tab", {
          name: "Test Templates",
          exact: true,
        }),
      ).toBeVisible();
    });

    await test.step("WHEN user clicks the Test Templates tab", async () => {
      await partDetailPage.tabBar.chooseTab("Test Templates");
    });

    await test.step("THEN URL updates to the test_templates path", async () => {
      await expect(page).toHaveURL(
        /\/part\/77\/(?:details\/)?test_templates/
      );
    });

    await test.step("AND the Test Templates table shows at least 4 rows", async () => {
      const tabPanel = page.getByRole("tabpanel", { name: "Test Templates" });
      await expect(tabPanel).toBeVisible();
      await expect(tabPanel.getByRole("row").nth(4)).toBeVisible({
        timeout: 20_000,
      });
    });

    await test.step("AND the known test template names are visible", async () => {
      await expect(
        page.getByText("Commissioning", { exact: true }),
      ).toBeVisible();
      await expect(page.getByText("Optional", { exact: true })).toBeVisible();
    });
  });

  test("TC-UI-TABS-030: Test Templates tab is absent for a non-testable part", async ({
    partDetailPage,
  }) => {
    await test.step("GIVEN user navigates to Part 82 detail page", async () => {
      await partDetailPage.navigate(PART_1551ABK);
    });

    await test.step("THEN the Test Templates tab is NOT present in the tab bar", async () => {
      await expect(
        partDetailPage.tabBar.root.getByRole("tab", {
          name: "Test Templates",
          exact: true,
        }),
      ).not.toBeVisible({ timeout: 5_000 });
    });
  });
});
