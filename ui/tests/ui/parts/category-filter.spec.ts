import { test, expect } from "@fixtures/categories.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const FASTENERS_ID = 3;
const ELECTRONICS_ID = 719;

test.describe("TC_UI_CAT_FILTER", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test("TC-UI-CAT-009: Parts tab shows all parts including sub-category parts by default (cascade on)", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates to the Electronics category parts tab", async () => {
      await categoryDetailPage.navigate(ELECTRONICS_ID, "parts");
    });

    await test.step("THEN the URL is the parts path", async () => {
      await categoryDetailPage.assertUrl(ELECTRONICS_ID, "parts");
    });

    await test.step("AND the parts table is visible", async () => {
      await categoryDetailPage.partsTable.assertVisible();
    });

    await test.step("AND the total count includes parts from sub-categories", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).toHaveText(/\d+ - \d+ \/ [1-9]/);
    });
  });

  test("TC-UI-CAT-010: Parts table search filter narrows results by part name", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates to the Fasteners category parts tab", async () => {
      await categoryDetailPage.navigate(FASTENERS_ID, "parts");
    });

    let initialText: string;

    await test.step("AND the full parts list is shown", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
      initialText = (await categoryDetailPage.partsTable.paginationText.textContent()) ?? "";
      expect(initialText).toMatch(/\d+ - \d+ \/ [1-9]/);
    });

    await test.step("WHEN user types M3x10 in the search box", async () => {
      await categoryDetailPage.partsTable.search("M3x10");
    });

    await test.step("THEN the pagination count decreases", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).not.toHaveText(initialText);
    });

    await test.step("AND all visible part names contain M3x10", async () => {
      await categoryDetailPage.partsTable.assertFirstNCellsContainText(5, "M3x10");
    });

    await test.step("WHEN user clears the search box", async () => {
      await categoryDetailPage.partsTable.clearSearch();
    });

    await test.step("THEN the table returns to the original count", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).toHaveText(initialText);
    });
  });

  test("TC-UI-CAT-011: Table Filters drawer opens and contains an Add Filter button", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates to the Fasteners category parts tab", async () => {
      await categoryDetailPage.navigate(FASTENERS_ID, "parts");
    });

    await test.step("AND the table is loaded", async () => {
      await expect(categoryDetailPage.partsTable.filterButton).toBeVisible();
    });

    await test.step("WHEN user clicks the table-select-filters button", async () => {
      await categoryDetailPage.partsTable.filterButton.click();
    });

    await test.step("THEN a Table Filters dialog appears", async () => {
      await categoryDetailPage.partsTable.assertFilterDrawerVisible();
    });

    await test.step("AND the dialog contains an Add Filter button", async () => {
      await categoryDetailPage.partsTable.assertAddFilterButtonVisible();
    });

    await test.step("WHEN user clicks the Add Filter button", async () => {
      await categoryDetailPage.partsTable.clickAddFilterButton();
    });

    await test.step("THEN a filter configuration sub-form appears", async () => {
      await categoryDetailPage.partsTable.assertFilterSubFormVisible();
    });

    await test.step("WHEN user clicks Cancel", async () => {
      await categoryDetailPage.partsTable.clickFilterSubFormCancel();
    });

    await test.step("THEN the filter sub-form closes", async () => {
      await categoryDetailPage.partsTable.assertFilterSubFormHidden();
    });
  });

  test("TC-UI-CAT-012: Category search in top-level list filters categories by name", async ({
    categoriesPage,
  }) => {
    await test.step("GIVEN user navigates to the top-level categories page", async () => {
      await categoriesPage.navigate();
      await categoriesPage.waitForLoad();
    });

    let initialText: string;

    await test.step("AND the categories table shows categories", async () => {
      await expect(categoriesPage.categoriesTable.paginationText).toBeVisible();
      initialText = (await categoriesPage.categoriesTable.paginationText.textContent()) ?? "";
      expect(initialText).toMatch(/\d+ - \d+ \/ [1-9]/);
    });

    await test.step("WHEN user searches for Fasteners", async () => {
      await categoriesPage.categoriesTable.search("Fasteners");
    });

    await test.step("THEN the pagination count drops", async () => {
      await expect(categoriesPage.categoriesTable.paginationText).not.toHaveText(initialText);
    });

    await test.step("AND the visible row shows Fasteners in the Name column", async () => {
      await categoriesPage.categoriesTable.assertRowVisible(/Fasteners/);
    });

    await test.step("WHEN user clears the search", async () => {
      await categoriesPage.categoriesTable.clearSearch();
    });

    await test.step("THEN the pagination returns to the original count", async () => {
      await expect(categoriesPage.categoriesTable.paginationText).toHaveText(initialText);
    });
  });

  test("TC-UI-CAT-013: Parts table Name column is sortable", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates to the Fasteners category parts tab", async () => {
      await categoryDetailPage.navigate(FASTENERS_ID, "parts");
    });

    await test.step("AND the table is loaded", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
    });

    await test.step("WHEN user clicks the Part column header to sort ascending", async () => {
      await categoryDetailPage.partsTable.clickSortColumn("Part");
    });

    await test.step("THEN the table is sorted ascending", async () => {
      const values = await categoryDetailPage.partsTable.getColumnValues(0);
      const nonEmpty = values.filter((v) => v.trim() !== "");
      if (nonEmpty.length > 1) {
        expect(nonEmpty[0].toLowerCase() <= nonEmpty[nonEmpty.length - 1].toLowerCase()).toBe(true);
      }
    });

    await test.step("WHEN user clicks the Part column header again to sort descending", async () => {
      await categoryDetailPage.partsTable.clickSortColumn("Part");
    });

    await test.step("THEN the table is sorted descending", async () => {
      const values = await categoryDetailPage.partsTable.getColumnValues(0);
      const nonEmpty = values.filter((v) => v.trim() !== "");
      if (nonEmpty.length > 1) {
        expect(nonEmpty[0].toLowerCase() >= nonEmpty[nonEmpty.length - 1].toLowerCase()).toBe(true);
      }
    });
  });

  test("TC-UI-CAT-014: Parts table shows pagination controls and navigates between pages", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN user navigates to the Fasteners category parts tab", async () => {
      await categoryDetailPage.navigate(FASTENERS_ID, "parts");
    });

    let firstPageFirstPart: string;

    await test.step("AND the pagination indicator shows more than 25 parts", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).toHaveText(/\d+ - \d+ \/ (2[6-9]|[3-9]\d|\d{3,})/);
    });

    await test.step("AND the Previous page button is disabled", async () => {
      await expect(categoryDetailPage.partsTable.prevPageButton).toBeDisabled();
    });

    await test.step("AND the Next page button is enabled", async () => {
      await expect(categoryDetailPage.partsTable.nextPageButton).toBeEnabled();
    });

    await test.step("AND the first part name is recorded", async () => {
      firstPageFirstPart = await categoryDetailPage.partsTable.getFirstRowFirstCellText();
    });

    await test.step("WHEN user clicks the Next page button", async () => {
      await categoryDetailPage.partsTable.clickNextPage();
    });

    await test.step("THEN the pagination indicator updates to show page 2", async () => {
      await categoryDetailPage.partsTable.assertPaginationRange(/26 - \d+ \/ \d+/);
    });

    await test.step("AND the first part on the new page differs from page 1", async () => {
      const page2FirstPart = await categoryDetailPage.partsTable.getFirstRowFirstCellText();
      expect(page2FirstPart).not.toBe(firstPageFirstPart);
    });

    await test.step("WHEN user clicks the Previous page button", async () => {
      await categoryDetailPage.partsTable.clickPrevPage();
    });

    await test.step("THEN the pagination returns to page 1", async () => {
      await categoryDetailPage.partsTable.assertPaginationRange(/1 - 25 \/ \d+/);
    });

    await test.step("AND the first part matches the recorded first part", async () => {
      const restoredFirstPart = await categoryDetailPage.partsTable.getFirstRowFirstCellText();
      expect(restoredFirstPart).toBe(firstPageFirstPart);
    });
  });
});
