import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { AdministrationComponent } from "./administration.component";
import { GroupOverviewComponent } from "./groups/group-overview/group-overview.component";

const routes: Routes = [{
  path: "",
  component: AdministrationComponent,
  children: [
    {
      path: "group-manager",
      component: GroupOverviewComponent,
    },
  ],
}];

@NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule],
          })
export class AdministrationRoutingModule {
}
