import { Component, OnInit } from "@angular/core";
import { Store } from "@ngrx/store";
import { JwksValidationHandler, OAuthService } from "angular-oauth2-oidc";
import * as jwt_decode from "jwt-decode";

import { authConfig, PsaJwt } from "./@core/auth/auth-config";
import { getLogger, Logger } from "./@core/logging";
import { AppState } from "./store/app";
import { loginSuccess } from "./store/user/user.action";

@Component({
             selector: "app-root",
             templateUrl: "./app.component.html",
             styleUrls: ["./app.component.scss"],
           })
export class AppComponent implements OnInit {
  private readonly log: Logger = getLogger("AppComponent");

  constructor(
    private readonly oauthService: OAuthService,
    private readonly store: Store<AppState>,
  ) {
  }

  ngOnInit(): void {
    this.log.info("Browser version: ", navigator.userAgent);
    this.log.info("Started PSA frontend Angular application");

    // We don't have an id token, but its required to perform the logout properly, so we just provide a uuid
    sessionStorage.setItem("id_token", "8c1f517c-9e6d-4881-9a79-8d2ddf8f7c84");
    this.oauthService.configure(authConfig);
    this.oauthService.setStorage(sessionStorage);
    this.oauthService.tokenValidationHandler = new JwksValidationHandler();

    if (!this.oauthService.hasValidAccessToken()) {
      this.login();
    } else {
      this.handleAccessToken();
    }
  }

  private login(): void {
    this.log.info("Try login with implicit flow");
    this.oauthService.tryLoginImplicitFlow()
      .then(isSuccessful => {
        if (!isSuccessful) {
          this.log.info("Login failed, init implicit flow");
          this.oauthService.initImplicitFlowInternal();
        } else {
          this.handleAccessToken();
        }
      });
  }

  private handleAccessToken(): void {
    const parsedToken = jwt_decode<PsaJwt>(this.oauthService.getAccessToken());
    this.log.info("Successfully logged in as user", parsedToken.user_name);
    this.store.dispatch(
      loginSuccess({
                     username: parsedToken.user_name,
                     authorities: parsedToken.authorities,
                   }));
  }
}
