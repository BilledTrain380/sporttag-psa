import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectAthletics = (state: AppState) => state.athletics;

export const selectGroups = createSelector(
  selectAthletics,
  athletics => athletics.groups,
);

export const selectCompetitors = createSelector(
  selectAthletics,
  athletics => athletics.competitors,
);