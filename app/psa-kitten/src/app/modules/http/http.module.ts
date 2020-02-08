import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthRestService, AuthHttpService, HTTP_SERVICE, PSARestService, REST_SERVICE} from './http-service';

@NgModule({
    imports: [
        CommonModule,
    ],
    declarations: [],
    providers: [
        {
            provide: REST_SERVICE,
            useExisting: PSARestService,
        },
        {
            provide: HTTP_SERVICE,
            useExisting: PSARestService,
        },
        PSARestService,
        AuthRestService,
        AuthHttpService,
    ],
})
export class HttpModule {
}
