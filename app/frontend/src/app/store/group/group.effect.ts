import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY, of } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger } from "../../@core/logging";
import { GroupApi, OverviewGroupsParameters } from "../../@core/service/api/group-api";
import { WebApi } from "../../@core/service/api/web-api";
import { AlertFactory } from "../../modules/alert/alert";

import {
  importGroupsAction,
  ImportGroupsProps,
  loadOverviewGroupsAction,
  LoadOverviewGroupsProps,
  setImportGroupsAlertAction,
  setOverviewGroupsAction,
} from "./group.action";

@Injectable()
export class GroupEffects {
  readonly loadGroups$ = createEffect(() => this.actions$
    .pipe(ofType(loadOverviewGroupsAction.type))
    .pipe(switchMap((action: LoadOverviewGroupsProps) => {
      const parameters = action.statusType ? new OverviewGroupsParameters(action.statusType) : undefined;

      return this.groupApi.getOverviewGroups(parameters)
        .pipe(map(groups => setOverviewGroupsAction({groups})))
        .pipe(catchError(err => {
          this.log.warn("Could not load overview groups", err);

          return EMPTY;
        }));
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

                          const alert = (typeof err.error === "string")
                            ? textAlert.error(err.error)
                            : textAlert.error($localize`Unknown error`);

                          return of(setImportGroupsAlertAction({alert}));
                        }));
                    },
    )));

  private readonly log = getLogger("GroupEffect");

  constructor(
    private readonly actions$: Actions,
    private readonly groupApi: GroupApi,
    private readonly webApi: WebApi,
    private readonly alertFactory: AlertFactory,
  ) {
  }
}
