import { Component, Input } from "@angular/core";

import { Alert } from "../alert";

@Component({
             selector: "app-text-alert",
             templateUrl: "./text-alert.component.html",
           })
export class TextAlertComponent {

  @Input()
  alert?: Alert;

  close(): void {
    this.alert = undefined;
  }
}
