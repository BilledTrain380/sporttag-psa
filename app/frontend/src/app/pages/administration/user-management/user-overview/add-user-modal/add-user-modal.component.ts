import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

import { FormControlsObject } from "../../../../../@core/forms/form-util";
import { requireNonNullOrUndefined } from "../../../../../@core/lib/lib";
import { UserInput } from "../../../../../dto/user";
import { Alert } from "../../../../../modules/alert/alert";
import { psaPasswordValidator } from "../../../../../modules/common-forms/validatiors/password-validators";
import { AbstractSubmitModalComponent } from "../../../../../modules/modal/submit-modal/abstract-submit-modal.component";
import { addUserAction } from "../../../../../store/user-management/user-management.action";
import { selectUserManagementAlert } from "../../../../../store/user-management/user-management.selector";

@Component({
             selector: "app-add-user-modal",
             templateUrl: "./add-user-modal.component.html",
             styleUrls: ["./add-user-modal.component.scss"],
           })
export class AddUserModalComponent extends AbstractSubmitModalComponent implements OnInit {
  form?: FormGroup;
  readonly formControls: FormControlsObject = {
    username: "username",
    password: "password",
    enabled: "enabled",
  };

  protected readonly alert$: Observable<Alert | undefined> = this.store.select(selectUserManagementAlert);

  constructor(
    modal: NgbActiveModal,
    private readonly store: Store,
    private readonly formBuilder: FormBuilder,
  ) {
    super(modal);
  }

  ngOnInit(): void {
    super.ngOnInit();

    this.form = this.formBuilder.group({
                                         [this.formControls.username]: ["", Validators.required],
                                         [this.formControls.password]: ["", [Validators.required, psaPasswordValidator]],
                                         [this.formControls.enabled]: true,
                                       });
  }

  violatedPoliciesAsList(policy: object): ReadonlyArray<string> {
    return Object.values(policy);
  }

  submit(): void {
    const user: UserInput = {
      username: requireNonNullOrUndefined(this.form?.value[this.formControls.username]),
      password: requireNonNullOrUndefined(this.form?.value[this.formControls.password]),
      enabled: requireNonNullOrUndefined(this.form?.value[this.formControls.enabled]),
    };

    this.store.dispatch(addUserAction({user}));
  }
}
