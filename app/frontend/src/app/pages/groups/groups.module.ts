import { NgModule } from "@angular/core";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../@theme/theme.module";
import { AlertModule } from "../../modules/alert/alert.module";
import { DragAndDropModule } from "../../modules/drag-and-drop/drag-and-drop.module";
import { ModalModule } from "../../modules/modal/modal.module";
import { StatusModule } from "../../modules/status/status.module";
import { GroupEffects } from "../../store/group/group.effect";

import { GroupOverviewComponent } from "./group-overview/group-overview.component";
import { ImportGroupsComponent } from "./group-overview/import-groups/import-groups.component";

@NgModule({
            declarations: [
              GroupOverviewComponent,
              ImportGroupsComponent,
            ],
            imports: [
              ThemeModule,
              StatusModule,
              AlertModule,
              ModalModule,
              DragAndDropModule,
              EffectsModule.forFeature([GroupEffects]),
            ],
          })
export class GroupsModule {
}
