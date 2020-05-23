import { by, element } from "protractor";

export class GroupManagementPage {
  readonly importButton = element(by.css("[data-test-selector='import-groups']"));
  readonly groupTable = element(by.css("[data-test-selector='group-table']"));

  readonly importDialog = element(by.css("app-submit-modal"));
  readonly groupInput = element(by.css("[data-test-selector='group-input'] input[type='file']"));
  readonly importSubmit = element(by.css("app-import-groups button[type='submit']"));

  getGroupCount(): Promise<number> {
    return this.groupTable
      .all(by.tagName("tbody tr"))
      .count() as Promise<number>;
  }
}
