import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { AdministrationComponent } from "./administration.component";
import { OverviewComponent } from "./overview/overview.component";

const routes: Routes = [
  {
    path: "",
    component: AdministrationComponent,
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
