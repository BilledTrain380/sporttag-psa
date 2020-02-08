import {Group} from '../group/group-models';
import {Sport} from '../sport/sport-models';

/**
 *
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface Participant {
    readonly id: number;
    readonly surname: string;
    readonly prename: string;
    readonly gender: Gender;
    readonly birthday: number;
    readonly absent: boolean;
    readonly address: string;
    readonly town: Town;
    readonly group: Group;
    readonly sport?: Sport;
}

/**
 *
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
}

/**
 *
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface Town {
    readonly zip: string;
    readonly name: string;
}
