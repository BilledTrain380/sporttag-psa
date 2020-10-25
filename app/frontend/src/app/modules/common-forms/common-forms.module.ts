import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbDatepickerModule } from "@ng-bootstrap/ng-bootstrap";

import { ParticipantModule } from "../participant/participant.module";

import { DateInputComponent } from "./date-input/date-input.component";
import { FormHintRequiredDirective } from "./form-hint-required.directive";
import { FormHintDirective } from "./form-hint.directive";
import { GenderInputComponent } from "./gender-input/gender-input.component";
import { InputAutoFocusDirective } from "./input-auto-focus.directive";
import { InputValidationDirective } from "./input-validation.directive";
import { PasswordInputComponent } from "./password-input/password-input.component";
import { PasswordPolicyFeedbackComponent } from "./password-policy-feedback/password-policy-feedback.component";
import { PasswordPolicyComponent } from "./password-policy/password-policy.component";
import { PreNextSelectComponent } from "./pre-next-select/pre-next-select.component";
import { SportTypeInputComponent } from "./sport-type-input/sport-type-input.component";

const COMPONENTS = [
  GenderInputComponent,
  DateInputComponent,
  InputValidationDirective,
  SportTypeInputComponent,
  FormHintDirective,
  FormHintRequiredDirective,
  PreNextSelectComponent,
  InputAutoFocusDirective,
  PasswordPolicyComponent,
  PasswordInputComponent,
  PasswordPolicyFeedbackComponent,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
              ReactiveFormsModule,
              NgbDatepickerModule,
              FontAwesomeModule,
              ParticipantModule,
            ],
            exports: [...COMPONENTS],
          })
export class CommonFormsModule {
}
