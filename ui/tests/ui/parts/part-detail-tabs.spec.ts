import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const PART_WIDGET_ASSEMBLY = 77;
const PART_DOOHICKEY = 87;
const PART_1551ABK = 82;

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
        page.locator('button[aria-label="action-button-validate-bom"]'),
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
      ).toBeVisible();
    });
  });

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
      ).toBeVisible();
    });
  });

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
