import { ValidatorFn } from "@angular/forms";

// tslint:disable-next-line:no-unnecessary-class
export class FileValidators {
  static accepts(...extensions: ReadonlyArray<string>): ValidatorFn {
    return control => {
      const fileName = (control.value as File)?.name;
      const anyMatch = extensions.some(extension =>
                                         fileName
                                           ?.toLocaleLowerCase()
                                           ?.endsWith(extension));

      if (fileName === undefined || anyMatch) {
        // tslint:disable-next-line:no-null-keyword
        return null;
      }

      return {accepts: true};
    };
  }

  static maxFileSize(bytes: number): ValidatorFn {
    return control => {
      const size = (control.value as File)?.size;

      if (size !== undefined && size > bytes) {
        return {maxFileSize: true};
      }

      // tslint:disable-next-line:no-null-keyword
      return null;
    };
  }

  private constructor() {
  }
}
