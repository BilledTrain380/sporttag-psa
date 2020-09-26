import { Component, Input } from "@angular/core";

import { Alert } from "../../../modules/alert/alert";

@Component({
             selector: "app-card",
             templateUrl: "./card.component.html",
             styleUrls: ["./card.component.scss"],
           })
export class CardComponent {

  @Input()
  title?: string;

  @Input()
  alert?: Alert;

  get alertCssClass(): object {
    return {
      "text-success": this.alert?.isSuccess(),
      "text-info": this.alert?.isInfo(),
      "text-warning": this.alert?.isWarning(),
      "text-danger": this.alert?.isDanger(),
      "text-primary": this.alert?.isPrimary(),
      "text-secondary": this.alert?.isSecondary(),
    };
  }
}
