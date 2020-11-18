import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { ThemeModule } from "../../@theme/theme.module";
import { TreeModule } from "../../modules/tree/tree.module";

import { RankingExportComponent } from "./ranking-export/ranking-export.component";
import { RankingRoutingModule } from "./ranking-routing.module";

@NgModule({
            declarations: [RankingExportComponent],
            imports: [
              CommonModule,
              RankingRoutingModule,
              ThemeModule,
              TreeModule,
            ],
          })
export class RankingModule {
}
