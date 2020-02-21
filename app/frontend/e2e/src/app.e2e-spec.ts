import { browser, ExpectedConditions as EC, logging } from "protractor";

import { AppPage } from "./app.po";

describe("PSA frontend", () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
    page.navigateTo();
  });

  it("should display the username", () => {
    expect(page.getUsername())
      .toEqual(browser.params.username);
  });

  describe("on responsiveness", () => {
    afterAll(async () => {
      // Ensure a desktop screen size even if a test fails.
      const width = 1440;
      const height = 900;
      await browser.driver.manage()
        .window()
        .setSize(width, height);
    });

    it("should hide the navigation menu on tablet screen size", async () => {
      const width = 786;
      const height = 1024;
      await browser.driver.manage()
        .window()
        .setSize(width, height);

      const menu = page.getMenu();
      browser.wait(EC.invisibilityOf(menu));

      expect(menu.isPresent())
        .toBe(true, "Expected navigation menu to be hidden");
    });

    it("should show the navigation menu on desktop screen size", async () => {
      const width = 1440;
      const height = 900;
      await browser.driver.manage()
        .window()
        .setSize(width, height);

      const menu = page.getMenu();
      browser.wait(EC.visibilityOf(menu));

      expect(menu.isPresent())
        .toBe(true, "Expected navigation menu to be visible");
    });
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage()
      .logs()
      .get(logging.Type.BROWSER);
    expect(logs)
      .not
      .toContain(jasmine.objectContaining({
        level: logging.Level.SEVERE,
      }));
  });
});
