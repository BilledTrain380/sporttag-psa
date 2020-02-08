import {Component, Inject, OnInit} from '@angular/core';
import {PARTICIPATION_PROVIDER, ParticipationProvider} from '../../modules/participation/participation-providers';
import {GROUP_PROVIDER, GroupProvider} from '../../modules/group/group-providers';
import {Group} from '../../modules/group/group-models';
import {ParticipationStatus} from '../../modules/participation/participation-models';
import {ConfirmationComponent} from '../../modules/confirmation/confirmation.component';
import {TranslateService} from '@ngx-translate/core';
import {NbDialogRef, NbDialogService} from '@nebular/theme';
import {filter} from 'rxjs/operators';

@Component({
    selector: 'ngx-management',
    templateUrl: './management.component.html',
    styleUrls: ['./management.component.scss'],
})
export class ManagementComponent implements OnInit {

    resetSuccess: boolean = false;

    participationStatus: ParticipationStatus = ParticipationStatus.CLOSE;
    groupList: ReadonlyArray<Group> = [];

    constructor(
        private readonly dialogService: NbDialogService,
        private readonly translateService: TranslateService,

        @Inject(GROUP_PROVIDER)
        private readonly groupProvider: GroupProvider,
        @Inject(PARTICIPATION_PROVIDER)
        private readonly participationProvider: ParticipationProvider,
    ) {
    }

    ngOnInit() {

        this.groupProvider.getGroupList({pendingParticipation: true}).then(groups => {
            this.groupList = this.groupList.concat(...groups);
        });

        this.participationProvider.getStatus().then(status => {
            this.participationStatus = status;
        });
    }

    closeParticipation(): void {
        this.participationProvider.close();
        this.participationStatus = ParticipationStatus.CLOSE;
    }

    async resetParticipation(): Promise<void> {

        const message: string = await this.translateService.get('participation.alert.confirmReset').toPromise();

        const ref: NbDialogRef<any> = this.dialogService.open(ConfirmationComponent, {
            context: {
                message,
            },
        });

        ref.onClose
            .pipe(filter(success => success))
            .subscribe(async () => {

                await this.participationProvider.reset();

                this.ngOnInit();

                this.resetSuccess = true;
                setTimeout(() => {
                    this.resetSuccess = false;
                }, 5000);
            });
    }
}
