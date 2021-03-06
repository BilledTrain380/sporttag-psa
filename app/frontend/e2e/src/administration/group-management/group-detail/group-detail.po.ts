import { browser, by, element, ElementFinder, ExpectedConditions as EC } from "protractor";

import { ACTION_BUTTON_SELECTOR, clickActionButtonGroup } from "../../../util/table-button-group-util";

export class GroupDetailPage {
  readonly addParticipantButton = element(by.css("[data-test-selector='add-participant'"));
  readonly participantRows = element.all(by.css("[data-test-selector='participant-table'] tr"));

  getRowByName(name: string): ElementFinder {
    return element(by.cssContainingText("[data-test-selector='participant-table'] tr", name));
  }

  async toggleAbsentByRow(row: ElementFinder): Promise<void> {
    await row
      .element(by.css("td input[type='checkbox']"))
      .click();
  }

  async getAbsentValueByRow(row: ElementFinder): Promise<boolean> {
    return row
      .element(by.css("td input[type='checkbox']"))
      .isSelected();
  }

  async changeSportTypeByRow(row: ElementFinder, sportType: string): Promise<void> {
    const sportTypeInput = row
      .element(by.css("td app-sport-type-input select"));

    await sportTypeInput.click();
    await sportTypeInput
      .element(by.css(`select option[value='${sportType}']`))
      .click();
    await sportTypeInput.click();
  }

  getSportTypeByRow(row: ElementFinder, desiredValue: string): ElementFinder {
    return row
      .element(by.cssContainingText("td app-sport-type-input select option", desiredValue));
  }

  async clickEditParticipantByRow(row: ElementFinder): Promise<void> {
    await clickActionButtonGroup(row);

    const button = row
      .element(by.cssContainingText(ACTION_BUTTON_SELECTOR, "Edit"));

    await browser.wait(EC.visibilityOf(button));
    await button.click();
  }

  async clickDeleteParticipantByRow(row: ElementFinder): Promise<void> {
    await clickActionButtonGroup(row);

    const button = row
      .element(by.cssContainingText(ACTION_BUTTON_SELECTOR, "Delete"));

    await browser.wait(EC.visibilityOf(button));
    await button.click();
  }
}
