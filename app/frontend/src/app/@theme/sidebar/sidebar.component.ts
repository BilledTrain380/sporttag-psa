import { Component, Input, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { PageMenu } from "../../@core/menu/page-menu";
import { faChevronDown, faChevronLeft } from "@fortawesome/free-solid-svg-icons";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

@Component({
  selector: "app-sidebar",
  templateUrl: "./sidebar.component.html",
  styleUrls: ["./sidebar.component.scss"],
})
export class SidebarComponent {

  @Input()
  items: ReadonlyArray<PageMenu> = [];

  faChevronDown = faChevronDown;
  faChevronLeft = faChevronLeft;

  get currentExpandedMenuId(): string | undefined {
    return this._currentExpandedMenuId;
  }

  private _currentExpandedMenuId?: string;

  constructor(readonly route: ActivatedRoute) {
  }


  toggleCollapse(item: PageMenu): void {
    this._currentExpandedMenuId = this._currentExpandedMenuId === item.id
      ? undefined
      : item.id;
  }
}
