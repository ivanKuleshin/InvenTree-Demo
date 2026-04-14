import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_1551ABK = 82;

test.describe("TC_UI_PART_DETAIL_TABS", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-TABS-019: Revision selector dropdown appears for a part that has revisions", async ({
    partDetailPage,
    page,
  }) => {
    await test.step("GIVEN user navigates to Part 82 detail page", async () => {
      await partDetailPage.navigate(PART_1551ABK);
    });

    await test.step("THEN the revision selector combobox is visible", async () => {
      await expect(
        page.getByRole("combobox", { name: "part-revision-select" }),
      ).toBeVisible({ timeout: 10_000 });
    });
  });
});
