import { browser, ElementFinder, ExpectedConditions as EC } from "protractor";

import { AppPage } from "../../../app.po";

import { AddParticipantModalPage } from "./add-participant-modal.po";
import { EditParticipantModalPage } from "./edit-participant-modal.po";
import { GroupDetailPage } from "./group-detail.po";

describe("Group Management", () => {
  let appPage: AppPage;
  let page: GroupDetailPage;

  beforeAll(() => {
    appPage = new AppPage();
    page = new GroupDetailPage();
    appPage.navigateToGroup2a();
  });

  it("should display the group name and the according coach name", async () => {
    expect(await appPage.title.getText())
      .toEqual("Group 2a Peter Muster");
  });

  it("should list all participants of the group 2a", async () => {
    expect(await page.participantRows.count())
      .toBeGreaterThan(1);
  });

  describe("on add / edit / delete participant", () => {
    it("should add a new participant", async () => {
      const addParticipantPage = new AddParticipantModalPage();

      await page.addParticipantButton.click();
      await browser.wait(EC.visibilityOf(addParticipantPage.modalContainer));

      await addParticipantPage.prenameInput.sendKeys("Max");
      await addParticipantPage.surnameInput.sendKeys("Muster");
      await addParticipantPage.addressInput.sendKeys("Bakerstreet 12");
      await addParticipantPage.selectBirthdayToday();
      await addParticipantPage.zipInput.sendKeys("0311");
      await addParticipantPage.townInput.sendKeys("Manchester");
      await addParticipantPage.selectSportTypeAthletics();

      await browser.wait(EC.elementToBeClickable(addParticipantPage.submitButton));
      await addParticipantPage.submitButton.click();
      await browser.wait(EC.invisibilityOf(addParticipantPage.modalContainer));

      const maxMuster = page.getRowByName("Muster");
      await expect(maxMuster.isPresent())
        .toBe(true, "Expected Max Muster to be visible in table");
    });

    describe("on edit / delete participant", () => {
      let mmusterRow: ElementFinder;

      beforeEach(() => {
        mmusterRow = page.getRowByName("Muster");
      });

      it("should edit absent", async () => {
        await page.toggleAbsentByRow(mmusterRow);

        await expect(page.getAbsentValueByRow(mmusterRow))
          .toBe(true, "Expected absent checkbox to be true");
      });

      it("should edit the sport type", async () => {
        await page.changeSportTypeByRow(mmusterRow, "Schatzsuche");

        const expectedSportType = page.getSportTypeByRow(mmusterRow, "Schatzsuche");
        await expect(expectedSportType.isSelected())
          .toBe(true, "Expected Schatzsuche sport type to be selected");
      });

      it("should edit participant", async () => {
        const editParticipantModalPage = new EditParticipantModalPage();

        await page.clickEditParticipantByRow(mmusterRow);
        await browser.wait(EC.visibilityOf(editParticipantModalPage.modalContainer));

        await editParticipantModalPage.prenameInput.clear();
        await editParticipantModalPage.prenameInput.sendKeys("Maximiliam");

        await editParticipantModalPage.surnameInput.clear();
        await editParticipantModalPage.surnameInput.sendKeys("Mustermann");

        await browser.wait(EC.elementToBeClickable(editParticipantModalPage.submitButton));
        await editParticipantModalPage.submitButton.click();
        await browser.wait(EC.invisibilityOf(editParticipantModalPage.modalContainer));

        const mmustermann = page.getRowByName("Mustermann");
        await expect(mmustermann.isPresent())
          .toBe(true, "Expected Maximiliam Mustermann to be visible in table");
      });

      it("should delete participant", async () => {
        await page.clickDeleteParticipantByRow(mmusterRow);
        await browser.wait(EC.visibilityOf(appPage.confirmModal));

        await appPage.clickConfirmOnModal();
        await browser.wait(EC.invisibilityOf(appPage.confirmModal));

        const mmuster = page.getRowByName("Muster");
        await expect(mmuster.isPresent())
          .toBe(false, "Expected Max Muster to be deleted");
      });
    });
  });
});
