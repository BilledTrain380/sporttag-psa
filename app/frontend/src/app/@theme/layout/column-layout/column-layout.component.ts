import { Component, HostBinding, Input } from "@angular/core";

import { Alert } from "../../../modules/alert/alert";

@Component({
             selector: "app-column-layout",
             templateUrl: "./column-layout.component.html",
           })
export class ColumnLayoutComponent {

  @Input()
  title?: string;

  @Input()
  size = 12;

  @Input()
  alert?: Alert;

  @HostBinding("class")
  get class(): string {
    return `col-lg-${this.size}`;
  }
}
