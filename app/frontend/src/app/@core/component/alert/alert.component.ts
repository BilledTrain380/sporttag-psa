import { Component, Input } from "@angular/core";

import { Alert } from "./alert";

@Component({
             selector: "app-alert",
             templateUrl: "./alert.component.html",
           })
export class AlertComponent {

  @Input()
  alert?: Alert;

  close(): void {
    this.alert = undefined;
  }
}
