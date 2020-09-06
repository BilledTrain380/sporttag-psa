import { createAction, props } from "@ngrx/store";

import { CompetitorDto, ResultDto, ResultElement } from "../../dto/athletics";

export interface LoadCompetitorProps {
  readonly group: string;
}

export const loadCompetitorsAction = createAction(
  "[AthleticsPage] Load competitors",
  props<LoadCompetitorProps>(),
);

export interface SetCompetitorsProps {
  readonly competitors: ReadonlyArray<CompetitorDto>;
}

export const setCompetitorsAction = createAction(
  "[AthleticsPage] Set competitors",
  props<SetCompetitorsProps>(),
);

export interface UpdateCompetitorResultProps {
  readonly competitorId: number;
  readonly result: ResultElement;
}

export const updateCompetitorResultAction = createAction(
  "[AthleticsPage] Update competitor result",
  props<UpdateCompetitorResultProps>(),
);

export interface UpdateCompetitorRelationProps {
  readonly competitorId: number;
  readonly results: ReadonlyArray<ResultDto>;
}

export const updateCompetitorRelationAction = createAction(
  "[AthleticsEffects] Update competitor relation",
  props<UpdateCompetitorRelationProps>(),
);
