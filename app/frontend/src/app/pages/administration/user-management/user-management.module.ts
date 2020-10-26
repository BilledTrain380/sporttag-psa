import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../../@theme/theme.module";
import { ActionButtonsModule } from "../../../modules/action-buttons/action-buttons.module";
import { CommonFormsModule } from "../../../modules/common-forms/common-forms.module";
import { ModalModule } from "../../../modules/modal/modal.module";
import { TableModule } from "../../../modules/table/table.module";
import { UserManagementEffects } from "../../../store/user-management/user-management.effect";

import { UserManagementRoutingModule } from "./user-management-routing.module";
import { AddUserModalComponent } from "./user-overview/add-user-modal/add-user-modal.component";
import { ChangePasswordModalComponent } from "./user-overview/change-password-modal/change-password-modal.component";
import { UserOverviewComponent } from "./user-overview/user-overview.component";

@NgModule({
            declarations: [
              UserOverviewComponent,
              AddUserModalComponent,
              ChangePasswordModalComponent,
            ],
            imports: [
              CommonModule,
              FormsModule,
              ThemeModule,
              ModalModule,
              ActionButtonsModule,
              TableModule,
              EffectsModule.forFeature([UserManagementEffects]),
              UserManagementRoutingModule,
              CommonFormsModule,
            ],
          })
export class UserManagementModule {
}
