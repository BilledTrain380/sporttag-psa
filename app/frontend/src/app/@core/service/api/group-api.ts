import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { NGXLogger } from "ngx-logger";
import { Observable } from "rxjs";

import { API_ENDPOINT } from "./pas-api";

export interface GroupDto {
  readonly name: string;
  readonly coach: string;
}

@Injectable({
  providedIn: "root",
})
export class GroupApi {
  constructor(
    private readonly http: HttpClient,
    private readonly log: NGXLogger,
  ) {
  }

  getGroups(options?: {
    readonly competitive?: boolean;
    readonly pendingParticipation?: boolean;
  }): Observable<ReadonlyArray<GroupDto>> {
    this.log.info(`Get groups: options=${options}`);

    const params = new HttpParams();

    if (options && options.competitive) {
      params.append("competitive", `${options.competitive}`);
    }

    if (options && options.pendingParticipation) {
      params.append("pendingParticipation", `${options.pendingParticipation}`);
    }

    return this.http.get<ReadonlyArray<GroupDto>>(`${API_ENDPOINT}/groups`, {params});
  }

  getGroup(groupName: string): Observable<GroupDto> {
    this.log.info(`Get group: name=${groupName}`);

    return this.http.get<GroupDto>(`${API_ENDPOINT}/group/${groupName}`);
  }
}
