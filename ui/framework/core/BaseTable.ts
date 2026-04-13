import { type Locator, type Page } from "@playwright/test";
import { BaseComponent } from "./BaseComponent";

/**
 * Base class for table components.
 * Provides row/cell access helpers that work with standard HTML tables
 * and Mantine-style data tables used by InvenTree.
 *
 * Usage:
 *   class PartsTable extends BaseTable {
 *     constructor(page: Page) {
 *       super(page, '[data-testid="parts-table"]');
 *     }
 *   }
 */
export abstract class BaseTable extends BaseComponent {
  constructor(page: Page, rootSelector: string | Locator) {
    super(page, rootSelector);
  }

  // ── Row access ────────────────────────────────────────────────────────────

  /** All body rows in the table */
  get rows(): Locator {
    return this.locator('tbody tr, [role="row"]:not([aria-rowindex="1"])');
  }

  /** A single row by zero-based index */
  getRowByIdx(index: number): Locator {
    return this.rows.nth(index);
  }

  /**
   * Find the first row that contains the given text in any cell.
   * @param text Exact or partial cell text to match
   */
  getRowByText(text: string): Locator {
    return this.rows.filter({ hasText: text }).first();
  }

  /**
   * Find the first row where a specific column (zero-based) contains the given text.
   * @param colIndex Zero-based column index
   * @param text     Text to match
   */
  getRowByCell(colIndex: number, text: string): Locator {
    return this.rows
      .filter({
        has: this.page
          .locator(
            `td:nth-child(${colIndex + 1}), [role="cell"]:nth-child(${colIndex + 1})`,
          )
          .filter({ hasText: text }),
      })
      .first();
  }

  // ── Cell access ───────────────────────────────────────────────────────────

  /** Get a specific cell within a row locator */
  getCellInRow(row: Locator, colIndex: number): Locator {
    return row.locator(`td, [role="cell"]`).nth(colIndex);
  }

  // ── Column values ─────────────────────────────────────────────────────────

  /**
   * Return all text values from a given column (zero-based).
   */
  async getColumnValues(colIndex: number): Promise<string[]> {
    const cells = this.rows.locator(
      `td:nth-child(${colIndex + 1}), [role="cell"]:nth-child(${colIndex + 1})`,
    );
    return cells.allInnerTexts();
  }

  // ── State ────────────────────────────────────────────────────────────────

  /** Total number of visible rows */
  async rowCount(): Promise<number> {
    return this.rows.count();
  }

  /** Wait for the table to have at least one row */
  async waitForRows(): Promise<void> {
    await this.rows.first().waitFor({ state: "visible" });
  }

  /** Whether the table is displaying an empty-state message */
  async isEmpty(): Promise<boolean> {
    const count = await this.rowCount();
    return count === 0;
  }
}
