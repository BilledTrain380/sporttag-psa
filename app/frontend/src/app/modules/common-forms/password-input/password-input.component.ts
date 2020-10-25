import { Component, ElementRef, Optional, Self, ViewChild } from "@angular/core";
import { FormControl, NgControl, ValidationErrors } from "@angular/forms";
import { faEye } from "@fortawesome/free-solid-svg-icons/faEye";
import { faEyeSlash } from "@fortawesome/free-solid-svg-icons/faEyeSlash";

import { AbstractFormInput } from "../abstract-form-input";

@Component({
             selector: "app-password-input",
             templateUrl: "./password-input.component.html",
           })
export class PasswordInputComponent extends AbstractFormInput<string> {
  @ViewChild("passwordInput", {static: false})
  passwordInput?: ElementRef;

  readonly faEye = faEye;
  readonly faEyeLash = faEyeSlash;

  isShowPassword = false;

  protected val = "";

  constructor(
    @Self() @Optional() private readonly controlDirective: NgControl,
  ) {
    super();
    this.controlDirective.valueAccessor = this;
    this.formControl.setValidators(this.validate.bind(this));
  }

  toggleShowPassword(): void {
    this.isShowPassword = !this.isShowPassword;

    if (this.passwordInput) {
      this.passwordInput.nativeElement.type = this.isShowPassword ? "text" : "password";
    }
  }

  private validate(value: FormControl): ValidationErrors | null {
    if (this.controlDirective.control?.validator) {
      return this.controlDirective.control.validator(value);
    }

    // tslint:disable-next-line:no-null-keyword
    return null;
  }
}
