import { Action, createReducer, on } from "@ngrx/store";

import { loginSuccess, logout } from "./user.action";

export interface UserState {
  readonly username: string;
  readonly authorities: Array<string>;
}

const initialState: UserState = {
  username: "anonymous",
  authorities: [],
};

const reducer = createReducer(
  initialState,
  on(loginSuccess, ((_, action) => ({username: action.username, authorities: action.authorities}))),
  on(logout, (() => initialState)),
);

export function userReducer(state: UserState | undefined, action: Action): UserState {
  return reducer(state, action);
}
