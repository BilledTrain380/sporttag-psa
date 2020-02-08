import {NavigationEnd, Router} from '@angular/router';
import {Injectable, InjectionToken} from '@angular/core';

export const PREVIOUS_ROUTE_SERVICE: InjectionToken<PreviousRouteService> =
    new InjectionToken('token for previous route service');

/**
 * Describes a service to access the previous route.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface PreviousRouteService {
    readonly previousUrl: string;
}

/**
 * Saves the previous route in memory.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class InMemoryPreviousRouteService implements PreviousRouteService {

    private _previousUrl: string = '';
    private currentUrl: string = '';

    get previousUrl(): string {
        return this._previousUrl;
    }

    constructor(
        router: Router,
    ) {
        router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this._previousUrl = this.currentUrl;
                this.currentUrl = event.url;
            }
        });
    }
}
