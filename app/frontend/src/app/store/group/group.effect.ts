import { Injectable } from "@angular/core";
import { Actions, createEffect, Effect, ofType } from "@ngrx/effects";
import { EMPTY } from "rxjs";
import { catchError, map, mergeMap } from "rxjs/operators";

import { getLogger, Logger } from "../../@core/logging";
import { GroupApi, GroupOverviewParameters } from "../../@core/service/api/group-api";

import { loadGroupsOverviewAction, LoadGroupsOverviewProps, setGroupsAction } from "./group.action";

@Injectable()
export class GroupEffects {

  @Effect()
  readonly loadGroups$ = createEffect(() => this.actions$
    .pipe(ofType(loadGroupsOverviewAction.type))
    .pipe(mergeMap((action: LoadGroupsOverviewProps) => {
      const parameters = action.statusType ? new GroupOverviewParameters(action.statusType) : undefined;

      return this.groupApi.getGroupsOverview(parameters);
    }))
    .pipe(map(groups => setGroupsAction({groups})))
    .pipe(catchError(err => {
      this.log.warn("Could not load groups overview", err);

      // FIXME: Dispatch error notification action
      return EMPTY;
    })));

  private log: Logger = getLogger("GroupEffect");

  constructor(
    private readonly actions$: Actions,
    private readonly groupApi: GroupApi,
  ) {
  }
}
