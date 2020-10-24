import { AbstractControl, ValidationErrors } from "@angular/forms";

export const MIN_PASS_LENGTH = 8;
export const MAX_PASS_LENGTH = 64;

export function psaPasswordValidator(control: AbstractControl): ValidationErrors | null {
  const value: string | undefined = control.value;

  if (!value) {
    // tslint:disable-next-line:no-null-keyword
    return null;
  }

  const errors: ValidationErrors = {
    psaPassword: {},
  };

  if (value.length < MIN_PASS_LENGTH || value.length > MAX_PASS_LENGTH) {
    errors.psaPassword.length = $localize`Password must be between 8 and 64 characters`;
  }

  if (!value.match(/[A-Z]+/)) {
    errors.psaPassword.upperCase = $localize`Password must contain an upper case character`;
  }

  if (!value.match(/[a-z]+/)) {
    errors.psaPassword.lowerCase = $localize`Password must contain a lower case character`;
  }

  if (!value.match(/\W+/)) {
    errors.psaPassword.special = $localize`Password must contain a special character`;
  }

  if (!value.match(/\d+/)) {
    errors.psaPassword.digit = $localize`Password must contain a digit`;
  }

  if (value.match(/\s/)) {
    errors.psaPassword.whitespace = $localize`Password must not contain any whitespace character`;
  }

  if (Object.keys(errors.psaPassword).length > 0) {
    return errors;
  }

  // tslint:disable-next-line:no-null-keyword
  return null;
}
