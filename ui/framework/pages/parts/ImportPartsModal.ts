import { type Locator, type Page } from "@playwright/test";
import { BaseComponent } from "@framework/core/BaseComponent";

export class ImportPartsModal extends BaseComponent {
  constructor(page: Page) {
    super(
      page,
      page.getByRole("dialog").filter({ hasText: /Import/i }),
    );
  }

  // ── Step 1: Upload File ───────────────────────────────────────────────────

  get heading(): Locator {
    return this.root.getByRole("heading").first();
  }

  get fileInput(): Locator {
    return this.root.locator('input[type="file"][name="data_file"]');
  }

  get fileButton(): Locator {
    return this.root.getByRole("button", { name: "file-field-data_file" });
  }

  get submitButton(): Locator {
    return this.root.getByRole("button", { name: "Submit" });
  }

  get cancelButton(): Locator {
    return this.root.getByRole("button", { name: "Cancel" });
  }

  async uploadFile(
    buffer: Buffer,
    fileName: string = "import.csv",
  ): Promise<void> {
    await this.fileButton.click();
    await this.fileInput.setInputFiles({
      name: fileName,
      mimeType: "text/csv",
      buffer,
    });
  }

  async submit(): Promise<void> {
    await this.submitButton.click();
  }

  // ── Step 2: Map Columns ───────────────────────────────────────────────────

  get acceptMappingButton(): Locator {
    return this.root.getByRole("button", { name: "Accept Column Mapping" });
  }

  columnMappingFor(fieldName: string): Locator {
    return this.root.getByRole("textbox", {
      name: `import-column-map-${fieldName}`,
    });
  }

  async acceptColumnMapping(): Promise<void> {
    await this.acceptMappingButton.click();
  }

  // ── Step 3: Import Rows ───────────────────────────────────────────────────

  get importSelectedRowsButton(): Locator {
    return this.root.getByRole("button", {
      name: "action-button-import-selected-rows",
    });
  }

  get selectAllRowsCheckbox(): Locator {
    return this.root.getByRole("checkbox", { name: "Select all records" });
  }

  get importTableRows(): Locator {
    return this.root.locator("table tbody tr");
  }

  async getImportRowCount(): Promise<number> {
    return this.importTableRows.count();
  }

  async importAllRows(): Promise<void> {
    await this.selectAllRowsCheckbox.check();
    await this.importSelectedRowsButton.click();
  }

  get successRows(): Locator {
    return this.importTableRows.filter({
      has: this.page.locator(".tabler-icon-circle-dashed-check"),
    });
  }

  get errorRows(): Locator {
    return this.importTableRows.filter({
      has: this.page.locator(".tabler-icon-exclamation-circle"),
    });
  }

  get importCompleteHeading(): Locator {
    return this.root.getByText("Import Complete");
  }

  rowCheckbox(rowIndex: number): Locator {
    return this.importTableRows.nth(rowIndex).getByRole("checkbox");
  }

  async importRowByIndex(index: number): Promise<void> {
    await this.rowCheckbox(index).check();
    await this.importSelectedRowsButton.click();
  }
}
