import { createAction, props } from "@ngrx/store";

export const loginSuccess = createAction(
  "[Authentication] Login Success",
  props<{ username: string; authorities: Array<string> }>(),
);

export const logout = createAction(
  "[Authentication] Logout",
);
