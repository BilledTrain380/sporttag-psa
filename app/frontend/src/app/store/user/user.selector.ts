import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

import { UserState } from "./user.reducer";

export const selectUser = (state: AppState) => state.user;

export const selectUsername = createSelector(
  selectUser,
  (state: UserState) => state.username,
);
