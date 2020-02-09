import { Component, Input, OnChanges, OnInit, SimpleChanges } from "@angular/core";

import { PageMenu } from "../page-menu";

@Component({
  selector: "app-menu-item",
  templateUrl: "./menu-item.component.html",
  styleUrls: ["./menu-item.component.scss"],
})
export class MenuItemComponent implements OnChanges {
  @Input()
  readonly item?: PageMenu;

  @Input()
  readonly parentId?: string;

  ngOnChanges(changes: SimpleChanges): void {
    console.log(this.item);
  }


}
