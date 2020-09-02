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

  it("should display the participation as open", async () => {
    const currentStatusText = await page.participationStatusText.getText();
    expect(currentStatusText)
      .toBe("Participation is open");
  });

  it("should close the participation", async () => {
    await page.closeParticipationButton.click();

    await browser.wait(EC.visibilityOf(appPage.confirmModal));
    await appPage.clickConfirmOnModal();

    await expect(page.closeParticipationButton.isEnabled())
      .toBe(false, "Expected close participation button to be disabled");

    const currentStatusText = await page.participationStatusText.getText();
    expect(currentStatusText)
      .toBe("Participation is closed");
  });
});
