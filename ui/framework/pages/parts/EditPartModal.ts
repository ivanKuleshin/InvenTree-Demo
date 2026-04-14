import { type Page } from "@playwright/test";
import { BaseComponent } from "@framework/core/BaseComponent";

export class EditPartModal extends BaseComponent {
  constructor(page: Page) {
    super(page, page.getByRole("dialog", { name: "Edit Part" }));
  }

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

  get salableSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-salable" });
  }

  get virtualSwitch() {
    return this.root.getByRole("switch", { name: "boolean-field-virtual" });
  }

  get cancelButton() {
    return this.root.getByRole("button", { name: "Cancel" });
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
    await this.waitForHidden();
  }
}
