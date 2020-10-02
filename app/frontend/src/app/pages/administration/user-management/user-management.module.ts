import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { UserManagementRoutingModule } from "./user-management-routing.module";
import { UserOverviewComponent } from "./user-overview/user-overview.component";

@NgModule({
            declarations: [UserOverviewComponent],
            imports: [
              CommonModule,
              UserManagementRoutingModule,
            ],
          })
export class UserManagementModule {
}
