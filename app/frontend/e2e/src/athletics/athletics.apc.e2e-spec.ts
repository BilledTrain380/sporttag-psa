import { browser, by, ExpectedConditions as EC, protractor } from "protractor";

import { AppPage } from "../app.po";

import { AthleticsPage } from "./athletics.po";

describe("AthleticsPage", () => {
  let appPage: AppPage;
  let page: AthleticsPage;

  const SURNAME_CELL = 1;

  beforeAll(async () => {
    appPage = new AppPage();
    page = new AthleticsPage();

    await appPage.navigateToAthletics();
  });

  it("should switch group", async () => {
    const firstRow = page.tableRows.first();

    await browser.wait(EC.visibilityOf(firstRow));

    await page.groupNextButton.click();

    const firstRowValue = firstRow.element(by.cssContainingText("td", "Hill"));
    await browser.wait(EC.invisibilityOf(firstRowValue));

    const newFirstRowValue = firstRow
      .all(by.css("td"))
      .get(SURNAME_CELL);

    await expect(newFirstRowValue.getText())
      .toBe("Mason");
  });

  it("should update the points when updating a result", async () => {
    const resultInput = page.tableRows.first()
      .element(by.css('[data-test-selector="result-input"]'));

    await browser.wait(EC.visibilityOf(resultInput));

    /* Send a back space and the actual value at once.
     * The .clear() method does not work here, because due the focus out,
     * the previous value will be inserted again.
     */
    await resultInput.sendKeys(`${protractor.Key.BACK_SPACE}11.25`);

    // Trigger focus out by clicking into another input
    await page.tableRows.get(1)
      .element(by.css('[data-test-selector="result-input"]'))
      .click();

    const points = page.tableRows.get(0)
      .element(by.cssContainingText('[data-test-selector="result-points"]', "144"));

    await browser.wait(EC.visibilityOf(points));

    await expect(points.getText())
      .toBe("144");
  });

  it("should switch discipline", async () => {
    const firstRow = page.tableRows.first();

    await browser.wait(EC.visibilityOf(firstRow));

    const firstRowUnitValue = firstRow.element(by.css("td .result-input-container .input-group-append"));
    const unitValue = await firstRowUnitValue.getText();

    await page.disciplineNextButton.click();

    const newUnitValue = await firstRowUnitValue.getText();
    expect(newUnitValue)
      .not
      .toBe(unitValue);
  });
});
