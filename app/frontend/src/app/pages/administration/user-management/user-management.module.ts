import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../../@theme/theme.module";
import { ActionButtonsModule } from "../../../modules/action-buttons/action-buttons.module";
import { TableModule } from "../../../modules/table/table.module";
import { UserManagementEffects } from "../../../store/user-management/user-management.effect";

import { UserManagementRoutingModule } from "./user-management-routing.module";
import { UserOverviewComponent } from "./user-overview/user-overview.component";

@NgModule({
            declarations: [UserOverviewComponent],
            imports: [
              CommonModule,
              FormsModule,
              ThemeModule,
              ActionButtonsModule,
              TableModule,
              EffectsModule.forFeature([UserManagementEffects]),
              UserManagementRoutingModule,
            ],
          })
export class UserManagementModule {
}
