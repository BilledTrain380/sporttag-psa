import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY, of } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger } from "../../@core/logging";
import { UserApi } from "../../@core/service/api/user-api";
import { AlertFactory } from "../../modules/alert/alert";

import {
  addUserAction,
  AddUserProps,
  addUserToStateAction,
  loadUsersAction,
  setUserManagementAlertAction,
  setUsersAction,
  updateUserAction,
  updateUserPasswordAction,
  UpdateUserPasswordProps,
  UpdateUserProps,
} from "./user-management.action";

@Injectable()
export class UserManagementEffects {
  readonly loadUsers$ = createEffect(() => this.actions$
    .pipe(ofType(loadUsersAction.type))
    .pipe(switchMap(() =>
                      this.userApi.getUsers()
                        .pipe(map(users => {
                          this.log.info("Successfully loaded users: size =", users.length);

                          return setUsersAction({users});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not load users", err);

                          return EMPTY;
                        })),
    )));

  readonly addUser$ = createEffect(() => this.actions$
    .pipe(ofType(addUserAction.type))
    .pipe(switchMap((action: AddUserProps) => {
                      const textAlert = this.alertFactory.textAlert();

                      return this.userApi.createUser(action.user)
                        .pipe(switchMap(user => {
                          this.log.info("Successfully created user: id =", user.id);

                          const alert = textAlert.success($localize`Successfully added user`);

                          return [
                            addUserToStateAction({user}),
                            setUserManagementAlertAction({alert}),
                          ];
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could add user", err);

                          const alert = textAlert.error($localize`Could not add user`);

                          return of(setUserManagementAlertAction({alert}));
                        }));
                    },
    )));

  readonly updateUser$ = createEffect(() => this.actions$
    .pipe(ofType(updateUserAction.type))
    .pipe(switchMap((action: UpdateUserProps) => {
                      const textAlert = this.alertFactory.textAlert();

                      return this.userApi.updateUserElement(action.userId, action.user)
                        .pipe(map(() => {
                          this.log.info("Successfully updated user: id =", action.userId);

                          const alert = textAlert.success($localize`Successfully updated user`);

                          return setUserManagementAlertAction({alert});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not update user: id =", action.userId, err);

                          const alert = textAlert.error($localize`Could not update user`);

                          return of(setUserManagementAlertAction({alert}));
                        }));
                    },
    )));

  readonly updateUserPassword$ = createEffect(() => this.actions$
    .pipe(ofType(updateUserPasswordAction.type))
    .pipe(switchMap((action: UpdateUserPasswordProps) => {
                      const textAlert = this.alertFactory.textAlert();

                      return this.userApi.updateUserRelation(action.userId, action.user)
                        .pipe(map(() => {
                          this.log.info("Successfully updated user password: id =", action.userId);

                          const alert = textAlert.success($localize`Successfully updated user password`);

                          return setUserManagementAlertAction({alert});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not update user: id =", action.userId, err);

                          const alert = textAlert.error($localize`Could not update user password`);

                          return of(setUserManagementAlertAction({alert}));
                        }));
                    },
    )));

  private readonly log = getLogger("UserManagementEffects");

  constructor(
    private readonly actions$: Actions,
    private readonly userApi: UserApi,
    private readonly alertFactory: AlertFactory,
  ) {
  }
}
