import { by, ElementFinder } from "protractor";

export const ACTION_BUTTON_SELECTOR = "td app-button-group app-button-group-item button";

export async function clickActionButtonGroup(row: ElementFinder): Promise<void> {
  await row
    .element(by.css("td app-button-group #action-button-group"))
    .click();
}
