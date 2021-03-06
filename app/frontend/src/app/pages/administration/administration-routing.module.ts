import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { ROLE_ADMIN } from "../../@security/auth-constants";
import { AuthorityAuthGuard } from "../../@security/authority-auth-guard";

import { AdministrationComponent } from "./administration.component";
import { OverviewComponent } from "./overview/overview.component";

const routes: Routes = [
  {
    path: "",
    component: AdministrationComponent,
    canActivate: [AuthorityAuthGuard],
    data: {
      requiredAuthorities: [ROLE_ADMIN],
    },
    children: [
      {
        path: "overview",
        component: OverviewComponent,
      },
      {
        path: "group-management",
        loadChildren: () => import("./groups/groups.module")
          .then(module => module.GroupsModule),
      },
      {
        path: "participation-management",
        loadChildren: () => import("./participation/participation.module")
          .then(module => module.ParticipationModule),
      },
      {
        path: "event-sheets-management",
        loadChildren: () => import("./event-sheets-management/event-sheets-management.module")
          .then(module => module.EventSheetsManagementModule),
      },
      {
        path: "user-management",
        loadChildren: () => import("./user-management/user-management.module")
          .then(module => module.UserManagementModule),
      },
      {
        path: "",
        redirectTo: "overview",
        pathMatch: "full",
      },
    ],
  },
];

@NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule],
          })
export class AdministrationRoutingModule {
}
