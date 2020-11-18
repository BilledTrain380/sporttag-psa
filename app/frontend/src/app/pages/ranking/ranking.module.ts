import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../@theme/theme.module";
import { TreeModule } from "../../modules/tree/tree.module";
import { RankingEffects } from "../../store/ranking/ranking.effect";

import { RankingExportComponent } from "./ranking-export/ranking-export.component";
import { RankingRoutingModule } from "./ranking-routing.module";

@NgModule({
            declarations: [RankingExportComponent],
            imports: [
              CommonModule,
              RankingRoutingModule,
              ThemeModule,
              TreeModule,
              EffectsModule.forFeature([RankingEffects]),
            ],
          })
export class RankingModule {
}
