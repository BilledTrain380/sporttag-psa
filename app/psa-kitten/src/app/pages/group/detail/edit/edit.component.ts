import {Component, Inject} from '@angular/core';
import {Participant} from '../../../../modules/participant/participant-models';
import {PARTICIPANT_PROVIDER, ParticipantProvider} from '../../../../modules/participant/participant-providers';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NbDialogRef} from '@nebular/theme';

@Component({
    selector: 'ngx-edit',
    templateUrl: './edit.component.html',
    styleUrls: ['./edit.component.scss'],
})
export class EditComponent {

    readonly editForm: FormGroup;

    private _participant: Participant;

    // will be set through dialog context object
    set participant(participant: Participant) {
        this._participant = participant;
        this.editForm.setValue({
            firstName: this._participant.prename,
            lastName: this._participant.surname,
            gender: this._participant.gender,
            birthday: new Date(this._participant.birthday),
            address: this._participant.address,
        });
    }

    constructor(
        private readonly ref: NbDialogRef<EditComponent>,
        formBuilder: FormBuilder,

        @Inject(PARTICIPANT_PROVIDER)
        private readonly participantProvider: ParticipantProvider,
    ) {
        this.editForm = formBuilder.group({
            firstName: ['', [Validators.required]],
            lastName: ['', [Validators.required]],
            gender: ['MALE', [Validators.required]],
            birthday: ['', [Validators.required]],
            address: ['', [Validators.required]],
        });
    }

    dismissModal(): void {
        this.ref.close();
    }

    submit(): void {

        const updatedParticipant: Participant = {
            id: this._participant.id,
            surname: this.editForm.controls.lastName.value,
            prename: this.editForm.controls.firstName.value,
            gender: this.editForm.controls.gender.value,
            birthday: this.editForm.controls.birthday.value.getTime(),
            address: this.editForm.controls.address.value,
            town: this._participant.town,
            absent: this._participant.absent,
            group: this._participant.group,
            sport: this._participant.sport,
        };

        this.participantProvider.update(updatedParticipant).then(() => {
            this.ref.close(true);
        }).catch(() => {
            this.dismissModal();
        });
    }
}
