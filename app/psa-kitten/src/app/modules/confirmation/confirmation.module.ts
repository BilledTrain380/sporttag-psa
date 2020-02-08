import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ConfirmationComponent} from './confirmation.component';
import {TranslateModule} from '@ngx-translate/core';
import {NbCardModule, NbDialogModule} from '@nebular/theme';
import {ThemeModule} from '../../@theme/theme.module';

@NgModule({
    imports: [
        CommonModule,
        TranslateModule,
        ThemeModule,
        NbCardModule,
        NbDialogModule,
    ],
    exports: [
        ConfirmationComponent,
    ],
    declarations: [ConfirmationComponent],
    entryComponents: [ConfirmationComponent],
})
export class ConfirmationModule {
}
