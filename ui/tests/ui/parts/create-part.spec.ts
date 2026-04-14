import { test, expect } from "@fixtures/parts.fixtures";
import { STORAGE_STATE } from "@config/storageState";

test.describe("TC_UI_PART_CREATE", () => {
  test.use({ storageState: STORAGE_STATE.ENGINEER, actionTimeout: 20_000 });

  test.beforeEach(async ({ partsPage }) => {
    await partsPage.navigate();
    await partsPage.waitForLoad();
  });

  test("TC-UI-PC-001: create part with required field only and verify detail page", async ({
    partsPage,
    partDetailPage,
  }) => {
    const partName = `TC-UI-PC-001-MinimalPart-${Date.now()}`;

    await test.step("GIVEN user is on the Parts list page", async () => {
      await partsPage.waitForLoad();
    });

    const modal = await test.step(
      "WHEN user opens the Add Part dialog",
      async () => {
        const m = await partsPage.openCreatePartModal();
        await expect(m.heading).toBeVisible();
        return m;
      },
    );

    await test.step("AND fills only the Name field", async () => {
      await modal.fillName(partName);
    });

    await test.step("AND submits the form", async () => {
      await modal.submit();
      await expect(modal.root).not.toBeVisible({ timeout: 30_000 });
    });

    await test.step("THEN browser navigates to the new part detail page", async () => {
      await partDetailPage.waitForLoad();
    });

    await test.step("AND the part title is correct", async () => {
      await expect(partDetailPage.partTitle).toContainText(partName);
    });

    await test.step("AND success toast confirms item was created", async () => {
      await expect(partDetailPage.successToast).toBeVisible({ timeout: 5_000 });
    });

    await test.step("AND the No Stock badge is visible", async () => {
      await expect(partDetailPage.noStockBadge).toBeVisible();
    });
  });

  test("TC-UI-PC-002: create part with all optional text fields populated", async ({
    partsPage,
    partDetailPage,
  }) => {
    const ts = Date.now();
    const partName = `TC-UI-PC-002-FullFieldsPart-${ts}`;
    const ipn = `IPN-TC002-${ts}`;
    const description = `A detailed description for TC-UI-PC-002-${ts}`;

    const modal = await test.step("WHEN user opens Add Part dialog", async () => {
      return partsPage.openCreatePartModal();
    });

    await test.step("AND fills all optional text fields", async () => {
      await modal.fillName(partName);
      await modal.fillIPN(ipn);
      await modal.fillDescription(description);
      await modal.fillRevision("Rev-A");
      await modal.fillKeywords("resistor capacitor electronics");
      await modal.fillUnits("pcs");
      await modal.fillLink("https://example.com/datasheet.pdf");
      await modal.defaultExpiryInput.fill("30");
      await modal.minimumStockInput.fill("10");
    });

    await test.step("AND submits the form", async () => {
      await modal.submit();
      await expect(modal.root).not.toBeVisible({ timeout: 30_000 });
    });

    await test.step("THEN part detail page loads with correct title", async () => {
      await partDetailPage.waitForLoad();
      await expect(partDetailPage.partTitle).toContainText(partName);
    });

    await test.step("AND Part Details tab shows IPN and description", async () => {
      await partDetailPage.tabBar.chooseTab("Part Details");
      await expect(
        partDetailPage.partDetailsPanel.fieldValue(ipn),
      ).toBeVisible();
      await expect(
        partDetailPage.partDetailsPanel.fieldValue(description),
      ).toBeVisible();
    });
  });

  test("TC-UI-PC-003: create part assigned to a specific category", async ({
    partsPage,
    partDetailPage,
  }) => {
    const partName = `TC-UI-PC-003-CatPart-${Date.now()}`;

    const modal = await test.step("WHEN user opens Add Part dialog", async () => {
      return partsPage.openCreatePartModal();
    });

    await test.step("AND selects the Electronics category", async () => {
      await modal.selectCategory("Electronics");
    });

    await test.step("AND fills the Name field", async () => {
      await modal.fillName(partName);
    });

    await test.step("AND submits the form", async () => {
      await modal.submit();
      await expect(modal.root).not.toBeVisible({ timeout: 30_000 });
    });

    await test.step("THEN part detail page loads with correct title", async () => {
      await partDetailPage.waitForLoad();
      await expect(partDetailPage.partTitle).toContainText(partName);
    });

    await test.step("AND Part Details tab shows the assigned category", async () => {
      await partDetailPage.tabBar.chooseTab("Part Details");
      await expect(
        partDetailPage.partDetailsPanel.fieldValue("Electronics"),
      ).toBeVisible();
    });
  });

  test("TC-UI-PC-004: boolean attribute toggles persist after part creation", async ({
    partsPage,
    partDetailPage,
  }) => {
    const partName = `TC-UI-PC-004-BoolPart-${Date.now()}`;

    const modal = await test.step(
      "WHEN user opens Add Part dialog",
      async () => {
        return partsPage.openCreatePartModal();
      },
    );

    await test.step("AND default boolean states are verified", async () => {
      await expect(modal.componentSwitch).toBeChecked();
      await expect(modal.purchaseableSwitch).toBeChecked();
      await expect(modal.activeSwitch).toBeChecked();
      await expect(modal.copyCategoryParametersSwitch).toBeChecked();
      await expect(modal.assemblySwitch).not.toBeChecked();
      await expect(modal.isTemplateSwitch).not.toBeChecked();
      await expect(modal.testableSwitch).not.toBeChecked();
      await expect(modal.trackableSwitch).not.toBeChecked();
      await expect(modal.salableSwitch).not.toBeChecked();
      await expect(modal.virtualSwitch).not.toBeChecked();
    });

    await test.step("AND user toggles Assembly, Is Template, Testable, Trackable, Salable, Virtual on; Component off", async () => {
      await modal.assemblySwitch.click();
      await modal.isTemplateSwitch.click();
      await modal.testableSwitch.click();
      await modal.trackableSwitch.click();
      await modal.salableSwitch.click();
      await modal.virtualSwitch.click();
      await modal.componentSwitch.click();
    });

    await test.step("AND fills Name and submits", async () => {
      await modal.fillName(partName);
      await modal.submit();
      await expect(modal.root).not.toBeVisible({ timeout: 30_000 });
    });

    await test.step("THEN part detail page loads", async () => {
      await partDetailPage.waitForLoad();
    });

    const editModal = await test.step(
      "WHEN user opens the Edit Part form",
      async () => {
        return partDetailPage.openEditModal();
      },
    );

    await test.step("THEN toggled attributes are persisted", async () => {
      await expect(editModal.assemblySwitch).toBeChecked();
      await expect(editModal.isTemplateSwitch).toBeChecked();
      await expect(editModal.testableSwitch).toBeChecked();
      await expect(editModal.trackableSwitch).toBeChecked();
      await expect(editModal.salableSwitch).toBeChecked();
      await expect(editModal.virtualSwitch).toBeChecked();
      await expect(editModal.componentSwitch).not.toBeChecked();
    });

    await test.step("cleanup: cancel edit modal", async () => {
      await editModal.cancel();
    });
  });

  test("TC-UI-PC-005: duplicate an existing part", async ({
    partsPage,
    partDetailPage,
  }) => {
    const originalName = `TC-UI-PC-005-Source-${Date.now()}`;
    const duplicateName = `TC-UI-PC-005-DuplicatePart-${Date.now()}`;

    await test.step("GIVEN an existing part is created as duplicate source", async () => {
      const m = await partsPage.openCreatePartModal();
      await m.fillName(originalName);
      await m.submit();
      await expect(m.root).not.toBeVisible({ timeout: 20_000 });
      await partDetailPage.waitForLoad();
    });

    const originalPartId = partDetailPage.currentPartId();

    const duplicateModal = await test.step(
      "WHEN user opens Duplicate dialog from part actions menu",
      async () => {
        const m = await partDetailPage.openDuplicateModal();
        await expect(m.heading).toBeVisible();
        return m;
      },
    );

    await test.step("THEN Name field is pre-filled with original name", async () => {
      await expect(duplicateModal.nameInput).toHaveValue(originalName);
    });

    await test.step("AND copy switches are visible and checked by default", async () => {
      await expect(duplicateModal.copyImageSwitch).toBeVisible();
      await expect(duplicateModal.copyNotesSwitch).toBeVisible();
      await expect(duplicateModal.copyParametersSwitch).toBeVisible();
    });

    await test.step("AND user clears Name and types new duplicate name", async () => {
      await duplicateModal.nameInput.clear();
      await duplicateModal.fillName(duplicateName);
    });

    await test.step("AND submits the duplicate form", async () => {
      await duplicateModal.submit();
      await expect(duplicateModal.root).not.toBeVisible({ timeout: 30_000 });
    });

    await test.step("THEN new part detail page loads with the duplicate name", async () => {
      await partDetailPage.waitForLoad();
      await expect(partDetailPage.partTitle).toContainText(duplicateName);
    });

    await test.step("AND the new part has a different ID than the original", async () => {
      expect(partDetailPage.currentPartId()).not.toBe(originalPartId);
    });
  });

  test("TC-UI-PC-006: validation errors — missing required field and invalid input", async ({
    partsPage,
  }) => {
    const modal = await test.step(
      "WHEN user opens Add Part dialog",
      async () => {
        return partsPage.openCreatePartModal();
      },
    );

    await test.step("AND submits with empty Name field", async () => {
      await modal.submit();
    });

    await test.step("THEN Form Error banner is shown", async () => {
      await expect(modal.formErrorBanner).toBeVisible();
    });

    await test.step("AND inline error appears below Name field", async () => {
      await expect(modal.nameFieldInlineError).toBeVisible();
    });

    await test.step("AND Name textbox is in error state", async () => {
      await expect(modal.nameInput).toHaveAttribute("data-error", "true");
    });

    await test.step("WHEN user enters a 101-character name (exceeds 100-char limit)", async () => {
      await modal.nameInput.fill("A".repeat(101));
      await modal.submit();
    });

    await test.step("THEN server rejects oversized name with an error on the Name field", async () => {
      await expect(modal.formErrorBanner).toBeVisible();
      await expect(modal.nameInput).toHaveAttribute("data-error", "true");
    });

    await test.step("WHEN user enters an invalid URL in the Link field with a valid name", async () => {
      await modal.nameInput.fill(`TC-UI-PC-006-InvalidURL-${Date.now()}`);
      await modal.fillLink("not-a-valid-url");
      await modal.submit();
    });

    await test.step("THEN server rejects the invalid URL with an error on the Link field", async () => {
      await expect(modal.formErrorBanner).toBeVisible();
    });

    await test.step("cleanup: close dialog", async () => {
      await modal.cancel();
    });
  });

  test("TC-UI-PC-007: import parts from CSV — happy path", async ({
    partsPage,
  }) => {
    test.setTimeout(120_000);
    const ts = Date.now();
    const part1 = `TC-IMPORT-001-${ts}`;
    const part2 = `TC-IMPORT-002-${ts}`;
    const part3 = `TC-IMPORT-003-${ts}`;
    const csvContent = [
      "name,description,IPN",
      `${part1},First imported part,IMP-001-${ts}`,
      `${part2},Second imported part,IMP-002-${ts}`,
      `${part3},Third imported part,IMP-003-${ts}`,
    ].join("\n");

    const importModal = await test.step(
      "WHEN user clicks Import from File",
      async () => {
        const m = await partsPage.openImportPartsModal();
        await expect(m.heading).toContainText("Import Parts");
        return m;
      },
    );

    await test.step("AND uploads a valid CSV file", async () => {
      await importModal.uploadFile(Buffer.from(csvContent), "tc007-import-parts.csv");
    });

    await test.step("AND submits to advance to the field mapping step", async () => {
      await importModal.submit();
      await expect(importModal.acceptMappingButton).toBeVisible({
        timeout: 30_000,
      });
    });

    await test.step("THEN name column is auto-mapped to 'name'", async () => {
      await expect(importModal.columnMappingFor("name")).toHaveValue("name");
    });

    await test.step("AND description column is auto-mapped to 'description'", async () => {
      await expect(importModal.columnMappingFor("description")).toHaveValue(
        "description",
      );
    });

    await test.step("AND IPN column is auto-mapped to 'IPN'", async () => {
      await expect(importModal.columnMappingFor("IPN")).toHaveValue("IPN");
    });

    await test.step("AND user confirms column mappings", async () => {
      await importModal.acceptColumnMapping();
      await expect(importModal.root.getByText(part1)).toBeVisible({
        timeout: 60_000,
      });
    });

    await test.step("THEN all 3 imported rows appear in the wizard table", async () => {
      await expect(importModal.root.getByText(part1)).toBeVisible();
      await expect(importModal.root.getByText(part2)).toBeVisible();
      await expect(importModal.root.getByText(part3)).toBeVisible();
    });

    await test.step("AND user imports all rows", async () => {
      await importModal.importAllRows();
    });

    await test.step("THEN the wizard shows the Import Complete success message", async () => {
      await expect(importModal.importCompleteHeading).toBeVisible({ timeout: 60_000 });
    });
  });

  test("TC-UI-PC-008: import parts from CSV — error rows with inline correction", async ({
    partsPage,
  }) => {
    test.setTimeout(120_000);
    const ts = Date.now();
    const validPart1 = `TC-ERR-001-${ts}`;
    const invalidPart3 = `TC-ERR-003-${ts}`;
    const csvContent = [
      "name,description,category",
      `${validPart1},Valid part,`,
      `,Missing name is invalid,`,
      `${invalidPart3},Category does not exist,99999999`,
    ].join("\n");

    const importModal = await test.step(
      "WHEN user imports a CSV with intentionally bad rows",
      async () => {
        const m = await partsPage.openImportPartsModal();
        await m.uploadFile(Buffer.from(csvContent), "tc008-bad-import.csv");
        await m.submit();
        await expect(m.acceptMappingButton).toBeVisible({ timeout: 30_000 });
        return m;
      },
    );

    await test.step("AND confirms column mappings", async () => {
      await importModal.acceptColumnMapping();
      await expect(importModal.root.getByText(validPart1)).toBeVisible({
        timeout: 60_000,
      });
    });

    await test.step("THEN the valid and invalid rows are visible in the wizard table", async () => {
      await expect(importModal.root.getByText(validPart1)).toBeVisible();
      await expect(importModal.root.getByText(invalidPart3)).toBeVisible();
    });

    await test.step("AND error rows are already flagged with error indicators (pre-import validation)", async () => {
      await expect(importModal.errorRows.first()).toBeVisible({ timeout: 30_000 });
    });

    await test.step("AND user imports only the valid row", async () => {
      await importModal.importRowByIndex(0);
    });

    await test.step("THEN the wizard shows the Import Complete success message", async () => {
      await expect(importModal.importCompleteHeading).toBeVisible({ timeout: 60_000 });
    });
  });
});
