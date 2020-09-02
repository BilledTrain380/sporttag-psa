import { Action, createReducer, on } from "@ngrx/store";

import { ParticipationDto } from "../../dto/participation";
import { StatusSeverity } from "../../dto/status";

import { setParticipationStatusAction } from "./participation.action";

export interface ParticipationState {
  readonly dto: ParticipationDto;
}

const initialState: ParticipationState = {
  dto: {
    status: {
      severity: StatusSeverity.INFO,
      entries: [],
    },
    unfinishedGroups: [],
  },
};

const reducer = createReducer(
  initialState,
  on(setParticipationStatusAction, (state, action) => ({
    ...state,
    dto: action.dto,
  })),
);

export function participationReducer(state: ParticipationState | undefined, action: Action): ParticipationState {
  return reducer(state, action);
}
