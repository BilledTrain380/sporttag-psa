import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

import { GroupState } from "./group.reducer";

export const selectGroup = (state: AppState) => state.group;

export const selectOverviewGroups = createSelector(
  selectGroup,
  (state: GroupState) => state.overviewGroups,
);

export const selectImportAlert = createSelector(
  selectGroup,
  (state: GroupState) => state.importAlert,
);

export const selectActiveGroup = createSelector(
  selectGroup,
  (state: GroupState) => state.activeGroup,
);

export const selectActiveGroupAlert = createSelector(
  selectGroup,
  (state: GroupState) => state.activeGroup?.alert,
);
