import { Action, createReducer, on } from "@ngrx/store";

import { ANONYMOUS } from "../../@core/auth/auth-constants";

import { loginSuccess, logout } from "./user.action";

export interface UserState {
  readonly username: string;
  readonly authorities: ReadonlyArray<string>;
}

const initialState: UserState = {
  username: ANONYMOUS,
  authorities: [],
};

const reducer = createReducer(
  initialState,
  on(loginSuccess, ((_, action) => ({username: action.username, authorities: action.authorities}))),
  on(logout, (() => ({...initialState}))),
);

export function userReducer(state: UserState | undefined, action: Action): UserState {
  return reducer(state, action);
}
