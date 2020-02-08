import {RouterModule, Routes} from '@angular/router';
import {ResultsComponent} from './results/results.component';
import {NgModule} from '@angular/core';
import {RankingPageComponent} from './ranking/ranking-page.component';

const routes: Routes = [{
        path: 'results',
        component: ResultsComponent,
    }, {
        path: 'ranking',
        component: RankingPageComponent,
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class AthleticsRoutingModule {}
