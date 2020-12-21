import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY, forkJoin, of } from "rxjs";
import { catchError, filter, map, switchMap } from "rxjs/operators";

import { requireNonNullOrUndefined } from "../../@core/lib/lib";
import { getLogger } from "../../@core/logging";
import { GroupApi, OverviewGroupsParameters } from "../../@core/service/api/group-api";
import { ParticipantApi, ParticipantParameters } from "../../@core/service/api/participant-api";
import { ParticipantInput } from "../../dto/participation";
import { AlertFactory } from "../../modules/alert/alert";

import {
  addParticipantAction,
  AddParticipantProps,
  deleteParticipantAction,
  DeleteParticipantProps,
  importGroupsAction,
  ImportGroupsProps,
  loadActiveParticipantAction,
  loadGroupAction,
  LoadGroupProps,
  loadOverviewGroupsAction,
  LoadOverviewGroupsProps,
  LoadParticipantProps,
  setActiveGroupAction,
  setActiveParticipantAction,
  setImportGroupsAlertAction,
  setOverviewGroupsAction,
  setParticipantAlertAction,
  updateParticipantAction,
  UpdateParticipantProps,
  updateParticipantRelationAction,
  UpdateParticipantRelationProps,
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

                      return this.groupApi.importGroups(action.file)
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
                          this.log.warn(`Could not load active group: name=${action.name}`, err);

                          return EMPTY;
                        })))));

  readonly loadActiveParticipant = createEffect(() => this.actions$
    .pipe(ofType(loadActiveParticipantAction.type))
    .pipe(switchMap((action: LoadParticipantProps) =>
                      this.participantApi.getParticipant(action.participantId)
                        .pipe(map(participant => {
                          this.log.info(`Successfully loaded participant: participantId=${action.participantId}`);

                          return setActiveParticipantAction({participant});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn(`Could not load participant: participantId=${action.participantId}`, err);

                          return EMPTY;
                        })))));

  readonly addParticipant = createEffect(() => this.actions$
    .pipe(ofType(addParticipantAction.type))
    .pipe(filter((action: AddParticipantProps) => action.participant.id === 0))
    .pipe(switchMap((action: AddParticipantProps) => {
                      const textAlert = this.alertFactory.textAlert();

                      const participantInput: ParticipantInput = {
                        prename: action.participant.prename,
                        surname: action.participant.surname,
                        gender: action.participant.gender,
                        address: action.participant.address,
                        birthday: action.participant.birthday,
                        group: action.participant.group.name,
                        town: action.participant.town,
                        sportType: requireNonNullOrUndefined(action.participant.sportType),
                      };

                      return this.participantApi.createParticipant(participantInput)
                        .pipe(switchMap(participant => {
                          this.log.info(`Successfully added participant: prename=${action.participant.prename}, surname=${action.participant.surname}`);

                          const alert = textAlert.success($localize`Successfully added participant`);

                          return [addParticipantAction({participant}), setParticipantAlertAction({alert})];
                        }))
                        .pipe(catchError(err => {
                          this.log.warn(`Could not add participant: prename=${action.participant.prename}, surname=${action.participant.surname}`, err);

                          const alert = textAlert.error($localize`Could not add participant`);

                          return of(setParticipantAlertAction({alert}));
                        }));
                    },
    )));

  readonly updateParticipant = createEffect(() => this.actions$
    .pipe(ofType(updateParticipantAction.type))
    .pipe(switchMap((action: UpdateParticipantProps) => {
                      const textAlert = this.alertFactory.textAlert();

                      return this.participantApi.updateParticipant(action.participant)
                        .pipe(map(() => {
                          this.log.info(`Successfully updated participant: participantId=${action.participant.id}`);

                          const alert = textAlert.success($localize`Successfully updated participant`);

                          return setParticipantAlertAction({alert});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn(`Could not update participant: participantId=${action.participant.id}`, err);

                          const alert = textAlert.error($localize`Could not update participant`);

                          return of(setParticipantAlertAction({alert}));
                        }));
                    },
    )));

  readonly updateParticipantRelation = createEffect(() => this.actions$
    .pipe(ofType(updateParticipantRelationAction.type))
    .pipe(switchMap((action: UpdateParticipantRelationProps) => {
                      const textAlert = this.alertFactory.textAlert();

                      return this.participantApi.updateParticipantRelation(action.participant)
                        .pipe(map(() => {
                          this.log.info(`Successfully updated participant relation: participantId=${action.participant.id}`);

                          const alert = textAlert.success($localize`Successfully updated participant`);

                          return setParticipantAlertAction({alert});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn(`Could not update participant relation: participantId=${action.participant.id}`, err);

                          const alert = textAlert.error($localize`Could not update participant`);

                          return of(setParticipantAlertAction({alert}));
                        }));
                    },
    )));

  readonly deleteParticipant = createEffect(() => this.actions$
    .pipe(ofType(deleteParticipantAction.type))
    .pipe(switchMap((action: DeleteParticipantProps) => {
                      const textAlert = this.alertFactory.textAlert();

                      return this.participantApi.deleteParticipant(action.participant_id)
                        .pipe(map(() => {
                          this.log.info(`Successfully deleted participant: participantId=${action.participant_id}`);

                          const alert = textAlert.success($localize`Successfully deleted participant`);

                          return setParticipantAlertAction({alert});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not delete participant", err);

                          const alert = textAlert.error($localize`Could not delete participant`);

                          return of(setParticipantAlertAction({alert}));
                        }));
                    },
    )));

  private readonly log = getLogger("GroupEffect");

  constructor(
    private readonly actions$: Actions,
    private readonly groupApi: GroupApi,
    private readonly participantApi: ParticipantApi,
    private readonly alertFactory: AlertFactory,
  ) {
  }
}
