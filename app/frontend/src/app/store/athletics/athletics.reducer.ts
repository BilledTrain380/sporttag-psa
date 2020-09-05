import { Action, createReducer, on } from "@ngrx/store";

import { CompetitorDto } from "../../dto/athletics";

import { setCompetitorsAction } from "./athletics.action";

export interface AthleticsState {
  readonly competitors: ReadonlyArray<CompetitorDto>;
}

const initialState: AthleticsState = {
  competitors: [],
};

const reducer = createReducer(
  initialState,
  on(setCompetitorsAction, ((_, action) => (
    {
      competitors: action.competitors,
    }
  ))),
);

export function athleticsReducer(state: AthleticsState | undefined, action: Action): AthleticsState {
  return reducer(state, action);
}
