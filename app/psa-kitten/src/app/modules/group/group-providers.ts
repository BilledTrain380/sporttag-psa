import {Inject, Injectable, InjectionToken} from '@angular/core';
import {Group} from './group-models';
import {groupJsonSchema, groupsJsonSchema} from './json-schemas';
import {HTTP_SERVICE, HttpService, REST_SERVICE, RestService} from '../http/http-service';

export const GROUP_PROVIDER: InjectionToken<GroupProvider> = new InjectionToken('Token for a group provider');

/**
 * Describes a provider which access data related to a Group.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface GroupProvider {

    /**
     * @param filter - filter options to filter the returned list
     *
     * @return all available Groups
     * @throws {AuthenticationError} if the response status is 401
     * @throws {Error} If the response is not ok.
     */
    getGroupList(filter?: { competitive?: boolean, pendingParticipation?: boolean }): Promise<Array<Group>>;

    /**
     * Gets the group with the given {@code name}.
     *
     * @param name - The name of the group
     *
     * @return the group matching the given name
     * @throws {AuthenticationError} if the response status is 401
     * @throws {ResourceNotFoundError} if the given {@code url} does not exist
     * @throws {Error} If the response is not ok.
     */
    getGroup(name: string): Promise<Group>;

    /**
     * Imports the the given file.
     *
     * @param file - The file to import
     *
     * @throws {AuthenticationError} if the response status is 401
     * @throws {BadRequestError} if the file is not valid
     * @throws {Error} If the response is not ok.
     */
    import(file: File): Promise<void>;
}

/**
 * Http implementation of a {@link GroupProvider}.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class HttpGroupProvider implements GroupProvider {

    constructor(
        @Inject(REST_SERVICE)
        private readonly rest: RestService,

        @Inject(HTTP_SERVICE)
        private readonly http: HttpService,
    ) {}

    async getGroupList(filter?: { competitive?: boolean, pendingParticipation?: boolean }): Promise<Array<Group>> {

        const params: string = (filter.competitive !== undefined ? `competitive=${filter.competitive}` : '') +
            (filter.pendingParticipation !== undefined ? `&pendingParticipation=${filter.pendingParticipation}` : '');

        const url: string = 'api/rest/groups' + (params === '' ? '' : `?${params}`);

        return this.rest.getRequest<Array<Group>>(url, groupsJsonSchema);
    }

    async getGroup(name: string): Promise<Group> {
        return this.rest.getRequest<Group>(`api/rest/group/${name}`, groupJsonSchema);
    }

    async import(file: File): Promise<void> {

        const formData: FormData = new FormData();
        formData.append('group-input', file);

        await this.http.postForm('api/web/import-group', formData);
    }
}
