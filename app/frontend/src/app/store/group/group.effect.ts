import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY, of } from "rxjs";
import { catchError, map, mergeMap, switchMap } from "rxjs/operators";

import { getLogger } from "../../@core/logging";
import { GroupApi, GroupOverviewParameters } from "../../@core/service/api/group-api";
import { WebApi } from "../../@core/service/api/web-api";
import { AlertFactory } from "../../modules/alert/alert";

import {
  importGroupsAction,
  ImportGroupsProps,
  loadGroupsOverviewAction,
  LoadGroupsOverviewProps,
  setGroupsAction,
  setImportGroupsAlertAction,
} from "./group.action";

@Injectable()
export class GroupEffects {

  readonly loadGroups$ = createEffect(() => this.actions$
    .pipe(ofType(loadGroupsOverviewAction.type))
    .pipe(mergeMap((action: LoadGroupsOverviewProps) => {
      const parameters = action.statusType ? new GroupOverviewParameters(action.statusType) : undefined;

      return this.groupApi.getGroupsOverview(parameters);
    }))
    .pipe(map(groups => setGroupsAction({groups})))
    .pipe(catchError(err => {
      this.log.warn("Could not load groups overview", err);

      return EMPTY;
    })));

  readonly importGroups$ = createEffect(() => this.actions$
    .pipe(ofType(importGroupsAction.type))
    .pipe(switchMap((action: ImportGroupsProps) => {
                      const textAlert = this.alertFactory.textAlert();

                      return this.webApi.importGroups(action.file)
                        .pipe(map(() => {
                          this.log.info("Successfully imported groups");
                          const alert = textAlert.success($localize`Successfully imported groups`);

                          return setImportGroupsAlertAction({alert});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not import groups", err);
                          const alert = textAlert.error(err.error.message);

                          return of(setImportGroupsAlertAction({alert}));
                        }));
                    },
    )));

  private log = getLogger("GroupEffect");

  constructor(
    private readonly actions$: Actions,
    private readonly groupApi: GroupApi,
    private readonly webApi: WebApi,
    private readonly alertFactory: AlertFactory,
  ) {
  }
}
