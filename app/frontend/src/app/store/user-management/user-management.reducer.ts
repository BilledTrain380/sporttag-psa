import { Action, createReducer, on } from "@ngrx/store";

import { UserDto } from "../../dto/user";
import { Alert } from "../../modules/alert/alert";

import {
  addUserToStateAction,
  clearUserManagementAlertAction,
  setUserManagementAlertAction,
  setUsersAction,
} from "./user-management.action";

export interface UserManagementState {
  readonly users: ReadonlyArray<UserDto>;
  readonly userAlert?: Alert;
}

const initialState: UserManagementState = {
  users: [],
};

const reducer = createReducer(
  initialState,
  on(setUsersAction, (state, action) => (
    {
      ...state,
      users: action.users,
    }
  )),
  on(addUserToStateAction, (state, action) => {
    const users = [...state.users, action.user];

    return {
      ...state,
      users,
    };
  }),
  on(setUserManagementAlertAction, (state, action) => (
    {
      ...state,
      userAlert: action.alert,
    }
  )),
  on(clearUserManagementAlertAction, state => (
    {
      ...state,
      userAlert: undefined,
    }
  )),
);

export function userManagementReducer(state: UserManagementState | undefined, action: Action): UserManagementState {
  return reducer(state, action);
}
