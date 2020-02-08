import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpParticipantProvider, PARTICIPANT_PROVIDER} from './participant-providers';

@NgModule({
    imports: [
        CommonModule,
    ],
    declarations: [],
    providers: [
        {
            provide: PARTICIPANT_PROVIDER,
            useClass: HttpParticipantProvider,
        },
    ],
})
export class ParticipantModule {
}
