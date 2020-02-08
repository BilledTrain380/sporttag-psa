import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SettingsRoutingModule} from './settings-routing.module';
import {UserPageModule} from './users/user-page.module';
import {NbDialogModule} from '@nebular/theme';

@NgModule({
    imports: [
        CommonModule,
        UserPageModule,
        SettingsRoutingModule,
        NbDialogModule.forRoot(),
    ],
    declarations: [],
})
export class SettingsModule {
}
