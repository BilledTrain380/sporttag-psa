import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger, Logger } from "../../@core/logging";
import { CompetitorApi, CompetitorParameters } from "../../@core/service/api/competitor-api";

import { loadCompetitorsAction, setCompetitorsAction } from "./athletics.action";

@Injectable({
              providedIn: "root",
            })
export class AthleticsEffects {
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

  private readonly log: Logger = getLogger("AthleticsEffects");

  constructor(
    private readonly actions$: Actions,
    private readonly competitorApi: CompetitorApi,
  ) {
  }
}
