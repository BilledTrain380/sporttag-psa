import {Competitor, Discipline, Result} from '../../../modules/athletics/athletics-models';
import {Observable, Subject} from 'rxjs';
import {Gender} from '../../../modules/participant/participant-models';

export class ResultModel {

    readonly id: number;
    readonly distance?: string;
    readonly discipline: Discipline;

    readonly valueProperty: Observable<number>;
    value: string;
    isValueValid: boolean = true;

    points: number;
    readonly unit: string;

    isCalculating: boolean = false;

    private readonly valueEmitter: Subject<number> = new Subject();

    constructor(
        result: Result,
    ) {
        this.id = result.id;
        this.discipline = result.discipline;
        this.distance = result.distance;
        this.value = String(result.value / result.discipline.unit.factor);
        this.points = result.points;
        this.unit = result.discipline.unit.name;
        this.valueProperty = this.valueEmitter.asObservable();
    }

    validate(): void {

        // just replace comma with decimal point, so it does not matter whenever its a comma or decimal point
        this.value = this.value.replace(',', '.');

        if (Number.isNaN(Number(this.value))) {
            this.isValueValid = false;
            return;
        }

        this.isValueValid = true;
    }

    valueChanged(): void {
        this.validate();
        if (this.isValueValid) this.valueEmitter.next(Number(this.value));
    }
}

export class CompetitorModel {

    readonly startNumber: number;
    readonly prename: string;
    readonly surname: string;
    readonly gender: Gender;
    readonly result?: ResultModel;

    constructor(
        competitor: Competitor,
        currentDiscipline: Discipline,
    ) {
        this.startNumber = competitor.startNumber;
        this.prename = competitor.prename;
        this.surname = competitor.surname;
        this.gender = competitor.gender;
        this.result = new ResultModel(
            competitor.results.find(it => it.discipline.name === currentDiscipline.name),
        );
    }
}
