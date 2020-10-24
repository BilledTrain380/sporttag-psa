import { FormControl } from "@angular/forms";

import { psaPasswordValidator } from "./password-validators";

describe("PSA Password validator", () => {
  const CONTEXT = "Validation errors";

  it("should return null when input is valid", () => {
    const control = new FormControl("gibbiX12345$");

    const errors = psaPasswordValidator(control);

    expect(errors)
      .withContext(CONTEXT)
      .toBeNull();
  });

  it("should return null when no value is given", () => {
    const control = new FormControl();

    const errors = psaPasswordValidator(control);

    expect(errors)
      .withContext(CONTEXT)
      .toBeNull();
  });

  it("should return errors when input is invalid", () => {
    const control = new FormControl("$");

    const errors = psaPasswordValidator(control);

    expect(errors?.psaPassword)
      .withContext(CONTEXT)
      .not
      .toBeNull();

    expect(errors?.psaPassword.length)
      .toBe("Password must be between 8 and 64 characters");

    expect(errors?.psaPassword.upperCase)
      .toBe("Password must contain an upper case character");

    expect(errors?.psaPassword.lowerCase)
      .toBe("Password must contain a lower case character");

    expect(errors?.psaPassword.special)
      .withContext("Special character validation")
      .toBeUndefined();

    expect(errors?.psaPassword.digit)
      .toBe("Password must contain a digit");

    expect(errors?.psaPassword.whitespace)
      .withContext("White space validation")
      .toBeUndefined();
  });

  it("should return errors when input does not contain any special character", () => {
    const control = new FormControl("foo");

    const errors = psaPasswordValidator(control);

    expect(errors?.psaPassword)
      .withContext(CONTEXT)
      .not
      .toBeNull();

    expect(errors?.psaPassword.special)
      .toBe("Password must contain a special character");
  });

  it("should return errors when input contains a whitespace character", () => {
    const control = new FormControl("gibbiX1234 5$");

    const errors = psaPasswordValidator(control);

    expect(errors?.psaPassword)
      .withContext(CONTEXT)
      .not
      .toBeNull();

    expect(errors?.psaPassword.whitespace)
      .toBe("Password must not contain any whitespace character");
  });
});
