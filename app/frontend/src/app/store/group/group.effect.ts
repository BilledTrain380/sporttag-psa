import { Injectable } from "@angular/core";
import { Actions, createEffect, Effect, ofType } from "@ngrx/effects";
import { NGXLogger } from "ngx-logger";
import { EMPTY } from "rxjs";
import { catchError, map, mergeMap } from "rxjs/operators";

import { GroupApi } from "../../@core/service/api/group-api";

import { loadGroups, setGroups } from "./group.action";

@Injectable()
export class GroupEffects {

  @Effect()
  readonly loadGroups$ = createEffect(() => this.actions$
    .pipe(ofType(loadGroups.type))
    .pipe(mergeMap(() => this.groupApi.getGroups()))
    .pipe(map(groups => setGroups({groups})))
    .pipe(catchError(err => {
      this.log.warn("Could not load groups", err);

      // FIXME: Dispatch error notification action
      return EMPTY;
    })));

  constructor(
    private readonly actions$: Actions,
    private readonly groupApi: GroupApi,
    private readonly log: NGXLogger,
  ) {
  }
}
