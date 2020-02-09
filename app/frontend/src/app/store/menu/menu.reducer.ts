import { Action, createReducer } from "@ngrx/store";

export interface MenuState {
  readonly parent?: string;
  readonly self: string;
}

const initialState: MenuState = {
  self: "home",
};

const reducer = createReducer(
  initialState,
  // TODO: Add on actions
);

export function menuReducer(state: MenuState | undefined, action: Action): MenuState {
  return reducer(state, action);
}
