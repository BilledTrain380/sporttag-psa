import { FormControl } from "@angular/forms";

import { floatNumberValidator, intNumberValidator } from "./number-validators";

describe("NumberValidators", () => {
  describe("on float number validator", () => {
    it("should return null when input is valid", () => {
      const control = new FormControl("2.43");

      const errors = floatNumberValidator()
        .call(undefined, control);

      expect(errors)
        .withContext("Float number errors")
        .toBeNull();
    });

    it("should return errors NaN when input is not a number", () => {
      const control = new FormControl("not a number");

      const errors = floatNumberValidator()
        .call(undefined, control);

      expect(errors.floatNumber)
        .withContext("Float number errors")
        .toBeDefined();
    });

    it("should return errors when input does not match the parsed number", () => {
      // Number.parseFloat will still parse this, but cuts away the .flubber part
      const control = new FormControl("11.flubber");

      const errors = floatNumberValidator()
        .call(undefined, control);

      expect(errors.floatNumber)
        .withContext("Float number errors")
        .toBeDefined();
    });

    it("should return errors when precision does not match", () => {
      const control = new FormControl("2.2456");

      // tslint:disable-next-line:no-magic-numbers
      const errors = floatNumberValidator(2)
        .call(undefined, control);

      expect(errors.floatNumber)
        .withContext("Float number errors")
        .toBeDefined();
    });
  });

  describe("on int number validator", () => {
    it("should return null when the input is valid", () => {
      const control = new FormControl("15");

      const errors = intNumberValidator()
        .call(undefined, control);

      expect(errors)
        .withContext("Int number errors")
        .toBeNull();
    });

    it("should return errors NaN when input is not a number", () => {
      const control = new FormControl("not a number");

      const errors = intNumberValidator()
        .call(undefined, control);

      expect(errors.intNumber)
        .withContext("Int number errors")
        .toBeDefined();
    });

    it("should return errors when a float is given", () => {
      const control = new FormControl("15.486");

      // tslint:disable-next-line:no-magic-numbers
      const errors = intNumberValidator()
        .call(undefined, control);

      expect(errors.intNumber)
        .withContext("Int number errors")
        .toBeDefined();
    });
  });
});
