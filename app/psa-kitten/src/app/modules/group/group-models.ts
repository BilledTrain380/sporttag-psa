
/**
 * Represents a coach object.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface Coach {
    readonly id: number;
    readonly name: string;
}

/**
 * Represents a group object.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface Group {
    readonly name: string;
    readonly coach: Coach;
}
