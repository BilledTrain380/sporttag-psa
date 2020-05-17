import { Component, OnInit } from "@angular/core";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { Store } from "@ngrx/store";
import { OAuthService } from "angular-oauth2-oidc";
import { Observable } from "rxjs";

import { getLogger, Logger } from "../../@core/logging";
import { AppState } from "../../store/app";
import { logout } from "../../store/user/user.action";
import { selectUsername } from "../../store/user/user.selector";

@Component({
             selector: "app-header",
             templateUrl: "./header.component.html",
             styleUrls: ["./header.component.scss"],
           })
export class HeaderComponent implements OnInit {

  username$?: Observable<string>;

  readonly faUser = faUser;

  private readonly log: Logger = getLogger("HeaderComponent");

  constructor(
    private readonly store: Store<AppState>,
    private readonly oauthService: OAuthService,
  ) {
  }

  ngOnInit(): void {
    this.username$ = this.store.select(selectUsername);
  }

  logout(event: Event): void {
    this.log.info("Log out user");
    event.preventDefault();
    this.store.dispatch(logout());
    this.oauthService.logOut();
  }
}
