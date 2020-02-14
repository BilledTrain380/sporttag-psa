import { browser, by, element, ElementFinder } from "protractor";

export class AppPage {
  navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl) as Promise<unknown>;
  }

  getUsername(): Promise<string> {
    return element(by.css('[data-test-selector="user-menu"]'))
      .getText() as Promise<string>;
  }

  getMenu(): ElementFinder {
    return element(by.css('app-sidebar [data-test-selector="navigation-menu"]'));
  }
}
