import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { OAuthService } from "angular-oauth2-oidc";
import { Observable } from "rxjs";
import { tap } from "rxjs/operators";

import { TokenRevokeDto } from "../../dto/oauth";
import { getLogger } from "../logging";

import { API_ENDPOINT } from "./api/pas-api";

@Injectable({
              providedIn: "root",
            })
export class PsaAuthService {
  private readonly log = getLogger("PsaAuthService");

  constructor(
    private readonly http: HttpClient,
    private readonly oauthService: OAuthService,
  ) {
  }

  revokeToken(): Observable<void> {
    this.log.info("Revoke access token");

    const token = this.oauthService.getAccessToken();
    const body: TokenRevokeDto = {token};

    return this.http.post<void>(`${API_ENDPOINT}/oauth/token/revoke`, body)
      .pipe(tap(() => this.oauthService.logOut(true)));
  }
}
