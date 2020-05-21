import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbPopoverModule } from "@ng-bootstrap/ng-bootstrap";

import { StatusDetailComponent } from "./status/status-detail/status-detail.component";
import { StatusComponent } from "./status/status.component";

const COMPONENTS = [
  StatusComponent,
  StatusDetailComponent,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
              NgbPopoverModule,
              FontAwesomeModule,
            ],
            exports: [...COMPONENTS],
          })
export class StatusModule {
}
