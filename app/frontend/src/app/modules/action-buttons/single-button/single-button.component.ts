import { Component, Input } from "@angular/core";

@Component({
             selector: "app-single-button",
             templateUrl: "./single-button.component.html",
           })
export class SingleButtonComponent {
  @Input()
  text = "";
}
