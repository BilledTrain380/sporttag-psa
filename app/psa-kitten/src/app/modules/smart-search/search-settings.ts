/**
 * Describes settings for the SmartSearch.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 *
 * @property matchFunction - Will be invoked to filter search results
 * @property toStringValue - Will be invoked to display search results
 * @property limit - The search result limit.
 */
export interface SearchSettings<T> {
    readonly matchFunction: (term: string, value: T) => boolean;
    readonly toStringValue: (value: T) => string;
    readonly placeHolder: string;
    readonly limit: number;
}
