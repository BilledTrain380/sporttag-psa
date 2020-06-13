import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { GroupDetailComponent } from "./group-detail/group-detail.component";
import { GroupOverviewComponent } from "./group-overview/group-overview.component";
import { GroupsComponent } from "./groups.component";

const routes: Routes = [
  {
    path: "",
    component: GroupsComponent,
    children: [
      {
        path: "overview",
        component: GroupOverviewComponent,
      },
      {
        path: ":name",
        component: GroupDetailComponent,
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
export class GroupsRoutingModule {
}
