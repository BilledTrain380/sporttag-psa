import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbPopoverModule } from "@ng-bootstrap/ng-bootstrap";

import { StatusDetailComponent } from "./status/status-detail/status-detail.component";
import { StatusPopoverComponent } from "./status/status-popover.component";

const COMPONENTS = [
  StatusPopoverComponent,
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
