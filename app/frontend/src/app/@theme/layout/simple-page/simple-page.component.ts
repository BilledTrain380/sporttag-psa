import { Component, Input } from "@angular/core";

@Component({
  selector: "app-simple-page",
  templateUrl: "./simple-page.component.html",
  styleUrls: ["./simple-page.component.scss"],
})
export class SimplePageComponent {

  @Input()
  pageTitle?: string;

  @Input()
  title?: string;
}
