import { BreakpointObserver, Breakpoints } from "@angular/cdk/layout";
import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { faBars, faChevronDown, faChevronLeft } from "@fortawesome/free-solid-svg-icons";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import { PageMenu } from "../../@core/menu/page-menu";

@Component({
  selector: "app-sidebar",
  templateUrl: "./sidebar.component.html",
  styleUrls: ["./sidebar.component.scss"],
})
export class SidebarComponent implements OnInit, OnDestroy {

  @Input()
  items: ReadonlyArray<PageMenu> = [];

  collapsed = false;

  faBars = faBars;
  faChevronDown = faChevronDown;
  faChevronLeft = faChevronLeft;

  get currentExpandedMenuId(): string | undefined {
    return this._currentExpandedMenuId;
  }

  private _currentExpandedMenuId?: string;

  private readonly destroy$ = new Subject();

  constructor(
    readonly route: ActivatedRoute,
    private readonly breakpointObserver: BreakpointObserver,
  ) {
  }

  ngOnInit(): void {
    this.breakpointObserver
      .observe([
        Breakpoints.Tablet,
        Breakpoints.Small,
        Breakpoints.XSmall,
      ])
      .pipe(takeUntil(this.destroy$))
      .subscribe(result => this.collapsed = result.matches);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  toggleCollapse(item: PageMenu): void {
    this._currentExpandedMenuId = this._currentExpandedMenuId === item.id
      ? undefined
      : item.id;
  }
}
