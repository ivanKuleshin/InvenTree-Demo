import { type Page } from "@playwright/test";
import { BaseComponent } from "@framework/core/BaseComponent";

/**
 * "Add Part" modal dialog.
 * Opened from the Parts list page via the "Add Parts" action menu → "Create Part".
 */
export class CreatePartModal extends BaseComponent {
  constructor(page: Page) {
    super(page, page.getByRole("dialog", { name: "Add Part" }));
  }

  // ── Header ────────────────────────────────────────────────────────────────

  get heading() {
    return this.root.getByRole("heading", { name: "Add Part" });
  }

  get closeButton() {
    return this.root.getByRole("banner").getByRole("button");
  }

  // ── Related-field dropdowns ───────────────────────────────────────────────

  /** Part category search/select combobox */
  get categoryInput() {
    return this.root.getByRole("combobox", { name: "related-field-category" });
  }

  /** Revision Of — select the part this is a revision of */
  get revisionOfInput() {
    return this.root.getByRole("combobox", {
      name: "related-field-revision_of",
    });
  }

  /** Variant Of — select the part this is a variant of */
  get variantOfInput() {
    return this.root.getByRole("combobox", {
      name: "related-field-variant_of",
    });
  }

  /** Default Location search/select combobox */
  get defaultLocationInput() {
    return this.root.getByRole("combobox", {
      name: "related-field-default_location",
    });
  }

  /** Responsible party search/select combobox */
  get responsibleInput() {
    return this.root.getByRole("combobox", {
      name: "related-field-responsible",
    });
  }

  // ── Text fields ───────────────────────────────────────────────────────────

  get nameInput() {
    return this.root.getByRole("textbox", { name: "text-field-name" });
  }

  get ipnInput() {
    return this.root.getByRole("textbox", { name: "text-field-IPN" });
  }

  get descriptionInput() {
    return this.root.getByRole("textbox", { name: "text-field-description" });
  }

  get revisionInput() {
    return this.root.getByRole("textbox", { name: "text-field-revision" });
  }

  get keywordsInput() {
    return this.root.getByRole("textbox", { name: "text-field-keywords" });
  }

  get unitsInput() {
    return this.root.getByRole("textbox", { name: "text-field-units" });
  }

  get linkInput() {
    return this.root.getByRole("textbox", { name: "text-field-link" });
  }

  // ── Number fields ─────────────────────────────────────────────────────────

  get defaultExpiryInput() {
    return this.root.getByRole("textbox", {
      name: "number-field-default_expiry",
    });
  }

  get minimumStockInput() {
    return this.root.getByRole("textbox", {
      name: "number-field-minimum_stock",
    });
  }

  // ── Boolean switches ──────────────────────────────────────────────────────

  get componentSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-component" });
  }

  get assemblySwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-assembly" });
  }

  get isTemplateSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-is_template" });
  }

  get testableSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-testable" });
  }

  get trackableSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-trackable" });
  }

  get purchaseableSwitch() {
    return this.root.getByRole("switch", {
      name: "boolean-field-purchaseable",
    });
  }

  get salableSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-salable" });
  }

  get virtualSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-virtual" });
  }

  get lockedSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-locked" });
  }

  get activeSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-active" });
  }

  get copyCategoryParametersSwitch() {
    return this.root.getByRole("switch", {
      name: "boolean-field-copy_category_parameters",
    });
  }

  get keepFormOpenSwitch() {
    return this.root.getByRole("switch", { name: /keep form open/i });
  }

  // ── Initial Stock accordion ───────────────────────────────────────────────

  /** Expand / collapse the "Initial Stock" section */
  get initialStockAccordion() {
    return this.root.getByRole("button", { name: "Initial Stock" });
  }

  get initialStockQuantityInput() {
    return this.root.getByRole("textbox", {
      name: "number-field-initial_stock.quantity",
    });
  }

  get initialStockLocationInput() {
    return this.root.getByRole("combobox", {
      name: "related-field-initial_stock.location",
    });
  }

  // ── Actions ───────────────────────────────────────────────────────────────

  get submitButton() {
    return this.root.getByRole("button", { name: "Submit" });
  }

  get cancelButton() {
    return this.root.getByRole("button", { name: "Cancel" });
  }

  async fillName(name: string): Promise<void> {
    await this.nameInput.fill(name);
  }

  async fillDescription(description: string): Promise<void> {
    await this.descriptionInput.fill(description);
  }

  async fillIPN(ipn: string): Promise<void> {
    await this.ipnInput.fill(ipn);
  }

  async fillRevision(revision: string): Promise<void> {
    await this.revisionInput.fill(revision);
  }

  async fillKeywords(keywords: string): Promise<void> {
    await this.keywordsInput.fill(keywords);
  }

  async fillUnits(units: string): Promise<void> {
    await this.unitsInput.fill(units);
  }

  async fillLink(url: string): Promise<void> {
    await this.linkInput.fill(url);
  }

  async submit(): Promise<void> {
    await this.submitButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
    await this.waitForHidden();
  }

  async isSubmitEnabled(): Promise<boolean> {
    return this.submitButton.isEnabled();
  }
}
