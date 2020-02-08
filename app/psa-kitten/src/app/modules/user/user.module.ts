import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpUserProvider, JWTUserSupplier, USER_PROVIDER, USER_SUPPLIER} from './user-providers';
import {TranslateModule} from '@ngx-translate/core';
import {FormsModule} from '@angular/forms';
import {NbCardModule} from '@nebular/theme';

@NgModule({
    imports: [
        CommonModule,
        TranslateModule,
        FormsModule,
        NbCardModule,
    ],
    providers: [
        {
            provide: USER_PROVIDER,
            useClass: HttpUserProvider,
        },
        {
            provide: USER_SUPPLIER,
            useClass: JWTUserSupplier,
        },
    ],
})
export class UserModule {
}
