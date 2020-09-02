import { by, element } from "protractor";

export class ParticipationManagementPage {
  readonly closeParticipationButton = element(by.css('[data-test-selector="close-participation-button"] button'));
  readonly resetParticipationButton = element(by.css('[data-test-selector="reset-participation-button"] button'));
  readonly participationStatusText = element(by.css('[data-test-selector="participation-information"] app-status app-status-detail span'));
}
