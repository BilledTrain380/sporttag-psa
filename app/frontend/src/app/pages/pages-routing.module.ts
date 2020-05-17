import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { GroupOverviewComponent } from "./groups/group-overview/group-overview.component";
import { PagesComponent } from "./pages.component";

const routes: Routes = [{
  path: "",
  component: PagesComponent,
  children: [
    {
      path: "groups/overview",
      component: GroupOverviewComponent,
    },
    {
      path: "",
      redirectTo: "groups/overview",
      pathMatch: "full",
    },
  ],
}];

@NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule],
          })
export class PagesRoutingModule {
}
