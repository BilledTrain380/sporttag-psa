import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger } from "../../@core/logging";
import { PsaAuthService } from "../../@core/service/psa-auth.service";
import { ANONYMOUS } from "../../@security/auth-constants";

import { logoutAction, setUserAction } from "./user.action";

@Injectable()
export class UserEffects {
  readonly logout$ = createEffect(() => this.actions$
    .pipe(ofType(logoutAction.type))
    .pipe(switchMap(() =>
                      this.authService.revokeToken()
                        .pipe(map(() => setUserAction({username: ANONYMOUS, authorities: [], locale: "en"})))
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
