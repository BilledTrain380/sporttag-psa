import { NgModule } from "@angular/core";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../../@theme/theme.module";
import { ActionButtonsModule } from "../../../modules/action-buttons/action-buttons.module";
import { AlertModule } from "../../../modules/alert/alert.module";
import { DragAndDropModule } from "../../../modules/drag-and-drop/drag-and-drop.module";
import { ModalModule } from "../../../modules/modal/modal.module";
import { StatusModule } from "../../../modules/status/status.module";
import { GroupEffects } from "../../../store/group/group.effect";

import { GroupDetailComponent } from "./group-detail/group-detail.component";
import { GroupOverviewComponent } from "./group-overview/group-overview.component";
import { ImportGroupsComponent } from "./group-overview/import-groups/import-groups.component";
import { GroupsRoutingModule } from "./groups-routing.module";
import { GroupsComponent } from "./groups.component";

@NgModule({
            declarations: [
              GroupsComponent,
              GroupOverviewComponent,
              ImportGroupsComponent,
              GroupDetailComponent,
            ],
            imports: [
              ThemeModule,
              StatusModule,
              AlertModule,
              ActionButtonsModule,
              ModalModule,
              GroupsRoutingModule,
              DragAndDropModule,
              EffectsModule.forFeature([GroupEffects]),
            ],
          })
export class GroupsModule {
}
