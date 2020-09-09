import { AppPage } from "../app.po";

import { AthleticsPage } from "./athletics.po";

describe("AthleticsPage", () => {
  let appPage: AppPage;
  let page: AthleticsPage;

  beforeAll(async () => {
    appPage = new AppPage();
    page = new AthleticsPage();

    await appPage.navigateToAthletics();
  });

  it("should switch group", async () => {
    await expect(page.participationNotStarted.isPresent())
      .toBe(true, "Participation not started visibility");
  });
});
