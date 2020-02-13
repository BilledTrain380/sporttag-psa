import { AuthConfig } from "angular-oauth2-oidc";

export interface PsaJwt {
  readonly user_id: number;
  readonly user_name: string;
  readonly authorities: Array<string>;
  readonly scope: Array<string>;
  readonly exp: number;
  readonly jti: string;
  readonly client_id: string;
}

// FIXME: Don't use http://localhost address
export const authConfig: AuthConfig = {
  loginUrl: "http://localhost:8100/oauth/authorize",
  logoutUrl: "http://localhost:8100/logout",
  tokenEndpoint: "http://localhost:8100/oauth/token",
  redirectUri: `${window.location.origin}/index.html`,
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
    "files",
    "ranking",
    "event_sheets",
    "participant_list",
  ].join(" "),
};
