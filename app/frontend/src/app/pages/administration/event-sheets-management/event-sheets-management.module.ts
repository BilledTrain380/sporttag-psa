import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../../@theme/theme.module";
import { TreeModule } from "../../../modules/tree/tree.module";
import { EventSheetsEffects } from "../../../store/event-sheets/event-sheets.effect";
import { ParticipationEffects } from "../../../store/participation/participation.effect";

import { EventSheetsManagementRoutingModule } from "./event-sheets-management-routing.module";
import { EventSheetsSelectionComponent } from "./event-sheets-selection/event-sheets-selection.component";

@NgModule({
            declarations: [EventSheetsSelectionComponent],
            imports: [
              CommonModule,
              TreeModule,
              ThemeModule,
              EffectsModule.forFeature([EventSheetsEffects, ParticipationEffects]),
              EventSheetsManagementRoutingModule,
            ],
          })
export class EventSheetsManagementModule {
}
