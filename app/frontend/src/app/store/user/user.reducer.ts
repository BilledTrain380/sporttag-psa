import { Action, createReducer, on } from "@ngrx/store";

import { ANONYMOUS } from "../../@core/auth/auth-constants";

import { setUser } from "./user.action";

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
  on(setUser, (state, action) => (
    {
      ...state,
      username: action.username,
      authorities: action.authorities,
    }
  )),
);

export function userReducer(state: UserState | undefined, action: Action): UserState {
  return reducer(state, action);
}
