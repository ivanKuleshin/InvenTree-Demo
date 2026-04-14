import { type Page } from "@playwright/test";
import { BaseComponent } from "@framework/core/BaseComponent";

export type PartDetailTab =
  | "Part Details"
  | "Stock"
  | "Allocations"
  | "Used In"
  | "Part Pricing"
  | "Suppliers"
  | "Purchase Orders"
  | "Stock History"
  | "Related Parts"
  | "Parameters"
  | "Attachments"
  | "Notes"
  | "Variants"
  | "Bill of Materials"
  | "Build Orders"
  | "Test Templates"
  | "Test Results"
  | "Sales Orders"
  | "Return Orders";

export class PartDetailTabBar extends BaseComponent {
  constructor(page: Page) {
    super(page, page.getByRole("tablist", { name: "panel-tabs-part" }));
  }

  async chooseTab(tab: PartDetailTab): Promise<void> {
    await this.root.getByRole("tab", { name: tab, exact: true }).click();
    await this.page.waitForLoadState("networkidle");
  }

  async activeTabName(): Promise<string | null> {
    return this.root.getByRole("tab", { selected: true }).textContent();
  }
}
