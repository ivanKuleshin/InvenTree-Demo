import { test, expect } from "@fixtures/categories.fixtures";
import { STORAGE_STATE } from "@config/storageState";

const FASTENERS_ID = 3;

test.describe("TC_UI_CAT_PARAMETRIC", () => {
  test.use({ storageState: STORAGE_STATE.ADMIN, actionTimeout: 20_000 });

  test.beforeEach(async ({ categoryDetailPage }) => {
    await categoryDetailPage.navigate(FASTENERS_ID, "parts");
  });

  test("TC-UI-CAT-015: Clicking the Parametric View button switches the parts table to parametric mode", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN the standard view is active", async () => {
      await expect(categoryDetailPage.partsTable.standardViewButton).toBeVisible();
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
    });

    await test.step("AND the standard columns are visible", async () => {
      await categoryDetailPage.partsTable.assertColumnHeadersVisible(["Part", "IPN", "Description"]);
    });

    await test.step("WHEN user clicks the Parametric View button", async () => {
      await categoryDetailPage.partsTable.switchToParametricView();
    });

    await test.step("THEN the parametric view button is active", async () => {
      await expect(categoryDetailPage.partsTable.parametricViewButton).toHaveAttribute("data-active", "true");
    });

    await test.step("AND the parametric columns are visible", async () => {
      await categoryDetailPage.partsTable.assertColumnHeadersVisible([
        "Part",
        "Total Stock",
        "Length",
        "Material",
        "Thread",
      ]);
    });

    await test.step("AND each parameter column header has a filter icon button", async () => {
      await expect(categoryDetailPage.partsTable.getParameterFilterIcon("Length")).toBeVisible();
      await expect(categoryDetailPage.partsTable.getParameterFilterIcon("Material")).toBeVisible();
    });
  });

  test("TC-UI-CAT-016: Clicking the standard view button returns the table to standard mode", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN the parametric view is active", async () => {
      await expect(categoryDetailPage.partsTable.parametricViewButton).toBeVisible();
      await categoryDetailPage.partsTable.switchToParametricView();
      await categoryDetailPage.partsTable.assertColumnHeadersVisible(["Length"]);
    });

    await test.step("WHEN user clicks the standard view button", async () => {
      await categoryDetailPage.partsTable.switchToStandardView();
    });

    await test.step("THEN the standard view button is active", async () => {
      await expect(categoryDetailPage.partsTable.standardViewButton).toHaveAttribute("data-active", "true");
    });

    await test.step("AND standard columns are visible", async () => {
      await categoryDetailPage.partsTable.assertColumnHeadersVisible(["IPN", "Description"]);
    });

    await test.step("AND parameter-specific columns are not visible", async () => {
      await categoryDetailPage.partsTable.assertColumnHeadersNotVisible([/Length \[mm\]/, /Thread \[mm\]/]);
    });
  });

  test("TC-UI-CAT-017: Clicking a parameter column header sorts the parametric table", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN the parametric view is active", async () => {
      await expect(categoryDetailPage.partsTable.parametricViewButton).toBeVisible();
      await categoryDetailPage.partsTable.switchToParametricView();
      await categoryDetailPage.partsTable.assertColumnHeadersVisible(["Length"]);
    });

    await test.step("WHEN user clicks the Length column header to sort ascending", async () => {
      await categoryDetailPage.partsTable.clickSortColumn("Length");
    });

    await test.step("THEN the sort indicator changes from Not sorted", async () => {
      const sortBtn = categoryDetailPage.partsTable.getSortedState("Length");
      const label = await sortBtn.getAttribute("aria-label");
      expect(label).not.toMatch(/Not sorted/i);
    });

    await test.step("WHEN user clicks the Length column header again to sort descending", async () => {
      await categoryDetailPage.partsTable.clickSortColumn("Length");
    });

    await test.step("THEN the sort direction is reversed", async () => {
      const sortBtn = categoryDetailPage.partsTable.getSortButtonForColumn("Length");
      await expect(sortBtn).toBeVisible();
    });
  });

  test("TC-UI-CAT-018: Parameter column filter dialog shows operator dropdown and value input", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN the parametric view is active", async () => {
      await expect(categoryDetailPage.partsTable.parametricViewButton).toBeVisible();
      await categoryDetailPage.partsTable.switchToParametricView();
      await categoryDetailPage.partsTable.assertColumnHeadersVisible(["Length"]);
    });

    await test.step("WHEN user clicks the filter icon inside the Length column header", async () => {
      await categoryDetailPage.partsTable.getParameterFilterIcon("Length").click();
    });

    await test.step("THEN a filter dialog appears", async () => {
      await expect(
        categoryDetailPage.partsTable.getParamFilterOperatorInput("Length"),
      ).toBeVisible({ timeout: 10_000 });
    });

    await test.step("AND the operator input defaults to =", async () => {
      const operatorInput = categoryDetailPage.partsTable.getParamFilterOperatorInput("Length");
      await expect(operatorInput).toHaveValue("=");
    });

    await test.step("AND the value input has the expected placeholder", async () => {
      const valueInput = categoryDetailPage.partsTable.getParamFilterValueInput("Length");
      await expect(valueInput).toBeVisible();
    });

    await test.step("AND the operator dropdown contains all seven operators", async () => {
      await categoryDetailPage.partsTable.assertOperatorDropdownOptions("Length", ["=", ">", ">=", "<", "<=", "!=", "~"]);
    });
  });

  test("TC-UI-CAT-019: Filtering by a parameter value narrows the parts list", async ({
    categoryDetailPage,
  }) => {
    let initialText: string;

    await test.step("GIVEN the parametric view is active with the full unfiltered count", async () => {
      await expect(categoryDetailPage.partsTable.parametricViewButton).toBeVisible();
      await categoryDetailPage.partsTable.switchToParametricView();
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
      initialText = (await categoryDetailPage.partsTable.paginationText.textContent()) ?? "";
    });

    await test.step("WHEN user applies a filter Length = 10", async () => {
      await categoryDetailPage.partsTable.applyParameterFilter("Length", "=", "10");
    });

    await test.step("THEN the pagination count decreases", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).not.toHaveText(initialText);
    });
  });

  test("TC-UI-CAT-020: Multiple parameter filters applied simultaneously narrow results with AND logic", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN the parametric view is active", async () => {
      await expect(categoryDetailPage.partsTable.parametricViewButton).toBeVisible();
      await categoryDetailPage.partsTable.switchToParametricView();
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
    });

    let textAfterFirstFilter: string;

    await test.step("WHEN user applies Length = 10 filter", async () => {
      await categoryDetailPage.partsTable.applyParameterFilter("Length", "=", "10");
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
      textAfterFirstFilter = (await categoryDetailPage.partsTable.paginationText.textContent()) ?? "";
    });

    await test.step("AND user applies Material = Alloy filter", async () => {
      await categoryDetailPage.partsTable.applyParameterFilter("Material", "=", "Alloy");
    });

    await test.step("THEN the count decreases further (AND logic)", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).not.toHaveText(textAfterFirstFilter);
    });
  });

  test("TC-UI-CAT-021: Two filters on the same parameter create a range query", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN the parametric view is active", async () => {
      await expect(categoryDetailPage.partsTable.parametricViewButton).toBeVisible();
      await categoryDetailPage.partsTable.switchToParametricView();
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
    });

    let textAfterFirstFilter: string;

    await test.step("WHEN user applies Length > 10 filter", async () => {
      await categoryDetailPage.partsTable.applyParameterFilter("Length", ">", "10");
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
      textAfterFirstFilter = (await categoryDetailPage.partsTable.paginationText.textContent()) ?? "";
    });

    await test.step("AND user applies Length < 20 filter", async () => {
      await categoryDetailPage.partsTable.applyParameterFilter("Length", "<", "20");
    });

    await test.step("THEN the count decreases further creating a range query", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).not.toHaveText(textAfterFirstFilter);
    });
  });

  test("TC-UI-CAT-022: Removing a parameter filter restores the unfiltered count", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN the parametric view is active and a Length = 10 filter is applied", async () => {
      await expect(categoryDetailPage.partsTable.parametricViewButton).toBeVisible();
      await categoryDetailPage.partsTable.switchToParametricView();
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
      await categoryDetailPage.partsTable.applyParameterFilter("Length", "=", "10");
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
    });

    let filteredText: string;

    await test.step("AND the filtered count is recorded", async () => {
      filteredText = (await categoryDetailPage.partsTable.paginationText.textContent()) ?? "";
    });

    await test.step("WHEN user removes the Length filter", async () => {
      await categoryDetailPage.partsTable.removeParameterFilter("Length");
    });

    await test.step("THEN the pagination count increases back", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).not.toHaveText(filteredText);
    });
  });

  test("TC-UI-CAT-023: Unit-aware filter interprets abbreviated unit notation correctly", async ({
    categoryDetailPage,
  }) => {
    await test.step("GIVEN the parametric view is active", async () => {
      await expect(categoryDetailPage.partsTable.parametricViewButton).toBeVisible();
      await categoryDetailPage.partsTable.switchToParametricView();
      await expect(categoryDetailPage.partsTable.paginationText).toBeVisible();
    });

    let initialText: string;

    await test.step("AND the full unfiltered count is recorded", async () => {
      initialText = (await categoryDetailPage.partsTable.paginationText.textContent()) ?? "";
    });

    await test.step("WHEN user applies Thread >= 3 filter", async () => {
      await categoryDetailPage.partsTable.applyParameterFilter("Thread", ">=", "3");
    });

    await test.step("THEN the pagination count changes to show only parts with Thread >= 3", async () => {
      await expect(categoryDetailPage.partsTable.paginationText).not.toHaveText(initialText);
    });
  });
});
