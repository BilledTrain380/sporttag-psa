import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbPopoverModule } from "@ng-bootstrap/ng-bootstrap";

import { StatusDetailComponent } from "./status-detail/status-detail.component";
import { StatusPopoverComponent } from "./status-popover/status-popover.component";
import { StatusComponent } from "./status/status.component";

const COMPONENTS = [
  StatusPopoverComponent,
  StatusComponent,
];

@NgModule({
            declarations: [...COMPONENTS, StatusDetailComponent],
            imports: [
              CommonModule,
              NgbPopoverModule,
              FontAwesomeModule,
            ],
            exports: [...COMPONENTS],
          })
export class StatusModule {
}
