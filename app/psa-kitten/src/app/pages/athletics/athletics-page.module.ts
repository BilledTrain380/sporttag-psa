import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ResultPageModule} from './results/result-page.module';
import {AthleticsRoutingModule} from './athletics-routing.module';
import {RankingModule} from './ranking/ranking.module';

@NgModule({
    imports: [
        CommonModule,
        ResultPageModule,
        RankingModule,
        AthleticsRoutingModule,
    ],
})
export class AthleticsPageModule {
}
