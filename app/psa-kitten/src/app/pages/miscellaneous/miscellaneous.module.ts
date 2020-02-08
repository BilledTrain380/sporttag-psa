import {NgModule} from '@angular/core';
import {ThemeModule} from '../../@theme/theme.module';
import {MiscellaneousRoutingModule, routedComponents} from './miscellaneous-routing.module';
import {TranslateModule} from '@ngx-translate/core';

@NgModule({
    imports: [
        ThemeModule,
        MiscellaneousRoutingModule,
        TranslateModule,
    ],
    declarations: [
        ...routedComponents,
    ],
})
export class MiscellaneousModule {
}
