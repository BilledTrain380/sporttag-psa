import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { TextAlertComponent } from "./text-alert/text-alert.component";

const COMPONENTS = [
  TextAlertComponent,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
            ],
            exports: [...COMPONENTS],
          })
export class AlertModule {
}
