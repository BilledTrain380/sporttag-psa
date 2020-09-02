import { browser, ExpectedConditions as EC } from "protractor";

import { AppPage } from "../../app.po";

import { ParticipationManagementPage } from "./participation-management.po";

describe("Participation Management", () => {
  let appPage: AppPage;
  let page: ParticipationManagementPage;

  beforeAll(async () => {
    appPage = new AppPage();
    page = new ParticipationManagementPage();

    await appPage.navigateToParticipationManagement();
  });

  it("should reset the participation", async () => {
    await page.resetParticipationButton.click();

    await browser.wait(EC.visibilityOf(appPage.confirmModal));
    await appPage.clickConfirmOnModal();

    const currentStatusText = await page.participationStatusText.getText();
    expect(currentStatusText)
      .toBe("Participation is open");
  });
});
