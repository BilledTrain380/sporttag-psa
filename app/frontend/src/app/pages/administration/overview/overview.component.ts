import { Component } from "@angular/core";

import { ADMINISTRATION_ITEMS } from "../administration-item";

@Component({
             selector: "app-overview",
             templateUrl: "./overview.component.html",
             styleUrls: ["./overview.component.scss"],
           })
export class OverviewComponent {
  readonly items = ADMINISTRATION_ITEMS;
}
