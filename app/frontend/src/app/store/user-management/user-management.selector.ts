import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectUserManagement = (state: AppState) => state.userManagement;

export const selectUsers = createSelector(
  selectUserManagement,
  state => state.users,
);

export const selectUserManagementAlert = createSelector(
  selectUserManagement,
  state => state.userAlert,
);
