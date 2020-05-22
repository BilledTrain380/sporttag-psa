import { FormControl } from "@angular/forms";

import { FILE_SIZE_1KB, FILE_SIZE_1MB } from "../lib/files";

import { FileValidators } from "./form-validators";

describe("FormValidators", () => {
  describe("FileValidators", () => {
    describe("on max files size validation", () => {
      it("should return null when file size is smaller than max file size", () => {
        const file = new File([], "file");

        spyOnProperty(file, "size")
          .and
          .returnValue(FILE_SIZE_1KB);

        const control = new FormControl(file);

        const validatorFn = FileValidators.maxFileSize(FILE_SIZE_1MB);

        expect(validatorFn(control))
          .toBeNull("Expected no validation errors");
      });

      it("should return null when no file is given at all", () => {
        const control = new FormControl();

        const validatorFn = FileValidators.maxFileSize(FILE_SIZE_1MB);

        expect(validatorFn(control))
          .toBeNull("Expected no validation errors");
      });

      it("should return validation errors when given file size is greater than max file size", () => {
        const file = new File([], "file");

        spyOnProperty(file, "size")
          .and
          .returnValue(FILE_SIZE_1MB);

        const control = new FormControl(file);

        const validatorFn = FileValidators.maxFileSize(FILE_SIZE_1KB);

        const errors = validatorFn(control);
        expect(errors)
          .toBeDefined();
        expect(errors?.maxFileSize)
          .withContext("Expected validation errors")
          .toBeTrue();
      });
    });

    describe("on accepts file extension validation", () => {
      it("should return null when file extension matches any of the allowed extensions", () => {
        const file = new File([], "file.txt");

        const control = new FormControl(file);

        const validatorFn = FileValidators.accepts("txt", "xml");

        expect(validatorFn(control))
          .toBeNull("Expected no validation errors");
      });

      it("should ignore case sensitive", () => {
        const file = new File([], "file.XML");

        const control = new FormControl(file);

        const validatorFn = FileValidators.accepts("txt", "xml");

        expect(validatorFn(control))
          .toBeNull("Expected no validation errors");
      });

      it("should return null when no file is given at all", () => {
        const control = new FormControl();

        const validatorFn = FileValidators.accepts("txt", "xml");

        expect(validatorFn(control))
          .toBeNull("Expected no validation errors");
      });

      it("should return validation errors when file extension does not match", () => {
        const file = new File([], "file.html");

        const control = new FormControl(file);

        const validatorFn = FileValidators.accepts("txt", "xml");

        const errors = validatorFn(control);
        expect(errors)
          .toBeDefined();
        expect(errors?.accepts)
          .withContext("Expected validation errors")
          .toBeTrue();
      });
    });
  });
});
