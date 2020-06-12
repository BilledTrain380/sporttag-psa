import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { OAuthService } from "angular-oauth2-oidc";
import { EMPTY, Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";

@Injectable({
              providedIn: "root",
            })
export class PsaAuthService {
  constructor(
    private readonly http: HttpClient,
    private readonly oauthService: OAuthService,
  ) {
  }

  revokeToken(): Observable<void> {
    const headers = new HttpHeaders();
    headers.append("Content-Type", "application/x-www-form-urlencoded");

    const token = this.oauthService.getAccessToken();

    const body: TokenRevokeDto = {
      token,
      token_type_hint: "access_token",
    };

    return this.http.post("/token/revoke", body, {headers})
      .pipe(mergeMap(() => EMPTY));
  }
}

interface TokenRevokeDto {
  readonly token: string;
  readonly token_type_hint: "access_token" | "refresh_token";
}
