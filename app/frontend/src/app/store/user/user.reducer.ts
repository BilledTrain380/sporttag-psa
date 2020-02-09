import { Action, createReducer } from "@ngrx/store";

export interface UserState {
  readonly username: string;
  readonly token: string;
}

const initialState: UserState = {
  username: "anonymous",
  token: "",
};

const reducer = createReducer(
  initialState,
  // TOOD add on actions
);

export function userReducer(state: UserState | undefined, action: Action): UserState {
  return reducer(state, action);
}
