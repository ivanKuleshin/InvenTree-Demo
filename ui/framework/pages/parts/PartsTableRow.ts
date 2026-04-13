import { type Locator, type Page } from "@playwright/test";
import { BaseTableRow } from "@framework/core/BaseTableRow";

/**
 * Represents a single row in the Parts table.
 * Extends BaseTableRow so generic row interactions (click, action menu) are inherited.
 */
export class PartsTableRow extends BaseTableRow {
  constructor(page: Page, rowLocator: Locator) {
    super(page, rowLocator);
  }

  // ── Cell accessors ────────────────────────────────────────────────────────

  /** The part name cell (column index 1, after the selection checkbox). */
  get nameCell(): Locator {
    return this.getCell(1);
  }

  /** Returns the visible text of the part name cell. */
  async getName(): Promise<string> {
    return this.nameCell.innerText();
  }

  // ── Action menu shortcuts ─────────────────────────────────────────────────

  /** Open the row action menu and click the Edit item. */
  async clickEdit(): Promise<void> {
    await this.openActionMenu();
    await this.clickMenuItem("Edit");
  }

  /** Open the row action menu and click the Duplicate item. */
  async clickDuplicate(): Promise<void> {
    await this.openActionMenu();
    await this.clickMenuItem("Duplicate");
  }
}
