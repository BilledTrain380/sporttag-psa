import { createAction, props } from "@ngrx/store";

export interface LoginSuccessProps {
  readonly username: string;
  readonly authorities: ReadonlyArray<string>;
  readonly locale: string;
}

export const setUserAction = createAction(
  "[Authentication] Set user",
  props<LoginSuccessProps>(),
);

export const logoutAction = createAction(
  "[Authentication] Logout",
);
