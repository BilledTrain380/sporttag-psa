import { BreakpointObserver, Breakpoints } from "@angular/cdk/layout";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { faCogs, faRunning, faTrophy, faUser } from "@fortawesome/free-solid-svg-icons";
import { Store } from "@ngrx/store";
import { OAuthService } from "angular-oauth2-oidc";
import { Observable, Subject } from "rxjs";
import { takeUntil, tap } from "rxjs/operators";

import { getLogger, Logger } from "../../@core/logging";
import { AppState } from "../../store/app";
import { logout } from "../../store/user/user.action";
import { selectUsername } from "../../store/user/user.selector";

@Component({
             selector: "app-header",
             templateUrl: "./header.component.html",
             styleUrls: ["./header.component.scss"],
           })
export class HeaderComponent implements OnInit, OnDestroy {
  username$?: Observable<string>;

  isMobile = false;

  readonly faTrophy = faTrophy;
  readonly faRunning = faRunning;
  readonly faCogs = faCogs;
  readonly faUser = faUser;

  private readonly destroy$ = new Subject<void>();

  private readonly log: Logger = getLogger("HeaderComponent");

  constructor(
    private readonly store: Store<AppState>,
    private readonly oauthService: OAuthService,
    // private readonly route: ActivatedRoute,
    private readonly breakpointObserver: BreakpointObserver,
  ) {
  }

  ngOnInit(): void {
    this.username$ = this.store.select(selectUsername);

    this.breakpointObserver
      .observe([
                 Breakpoints.Tablet,
                 Breakpoints.Small,
                 Breakpoints.XSmall,
               ])
      .pipe(tap(() => this.log.info("Detected media breakpoint change")))
      .pipe(takeUntil(this.destroy$))
      .subscribe(result => this.isMobile = result.matches);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  logout(event: Event): void {
    this.log.info("Log out user");
    event.preventDefault();
    this.store.dispatch(logout());
    this.oauthService.logOut();
  }
}
