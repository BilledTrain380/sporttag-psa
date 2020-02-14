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

      expect(await EC.invisibilityOf(page.getMenu()));
    });

    it("should show the navigation menu on desktop screen size", async () => {
      const width = 1440;
      const height = 900;
      await browser.driver.manage()
        .window()
        .setSize(width, height);

      expect(await EC.visibilityOf(page.getMenu()));
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
