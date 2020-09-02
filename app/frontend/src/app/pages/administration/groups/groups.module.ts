import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../../@theme/theme.module";
import { ActionButtonsModule } from "../../../modules/action-buttons/action-buttons.module";
import { AlertModule } from "../../../modules/alert/alert.module";
import { CommonFormsModule } from "../../../modules/common-forms/common-forms.module";
import { DragAndDropModule } from "../../../modules/drag-and-drop/drag-and-drop.module";
import { ModalModule } from "../../../modules/modal/modal.module";
import { ParticipantModule } from "../../../modules/participant/participant.module";
import { StatusModule } from "../../../modules/status/status.module";
import { TableModule } from "../../../modules/table/table.module";
import { GroupEffects } from "../../../store/group/group.effect";

import { AddParticipantModalComponent } from "./group-detail/add-participant/add-participant-modal.component";
import { EditParticipantModalComponent } from "./group-detail/edit-participant-modal/edit-participant-modal.component";
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
              AddParticipantModalComponent,
              EditParticipantModalComponent,
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
              ReactiveFormsModule,
              FormsModule,
              TableModule,
              CommonFormsModule,
              ParticipantModule,
            ],
          })
export class GroupsModule {
}
