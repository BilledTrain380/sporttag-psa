import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { ParticipationManagementComponent } from "./participation-management/participation-management.component";
import { ParticipationRoutingModule } from "./participation-routing.module";

@NgModule({
            declarations: [ParticipationManagementComponent],
            imports: [
              CommonModule,
              ParticipationRoutingModule,
            ],
          })
export class ParticipationModule {
}
