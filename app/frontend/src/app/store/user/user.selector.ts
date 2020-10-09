import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectUser = (state: AppState) => state.user;

export const selectUsername = createSelector(
  selectUser,
  state => state.username,
);
