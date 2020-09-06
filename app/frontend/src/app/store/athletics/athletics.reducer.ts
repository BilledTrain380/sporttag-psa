import { Action, createReducer, on } from "@ngrx/store";

import { CompetitorDto, CompetitorDtoBuilder } from "../../dto/athletics";

import { setCompetitorsAction, updateCompetitorRelationAction } from "./athletics.action";

export interface AthleticsState {
  readonly competitors: ReadonlyArray<CompetitorDto>;
}

const initialState: AthleticsState = {
  competitors: [],
};

const reducer = createReducer(
  initialState,
  on(setCompetitorsAction, ((state, action) => (
    {
      ...state,
      competitors: action.competitors,
    }
  ))),
  on(updateCompetitorRelationAction, ((state, action) => {
    const competitors = state.competitors
      .map(competitor => {
        if (competitor.id === action.competitorId) {
          const builder = CompetitorDtoBuilder.newBuilder(competitor);
          action.results.forEach(result => builder.setResult(result));

          return builder.build();
        }

        return competitor;
      });

    return {
      ...state,
      competitors,
    };
  })),
);

export function athleticsReducer(state: AthleticsState | undefined, action: Action): AthleticsState {
  return reducer(state, action);
}
