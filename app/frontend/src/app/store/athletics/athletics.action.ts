import { createAction, props } from "@ngrx/store";

import { CompetitorDto, ResultDto, ResultElement } from "../../dto/athletics";
import { SimpleGroupDto } from "../../dto/group";

export const loadGroupsAction = createAction(
  "[AthleticsPage] Load groups",
  props(),
);

export interface SetGroupsProps {
  readonly groups: ReadonlyArray<SimpleGroupDto>;
}

export const setGroupsAction = createAction(
  "[AthleticsEffects] Set groups",
  props<SetGroupsProps>(),
);

export interface LoadCompetitorsProps {
  readonly group: string;
}

export const loadCompetitorsAction = createAction(
  "[AthleticsPage] Load competitors",
  props<LoadCompetitorsProps>(),
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
