import { Component, Input } from "@angular/core";
import { IconDefinition } from "@fortawesome/fontawesome-common-types";

@Component({
             selector: "app-layout-button",
             templateUrl: "./layout-button.component.html",
           })
export class LayoutButtonComponent {

  @Input()
  icon?: IconDefinition;

  @Input()
  text?: string;

  @Input()
  buttonType = "light";

  @Input()
  disabled = false;

  @Input()
  showLoading = false;
}
