import { Component, Input } from "@angular/core";
import { AbstractControl } from "@angular/forms";

import { isNullOrUndefined } from "../../../@core/lib/lib";

@Component({
             selector: "app-drop-error",
             templateUrl: "./drop-error.component.html",
           })
export class DropErrorComponent {

  @Input()
  control?: AbstractControl;

  @Input()
  errorKey = "";

  isInvalid(): boolean {
    if (!this.control?.touched) {
      return false;
    }

    if (isNullOrUndefined(this.control?.errors)) {
      return false;
    }

    return this.control?.errors![this.errorKey] !== undefined;
  }
}
