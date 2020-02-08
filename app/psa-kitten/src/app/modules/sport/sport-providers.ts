import {Sport} from './sport-models';
import {Inject, Injectable, InjectionToken} from '@angular/core';
import {REST_SERVICE, RestService} from '../http/http-service';
import {sportListJsonSchema} from './json-schemas';

export const SPORT_PROVIDER: InjectionToken<SportProvider> = new InjectionToken('token for sport provider');

/**
 * Describes a provider for sport types.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface SportProvider {

    /**
     * @return all sport types
     * @throws {AuthenticationError} if the response status is 401
     * @throws {Error} If the response is not ok.
     */
    getAll(): Promise<Array<Sport>>;

    /**
     * Gets the sport type matching the given {@code name}.
     *
     * @param name - The name of the sport type.
     *
     * @return the resulting sport type
     * @throws {AuthenticationError} if the response status is 401
     * @throws {Error} If the response is not ok.
     */
    getOne(name: string): Promise<Sport>;
}

/**
 * Http implementation of {@link SportProvider}.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class HttpSportProvider implements SportProvider {

    constructor(
        @Inject(REST_SERVICE)
        private readonly rest: RestService,
    ) {}

    async getAll(): Promise<Array<Sport>> {
        return this.rest.getRequest<Array<Sport>>('api/rest/sports', sportListJsonSchema);
    }

    getOne(name: string): Promise<Sport> {
        throw new Error('Not implemented yet');
    }
}
