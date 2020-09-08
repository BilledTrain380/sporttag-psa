import { AbstractControl, ValidatorFn } from "@angular/forms";

export function floatNumber(precision?: number): ValidatorFn {
  return (control: AbstractControl) => {
    const parsedNumber = Number.parseFloat(control.value);

    if (Number.isNaN(parsedNumber)) {
      return {floatNumber: {nan: true}};
    }

    if (precision && parsedNumber.toFixed(precision) !== control.value) {
      return {floatNumber: {precision: true}};
    }

    if (parsedNumber.toString() !== control.value) {
      return {floatNumber: {floatNumber: true}};
    }

    // tslint:disable-next-line:no-null-keyword
    return null;
  };
}

export function intNumber(radix: number = 10): ValidatorFn {
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
