import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../../@theme/theme.module";
import { StatusModule } from "../../../modules/status/status.module";
import { TreeModule } from "../../../modules/tree/tree.module";
import { EventSheetsEffects } from "../../../store/event-sheets/event-sheets.effect";

import { EventSheetsManagementRoutingModule } from "./event-sheets-management-routing.module";
import { EventSheetsSelectionComponent } from "./event-sheets-selection/event-sheets-selection.component";

@NgModule({
            declarations: [EventSheetsSelectionComponent],
            imports: [
              CommonModule,
              TreeModule,
              ThemeModule,
              StatusModule,
              EffectsModule.forFeature([EventSheetsEffects]),
              EventSheetsManagementRoutingModule,
            ],
          })
export class EventSheetsManagementModule {
}
