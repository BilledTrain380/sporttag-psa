import { browser } from "protractor";

import { AppPage } from "./app.po";

describe("PSA frontend", () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
    page.navigateToBaseUrl();
  });

  it("should display the username", async () => {
    await expect(page.username)
      .toEqual(browser.params.username);
  });
});
