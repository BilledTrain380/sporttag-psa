import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

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
            ],
            exports: [...COMPONENTS],
          })
export class StatusModule {
}
