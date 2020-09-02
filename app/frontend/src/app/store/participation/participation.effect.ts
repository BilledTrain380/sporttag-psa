import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger } from "../../@core/logging";
import { ParticipationApi } from "../../@core/service/api/participation-api";

import { loadParticipationStatusAction, setParticipationStatusAction, updateParticipationStatusAction } from "./participation.action";

@Injectable()
export class ParticipationEffects {
  readonly loadParticipationStatus = createEffect(() => this.actions$
    .pipe(ofType(loadParticipationStatusAction))
    .pipe(switchMap(() =>
                      this.participationApi.getParticipation()
                        .pipe(map(dto => {
                          this.log.info("Successfully loaded participation status:", dto);

                          return setParticipationStatusAction({dto});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not load participation status", err);

                          return EMPTY;
                        })))));

  readonly updateParticipationStatus = createEffect(() => this.actions$
    .pipe(ofType(updateParticipationStatusAction))
    .pipe(switchMap(action =>
                      this.participationApi.updateParticipation(action.command)
                        .pipe(map(dto => {
                          this.log.info("Successfully updated participation status:", dto);

                          return setParticipationStatusAction({dto});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not update participation status", err);

                          return EMPTY;
                        })))));

  private readonly log = getLogger("ParticipationEffects");

  constructor(
    private readonly actions$: Actions,
    private readonly participationApi: ParticipationApi,
  ) {
  }
}
