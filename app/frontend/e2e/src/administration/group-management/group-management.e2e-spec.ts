import * as path from "path";
import { browser, ExpectedConditions as EC } from "protractor";

import { AppPage } from "../../app.po";

import { GroupManagementPage } from "./group-management.po";

describe("Group Management", () => {
  let appPage: AppPage;
  let page: GroupManagementPage;

  beforeEach(() => {
    appPage = new AppPage();
    page = new GroupManagementPage();
    appPage.navigateToGroupManagement();
  });

  it("should a list of groups", async () => {
    const rowCount = await page.getGroupCount();

    expect(rowCount)
      .toBeGreaterThan(0);
  });

  it("should import groups", async () => {
    const previousRowCount = await page.getGroupCount();

    await browser.wait(EC.visibilityOf(page.importButton));
    await page.importButton.click();

    await browser.wait(EC.visibilityOf(page.importDialog));
    await browser.wait(EC.presenceOf(page.groupInput));

    const groupFile = path.resolve(__dirname, "group-import.csv");

    await page.groupInput.sendKeys(groupFile);
    await browser.wait(EC.elementToBeClickable(page.importSubmit));
    await page.importSubmit.click();
    await browser.wait(EC.invisibilityOf(page.importDialog));

    const rowCount = await page.getGroupCount();
    expect(rowCount)
      .toBeGreaterThan(previousRowCount);
  });
});
