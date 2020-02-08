import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EventPageComponent} from './event-page/event-page.component';
import {ThemeModule} from '../../@theme/theme.module';
import {NbCardModule} from '@nebular/theme';
import {TranslateModule} from '@ngx-translate/core';
import {TreeModule} from 'angular-tree-component';
import {AthleticsModule} from '../../modules/athletics/athletics.module';
import {GroupModule} from '../../modules/group/group.module';
import {TreeCheckboxModule} from '../../modules/tree-checkbox/tree-checkbox.module';
import {HttpModule} from '../../modules/http/http.module';
import {RouterModule} from '@angular/router';

@NgModule({
    imports: [
        CommonModule,
        ThemeModule,
        NbCardModule,
        TreeModule,
        AthleticsModule,
        GroupModule,
        HttpModule,
        RouterModule,
        TranslateModule,
        TreeCheckboxModule,
    ],
    declarations: [EventPageComponent],
    entryComponents: [EventPageComponent],
})
export class EventPageModule {
}
