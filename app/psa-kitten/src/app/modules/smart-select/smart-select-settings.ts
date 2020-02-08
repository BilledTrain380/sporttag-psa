/**
 * Defines settings for the smart select component.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 *
 * @property {(T) => string} onDisplay - Function which will be invoked to get a string presentation of the data item.
 */
export interface SmartSelectSettings<T> {
    readonly onDisplay: (value: T) => string;
}
