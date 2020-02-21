import { Component, Input } from "@angular/core";

@Component({
  selector: "app-row-page",
  templateUrl: "./row-page.component.html",
})
export class RowPageComponent {

  @Input()
  pageTitle?: string;

  @Input()
  pageSubTitle?: string;
}
