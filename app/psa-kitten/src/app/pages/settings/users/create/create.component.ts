import {Component, Inject} from '@angular/core';
import {USER_PROVIDER, UserProvider} from '../../../../modules/user/user-providers';
import {User} from '../../../../modules/user/user-models';
import {NgForm} from '@angular/forms';
import {environment} from '../../../../../environments/environment';
import {NbDialogRef} from '@nebular/theme';

@Component({
    selector: 'ngx-create',
    templateUrl: './create.component.html',
    styleUrls: ['./create.component.scss'],
})
export class CreateComponent {

    isPasswordRevealed: boolean = false;

    // Will be set through dialog service context
    takenUsernames: ReadonlyArray<string> = [];

    usernameIsTaken: boolean = false;

    readonly passwordPolicyRegex: string = environment.passwordPolicy;

    constructor(
        private readonly ref: NbDialogRef<CreateComponent>,
        @Inject(USER_PROVIDER)
        private readonly userProvider: UserProvider,
    ) {}

    togglePasswordReveal(): void {
        this.isPasswordRevealed = !this.isPasswordRevealed;
    }

    dismissModal(): void {
        this.ref.close();
    }

    checkUsername(name: string): void {
        this.usernameIsTaken = !!this.takenUsernames.find(it => it === name);
    }

    async submit(form: NgForm): Promise<void> {

        const user: User = {
            id: 0,
            username: form.value.username,
            enabled: form.value.enabled,
        };

        await this.userProvider.createUser(user, form.value.password);
        this.ref.close(true);
    }
}
