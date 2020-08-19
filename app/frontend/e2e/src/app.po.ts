import { browser, by, element } from "protractor";

export class AppPage {
  readonly username = element(by.css('[data-test-selector="user-menu"]'))
    .getText() as Promise<string>;

  readonly title = element(by.css("app-page-header h2"));

  readonly confirmModal = element(by.css("app-confirm-modal"));

  navigateToBaseUrl(): Promise<void> {
    return browser.get(browser.baseUrl) as Promise<void>;
  }

  navigateToGroupManagement(): Promise<void> {
    return browser.get(`${browser.baseUrl}/pages/administration/group-management`) as Promise<void>;
  }

  navigateToGroup2a(): Promise<void> {
    return browser.get(`${browser.baseUrl}/pages/administration/group-management/2a`) as Promise<void>;
  }

  async clickConfirmOnModal(): Promise<void> {
    await this.confirmModal
      .element(by.css(".modal-footer button[type='submit']"))
      .click();
  }
}
