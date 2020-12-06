import { AuthConfig } from "angular-oauth2-oidc";

import { environment } from "../../environments/environment";

export interface PsaJwt {
  readonly user_id: number;
  readonly user_name: string;
  readonly authorities: Array<string>;
  readonly locale: string;
  readonly scope: Array<string>;
  readonly exp: number;
  readonly jti: string;
  readonly client_id: string;
}

const devHost = "http://localhost:8080";

export const authConfig: AuthConfig = {
  loginUrl: `${environment.production ? "" : devHost}/oauth/authorize`,
  logoutUrl: `${environment.production ? "" : devHost}/logout`,
  tokenEndpoint: `${environment.production ? "" : devHost}/oauth/token`,
  redirectUri: `${window.location.origin}${environment.production ? "/app" : "/index.html"}`,
  requireHttps: false,
  oidc: false,
  clientId: "psa-kitten",
  scope: [
    "user",
    "group_read",
    "group_write",
    "sport_read",
    "discipline_read",
    "competitor_read",
    "competitor_write",
    "participant_read",
    "participant_write",
    "participation",
    "ranking",
    "event_sheets",
  ].join(" "),
};
