import { Component, Input } from "@angular/core";

import { PageMenu } from "../page-menu";

@Component({
  selector: "app-menu-item",
  templateUrl: "./menu-item.component.html",
  styleUrls: ["./menu-item.component.scss"],
})
export class MenuItemComponent {
  @Input()
  readonly item?: PageMenu;

  @Input()
  readonly parentId?: string;
}
