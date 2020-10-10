import { Component, OnDestroy, OnInit } from "@angular/core";
import { faPlusCircle } from "@fortawesome/free-solid-svg-icons/faPlusCircle";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

import { Timer } from "../../../../@core/lib/timer";
import { UserElement } from "../../../../dto/user";
import { Alert } from "../../../../modules/alert/alert";
import {
  clearUserManagementAlertAction,
  loadUsersAction,
  updateUserAction,
  UpdateUserProps,
} from "../../../../store/user-management/user-management.action";
import { selectUserManagementAlert, selectUsers } from "../../../../store/user-management/user-management.selector";

import { UserModel } from "./user-model";

@Component({
             selector: "app-user-overview",
             templateUrl: "./user-overview.component.html",
           })
export class UserOverviewComponent implements OnInit, OnDestroy {
  readonly faPlusCircle = faPlusCircle;
  readonly users$ = this.store.select(selectUsers)
    .pipe(map(users => users.map(user => UserModel.ofDto(user))));

  readonly alert$: Observable<Alert | undefined> = this.store.select(selectUserManagementAlert);

  private readonly updateUserEnabledTimer: Timer<UpdateUserProps> = Timer.ofHalfSecond();

  constructor(
    private readonly store: Store,
  ) {
  }

  ngOnInit(): void {
    this.store.dispatch(loadUsersAction());

    this.updateUserEnabledTimer.run$
      .subscribe(userProps => this.store.dispatch(updateUserAction(userProps)));
  }

  ngOnDestroy(): void {
    this.updateUserEnabledTimer.abort();
  }

  openAddUserModal(): void {
    throw new Error("Not implemented yet");
  }

  toggleEnabled(userModel: UserModel): void {
    this.clearAlert();

    const user: UserElement = {
      enabled: userModel.isEnabled,
    };

    const userProps: UpdateUserProps = {
      userId: userModel.userId,
      user,
    };

    this.updateUserEnabledTimer.trigger(userProps);
  }

  private clearAlert(): void {
    this.store.dispatch(clearUserManagementAlertAction());
  }
}
