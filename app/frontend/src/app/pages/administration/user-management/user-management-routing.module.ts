import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { UserOverviewComponent } from "./user-overview/user-overview.component";

const routes: Routes = [
  {
    path: "",
    component: UserOverviewComponent,
  },
];

@NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule],
          })
export class UserManagementRoutingModule {
}
