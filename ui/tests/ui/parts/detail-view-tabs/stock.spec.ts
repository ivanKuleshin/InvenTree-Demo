import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_WIDGET_ASSEMBLY = 77;

test.describe("TC_UI_PART_DETAIL_TABS", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-TABS-001: Stock tab loads and displays stock items for an assembly part", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 detail page", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
    });

    await test.step("WHEN user clicks the Stock tab", async () => {
      await partDetailPage.tabBar.chooseTab("Stock");
    });

    await test.step("THEN URL updates to the stock path", async () => {
      await expect(page).toHaveURL(/\/part\/77\/(?:details\/)?stock(?!\w)/);
    });

    await test.step("AND the add-stock-item action button is visible", async () => {
      await expect(
        page.locator('button[aria-label="action-button-add-stock-item"]'),
      ).toBeVisible();
    });

    await test.step("AND at least one stock row is visible in the table", async () => {
      await expect(
        partDetailPage.stockTab.root.getByRole("row").nth(1),
      ).toBeVisible();
    });
  });

  test("TC-UI-TABS-002: Add a new stock item from the Stock tab", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 stock tab", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
      await partDetailPage.tabBar.chooseTab("Stock");
      await expect(page).toHaveURL(/\/part\/77\/(?:details\/)?stock(?!\w)/, {
        timeout: 15_000,
      });
    });

    await test.step("WHEN user clicks the Add Stock Item button", async () => {
      await page
        .locator('button[aria-label="action-button-add-stock-item"]')
        .click();
      await expect(page.getByRole("dialog")).toBeVisible({ timeout: 10_000 });
    });

    await test.step("THEN the Add Stock Item dialog is open with a quantity field", async () => {
      await expect(
        page.getByRole("dialog").locator("input, [role='spinbutton']").first(),
      ).toBeVisible();
    });

    await test.step("cleanup: close the dialog", async () => {
      await page.getByRole("button", { name: "Cancel" }).click();
      await expect(page.getByRole("dialog")).not.toBeVisible({
        timeout: 10_000,
      });
    });
  });
});
