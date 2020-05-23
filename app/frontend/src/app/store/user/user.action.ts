import { createAction, props } from "@ngrx/store";

export interface PropsLoginSuccess {
  readonly username: string;
  readonly authorities: ReadonlyArray<string>;
}

export const loginSuccess = createAction(
  "[Authentication] Login Success",
  props<PropsLoginSuccess>(),
);

export const logout = createAction(
  "[Authentication] Logout",
);
