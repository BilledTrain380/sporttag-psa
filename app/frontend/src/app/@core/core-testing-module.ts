import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";

import { CoreModule } from "./core.module";

const MODULES = [
  CommonModule,
  CoreModule,
  NoopAnimationsModule,
  FontAwesomeModule,
];

@NgModule({
            imports: [...MODULES],
            exports: [...MODULES],
          })
export class CoreTestingModule {
}
