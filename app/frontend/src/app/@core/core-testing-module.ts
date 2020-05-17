import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { LoggerTestingModule } from "ngx-logger/testing";

import { CoreModule } from "./core.module";

const MODULES = [
  CommonModule,
  CoreModule,
  NoopAnimationsModule,
  FontAwesomeModule,
  LoggerTestingModule,
];

@NgModule({
            imports: [...MODULES],
            exports: [...MODULES],
          })
export class CoreTestingModule {
}
