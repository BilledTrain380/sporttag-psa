import { Action, createReducer, on } from "@ngrx/store";

import { ANONYMOUS } from "../../@security/auth-constants";

import { setUser } from "./user.action";

export interface UserState {
  readonly username: string;
  readonly authorities: ReadonlyArray<string>;
  readonly locale: string;
}

const initialState: UserState = {
  username: ANONYMOUS,
  authorities: [],
  locale: "en",
};

const reducer = createReducer(
  initialState,
  on(setUser, (state, action) => (
    {
      ...state,
      username: action.username,
      authorities: action.authorities,
      locale: action.locale,
    }
  )),
);

export function userReducer(state: UserState | undefined, action: Action): UserState {
  return reducer(state, action);
}
