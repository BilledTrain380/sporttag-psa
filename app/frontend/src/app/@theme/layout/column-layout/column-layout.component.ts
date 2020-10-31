import { Component, HostBinding, Input } from "@angular/core";

import { Alert } from "../../../modules/alert/alert";
import { COL_LG_12 } from "../../theme-constants";
import { buildColumnCssClass } from "../../utils/css-classes-utils";

@Component({
             selector: "app-column-layout",
             templateUrl: "./column-layout.component.html",
           })
export class ColumnLayoutComponent {

  @Input()
  title?: string;

  @Input()
  set size(size: number) {
    this.class = buildColumnCssClass(size);
  }

  @Input()
  alert?: Alert;

  @HostBinding("class")
  class = COL_LG_12;
}
