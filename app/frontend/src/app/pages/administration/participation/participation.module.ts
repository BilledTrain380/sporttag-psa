import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../../@theme/theme.module";
import { ModalModule } from "../../../modules/modal/modal.module";
import { StatusModule } from "../../../modules/status/status.module";
import { ParticipationEffects } from "../../../store/participation/participation.effect";

import { ParticipationManagementComponent } from "./participation-management/participation-management.component";
import { ParticipationRoutingModule } from "./participation-routing.module";

@NgModule({
            declarations: [ParticipationManagementComponent],
            imports: [
              CommonModule,
              ParticipationRoutingModule,
              ThemeModule,
              StatusModule,
              ModalModule,
              EffectsModule.forFeature([ParticipationEffects]),
            ],
          })
export class ParticipationModule {
}
