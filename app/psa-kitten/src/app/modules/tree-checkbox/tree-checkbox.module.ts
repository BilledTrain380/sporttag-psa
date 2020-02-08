import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TreeCheckboxComponent} from './tree-checkbox.component';
import {ThemeModule} from '../../@theme/theme.module';

@NgModule({
    imports: [
        CommonModule,
        ThemeModule,
    ],
    exports: [TreeCheckboxComponent],
    declarations: [TreeCheckboxComponent],
    entryComponents: [TreeCheckboxComponent],
})
export class TreeCheckboxModule {
}
