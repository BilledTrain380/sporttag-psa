import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbDatepickerModule } from "@ng-bootstrap/ng-bootstrap";

import { DateInputComponent } from "./date-input/date-input.component";
import { FormHintRequiredDirective } from "./form-hint-required.directive";
import { FormHintDirective } from "./form-hint.directive";
import { GenderInputComponent } from "./gender-input/gender-input.component";
import { InputValidationDirective } from "./input-validation.directive";
import { SportTypeInputComponent } from "./sport-type-input/sport-type-input.component";

const COMPONENTS = [
  GenderInputComponent,
  DateInputComponent,
  InputValidationDirective,
  SportTypeInputComponent,
  FormHintDirective,
  FormHintRequiredDirective,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
              ReactiveFormsModule,
              NgbDatepickerModule,
              FontAwesomeModule,
            ],
            exports: [...COMPONENTS],
          })
export class CommonFormsModule {
}
