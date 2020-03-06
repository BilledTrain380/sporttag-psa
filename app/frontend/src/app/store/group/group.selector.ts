import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

import { GroupState } from "./group.reducer";

export const selectGroup = (state: AppState) => state.group;

export const selectGroups = createSelector(
  selectGroup,
  (state: GroupState) => state.groups,
);
