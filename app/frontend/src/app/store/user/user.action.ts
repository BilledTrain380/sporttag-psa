import { createAction, props } from "@ngrx/store";

export interface PropsLoginSuccess {
  readonly username: string;
  readonly authorities: ReadonlyArray<string>;
}

export const setUser = createAction(
  "[Authentication] Set user",
  props<PropsLoginSuccess>(),
);

export const logout = createAction(
  "[Authentication] Logout",
);
