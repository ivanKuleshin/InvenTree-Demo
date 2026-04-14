import { type Page } from "@playwright/test";
import { BaseComponent } from "@framework/core/BaseComponent";

export class NotesTab extends BaseComponent {
  constructor(page: Page) {
    super(page, page.getByRole("tabpanel", { name: "Notes" }));
  }
}
