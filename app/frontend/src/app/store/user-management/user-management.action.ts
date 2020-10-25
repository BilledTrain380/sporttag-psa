import { createAction, props } from "@ngrx/store";

import { UserDto, UserElement, UserInput } from "../../dto/user";
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

export interface AddUserProps {
  readonly user: UserInput;
}

export const addUserAction = createAction(
  "[AddUserModal] Add user",
  props<AddUserProps>(),
);

export interface AddUserToStoreProps {
  readonly user: UserDto;
}

export const addUserToStateAction = createAction(
  "[UserManagementEffects] Add user to state",
  props<AddUserToStoreProps>(),
);

export interface UpdateUserProps {
  readonly userId: number;
  readonly user: UserElement;
}

export const updateUserAction = createAction(
  "[UserOverview] Update user",
  props<UpdateUserProps>(),
);

export interface SetUserManagementAlertProps {
  readonly alert: Alert;
}

export const setUserManagementAlertAction = createAction(
  "[UserEffects] Set user management alert",
  props<SetUserManagementAlertProps>(),
);

export const clearUserManagementAlertAction = createAction(
  "[UserOverview] Clear user management alert",
);
