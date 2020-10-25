import { by, element } from "protractor";

const ROOT_SELECTOR = "app-change-password-modal";

export class ChangeUserPasswordModalPage {
  readonly modalContainer = element(by.css(ROOT_SELECTOR));
  readonly submitButton = element(by.css(`${ROOT_SELECTOR} button[type='submit']`));

  readonly passwordInput = element(by.css(`${ROOT_SELECTOR} #inputPassword`));
}
