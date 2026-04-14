import { defineConfig, devices } from "@playwright/test";
import "dotenv/config";

const BASE_URL = process.env["BASE_URL"] ?? "https://demo.inventree.org";

export default defineConfig({
  // ── Discovery ────────────────────────────────────────────────────────────
  testDir: "./tests",

  // ── Execution ────────────────────────────────────────────────────────────
  fullyParallel: true,
  workers: process.env["CI"] ? 4 : undefined,
  retries: process.env["CI"] ? 2 : 1,
  timeout: 30_000,
  expect: { timeout: 10_000 },


  // ── Reporting ────────────────────────────────────────────────────────────
  reporter: [
    ["list"],
    ["html", { outputFolder: "playwright-report", open: "always" }],
  ],

  // ── Shared settings ───────────────────────────────────────────────────────
  use: {
    baseURL: BASE_URL,
    trace: "retain-on-failure",
    screenshot: "only-on-failure",
    video: "retain-on-failure",
    actionTimeout: 10_000,
    navigationTimeout: 15_000,
  },

  // ── Projects ─────────────────────────────────────────────────────────────
  projects: [
    /**
     * Setup project — runs first, logs in all roles, writes .auth/<role>.json.
     * No storageState here: these tests start unauthenticated by design.
     */
    {
      name: "setup",
      testMatch: "**/setup/*.setup.ts",
    },

    /**
     * Main test project — depends on setup.
     * Individual test files declare their role via:
     *   test.use({ storageState: STORAGE_STATE.ALLACCESS })
     */
    {
      name: "chromium",
      use: {
        ...devices["Desktop Chrome"],
        viewport: { width: 1440, height: 900 },
      },
      dependencies: ["setup"],
    },
  ],

  // ── Output ───────────────────────────────────────────────────────────────
  outputDir: "test-results",
});
