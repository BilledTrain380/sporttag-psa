import {Discipline} from '../athletics/athletics-models';
import {Group} from '../group/group-models';
import {Gender} from '../participant/participant-models';

/**
 * Describes a participation.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface Participation {
    readonly status: ParticipationStatus;
}

export enum ParticipationStatus {
    OPEN = 'OPEN',
    CLOSE = 'CLOSE',
    RESET = 'RESET',
}

/**
 * Describes a data set used to generate an event sheet.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface EventSheetData {
    readonly discipline: Discipline;
    readonly group: Group;
    readonly gender: Gender;
}

/**
 * A special sport which must exists. To refer this sport
 * use this variable.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export const ATHLETICS: string = 'Athletics';
