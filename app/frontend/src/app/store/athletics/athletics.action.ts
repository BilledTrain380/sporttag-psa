import { createAction, props } from "@ngrx/store";

import { CompetitorDto } from "../../dto/athletics";

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
