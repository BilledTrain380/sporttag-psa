import { BreakpointObserver, Breakpoints } from "@angular/cdk/layout";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { faInfoCircle } from "@fortawesome/free-solid-svg-icons/faInfoCircle";
import { faLanguage } from "@fortawesome/free-solid-svg-icons/faLanguage";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { OAuthService } from "angular-oauth2-oidc";
import { Observable, Subject } from "rxjs";
import { map, takeUntil, tap } from "rxjs/operators";

import { environment } from "../../../environments/environment";
import { getLogger, Logger } from "../../@core/logging";
import { MENU_ITEMS } from "../../@core/menu/page-menu";
import { LanguageService } from "../../@core/service/language/language-service";
import { LANGUAGES } from "../../@core/service/language/languages";
import { PsaLocale } from "../../dto/profile";
import { AppState } from "../../store/app";
import { logoutAction } from "../../store/user/user.action";
import { selectLocale, selectUsername } from "../../store/user/user.selector";

import { AboutModalComponent } from "./about-modal/about-modal.component";

@Component({
             selector: "app-header",
             templateUrl: "./header.component.html",
           })
export class HeaderComponent implements OnInit, OnDestroy {
  readonly username$: Observable<string> = this.store.select(selectUsername);
  readonly locale$: Observable<string> = this.store.select(selectLocale)
    .pipe(map(locale =>
                LANGUAGES.find(lang => lang.value === locale)?.name ?? locale));

  isMobile = false;
  readonly menu = MENU_ITEMS;

  readonly languages = LANGUAGES;
  readonly faLanguage = faLanguage;
  readonly faUser = faUser;
  readonly faInfo = faInfoCircle;

  private readonly destroy$ = new Subject<void>();
  private readonly log: Logger = getLogger("HeaderComponent");

  constructor(
    private readonly store: Store<AppState>,
    private readonly oauthService: OAuthService,
    private readonly languageService: LanguageService,
    private readonly breakpointObserver: BreakpointObserver,
    private readonly modalService: NgbModal,
  ) {
  }

  ngOnInit(): void {
    this.breakpointObserver
      .observe([
                 Breakpoints.TabletPortrait,
                 Breakpoints.XSmall,
               ])
      .pipe(tap(() => this.log.info("Detected media breakpoint change")))
      .pipe(takeUntil(this.destroy$))
      .subscribe(result => this.isMobile = result.matches);
  }

  ngOnDestroy(): void {
    this.destroy$.complete();
  }

  changePassword(event: Event): void {
    event.preventDefault();

    this.store.dispatch(logoutAction());
    this.oauthService.logOut(true);

    if (environment.production) {
      window.location.pathname = "/user/change-pw";
    } else {
      window.location.href = `${window.location.protocol}//${window.location.hostname}:8080/user/change-pw`;
    }
  }

  changeLocale(event: Event, locale: PsaLocale): void {
    event.preventDefault();

    this.languageService.changeLocale(locale)
      .subscribe(() => {
        this.store.dispatch(logoutAction());
        window.location.replace("/app");
      });
  }

  logout(event: Event): void {
    this.log.info("Log out user");
    event.preventDefault();
    this.store.dispatch(logoutAction());
    this.oauthService.logOut();
  }

  openAboutDialog(event: Event): void {
    event.preventDefault();

    this.modalService.open(AboutModalComponent);
  }
}
