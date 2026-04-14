import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_WIDGET_ASSEMBLY = 77;

test.describe("TC_UI_PART_DETAIL_TABS", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-TABS-025: Related Parts tab loads and shows the panel", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 detail page", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
    });

    await test.step("WHEN user clicks the Related Parts tab", async () => {
      await partDetailPage.tabBar.chooseTab("Related Parts");
    });

    await test.step("THEN URL updates to the related_parts path", async () => {
      await expect(page).toHaveURL(/\/part\/77\/(?:details\/)?related_parts/, {
        timeout: 15_000,
      });
    });

    await test.step("AND the Related Parts tab panel is visible", async () => {
      await expect(partDetailPage.relatedPartsTab.root).toBeVisible({
        timeout: 10_000,
      });
    });
  });

  test("TC-UI-TABS-026: Add a related part from the Related Parts tab", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 related parts tab", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
      await partDetailPage.tabBar.chooseTab("Related Parts");
      await expect(page).toHaveURL(/\/part\/77\/(?:details\/)?related_parts/, {
        timeout: 15_000,
      });
    });

    await test.step("WHEN user opens the Add Related Part dialog", async () => {
      await page
        .locator('button[aria-label="action-button-add-related-part"]')
        .click();
      await expect(page.getByRole("dialog")).toBeVisible({ timeout: 10_000 });
    });

    await test.step("AND searches for and selects Doohickey (Part 87)", async () => {
      await page
        .getByRole("dialog")
        .getByRole("combobox", { name: /related-field-part/i })
        .fill("Doohickey");
      await page
        .getByRole("option", { name: /Doohickey/i })
        .first()
        .click();
    });

    await test.step("AND submits the form", async () => {
      await page.getByRole("button", { name: "Submit" }).click();
      await expect(page.getByRole("dialog")).not.toBeVisible({
        timeout: 15_000,
      });
    });

    await test.step("THEN Doohickey appears in the Related Parts table", async () => {
      await expect(
        partDetailPage.relatedPartsTab.root.getByText("Doohickey"),
      ).toBeVisible({ timeout: 15_000 });
    });
  });
});
