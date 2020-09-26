import { Component, Input } from "@angular/core";

import { Alert } from "../../../modules/alert/alert";

@Component({
             selector: "app-simple-page",
             templateUrl: "./simple-page.component.html",
           })
export class SimplePageComponent {

  @Input()
  pageTitle?: string;

  @Input()
  pageSubTitle?: string;

  @Input()
  cardTitle?: string;

  @Input()
  alert?: Alert;
}
