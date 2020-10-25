import { by, element } from "protractor";

const ROOT_SELECTOR = "app-add-user-modal";

export class AddUserModalPage {
  readonly modalContainer = element(by.css(ROOT_SELECTOR));
  readonly submitButton = element(by.css(`${ROOT_SELECTOR} button[type='submit']`));

  readonly usernameInput = element(by.css(`${ROOT_SELECTOR} #inputUsername`));
  readonly passwordInput = element(by.css(`${ROOT_SELECTOR} #inputPassword`));
}
