import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_WIDGET_ASSEMBLY = 77;

test.describe("TC_UI_PART_DETAIL_TABS", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-TABS-022: Attachments tab loads and shows the panel", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 detail page", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
    });

    await test.step("WHEN user clicks the Attachments tab", async () => {
      await partDetailPage.tabBar.chooseTab("Attachments");
    });

    await test.step("THEN URL updates to the attachments path", async () => {
      await expect(page).toHaveURL(/\/part\/77\/(?:details\/)?attachments/, {
        timeout: 15_000,
      });
    });

    await test.step("AND the Attachments tab panel is visible", async () => {
      await expect(partDetailPage.attachmentsTab.root).toBeVisible({
        timeout: 10_000,
      });
    });
  });

  test("TC-UI-TABS-023: Upload a file attachment from the Attachments tab", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 77 attachments tab", async () => {
      await partDetailPage.navigate(PART_WIDGET_ASSEMBLY);
      await partDetailPage.tabBar.chooseTab("Attachments");
      await expect(partDetailPage.attachmentsTab.root).toBeVisible({
        timeout: 10_000,
      });
    });

    await test.step("WHEN user clicks the Add Attachment button", async () => {
      await partDetailPage.attachmentsTab.root
        .locator('button[aria-label^="action-button-add"]')
        .first()
        .click();
      await expect(page.getByRole("dialog")).toBeVisible({ timeout: 10_000 });
    });

    await test.step("AND selects a file using the file input", async () => {
      await page
        .getByRole("dialog")
        .locator('input[type="file"]')
        .setInputFiles({
          name: "tc023-test-datasheet.txt",
          mimeType: "text/plain",
          buffer: Buffer.from("TC-023 test attachment content"),
        });
    });

    await test.step("AND submits the upload form", async () => {
      await page.getByRole("button", { name: "Submit" }).click();
    });

    await test.step("THEN the uploaded file appears in the Attachments table", async () => {
      await expect(
        partDetailPage.attachmentsTab.root.getByText(
          "tc023-test-datasheet.txt",
        ),
      ).toBeVisible({ timeout: 15_000 });
    });
  });
});
