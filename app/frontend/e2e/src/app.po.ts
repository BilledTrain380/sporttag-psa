import { browser, by, element } from "protractor";

export class AppPage {
  readonly username = element(by.css('[data-test-selector="user-menu"]'))
    .getText() as Promise<string>;

  navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl) as Promise<unknown>;
  }
}
