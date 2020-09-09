import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger, Logger } from "../../@core/logging";
import { CompetitorApi, CompetitorParameters } from "../../@core/service/api/competitor-api";
import { GroupApi } from "../../@core/service/api/group-api";
import { ParticipationApi } from "../../@core/service/api/participation-api";

import {
  loadCompetitorsAction,
  loadGroupsAction,
  loadParticipationStatusAction,
  setCompetitorsAction,
  setGroupsAction,
  setParticipationStatusAction,
  updateCompetitorRelationAction,
  updateCompetitorResultAction,
} from "./athletics.action";

@Injectable()
export class AthleticsEffects {
  readonly loadParticipationStatus = createEffect(() => this.actions$
    .pipe(ofType(loadParticipationStatusAction))
    .pipe(switchMap(() =>
                      this.participationApi.getParticipationStatus()
                        .pipe(map(status => {
                          this.log.info("Successfully loaded participation status:", status);

                          return setParticipationStatusAction({status});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not load participation status", err);

                          return EMPTY;
                        })))));

  readonly loadGroups = createEffect(() => this.actions$
    .pipe(ofType(loadGroupsAction))
    .pipe(switchMap(() =>
                      this.groupApi.getGroups()
                        .pipe(map(groups => {
                          this.log.info("Successfully loaded groups: size=", groups.length);

                          return setGroupsAction({groups});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not load groups", err);

                          return EMPTY;
                        })))));

  readonly loadCompetitors = createEffect(() => this.actions$
    .pipe(ofType(loadCompetitorsAction))
    .pipe(switchMap(action => {
      const params = new CompetitorParameters(action.group);

      return this.competitorApi.getCompetitors(params)
        .pipe(map(competitors => {
          this.log.info("Successfully loaded competitors: size =", competitors.length);

          return setCompetitorsAction({competitors});
        }))
        .pipe(catchError(err => {
          this.log.warn("Could not load competitors", err);

          return EMPTY;
        }));
    })));

  readonly updateCompetitorResult = createEffect(() => this.actions$
    .pipe(ofType(updateCompetitorResultAction))
    .pipe(switchMap(action =>
                      this.competitorApi.updateCompetitorResult(action.competitorId, action.result)
                        .pipe(map(result => {
                          this.log.info("Successfully updated competitor result: result =", result);

                          return updateCompetitorRelationAction({competitorId: action.competitorId, results: [result]});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not update competitor result", err);

                          return EMPTY;
                        })))));

  private readonly log: Logger = getLogger("AthleticsEffects");

  constructor(
    private readonly actions$: Actions,
    private readonly competitorApi: CompetitorApi,
    private readonly groupApi: GroupApi,
    private readonly participationApi: ParticipationApi,
  ) {
  }
}
