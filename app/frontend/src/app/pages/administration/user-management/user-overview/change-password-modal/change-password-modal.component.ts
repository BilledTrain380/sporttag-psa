import { Component, OnInit } from "@angular/core";
import { FormControl } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

import { requireNonNullOrUndefined } from "../../../../../@core/lib/lib";
import { Alert } from "../../../../../modules/alert/alert";
import { psaPasswordValidator } from "../../../../../modules/common-forms/validatiors/password-validators";
import { AbstractSubmitModalComponent } from "../../../../../modules/modal/submit-modal/abstract-submit-modal.component";
import { updateUserPasswordAction, UpdateUserPasswordProps } from "../../../../../store/user-management/user-management.action";
import { selectUserManagementAlert } from "../../../../../store/user-management/user-management.selector";
import { UserModel } from "../user-model";

@Component({
             selector: "app-change-password-modal",
             templateUrl: "./change-password-modal.component.html",
           })
export class ChangePasswordModalComponent extends AbstractSubmitModalComponent implements OnInit {
  user?: UserModel;

  get title(): string {
    return $localize`Change password of ` + (this.user?.username || "");
  }

  readonly passwordControl = new FormControl("", psaPasswordValidator);

  protected readonly alert$: Observable<Alert | undefined> = this.store.select(selectUserManagementAlert);

  constructor(
    activeModal: NgbActiveModal,
    private readonly store: Store,
  ) {
    super(activeModal);
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  submit(): void {
    const user: UpdateUserPasswordProps = {
      userId: requireNonNullOrUndefined(this.user?.userId),
      user: {
        password: requireNonNullOrUndefined(this.passwordControl.value),
      },
    };

    this.store.dispatch(updateUserPasswordAction(user));
  }
}
