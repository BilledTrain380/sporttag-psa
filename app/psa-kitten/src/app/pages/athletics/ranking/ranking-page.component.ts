import {Component, Inject, OnInit} from '@angular/core';
import {
    ATHLETICS_FILE_PROVIDER,
    AthleticsFileProvider,
    DISCIPLINE_PROVIDER,
    DisciplineProvider,
} from '../../../modules/athletics/athletics-providers';
import {DisciplineRanking, RankingData} from '../../../modules/athletics/athletics-models';
import {TreeCheckbox} from '../../../modules/tree-checkbox/tree-checkbox-models';
import {TranslateService} from '@ngx-translate/core';
import {Gender} from '../../../modules/participant/participant-models';
import {FileQualifier} from '../../../modules/http/http-models';
import {PARTICIPATION_PROVIDER, ParticipationProvider} from '../../../modules/participation/participation-providers';
import {ParticipationStatus} from '../../../modules/participation/participation-models';
import {HTTP_SERVICE, HttpService} from '../../../modules/http/http-service';

@Component({
    selector: 'ngx-ranking-page',
    templateUrl: './ranking-page.component.html',
    styleUrls: ['./ranking-page.component.scss'],
})
export class RankingPageComponent implements OnInit {

    isRankingLoading: boolean = false;
    isParticipationOpen: boolean = false;

    rootCheckbox: TreeCheckbox;

    private totalCheckbox: TreeCheckbox;
    private disciplineCheckbox: TreeCheckbox;
    private disciplineGroupCheckbox: TreeCheckbox;
    private ubsCupCheckbox: TreeCheckbox;

    private maleTranslate: string = 'Male';
    private femaleTranslate: string = 'Female';

    constructor(
        @Inject(DISCIPLINE_PROVIDER) private readonly disciplineProvider: DisciplineProvider,
        @Inject(ATHLETICS_FILE_PROVIDER) private readonly athleticsFileProvider: AthleticsFileProvider,
        @Inject(PARTICIPATION_PROVIDER) private readonly participationProvider: ParticipationProvider,
        @Inject(HTTP_SERVICE) private readonly http: HttpService,
        private readonly translateService: TranslateService,
    ) {}

    async ngOnInit() {

        this.isParticipationOpen = (await this.participationProvider.getStatus()) === ParticipationStatus.OPEN;

        // tslint:disable: max-line-length
        this.maleTranslate = await this.translateService.get(Gender.MALE).toPromise();
        this.femaleTranslate = await this.translateService.get(Gender.FEMALE).toPromise();
        const disciplinesTranslate: string = await this.translateService.get('athleticsPage.ranking.label.disciplines').toPromise();
        const disciplineGroupTranslate: string = await this.translateService.get('athleticsPage.ranking.label.disciplineGroup').toPromise();
        const totalTranslate: string = await this.translateService.get('athleticsPage.ranking.label.total').toPromise();
        const ubsCupTranslate: string = await this.translateService.get('athleticsPage.ranking.label.ubsCup').toPromise();
        const allTranslate: string = await this.translateService.get('athleticsPage.ranking.label.all').toPromise();
        // tslint:enable

        const disciplineCheckboxes: ReadonlyArray<TreeCheckbox> = (await this.disciplineProvider.getAll())
            .map(it => {

                const genders: ReadonlyArray<TreeCheckbox> = [
                    new TreeCheckbox(this.maleTranslate, [], 'col-lg-12', new DisciplineRanking(it.name, Gender.MALE)),
                    new TreeCheckbox(this.femaleTranslate, [], 'col-lg-12', new DisciplineRanking(it.name, Gender.FEMALE)), // tslint:disable-line: max-line-length
                ];

                return new TreeCheckbox(it.name, genders, 'col-lg-3');
            });

        this.disciplineCheckbox = new TreeCheckbox(disciplinesTranslate, disciplineCheckboxes);
        this.disciplineCheckbox.toggleCollapse();

        this.disciplineGroupCheckbox = new TreeCheckbox(disciplineGroupTranslate, this.createGenderCheckboxes());
        this.disciplineGroupCheckbox.toggleCollapse();

        this.totalCheckbox = new TreeCheckbox(totalTranslate, this.createGenderCheckboxes());
        this.totalCheckbox.toggleCollapse();

        this.ubsCupCheckbox = new TreeCheckbox(ubsCupTranslate, this.createGenderCheckboxes());
        this.ubsCupCheckbox.toggleCollapse();

        this.rootCheckbox = new TreeCheckbox(allTranslate, [
            this.disciplineCheckbox,
            this.disciplineGroupCheckbox,
            this.totalCheckbox,
            this.ubsCupCheckbox],
        );
        this.rootCheckbox.toggleCollapse();
    }

    async export(): Promise<void> {

        this.isRankingLoading = true;

        const disciplineRankings: ReadonlyArray<DisciplineRanking> = this.disciplineCheckbox.children
            .map(it => it.children)
            .reduce((previousValue, currentValue) => previousValue.concat(currentValue), [])
            .filter(it => it.checked === true)
            .map(it => it.data);

        const data: RankingData = new RankingData(
            this.totalCheckbox.children.filter(it => it.checked === true).map(it => it.data),
            disciplineRankings,
            this.disciplineGroupCheckbox.children.filter(it => it.checked === true).map(it => it.data),
            this.ubsCupCheckbox.children.filter(it => it.checked === true).map(it => it.data),
        );

        const fileQualifier: FileQualifier = await this.athleticsFileProvider.createRanking(data);
        await this.http.downloadFile(fileQualifier);

        this.isRankingLoading = false;
    }

    private createGenderCheckboxes(): ReadonlyArray<TreeCheckbox> {
        return [
            new TreeCheckbox(this.maleTranslate, [], 'col-lg-12', Gender.MALE),
            new TreeCheckbox(this.femaleTranslate, [], 'col-lg-12', Gender.FEMALE),
        ];
    }
}
