import {Component, Inject, OnInit} from '@angular/core';
import {DISCIPLINE_PROVIDER, DisciplineProvider} from '../../../modules/athletics/athletics-providers';
import {GROUP_PROVIDER, GroupProvider} from '../../../modules/group/group-providers';
import {Gender} from '../../../modules/participant/participant-models';
import {TreeCheckbox} from '../../../modules/tree-checkbox/tree-checkbox-models';
import {TranslateService} from '@ngx-translate/core';
import {ATHLETICS, EventSheetData, ParticipationStatus} from '../../../modules/participation/participation-models';
import {
    PARTICIPATION_FILE_PROVIDER,
    PARTICIPATION_PROVIDER,
    ParticipationFileProvider,
    ParticipationProvider,
} from '../../../modules/participation/participation-providers';
import {HTTP_SERVICE, HttpService} from '../../../modules/http/http-service';
import {FileQualifier} from '../../../modules/http/http-models';
import {SPORT_PROVIDER, SportProvider} from '../../../modules/sport/sport-providers';
import {Sport} from '../../../modules/sport/sport-models';

@Component({
    selector: 'ngx-event-page',
    templateUrl: './event-page.component.html',
    styleUrls: ['./event-page.component.scss'],
})
export class EventPageComponent implements OnInit {

    participantListTree: TreeCheckbox;
    eventSheetsTree: TreeCheckbox;

    isEventSheetLoading: boolean = false;
    isParticipantListLoading: boolean = false;

    isParticipationOpen: boolean = false;

    constructor(
        @Inject(DISCIPLINE_PROVIDER) private readonly disciplineProvider: DisciplineProvider,
        @Inject(GROUP_PROVIDER) private readonly groupProvider: GroupProvider,
        @Inject(SPORT_PROVIDER) private readonly sportProvider: SportProvider,
        @Inject(PARTICIPATION_FILE_PROVIDER) private readonly fileProvider: ParticipationFileProvider,
        @Inject(PARTICIPATION_PROVIDER) private readonly participationProvider: ParticipationProvider,
        @Inject(HTTP_SERVICE) private readonly http: HttpService,
        private readonly translateService: TranslateService,
    ) {}

    async ngOnInit() {

        this.isParticipationOpen = (await this.participationProvider.getStatus()) === ParticipationStatus.OPEN;

        const sports: ReadonlyArray<TreeCheckbox> = (await this.sportProvider.getAll())
            .filter(it => it.name !== ATHLETICS)
            .map(it => new TreeCheckbox(it.name, [], 'col-lg-12', it));

        const maleTranslate: string = await this.translateService.get(Gender.MALE).toPromise();
        const femaleTranslate: string = await this.translateService.get(Gender.FEMALE).toPromise();
        const allTranslate: string = await this.translateService.get('eventPage.label.all').toPromise();

        this.participantListTree = new TreeCheckbox(allTranslate, sports);
        this.participantListTree.toggleCollapse();

        const disciplineList = await this.disciplineProvider.getAll();
        const groupList = await this.groupProvider.getGroupList({ competitive: true });

        const disciplines: ReadonlyArray<TreeCheckbox> = disciplineList.map(discipline => {

            const groups: ReadonlyArray<TreeCheckbox> = groupList.map(group => {

                const maleEventSheetData: EventSheetData = {
                    gender: Gender.MALE,
                    group,
                    discipline,
                };

                const femaleEventSheetData: EventSheetData = {
                    gender: Gender.FEMALE,
                    group,
                    discipline,
                };

                const genders: ReadonlyArray<TreeCheckbox> = [
                    new TreeCheckbox(maleTranslate, [], 'col-lg-12', maleEventSheetData),
                    new TreeCheckbox(femaleTranslate, [], 'col-lg-12', femaleEventSheetData),
                ];

                return new TreeCheckbox(group.name, genders, 'col-lg-6');
            });

            return new TreeCheckbox(discipline.name, groups, 'col-lg-4');
        });

        this.eventSheetsTree = new TreeCheckbox(allTranslate, disciplines);
        this.eventSheetsTree.toggleCollapse();
    }

    async exportParticipantList(): Promise<void> {

        this.isParticipantListLoading = true;

        const data: ReadonlyArray<Sport> = this.participantListTree.children.map(it => it.data);

        const fileQualifier: FileQualifier = await this.fileProvider.createParticipantList(data);

        await this.http.downloadFile(fileQualifier);

        this.isParticipantListLoading = false;
    }

    async exportEventSheets(): Promise<void> {

        this.isEventSheetLoading = true;

        const data: ReadonlyArray<EventSheetData> = this.eventSheetsTree.children
            .map(discipline => discipline.children)
            .reduce(((previousValue, currentValue) => previousValue.concat(currentValue)), []) // flatten disciplines
            .map(group => group.children)
            .reduce(((previousValue, currentValue) => previousValue.concat(currentValue)), []) // flatten groups
            .filter(it => it.checked === true)
            .map(it => it.data);

        const fileQualifier: FileQualifier = await this.fileProvider.createEventSheets(data);

        await this.http.downloadFile(fileQualifier);

        this.isEventSheetLoading = false;
    }
}

