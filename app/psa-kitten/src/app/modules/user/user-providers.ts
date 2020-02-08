import {Inject, Injectable, InjectionToken} from '@angular/core';
import {User} from './user-models';
import {REST_SERVICE, RestService} from '../http/http-service';
import {userJsonSchema, userListJsonSchema} from './json-schema';
import {Observable} from 'rxjs';
import {NbAuthOAuth2JWTToken, NbAuthService} from '@nebular/auth';
import {filter, map} from 'rxjs/operators';

export const USER_PROVIDER: InjectionToken<UserProvider> = new InjectionToken('Token for user provider');
export const USER_SUPPLIER: InjectionToken<UserSupplier> = new InjectionToken('Token for user supplier');

/**
 * Provides user operations.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface UserProvider {

    createUser(user: User, password: string): Promise<void>;

    getUsers(): Promise<ReadonlyArray<User>>;

    getUser(id: number): Promise<User>;

    updateUser(user: User): Promise<void>;

    updateUserPassword(user: User, password: string): Promise<void>;

    deleteUser(user: User): Promise<void>;
}

/**
 * Supplier for various user data.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface UserSupplier {

    getActiveUser(): Observable<User>;
}

/**
 * User provider implementation over http connection.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class HttpUserProvider implements UserProvider {

    constructor(
        @Inject(REST_SERVICE)
        private readonly rest: RestService,
    ) {}

    async createUser(user: User, password: string): Promise<void> {

        const body: object = {
            username: user.username,
            enabled: user.enabled,
            password,
        };

        await this.rest.postRequest('api/rest/users', JSON.stringify(body));
    }

    async deleteUser(user: User): Promise<void> {
        await this.rest.deleteRequest(`api/rest/user/${user.id}`);
    }

    async getUser(id: number): Promise<User> {
        return await this.rest.getRequest<User>(`api/rest/user/${id}`, userJsonSchema);
    }

    async getUsers(): Promise<ReadonlyArray<User>> {
        return await this.rest.getRequest<ReadonlyArray<User>>('api/rest/users', userListJsonSchema);
    }

    async updateUser(user: User): Promise<void> {

        const body: object = {
            username: user.username,
            enabled: user.enabled,
        };

        await this.rest.patchRequest(`api/rest/user/${user.id}`, JSON.stringify(body));
    }

    async updateUserPassword(user: User, password: string): Promise<void> {

        const body: object = {
            password,
        };

        await this.rest.putRequest(`api/rest/user/${user.id}`, JSON.stringify(body));
    }
}

/**
 * Supplies user data based on a JWT.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class JWTUserSupplier implements UserSupplier {

    constructor(
        private readonly authService: NbAuthService,
    ) {}

    getActiveUser(): Observable<User> {

        return this.authService.onTokenChange()
            .pipe(filter(it => (it instanceof NbAuthOAuth2JWTToken)))
            .pipe(map<NbAuthOAuth2JWTToken, User>(it => {

                const payload: any = it.getAccessTokenPayload();

                return {
                    id: payload.user_id,
                    username: payload.user_name,
                    enabled: true, // can not be false, because the user would not be able to log in then
                };
            }));
    }
}
