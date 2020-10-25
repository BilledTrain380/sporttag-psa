import { by, element, ElementFinder } from "protractor";

export class UserManagementPage {
  readonly addUserButton = element(by.css("[data-test-selector='add-user-button'"));
  readonly userTable = element(by.css('[data-test-selector="user-table"]'));

  getUserCount(): Promise<number> {
    return this.userTable
      .all(by.tagName("tbody tr"))
      .count() as Promise<number>;
  }

  getRowByName(name: string): ElementFinder {
    return element(by.cssContainingText('[data-test-selector="user-table"] tr', name));
  }

  async toggleEnabledByRow(row: ElementFinder): Promise<void> {
    await row
      .element(by.css("td input[type='checkbox']"))
      .click();
  }

  async getEnabledValueByRow(row: ElementFinder): Promise<boolean> {
    return row
      .element(by.css("td input[type='checkbox']"))
      .isSelected();
  }
}
