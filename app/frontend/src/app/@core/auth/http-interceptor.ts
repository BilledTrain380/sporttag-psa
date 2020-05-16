import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { OAuthService } from "angular-oauth2-oidc";
import { Observable } from "rxjs";
import { catchError } from "rxjs/operators";

import { getLogger, Logger } from "../logging";
import { HTTP_STATUS_UNAUTHORIZED } from "../web";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  private readonly log: Logger = getLogger("TokenInterceptor");

  constructor(
    private readonly auth: OAuthService,
  ) {
  }

  // tslint:disable-next-line: no-any
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (this.auth.hasValidAccessToken()) {
      this.log.debug("Intercept http request with access token");
      const request = req.clone({
        setHeaders: {
          Authorization: `Bearer ${this.auth.getAccessToken()}`,
        },
      });

      return next.handle(request);
    }

    return next.handle(req)
      .pipe(catchError(err => {
        if (err instanceof HttpErrorResponse && err.status === HTTP_STATUS_UNAUTHORIZED) {
          location.reload();
        }

        throw err;
      }));
  }
}
