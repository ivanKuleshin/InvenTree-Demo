import { test, expect } from "@fixtures/categories.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const FASTENERS_ID = 3;
const ELECTRONICS_ID = 719;
const RESISTORS_ID = 768;
const RESISTORS_0805_ID = 770;
const STRUCTURAL_CATEGORY_ID = 24;

test.describe("TC_UI_CAT_HIERARCHY", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-CAT-001: Top-level category list loads with expected columns and data", async ({
    categoriesPage,
  }) => {
    await test.step("GIVEN user navigates to the top-level Part Categories page", async () => {
      await categoriesPage.navigate();
      await categoriesPage.waitForLoad();
    });

    await test.step("THEN the page title reads InvenTree Demo Server | Parts", async () => {
      await categoriesPage.assertPageTitle();
    });

    await test.step("AND the Part Categories tab is selected", async () => {
      await expect(categoriesPage.partCategoriesTab).toBeVisible();
    });

    await test.step("AND the categories table shows the expected column headers", async () => {
      await categoriesPage.categoriesTable.assertColumnHeadersVisible(["Name", "Description", "Path", "Parts"]);
    });

    await test.step("AND the pagination shows a count greater than zero", async () => {
      await expect(categoriesPage.categoriesTable.paginationText).toBeVisible();
      const count = await categoriesPage.categoriesTable.getTotalCount();
      expect(count).toBeGreaterThan(0);
    });

    await test.step("AND the expected category rows are present", async () => {
      await categoriesPage.categoriesTable.assertRowVisible(/Fasteners/);
      await categoriesPage.categoriesTable.assertRowVisible(/Electronics 033b68f9/);
      await categoriesPage.categoriesTable.assertRowVisible(/Furniture/);
    });

    await test.step("AND each category name cell is clickable", async () => {
      const fastenersCell = categoriesPage.categoriesTable.getCategoryNameCell("Fasteners");
      await expect(fastenersCell).toBeVisible();
    });
  });

  test("TC-UI-CAT-002: Navigating into a child category opens its dedicated page", async ({
    categoriesPage,
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user is on the top-level categories page", async () => {
      await categoriesPage.navigate();
      await categoriesPage.waitForLoad();
    });

    await test.step("WHEN user clicks the Fasteners category", async () => {
      await categoriesPage.categoriesTable.clickCategory("Fasteners");
    });

    await test.step("THEN the URL changes to the Fasteners category page", async () => {
      await categoryDetailPage.assertUrl(FASTENERS_ID, "details");
    });

    await test.step("AND the page title reads InvenTree Demo Server | Part Category", async () => {
      await categoryDetailPage.assertPageTitle();
    });

    await test.step("AND the breadcrumb shows Parts > Fasteners", async () => {
      await categoryDetailPage.assertBreadcrumbs(["Parts"]);
      await categoryDetailPage.assertBreadcrumbSegmentVisible("Fasteners");
    });

    await test.step("AND the category subtitle shows the description", async () => {
      await categoryDetailPage.assertDescriptionText("Screws / nuts / bolts / etc");
    });

    await test.step("AND all category tabs are visible", async () => {
      await categoryDetailPage.tabBar.assertTabsVisible([
        "Category Details",
        "Subcategories",
        "Parts",
        "Stock Items",
        "Category Parameters",
      ]);
    });
  });

  test("TC-UI-CAT-003: Category Details tab shows all metadata fields", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates directly to Fasteners category details", async () => {
      await categoryDetailPage.navigate(FASTENERS_ID, "details");
    });

    await test.step("THEN the Category Details tab is selected", async () => {
      await expect(categoryDetailPage.tabBar.getTab("Category Details")).toBeVisible();
    });

    await test.step("AND the Name field shows Fasteners", async () => {
      await expect(categoryDetailPage.getDetailFieldValue("Fasteners")).toBeVisible();
    });

    await test.step("AND the Description field shows the correct value", async () => {
      await expect(categoryDetailPage.getDetailFieldValue("Screws / nuts / bolts / etc")).toBeVisible();
    });

    await test.step("AND the Structural field shows NO", async () => {
      await expect(categoryDetailPage.categoryDetailsPanel.getByText("NO")).toBeVisible();
    });

    await test.step("AND the Parts field shows a numeric count", async () => {
      await categoryDetailPage.assertPartsCountVisible(247);
    });
  });

  test("TC-UI-CAT-004: Three-level nested category shows full pathstring and multi-segment breadcrumb", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates to the 0805 category details page", async () => {
      await categoryDetailPage.navigate(RESISTORS_0805_ID, "details");
    });

    await test.step("THEN the page title reads InvenTree Demo Server | Part Category", async () => {
      await categoryDetailPage.assertPageTitle();
    });

    await test.step("AND the breadcrumb shows all four segments", async () => {
      await categoryDetailPage.assertBreadcrumbs(["Parts", "Electronics 033b68f9", "电阻"]);
      await categoryDetailPage.assertBreadcrumbSegmentVisible("0805");
    });

    await test.step("AND the Path field shows the full hierarchy", async () => {
      await expect(categoryDetailPage.getDetailFieldValue("Electronics 033b68f9/电阻/0805")).toBeVisible();
    });

    await test.step("AND breadcrumb ancestor links are clickable", async () => {
      const electronicsLink = categoryDetailPage.getBreadcrumbLink("Electronics 033b68f9");
      await expect(electronicsLink).toHaveAttribute("href");
    });

    await test.step("WHEN user clicks the 电阻 breadcrumb segment", async () => {
      await categoryDetailPage.getBreadcrumbLink("电阻").click();
    });

    await test.step("THEN the URL changes to the 电阻 category page", async () => {
      await categoryDetailPage.assertUrl(RESISTORS_ID);
    });
  });

  test("TC-UI-CAT-005: Structural category shows Structural = YES and contains no directly assigned parts", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates to the structural category details page", async () => {
      await categoryDetailPage.navigate(STRUCTURAL_CATEGORY_ID, "details");
    });

    await test.step("THEN the Structural field shows YES", async () => {
      await expect(categoryDetailPage.getDetailFieldValue("YES")).toBeVisible();
    });

    await test.step("AND the Subcategories field shows 2", async () => {
      await expect(categoryDetailPage.categoryDetailsPanel.getByText("2", { exact: true }).first()).toBeVisible();
    });

    await test.step("WHEN user clicks the Parts tab", async () => {
      await categoryDetailPage.tabBar.chooseTab("Parts");
    });

    await test.step("THEN the URL updates to the parts path", async () => {
      await categoryDetailPage.assertUrl(STRUCTURAL_CATEGORY_ID, "parts");
    });

    await test.step("AND the parts table shows No records found", async () => {
      await expect(categoryDetailPage.partsTable.emptyStateText).toBeVisible();
    });
  });

  test("TC-UI-CAT-006: Breadcrumb link navigates up to parent category", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user is on the 0805 category details page", async () => {
      await categoryDetailPage.navigate(RESISTORS_0805_ID, "details");
    });

    await test.step("AND the breadcrumb shows the full hierarchy", async () => {
      await categoryDetailPage.assertBreadcrumbs(["Parts", "Electronics 033b68f9", "电阻"]);
    });

    await test.step("WHEN user clicks the 电阻 breadcrumb link", async () => {
      await categoryDetailPage.getBreadcrumbLink("电阻").click();
    });

    await test.step("THEN the URL changes to the 电阻 category page", async () => {
      await categoryDetailPage.assertUrl(RESISTORS_ID);
    });

    await test.step("AND the breadcrumb updates to show Parts > Electronics 033b68f9 > 电阻", async () => {
      await categoryDetailPage.assertBreadcrumbs(["Parts", "Electronics 033b68f9"]);
      await categoryDetailPage.assertBreadcrumbSegmentVisible("电阻");
    });

    await test.step("WHEN user clicks the Electronics 033b68f9 breadcrumb link", async () => {
      await categoryDetailPage.getBreadcrumbLink("Electronics 033b68f9").click();
    });

    await test.step("THEN the URL changes to the Electronics category page", async () => {
      await categoryDetailPage.assertUrl(ELECTRONICS_ID);
    });
  });

  test("TC-UI-CAT-007: Sub-categories tab lists direct children of a parent category", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates to the Electronics category subcategories", async () => {
      await categoryDetailPage.navigate(ELECTRONICS_ID, "subcategories");
    });

    await test.step("THEN the URL is the subcategories path", async () => {
      await categoryDetailPage.assertUrl(ELECTRONICS_ID, "subcategories");
    });

    await test.step("AND the subcategories table shows the expected columns", async () => {
      await categoryDetailPage.subcategoriesTable.assertColumnHeadersVisible(["Name", "Description", "Path", "Parts"]);
    });

    await test.step("AND the table contains child category rows", async () => {
      const rowCount = await categoryDetailPage.subcategoriesTable.rowCount();
      expect(rowCount).toBeGreaterThan(0);
    });

    await test.step("AND category name cells are clickable", async () => {
      await expect(categoryDetailPage.subcategoriesTable.getRowByIdx(0)).toBeVisible();
      const childName = await categoryDetailPage.subcategoriesTable.clickFirstCategory();
      await categoryDetailPage.assertCategoryDetailUrlPattern();

      await test.step("AND the breadcrumb gains a new segment", async () => {
        await categoryDetailPage.assertBreadcrumbSegmentVisible(childName);
      });
    });
  });

  test("TC-UI-CAT-008: Leaf-node category shows empty Subcategories tab", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates to Fasteners subcategories tab", async () => {
      await categoryDetailPage.navigate(FASTENERS_ID, "subcategories");
    });

    await test.step("THEN the URL is the subcategories path", async () => {
      await categoryDetailPage.assertUrl(FASTENERS_ID, "subcategories");
    });

    await test.step("AND the subcategories table shows No records found", async () => {
      await expect(categoryDetailPage.subcategoriesTable.emptyStateText).toBeVisible();
    });
  });
});
