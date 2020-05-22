import { Component, Input } from "@angular/core";
import { AbstractControl } from "@angular/forms";

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
    return this.control?.touched && this.control.errors && this.control.errors[this.errorKey];
  }
}
