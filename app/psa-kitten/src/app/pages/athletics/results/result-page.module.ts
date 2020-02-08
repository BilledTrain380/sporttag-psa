import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NbCardModule, NbCheckboxModule, NbSpinnerModule} from '@nebular/theme';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {SmartSelectModule} from '../../../modules/smart-select/smart-select.module';
import {AthleticsModule} from '../../../modules/athletics/athletics.module';
import {GroupModule} from '../../../modules/group/group.module';
import {ResultsComponent} from './results.component';
import {FormsModule} from '@angular/forms';
import {ThemeModule} from '../../../@theme/theme.module';
import {TranslateModule} from '@ngx-translate/core';
import {RouterModule} from '@angular/router';

@NgModule({
    imports: [
        CommonModule,
        ThemeModule,
        NbCardModule,
        NbSpinnerModule,
        Ng2SmartTableModule,
        NbCheckboxModule,
        RouterModule,
        FormsModule,
        SmartSelectModule,
        AthleticsModule,
        GroupModule,
        TranslateModule,
    ],
    entryComponents: [
        ResultsComponent,
    ],
    declarations: [
        ResultsComponent,
    ],
})
export class ResultPageModule {
}
