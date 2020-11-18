import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { RankingExportComponent } from "./ranking-export/ranking-export.component";

const routes: Routes = [{
  path: "",
  component: RankingExportComponent,
}];

@NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule],
          })
export class RankingRoutingModule {
}
