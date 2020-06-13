import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY, forkJoin, of } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger } from "../../@core/logging";
import { GroupApi, OverviewGroupsParameters } from "../../@core/service/api/group-api";
import { ParticipantApi, ParticipantParameters } from "../../@core/service/api/participant-api";
import { WebApi } from "../../@core/service/api/web-api";
import { AlertFactory } from "../../modules/alert/alert";

import {
  importGroupsAction,
  ImportGroupsProps,
  loadGroupAction,
  LoadGroupProps,
  loadOverviewGroupsAction,
  LoadOverviewGroupsProps,
  setActiveGroupAction,
  setActiveGroupAlertAction,
  setImportGroupsAlertAction,
  setOverviewGroupsAction,
  updateParticipantAction,
  UpdateParticipantProps,
} from "./group.action";

@Injectable()
export class GroupEffects {
  readonly loadOverviewGroups$ = createEffect(() => this.actions$
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

  readonly loadActiveGroup = createEffect(() => this.actions$
    .pipe(ofType(loadGroupAction.type))
    .pipe(switchMap((action: LoadGroupProps) =>
                      forkJoin([
                                 this.groupApi.getGroup(action.name),
                                 this.participantApi.getParticipants(new ParticipantParameters(action.name)),
                               ])
                        .pipe(map(result => setActiveGroupAction({group: result[0], participants: result[1]})))
                        .pipe(catchError(err => {
                          this.log.warn("Could not load active group", err);

                          return EMPTY;
                        })))));

  readonly updateParticipant = createEffect(() => this.actions$
    .pipe(ofType(updateParticipantAction.type))
    .pipe(switchMap((action: UpdateParticipantProps) => {
                      const textAlert = this.alertFactory.textAlert();

                      return this.participantApi.updateParticipant(action.participant)
                        .pipe(map(() => {
                          this.log.info("Successfully updated participant");

                          const alert = textAlert.success($localize`Successfully updated participant`);

                          return setActiveGroupAlertAction({alert});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not update participant", err);

                          const alert = textAlert.error($localize`Could not update participant`);

                          return of(setActiveGroupAlertAction({alert}));
                        }));
                    },
    )));

  private readonly log = getLogger("GroupEffect");

  constructor(
    private readonly actions$: Actions,
    private readonly groupApi: GroupApi,
    private readonly participantApi: ParticipantApi,
    private readonly webApi: WebApi,
    private readonly alertFactory: AlertFactory,
  ) {
  }
}
