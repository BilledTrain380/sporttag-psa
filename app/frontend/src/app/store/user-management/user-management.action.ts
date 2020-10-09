import { createAction, props } from "@ngrx/store";

import { UserDto, UserElement } from "../../dto/user";
import { Alert } from "../../modules/alert/alert";

export const loadUsersAction = createAction(
  "[UserOverview] Load users",
);

export interface SetUsersProps {
  readonly users: ReadonlyArray<UserDto>;
}

export const setUsersAction = createAction(
  "[UserEffects] Set users",
  props<SetUsersProps>(),
);

export interface UpdateUserProps {
  readonly userId: number;
  readonly user: UserElement;
}

export const updateUserAction = createAction(
  "[UserOverview] Update user",
  props<UpdateUserProps>(),
);

export interface SetUserAlertProps {
  readonly alert: Alert;
}

export const setUserManagementAlertAction = createAction(
  "[UserEffects] Set user management alert",
  props<SetUserAlertProps>(),
);

export const clearUserManagementAlertAction = createAction(
  "[UserOverview] Clear user management alert",
);
