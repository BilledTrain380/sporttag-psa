import {Component, Inject, OnInit} from '@angular/core';
import {Discipline, TemporaryResult} from '../../../modules/athletics/athletics-models';
import {GROUP_PROVIDER, GroupProvider} from '../../../modules/group/group-providers';
import {Group} from '../../../modules/group/group-models';
import {SmartSelectSettings} from '../../../modules/smart-select/smart-select-settings';
import {
    COMPETITOR_PROVIDER,
    CompetitorProvider,
    DISCIPLINE_PROVIDER,
    DisciplineProvider,
} from '../../../modules/athletics/athletics-providers';
import {Gender} from '../../../modules/participant/participant-models';
import {debounceTime, distinctUntilChanged} from 'rxjs/operators';
import {CompetitorModel} from './result.models';
import {PARTICIPATION_PROVIDER, ParticipationProvider} from '../../../modules/participation/participation-providers';
import {ParticipationStatus} from '../../../modules/participation/participation-models';


@Component({
    selector: 'ngx-results',
    templateUrl: './results.component.html',
    styleUrls: ['./results.component.scss'],
})
export class ResultsComponent implements OnInit {

    readonly groupSelectSettings: SmartSelectSettings<Group> = {
        onDisplay: value => value.name,
    };

    readonly disciplineSelectSettings: SmartSelectSettings<Discipline> = {
        onDisplay: value => value.name,
    };

    // display options control fields
    isMale: boolean = true;
    isFemale: boolean = false;
    selectedGroup?: Group;
    selectedDiscipline?: Discipline;

    isLoading: boolean = false;
    isParticipationOpen: boolean = false;

    groups: ReadonlyArray<Group> = [];
    disciplines: ReadonlyArray<Discipline> = [];
    competitors: ReadonlyArray<CompetitorModel> = [];
    absentCompetitors: ReadonlyArray<CompetitorModel> = [];

    constructor(
        @Inject(GROUP_PROVIDER)
        private readonly groupProvider: GroupProvider,

        @Inject(COMPETITOR_PROVIDER)
        private readonly competitorProvider: CompetitorProvider,

        @Inject(DISCIPLINE_PROVIDER)
        private readonly disciplineProvider: DisciplineProvider,

        @Inject(PARTICIPATION_PROVIDER)
        private readonly participationProvider: ParticipationProvider,
    ) {}

    ngOnInit(): void {
        this.groupProvider.getGroupList({competitive: true}).then(it => this.groups = it);
        this.disciplineProvider.getAll().then(it => this.disciplines = it);
        this.participationProvider.getStatus().then(it => this.isParticipationOpen = it === ParticipationStatus.OPEN);
    }

    onGroupChange(group: Group): void {
        this.selectedGroup = group;
        this.loadCompetitors();
    }

    onDisciplineChange(discipline: Discipline): void {
        this.selectedDiscipline = discipline;
        this.loadCompetitors();
    }

    maleChanged(): void {

        if (!(this.isMale && this.isFemale)) {
            this.isFemale = true;
        }

        this.loadCompetitors();
    }

    femaleChanged(): void {

        if (!(this.isMale && this.isFemale)) {
            this.isMale = true;
        }

        this.loadCompetitors();
    }

    private async loadCompetitors(): Promise<void> {

        if (!(this.selectedGroup && this.selectedDiscipline)) return;

        this.isLoading = true;

        let gender: Gender|undefined;

        if (this.isMale && !this.isFemale) { gender = Gender.MALE; }
        if (!this.isMale && this.isFemale) { gender = Gender.FEMALE; }

        this.competitors = (await this.competitorProvider.getCompetitorList({
            group: this.selectedGroup,
            gender,
            absent: false,
        })).map(it => {
                const model: CompetitorModel = new CompetitorModel(it, this.selectedDiscipline);

            // save result when its value get changed
            model.result.valueProperty
                .pipe(debounceTime(200))
                .pipe(distinctUntilChanged())
                .subscribe(resultValue => {

                    const result: TemporaryResult = {
                        id: model.result.id,
                        value: Math.floor(resultValue * model.result.discipline.unit.factor),
                        distance: model.result.distance,
                        discipline: model.result.discipline,
                    };

                    model.result.isCalculating = true;

                    this.competitorProvider.saveResults(it, [result]).then(results => {
                        // we only send 1 result, therefore the response only has one result
                        model.result.points = results[0].points;
                        model.result.isCalculating = false;
                    });
                });

            return model;
        }).sort((a, b) => a.surname.localeCompare(b.surname));

        this.absentCompetitors = (await this.competitorProvider.getCompetitorList({
            group: this.selectedGroup,
            gender,
            absent: true,
        }))
            .map(it => new CompetitorModel(it, this.selectedDiscipline))
            .sort((a, b) => a.surname.localeCompare(b.surname));

        this.isLoading = false;
    }
}
