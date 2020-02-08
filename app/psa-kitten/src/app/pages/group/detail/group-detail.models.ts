import {Observable, Subject} from 'rxjs';
import {Gender, Participant} from '../../../modules/participant/participant-models';

export enum ParticipantAction {
    EDIT = 'EDIT',
    DELETE = 'DELETE',
    REPARTICIPATE = 'REPARTICIPATE',
}

export class ParticipantModel {

    isReparticipate: boolean = false;
    readonly actionsProperty: Observable<ParticipantAction>;

    readonly prename: string;
    readonly surname: string;
    readonly gender: Gender;
    readonly address: string;

    readonly absentProperty: Observable<boolean>;
    absent: boolean;

    readonly sportProperty: Observable<string>;
    sport?: string;

    private readonly actionsEmitter: Subject<ParticipantAction> = new Subject();
    private readonly absentEmitter: Subject<boolean> = new Subject();
    private readonly sportEmitter: Subject<string> = new Subject();

    constructor(readonly participant: Participant) {
        this.actionsProperty = this.actionsEmitter.asObservable();

        this.prename = participant.prename;
        this.surname = participant.surname;
        this.gender = participant.gender;
        this.address = participant.address;

        this.absent = participant.absent;
        this.absentProperty = this.absentEmitter.asObservable();

        this.sport = participant.sport ? participant.sport.name : undefined;
        this.sportProperty = this.sportEmitter.asObservable();
    }

    actionClicked(type: ParticipantAction): void {
        this.actionsEmitter.next(type);
        if (type === ParticipantAction.REPARTICIPATE) {
            this.isReparticipate = true;
        }
    }

    absentChanged(): void {
        this.absentEmitter.next(this.absent);
    }

    sportChanged(): void {
        this.sportEmitter.next(this.sport);
    }
}
