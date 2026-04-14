/**
 * Part Creation E2E — create a part with mandatory fields only and verify
 * that it appears in the parts table afterwards.
 */
import { test, expect } from "@playwright/test";
import { STORAGE_STATE } from "@config/storageState";
import { PartsPage } from "@framework/pages/parts/PartsPage";

test.describe("Part Creation — mandatory fields only", () => {
  test.use({ storageState: STORAGE_STATE.ENGINEER });

  test("create a part with name only and verify it appears in the table", async ({
    page,
  }) => {
    const partsPage = new PartsPage(page);
    const partName = `E2E Part ${Date.now()}`;

    await test.step("GIVEN user is on the Parts page", async () => {
      await partsPage.navigate();
    });

    let modal = await test.step(
      "WHEN user opens the Create Part modal",
      async () => {
        const m = await partsPage.openCreatePartModal();
        await expect(m.root).toBeVisible();
        return m;
      },
    );

    await test.step("AND fills only the mandatory Name field", async () => {
      await modal.fillName(partName);
    });

    await test.step("AND submits the form", async () => {
      await modal.submit();
      await expect(modal.root).not.toBeVisible({ timeout: 10_000 });
    });

    await test.step("THEN the new part appears in the parts table", async () => {
      await partsPage.navigate();
      const row = await partsPage.findPart(partName);
      await expect(row.root).toBeVisible();
      expect(await row.getName()).toContain(partName);
    });
  });
});
