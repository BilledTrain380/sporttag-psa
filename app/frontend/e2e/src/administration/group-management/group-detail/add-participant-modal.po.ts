import { by, element } from "protractor";

const ROOT_SELECTOR = "app-add-participant-modal";

export class AddParticipantModalPage {
  readonly modalContainer = element(by.css(ROOT_SELECTOR));
  readonly submitButton = element(by.css(`${ROOT_SELECTOR} button[type='submit']`));

  readonly prenameInput = element(by.css(`${ROOT_SELECTOR} #inputPrename`));
  readonly surnameInput = element(by.css(`${ROOT_SELECTOR} #inputSurname`));
  readonly addressInput = element(by.css(`${ROOT_SELECTOR} #inputAddress`));
  readonly zipInput = element(by.css(`${ROOT_SELECTOR} #inputZip`));
  readonly townInput = element(by.css(`${ROOT_SELECTOR} #inputTown`));
  readonly sportTypeInput = element(by.css(`${ROOT_SELECTOR} #inputSportType`));

  async selectSportTypeAthletics(): Promise<void> {
    await this.sportTypeInput.click();
    await this.sportTypeInput
      .element(by.css(`${ROOT_SELECTOR} select option[value='Athletics']`))
      .click();
    await this.sportTypeInput.click();
  }
}
