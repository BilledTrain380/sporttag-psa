import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbDatepickerModule } from "@ng-bootstrap/ng-bootstrap";

import { DateInputComponent } from "./date-input/date-input.component";
import { GenderInputComponent } from "./gender-input/gender-input.component";
import { InputValidationDirective } from "./input-validation.directive";

const COMPONENT = [
  GenderInputComponent,
  DateInputComponent,
  InputValidationDirective,
];

@NgModule({
            declarations: [...COMPONENT],
            imports: [
              CommonModule,
              ReactiveFormsModule,
              NgbDatepickerModule,
              FontAwesomeModule,
            ],
            exports: [...COMPONENT],
          })
export class CommonFormsModule {
}
