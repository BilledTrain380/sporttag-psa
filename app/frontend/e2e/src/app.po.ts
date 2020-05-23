import { browser, by, element } from "protractor";

export class AppPage {
  readonly username = element(by.css('[data-test-selector="user-menu"]'))
    .getText() as Promise<string>;

  navigateToBaseUrl(): Promise<void> {
    return browser.get(browser.baseUrl) as Promise<void>;
  }

  navigateToGroupManagement(): Promise<void> {
    return browser.get(`${browser.baseUrl}/pages/administration/group-management`) as Promise<void>;
  }
}
