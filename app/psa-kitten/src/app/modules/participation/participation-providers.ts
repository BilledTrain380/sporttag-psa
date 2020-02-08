import {EventSheetData, Participation, ParticipationStatus} from './participation-models';
import {Inject, Injectable, InjectionToken} from '@angular/core';
import {REST_SERVICE, RestService} from '../http/http-service';
import {participationStatusJsonSchema} from './json-schemas';
import {Sport} from '../sport/sport-models';
import {FileQualifier} from '../http/http-models';
import {fileQualifierJsonSchema} from '../http/json-schema';

export const PARTICIPATION_PROVIDER: InjectionToken<ParticipationProvider> =
    new InjectionToken('token for participation provider');

export const PARTICIPATION_FILE_PROVIDER: InjectionToken<ParticipationFileProvider> =
    new InjectionToken('token for participation file provider');

/**
 * Describes a provider for the participation.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface ParticipationProvider {

    /**
     * @return the participation status
     * @throws {AuthenticationError} if the response status is 401
     * @throws {Error} If the response is not ok.
     */
    getStatus(): Promise<ParticipationStatus>;

    /**
     * Closes the paritcipation.
     *
     * @throws {AuthenticationError} if the response status is 401
     * @throws {Error} If the response is not ok.
     */
    close(): Promise<void>;

    /**
     * Resets the participation which causes all recorded data to be deleted.
     *
     * @throws {AuthenticationError} if the response status is 401
     * @throws {Error} If the response is not ok.
     */
    reset(): Promise<void>;
}

/**
 * Describes a provider to create various files
 * regarding the participation.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export interface ParticipationFileProvider {

    /**
     * Creates participant list files for the given {@code sports}.
     *
     * @param sports - the sports to create a participant list of
     *
     * @return a file qualifier to identify the file
     */
    createParticipantList(sports: ReadonlyArray<Sport>): Promise<FileQualifier>;

    /**
     * Creates event sheets for the given {@code data}.
     *
     * @param data - the data to create the event sheets
     *
     * @return a file qualifier to identify the file
     */
    createEventSheets(data: ReadonlyArray<EventSheetData>): Promise<FileQualifier>;
}

/**
 * Http implementation of {@link ParticipationProvider}.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class HttpParticipationProvider implements ParticipationProvider {

    constructor(
        @Inject(REST_SERVICE)
        private readonly rest: RestService,
    ) {}

    async close(): Promise<void> {
        await this.update(ParticipationStatus.CLOSE);
    }

    async getStatus(): Promise<ParticipationStatus> {

        const participation: Participation = await this.rest.getRequest<Participation>(
            'api/rest/participation',
            participationStatusJsonSchema,
        );

        return participation.status;
    }

    async reset(): Promise<void> {
        await this.update(ParticipationStatus.RESET);
    }

    private update(status: ParticipationStatus): Promise<void> {

        const body: Participation = {
            status,
        };

        return this.rest.patchRequest('api/rest/participation', JSON.stringify(body));
    }
}

/**
 * {@link ParticipationFileProvider} implementation over http connection.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
@Injectable()
export class HttpParticipationFileProvider implements ParticipationFileProvider {

    constructor(
        @Inject(REST_SERVICE)
        private readonly rest: RestService,
    ) {}

    async createEventSheets(data: ReadonlyArray<EventSheetData>): Promise<FileQualifier> {

        const body = data.map(it => ({
            discipline: it.discipline.name,
            group: it.group.name,
            gender: it.gender,
        }));

        return await this.rest.postRequest<FileQualifier>(
            'api/web/event-sheets',
            JSON.stringify(body),
            fileQualifierJsonSchema);
    }

    async createParticipantList(sports: ReadonlyArray<Sport>): Promise<FileQualifier> {

        return await this.rest.postRequest<FileQualifier>(
            'api/web/participant-list',
            JSON.stringify(sports),
            fileQualifierJsonSchema);
    }
}
