import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_DOOHICKEY = 87;
const PART_1551ABK = 82;

test.describe("TC_UI_PART_DETAIL_TABS", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-TABS-004: Bill of Materials tab visible for assembly part and shows BOM items", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 87 detail page", async () => {
      await partDetailPage.navigate(PART_DOOHICKEY);
    });

    await test.step("THEN the Bill of Materials tab is present in the tab bar", async () => {
      await expect(
        partDetailPage.tabBar.root.getByRole("tab", {
          name: "Bill of Materials",
          exact: true,
        }),
      ).toBeVisible();
    });

    await test.step("WHEN user clicks the Bill of Materials tab", async () => {
      await partDetailPage.tabBar.chooseTab("Bill of Materials");
    });

    await test.step("THEN URL updates to the bom path", async () => {
      await expect(page).toHaveURL(/\/part\/87\/(?:details\/)?bom(?!\w)/, {
        timeout: 15_000,
      });
    });

    await test.step("AND the validate-bom action button is visible", async () => {
      await expect(
        page.locator('button[aria-label="bom-validation-info"]'),
      ).toBeVisible();
    });

    await test.step("AND at least one BOM row is visible in the table", async () => {
      await expect(
        page
          .getByRole("tabpanel", { name: "Bill of Materials" })
          .getByRole("row")
          .nth(1),
      ).toBeVisible();
    });
  });

  test("TC-UI-TABS-006: Bill of Materials tab is absent for a non-assembly part", async ({
    partDetailPage,
  }) => {
    await test.step("GIVEN user navigates to Part 82 detail page", async () => {
      await partDetailPage.navigate(PART_1551ABK);
    });

    await test.step("THEN the Bill of Materials tab is NOT present in the tab bar", async () => {
      await expect(
        partDetailPage.tabBar.root.getByRole("tab", {
          name: "Bill of Materials",
          exact: true,
        }),
      ).not.toBeVisible({ timeout: 5_000 });
    });
  });
});
