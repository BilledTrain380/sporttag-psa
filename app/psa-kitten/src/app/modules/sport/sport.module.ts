import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpSportProvider, SPORT_PROVIDER} from './sport-providers';

@NgModule({
    imports: [
        CommonModule,
    ],
    declarations: [],
    providers: [
        {
            provide: SPORT_PROVIDER,
            useClass: HttpSportProvider,
        },
    ],
})
export class SportModule {
}
