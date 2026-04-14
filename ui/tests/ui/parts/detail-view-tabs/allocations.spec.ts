import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_WIDGET_ASSEMBLY = 77;

test.describe("TC_UI_PART_DETAIL_TABS", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-TABS-007: Allocations tab loads for a salable component part", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 detail page", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
    });

    await test.step("THEN the Allocations tab is present in the tab bar", async () => {
      await expect(
        partDetailPage.tabBar.root.getByRole("tab", {
          name: "Allocations",
          exact: true,
        }),
      ).toBeVisible();
    });

    await test.step("WHEN user clicks the Allocations tab", async () => {
      await partDetailPage.tabBar.chooseTab("Allocations");
    });

    await test.step("THEN URL updates to the allocations path", async () => {
      await expect(page).toHaveURL(/\/part\/77\/(?:details\/)?allocations/, {
        timeout: 15_000,
      });
    });

    await test.step("AND the Allocations tab panel is visible", async () => {
      await expect(partDetailPage.allocationsTab.root).toBeVisible({
        timeout: 10_000,
      });
    });
  });
});
