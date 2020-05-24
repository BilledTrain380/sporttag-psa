import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { AdministrationComponent } from "./administration.component";
import { GroupOverviewComponent } from "./groups/group-overview/group-overview.component";
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
        component: GroupOverviewComponent,
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
