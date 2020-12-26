import { AbstractControl, ValidatorFn } from "@angular/forms";

export function floatNumberValidator(precision?: number): ValidatorFn {
  return (control: AbstractControl) => {
    const value: string = control.value;
    const parsedNumber = Number.parseFloat(value);

    if (Number.isNaN(parsedNumber)) {
      return {floatNumber: {nan: true}};
    }

    if (value.match("[^\\d\\.]")) {
      return {floatNumber: {nan: true}};
    }

    if (precision && parsedNumber.toFixed(precision) !== value) {
      return {floatNumber: {precision: true}};
    }

    // tslint:disable-next-line:no-null-keyword
    return null;
  };
}

export function intNumberValidator(radix: number = 10): ValidatorFn {
  return (control: AbstractControl) => {
    const parsedNumber = Number.parseInt(control.value, radix);

    if (Number.isNaN(parsedNumber)) {
      return {intNumber: {nan: true}};
    }

    if (parsedNumber.toString() !== control.value) {
      return {intNumber: {integer: true}};
    }

    // tslint:disable-next-line:no-null-keyword
    return null;
  };
}
