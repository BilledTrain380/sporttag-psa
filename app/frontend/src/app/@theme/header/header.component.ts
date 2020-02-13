import { Component, OnInit } from "@angular/core";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { Store } from "@ngrx/store";
import { OAuthService } from "angular-oauth2-oidc";
import { NGXLogger } from "ngx-logger";
import { Observable } from "rxjs";

import { AppState } from "../../store/app";
import { selectUsername } from "../../store/user/user.selector";
import { logout } from "../../store/user/user.action";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"],
})
export class HeaderComponent implements OnInit {

  username$?: Observable<string>;

  readonly faUser = faUser;

  constructor(
    private readonly store: Store<AppState>,
    private readonly oauthService: OAuthService,
    private readonly log: NGXLogger,
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
