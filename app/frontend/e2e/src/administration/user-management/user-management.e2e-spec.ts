import { browser, ExpectedConditions as EC, logging } from "protractor";

import { AppPage } from "../../app.po";

import { AddUserModalPage } from "./add-user-modal.po";
import { ChangeUserPasswordModalPage } from "./change-user-password-modal.po";
import { UserManagementPage } from "./user-management.po";

describe("UserManagement", () => {
  let appPage: AppPage;
  let page: UserManagementPage;
  let addUserModalPage: AddUserModalPage;
  let changeUserPasswordModalPage: ChangeUserPasswordModalPage;

  beforeAll(() => {
    appPage = new AppPage();
    page = new UserManagementPage();
    addUserModalPage = new AddUserModalPage();
    changeUserPasswordModalPage = new ChangeUserPasswordModalPage();
    appPage.navigateToUserManagement();
  });

  it("should list available users", async () => {
    const rowCount = await page.getUserCount();

    expect(rowCount)
      .toBeGreaterThan(0);
  });

  describe("on add / edit / delete user", () => {
    const fmuellerUser = "fmueller";

    it("should add a new user", async () => {
      await page.addUserButton.click();

      await browser.wait(EC.visibilityOf(addUserModalPage.modalContainer));

      await addUserModalPage.usernameInput.sendKeys("fmueller");
      await addUserModalPage.passwordInput.sendKeys("Secret12345$");

      await browser.wait(EC.elementToBeClickable(addUserModalPage.submitButton));
      await addUserModalPage.submitButton.click();
      await browser.wait(EC.invisibilityOf(addUserModalPage.modalContainer));

      const fmueller = page.getRowByName(fmuellerUser);
      await expect(fmueller.isPresent())
        .toBe(true, "Expected fmueller to be visible in table");
    });

    it("should edit enabled attribute", async () => {
      const fmueller = page.getRowByName(fmuellerUser);
      await page.toggleEnabledByRow(fmueller);

      await expect(page.getEnabledValueByRow(fmueller))
        .toBe(false, "Expected enabled checkbox to be false");
    });

    xit("should change user password", async () => {
      const fmueller = page.getRowByName(fmuellerUser);
      await page.clickChangePasswordByRow(fmueller);

      await browser.wait(EC.visibilityOf(changeUserPasswordModalPage.modalContainer));
      await changeUserPasswordModalPage.passwordInput.sendKeys("Secret12345$");
      await browser.wait(EC.elementToBeClickable(changeUserPasswordModalPage.submitButton));
      await changeUserPasswordModalPage.submitButton.click();
      await browser.wait(EC.invisibilityOf(changeUserPasswordModalPage.modalContainer));

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

    it("should delete user", async () => {
      const fmueller = page.getRowByName(fmuellerUser);
      await page.clickDeleteUserButton(fmueller);

      await browser.wait(EC.visibilityOf(appPage.confirmModal));

      await appPage.clickConfirmOnModal();
      await browser.wait(EC.invisibilityOf(appPage.confirmModal));

      const fmuellerDeleted = page.getRowByName(fmuellerUser);
      await expect(fmuellerDeleted.isPresent())
        .toBe(false, "Expected fmueller to be deleted");
    });
  });
});
