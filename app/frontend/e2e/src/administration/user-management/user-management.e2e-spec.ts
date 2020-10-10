import { ElementFinder } from "protractor";

import { AppPage } from "../../app.po";

import { UserManagementPage } from "./user-management.po";

describe("UserManagement", () => {
  let appPage: AppPage;
  let page: UserManagementPage;

  beforeAll(() => {
    appPage = new AppPage();
    page = new UserManagementPage();
    appPage.navigateToUserManagement();
  });

  it("should list available users", async () => {
    const rowCount = await page.getUserCount();

    expect(rowCount)
      .toBeGreaterThan(0);
  });

  describe("on edit user", () => {
    let wwirbelwind: ElementFinder;

    beforeEach(() => {
      wwirbelwind = page.getRowByName("wwirbelwind");
    });

    it("should edit enabled attribute", async () => {
      await page.toggleEnabledByRow(wwirbelwind);

      await expect(page.getEnabledValueByRow(wwirbelwind))
        .toBe(false, "Expected enabled checkbox to be false");
    });
  });
});
