import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { ANONYMOUS } from "../../@core/auth/auth-constants";
import { PsaAuthService } from "../../@core/auth/psa-auth.service";
import { getLogger } from "../../@core/logging";

import { loginSuccess, logout } from "./user.action";

@Injectable()
export class UserEffects {
  readonly logout$ = createEffect(() => this.actions$
    .pipe(ofType(logout.type))
    .pipe(switchMap(() =>
                      this.authService.revokeToken()
                        .pipe(map(() => loginSuccess({username: ANONYMOUS, authorities: []})))
                        .pipe(catchError(err => {
                          this.log.warn("Could perform logout", err);

                          return EMPTY;
                        })))));

  private readonly log = getLogger("UserEffects");

  constructor(
    private readonly actions$: Actions,
    private readonly authService: PsaAuthService,
  ) {
  }
}
