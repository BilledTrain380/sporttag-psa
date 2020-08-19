import { by, element } from "protractor";

const ROOT_SELECTOR = "app-edit-participant-modal";

export class EditParticipantModalPage {
  readonly modalContainer = element(by.css(ROOT_SELECTOR));

  readonly submitButton = element(by.css(`${ROOT_SELECTOR} button[type='submit']`));

  readonly prenameInput = element(by.css(`${ROOT_SELECTOR} #inputPrename`));
  readonly surnameInput = element(by.css(`${ROOT_SELECTOR} #inputSurname`));
}
