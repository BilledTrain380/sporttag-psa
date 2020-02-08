import {Gender, Town} from '../participant/participant-models';
import {Group} from '../group/group-models';

export interface Competitor {
    readonly id: number;
    readonly startNumber: number;
    readonly surname: string;
    readonly prename: string;
    readonly gender: Gender;
    readonly birthday: number;
    readonly address: string;
    readonly absent: boolean;
    readonly town: Town;
    readonly group: Group;
    readonly results: ReadonlyArray<Result>;
}

export interface Result {
    readonly id: number;
    readonly value: number;
    readonly points: number;
    readonly distance?: string;
    readonly discipline: Discipline;
}

export interface Discipline {
    readonly name: string;
    readonly unit: Unit;
}

export interface Unit {
    readonly name: string;
    readonly factor: number;
}

export class TemporaryResult {

    constructor(
        readonly id: number,
        readonly value: number,
        readonly discipline: Discipline,
        readonly distance?: string,
    ) {}
}

export class DisciplineRanking {

    constructor(
        readonly discipline: string,
        readonly gender: Gender,
    ) {}
}

export class RankingData {

    constructor(
        readonly total: ReadonlyArray<Gender> = [],
        readonly discipline: ReadonlyArray<DisciplineRanking> = [],
        readonly disciplineGroup: ReadonlyArray<Gender> = [],
        readonly ubsCup: ReadonlyArray<Gender> = [],
    ) {}
}
