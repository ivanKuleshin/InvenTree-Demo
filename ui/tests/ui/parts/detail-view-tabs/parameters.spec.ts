import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_WIDGET_ASSEMBLY = 77;

test.describe("TC_UI_PART_DETAIL_TABS", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-TABS-013: Parameters tab loads and shows the parameters panel", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 detail page", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
    });

    await test.step("WHEN user clicks the Parameters tab", async () => {
      await partDetailPage.tabBar.chooseTab("Parameters");
    });

    await test.step("THEN URL updates to the parameters path", async () => {
      await expect(page).toHaveURL(/\/part\/77\/(?:details\/)?parameters/, {
        timeout: 15_000,
      });
    });

    await test.step("AND the Parameters tab panel is visible", async () => {
      await expect(partDetailPage.parametersTab.root).toBeVisible({
        timeout: 10_000,
      });
    });
  });
});
