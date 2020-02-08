import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {NbRoleProvider} from '@nebular/security';
import {NbAuthOAuth2JWTToken, NbAuthService, NbAuthSimpleToken} from '@nebular/auth';
import {map, tap} from 'rxjs/operators';

/**
 * Defines the match of the route in {@link RouteSecurity}.
 *
 * - FULL means the whole route has to be exactly the same
 * - START means the route only has to start the same, but can differ towards the end.
 *   This is useful to match path parameter routes.
 */
enum RouteMatch {
    FULL = 'FULL',
    START = 'START',
}

interface RouteSecurity {
    readonly route: string;
    readonly match: RouteMatch;
    readonly authority: string;
}

const routeSecurity: Array<RouteSecurity> = [{
        route: '/pages/group/overview',
        match: RouteMatch.FULL,
        authority: 'ROLE_USER',
    }, {
        route: '/pages/group/detail',
        match: RouteMatch.START,
        authority: 'ROLE_ADMIN',
    }, {
        route: '/pages/athletics/results',
        match: RouteMatch.FULL,
        authority: 'ROLE_USER',
    }, {
        route: '/pages/athletics/ranking',
        match: RouteMatch.FULL,
        authority: 'ROLE_ADMIN',
    }, {
        route: '/pages/management',
        match: RouteMatch.FULL,
        authority: 'ROLE_ADMIN',
    }, {
        route: '/pages/settings',
        match: RouteMatch.START,
        authority: 'ROLE_ADMIN',
    }, {
        route: '/pages/event',
        match: RouteMatch.FULL,
        authority: 'ROLE_ADMIN',
    }, {
        route: '/pages/miscellaneous',
        match: RouteMatch.START,
        authority: 'ROLE_USER',
    },
];

/**
 * Route guard which only allows the navigation if the user is authenticated.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild {

    constructor(
        private readonly authService: NbAuthService,
        private readonly router: Router,
    ) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot,
    ): Observable<boolean> | Promise<boolean> | boolean {

        return this.authService.isAuthenticated()
            .pipe(tap(authenticated => {
                if (!authenticated)
                    this.router.navigate(['auth/login']);
            }));
    }

    canActivateChild(
        childRoute: ActivatedRouteSnapshot,
        state: RouterStateSnapshot,
    ): Observable<boolean> | Promise<boolean> | boolean {
        return this.canActivate(childRoute, state);
    }
}

/**
 * Route guard which only allows the navigation if the user has the required role.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class RoleGuard implements CanActivate, CanActivateChild {

    constructor(
        private readonly authService: NbAuthService,
    ) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot,
    ): Observable<boolean> | Promise<boolean> | boolean {

        return this.authService.getToken()
            .pipe(map((it: NbAuthOAuth2JWTToken) => {

                const config: any = routeSecurity.find(security => {
                    if (security.match === RouteMatch.FULL && security.route === state.url) return true;
                    return security.match === RouteMatch.START && state.url.startsWith(security.route);

                });

                if (config === undefined) return false;

                const authorities: Array<string> = it.getAccessTokenPayload().authorities;

                return authorities.some(authority => authority === config.authority);
            }));
        // TODO: maybe redirect to forbidden page
    }

    canActivateChild(
        childRoute: ActivatedRouteSnapshot,
        state: RouterStateSnapshot,
    ): Observable<boolean> | Promise<boolean> | boolean {
        return this.canActivate(childRoute, state);
    }
}

/**
 *
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class RoleProvider implements NbRoleProvider {

    constructor(
        private readonly authService: NbAuthService,
    ) {}

    getRole(): Observable<string | string[]> {

        return this.authService.onTokenChange()
            .pipe(map((it: NbAuthSimpleToken) => {
                // console.log(it);

                if (it instanceof NbAuthOAuth2JWTToken) {
                    return it.getAccessTokenPayload()['authorities'];
                }

                return [];
            }));
    }
}
