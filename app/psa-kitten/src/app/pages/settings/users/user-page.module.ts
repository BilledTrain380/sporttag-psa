import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserPageComponent} from './user-page.component';
import {ThemeModule} from '../../../@theme/theme.module';
import {TranslateModule} from '@ngx-translate/core';
import {NbCardModule} from '@nebular/theme';
import {UserModule} from '../../../modules/user/user.module';
import {ConfirmationModule} from '../../../modules/confirmation/confirmation.module';
import {CreateComponent} from './create/create.component';

@NgModule({
    imports: [
        CommonModule,
        ThemeModule,
        TranslateModule,
        NbCardModule,
        UserModule,
        ConfirmationModule,
    ],
    exports: [UserPageComponent],
    declarations: [UserPageComponent, CreateComponent],
    entryComponents: [UserPageComponent, CreateComponent],
})
export class UserPageModule {
}
