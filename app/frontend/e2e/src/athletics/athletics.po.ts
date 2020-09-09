import { by, element } from "protractor";

export class AthleticsPage {
  readonly participationNotStarted = element(by.css("app-athletics-not-started"));

  readonly tableRows = element.all(by.css("app-simple-page table tbody tr"));

  readonly groupNextButton = element(by.css('[data-test-selector="group-dropdown"] .input-group-append button'));
  readonly disciplineNextButton = element(by.css('[data-test-selector="discipline-dropdown"] .input-group-append button'));
}
