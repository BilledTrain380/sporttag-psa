import {Component, Inject, OnInit} from '@angular/core';
import {USER_PROVIDER, UserProvider} from '../../../modules/user/user-providers';
import {NgForm} from '@angular/forms';
import {User} from '../../../modules/user/user-models';
import {environment} from '../../../../environments/environment';
import {NbDialogRef} from '@nebular/theme';

@Component({
    selector: 'ngx-change-password',
    templateUrl: './change-password.component.html',
    styleUrls: ['./change-password.component.scss'],
})
export class ChangePasswordComponent implements OnInit {

    isPasswordRevealed: boolean = false;

    // Will be set through dialog service context
    user: User = {
        id: 0,
        username: '',
        enabled: true,
    };

    readonly passwordPolicyRegex: string = environment.passwordPolicy;

    constructor(
        private readonly ref: NbDialogRef<ChangePasswordComponent>,

        @Inject(USER_PROVIDER)
        private readonly userProvider: UserProvider,
    ) {}

    ngOnInit() {
    }

    togglePasswordReveal(): void {
        this.isPasswordRevealed = !this.isPasswordRevealed;
    }

    dismissModal(): void {
        this.ref.close();
    }

    async submit(form: NgForm): Promise<void> {

        await this.userProvider.updateUserPassword(this.user, form.value.password);
        this.ref.close(true);
    }
}
