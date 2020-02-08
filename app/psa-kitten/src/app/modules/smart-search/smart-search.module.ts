import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SmartSearchComponent} from './smart-search.component';
import {ThemeModule} from '../../@theme/theme.module';
import {NbListModule} from '@nebular/theme';

@NgModule({
    imports: [
        CommonModule,
        ThemeModule,
        NbListModule,
    ],
    exports: [
        SmartSearchComponent,
    ],
    declarations: [SmartSearchComponent],
})
export class SmartSearchModule {
}
