import { Component, Input } from "@angular/core";

@Component({
  selector: "app-simple-page",
  templateUrl: "./simple-page.component.html",
})
export class SimplePageComponent {

  @Input()
  pageTitle?: string;

  @Input()
  title?: string;
}
