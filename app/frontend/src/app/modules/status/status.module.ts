import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbPopoverModule } from "@ng-bootstrap/ng-bootstrap";

import { SecurityModule } from "../../@security/security.module";

import { AthleticsNotStartedComponent } from "./athletics-not-started/athletics-not-started.component";
import { StatusDetailComponent } from "./status-detail/status-detail.component";
import { StatusPopoverComponent } from "./status-popover/status-popover.component";
import { StatusComponent } from "./status/status.component";

const COMPONENTS = [
  StatusPopoverComponent,
  StatusComponent,
  AthleticsNotStartedComponent,
];

@NgModule({
            declarations: [...COMPONENTS, StatusDetailComponent],
            imports: [
              CommonModule,
              NgbPopoverModule,
              FontAwesomeModule,
              RouterModule,
              SecurityModule,
            ],
            exports: [...COMPONENTS],
          })
export class StatusModule {
}
