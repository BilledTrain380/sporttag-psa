import { Component, Input } from "@angular/core";

@Component({
             selector: "app-password-policy-feedback",
             templateUrl: "./password-policy-feedback.component.html",
           })
export class PasswordPolicyFeedbackComponent {

  @Input()
  set violatedPolicies(value: object) {
    this.violatedPolicyList = Object.values(value);
  }

  violatedPolicyList: ReadonlyArray<string> = [];
}
