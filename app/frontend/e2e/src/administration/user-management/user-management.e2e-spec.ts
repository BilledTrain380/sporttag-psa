import { browser, ElementFinder, ExpectedConditions as EC } from "protractor";

import { AppPage } from "../../app.po";
import { AddUserModalPage } from "./add-user-modal.po";

import { UserManagementPage } from "./user-management.po";

describe("UserManagement", () => {
  let appPage: AppPage;
  let page: UserManagementPage;
  let addUserModalPage: AddUserModalPage;

  beforeAll(() => {
    appPage = new AppPage();
    page = new UserManagementPage();
    addUserModalPage = new AddUserModalPage();
    appPage.navigateToUserManagement();
  });

  it("should list available users", async () => {
    const rowCount = await page.getUserCount();

    expect(rowCount)
      .toBeGreaterThan(0);
  });

  it("should add a new user", async () => {
    await page.addUserButton.click();

    await browser.wait(EC.visibilityOf(addUserModalPage.modalContainer));

    await addUserModalPage.usernameInput.sendKeys("fmueller");
    await addUserModalPage.passwordInput.sendKeys("Secret12345$");

    await browser.wait(EC.elementToBeClickable(addUserModalPage.submitButton));
    await addUserModalPage.submitButton.click();
    await browser.wait(EC.invisibilityOf(addUserModalPage.modalContainer));

    const fmueller = page.getRowByName("fmueller");
    await expect(fmueller.isPresent())
      .toBe(true, "Expected fmueller to be visible in table");
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
