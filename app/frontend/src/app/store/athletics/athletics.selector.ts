import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectAthletics = (state: AppState) => state.athletics;

export const selectParticipationStatus = createSelector(
  selectAthletics,
  athletics => athletics.participationStatus,
);

export const selectGroups = createSelector(
  selectAthletics,
  athletics => athletics.groups,
);

export const selectCompetitors = createSelector(
  selectAthletics,
  athletics => athletics.competitors,
);

export const selectAthleticsAlert = createSelector(
  selectAthletics,
  athletics => athletics.alert,
);
