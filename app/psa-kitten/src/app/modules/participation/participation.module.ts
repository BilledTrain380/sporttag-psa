import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
    HttpParticipationFileProvider,
    HttpParticipationProvider,
    PARTICIPATION_FILE_PROVIDER,
    PARTICIPATION_PROVIDER,
} from './participation-providers';

@NgModule({
    imports: [
        CommonModule,
    ],
    declarations: [],
    providers: [
        {
            provide: PARTICIPATION_PROVIDER,
            useClass: HttpParticipationProvider,
        },
        {
            provide: PARTICIPATION_FILE_PROVIDER,
            useClass: HttpParticipationFileProvider,
        },
    ],
})
export class ParticipationModule {
}
