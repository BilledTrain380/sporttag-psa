import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
    ATHLETICS_FILE_PROVIDER,
    COMPETITOR_PROVIDER,
    DISCIPLINE_PROVIDER, HttpAthleticsFileProvider,
    HttpCompetitorProvider,
    HttpDisciplineProvider,
} from './athletics-providers';

@NgModule({
    imports: [
        CommonModule,
    ],
    providers: [
        {
            provide: COMPETITOR_PROVIDER,
            useClass: HttpCompetitorProvider,
        },
        {
            provide: DISCIPLINE_PROVIDER,
            useClass: HttpDisciplineProvider,
        },
        {
            provide: ATHLETICS_FILE_PROVIDER,
            useClass: HttpAthleticsFileProvider,
        },
    ],
    declarations: [],
})
export class AthleticsModule {
}
