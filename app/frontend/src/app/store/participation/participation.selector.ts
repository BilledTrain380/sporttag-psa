import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectParticipationState = (state: AppState) => state.participation;

export const selectParticipation = createSelector(
  selectParticipationState,
  participation => participation.dto,
);
