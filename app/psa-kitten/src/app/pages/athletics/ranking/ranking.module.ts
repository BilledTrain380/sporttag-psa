import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ThemeModule} from '../../../@theme/theme.module';
import {RankingPageComponent} from './ranking-page.component';
import {TranslateModule} from '@ngx-translate/core';
import {TreeCheckboxModule} from '../../../modules/tree-checkbox/tree-checkbox.module';
import {AthleticsModule} from '../../../modules/athletics/athletics.module';
import {ParticipationModule} from '../../../modules/participation/participation.module';
import {RouterModule} from '@angular/router';

@NgModule({
    imports: [
        CommonModule,
        ThemeModule,
        AthleticsModule,
        ParticipationModule,
        RouterModule,
        TranslateModule,
        TreeCheckboxModule,
    ],
    declarations: [RankingPageComponent],
    entryComponents: [RankingPageComponent],
})
export class RankingModule {
}
