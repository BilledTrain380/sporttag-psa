import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { NgbAlertModule } from "@ng-bootstrap/ng-bootstrap";

import { TextAlertComponent } from "./text-alert/text-alert.component";

const COMPONENTS = [
  TextAlertComponent,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
              NgbAlertModule,
            ],
            exports: [...COMPONENTS],
          })
export class AlertModule {
}
