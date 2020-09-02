import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectParticipation = (state: AppState) => state.participation;

export const selectParticipationStatus = createSelector(
  selectParticipation,
  participation => participation.dto,
);
