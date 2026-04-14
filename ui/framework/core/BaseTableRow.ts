import { type Locator, type Page } from "@playwright/test";
import { BaseComponent } from "./BaseComponent";

/**
 * Base class for a single table row.
 * Accepts a row Locator as root so all queries are scoped to the row.
 *
 * Usage:
 *   class PartsTableRow extends BaseTableRow {
 *     async getName(): Promise<string> {
 *       return this.getCell(1).innerText();
 *     }
 *   }
 */
export abstract class BaseTableRow extends BaseComponent {
  constructor(page: Page, rowLocator: Locator) {
    super(page, rowLocator);
  }

  /** Click the row (navigates to detail page in most InvenTree tables). */
  async click(): Promise<void> {
    await this.root.click();
  }

  /** Get a specific cell within this row by zero-based index. */
  getCell(colIndex: number): Locator {
    return this.root.locator('td, [role="cell"]').nth(colIndex);
  }

  /**
   * Click the row-action menu button (the three-dot kebab icon).
   * The aria-label follows the pattern `row-action-menu-N` — scoping to
   * the row means we can match by prefix without knowing the index.
   */
  async openActionMenu(): Promise<void> {
    await this.root.locator('button[aria-label^="row-action-menu"]').click();
  }

  /**
   * Click a menu item inside the action menu that has already been opened.
   * Matches by exact aria-label first; falls back to visible text.
   *
   * @param ariaLabelOrText aria-label value or visible text of the menu item
   */
  async clickMenuItem(ariaLabelOrText: string): Promise<void> {
    const byLabel = this.page.locator(
      `[role="menuitem"][aria-label="${ariaLabelOrText}"]`,
    );
    const byText = this.page.locator('[role="menuitem"]').filter({
      hasText: ariaLabelOrText,
    });

    const labelCount = await byLabel.count();
    await (labelCount > 0 ? byLabel.first() : byText.first()).click();
  }
}
