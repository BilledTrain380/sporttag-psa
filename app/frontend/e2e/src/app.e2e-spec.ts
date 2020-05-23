import { browser, logging } from "protractor";

import { AppPage } from "./app.po";

describe("PSA frontend", () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
    page.navigateTo();
  });

  it("should display the username", () => {
    expect(page.username)
      .toEqual(browser.params.username);
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
