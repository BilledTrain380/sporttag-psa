import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { GroupStatusType, OverviewGroupDto, SimpleGroupDto } from "../../../dto/group";
import { getLogger, Logger } from "../../logging";

import { API_ENDPOINT, ApiParameters } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class GroupApi {
  private log: Logger = getLogger("GroupApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  getGroupsOverview(parameters?: GroupOverviewParameters): Observable<ReadonlyArray<OverviewGroupDto>> {
    this.log.info(`Get groups overview: parameters=${parameters}`);

    const params = parameters?.buildParameters();

    return this.http.get<ReadonlyArray<OverviewGroupDto>>(`${API_ENDPOINT}/groups/overview`, {params});
  }

  getGroups(): Observable<ReadonlyArray<SimpleGroupDto>> {
    this.log.info("Get groups");

    return this.http.get<ReadonlyArray<SimpleGroupDto>>(`${API_ENDPOINT}/groups`);
  }

  getGroup(groupName: string): Observable<SimpleGroupDto> {
    this.log.info(`Get group: name=${groupName}`);

    return this.http.get<SimpleGroupDto>(`${API_ENDPOINT}/group/${groupName}`);
  }
}

export class GroupOverviewParameters implements ApiParameters {
  constructor(
    readonly statusType: GroupStatusType,
  ) {
  }

  buildParameters(): HttpParams {
    const params = new HttpParams();
    params.set("status_type", this.statusType);

    return params;
  }
}