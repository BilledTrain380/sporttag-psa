import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { NotFoundPageComponent } from "./miscellaneous/not-found-page/not-found-page.component";
import { PagesComponent } from "./pages.component";

const routes: Routes = [
  {
    path: "",
    component: PagesComponent,
    children: [
      {
        path: "ranking",
        loadChildren: () => import("./ranking/ranking.module")
          .then(module => module.RankingModule),
      },
      {
        path: "athletics",
        loadChildren: () => import("./athletics/athletics.module")
          .then(module => module.AthleticsModule),
      },
      {
        path: "administration",
        loadChildren: () => import("./administration/administration.module")
          .then(module => module.AdministrationModule),
      },
      {
        path: "",
        redirectTo: "athletics",
        pathMatch: "full",
      },
      {
        path: "404",
        component: NotFoundPageComponent,
      },
    ],
  },
];

@NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule],
          })
export class PagesRoutingModule {
}
