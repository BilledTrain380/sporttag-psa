import {AfterViewInit, Component, Inject} from '@angular/core';
import {USER_PROVIDER, UserProvider} from '../../../modules/user/user-providers';
import {User} from '../../../modules/user/user-models';
import {TranslateService} from '@ngx-translate/core';
import {ConfirmationComponent} from '../../../modules/confirmation/confirmation.component';
import {CreateComponent} from './create/create.component';
import {ChangePasswordComponent} from '../../../@theme/components/change-password/change-password.component';
import {NbDialogRef, NbDialogService} from '@nebular/theme';
import {filter} from 'rxjs/operators';

@Component({
    selector: 'ngx-users',
    templateUrl: './user-page.component.html',
    styleUrls: ['./user-page.component.scss'],
})
export class UserPageComponent implements AfterViewInit {

    userList: ReadonlyArray<User> = [];

    deleteSuccess: boolean = false;

    constructor(
        private readonly dialogService: NbDialogService,
        private readonly translateService: TranslateService,

        @Inject(USER_PROVIDER)
        private readonly userProvider: UserProvider,
    ) {}

    ngAfterViewInit() {
        this.loadUsers();
    }

    async openCreateUserModal(): Promise<void> {

        const ref: NbDialogRef<any> = this.dialogService.open(CreateComponent, {
            context: {
                takenUsernames: this.userList.map(it => it.username).concat('admin'),
            } as any,
        });

        ref.onClose
            .pipe(filter(success => success))
            .subscribe(() => {
                this.loadUsers();
            });
    }

    async openChangePasswordModal(user: User): Promise<void> {

        this.dialogService.open(ChangePasswordComponent, {
            context: {
                user,
            },
        });
    }

    async toggleEnableStatus(user: User): Promise<void> {
        await this.userProvider.updateUser(user);
    }

    async deleteUser(user: User): Promise<void> {

        const message: string = await this.translateService.get(
            'settings.users.alert.confirmDelete',
            { username: user.username })
            .toPromise();

        const ref: NbDialogRef<any> = this.dialogService.open(ConfirmationComponent, {
            context: {
                message,
            },
        });

        ref.onClose
            .pipe(filter(success => success))
            .subscribe(async () => {

                await this.userProvider.deleteUser(user);

                this.loadUsers();

                this.deleteSuccess = true;
                setTimeout(() => {
                    this.deleteSuccess = false;
                }, 5000);
            });
    }

    private async loadUsers(): Promise<void> {
        this.userList = (await this.userProvider.getUsers())
            .filter(it => it.username !== 'admin');
    }
}
