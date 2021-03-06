import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { mapTo } from "rxjs/operators";

import { GroupStatusType, OverviewGroupDto, SimpleGroupDto } from "../../../dto/group";
import { getLogger, Logger } from "../../logging";

import { ApiParameters, API_ENDPOINT } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class GroupApi {
  private readonly log: Logger = getLogger("GroupApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  getOverviewGroups(parameters?: OverviewGroupsParameters): Observable<ReadonlyArray<OverviewGroupDto>> {
    this.log.info("Get groups overview: parameters =", parameters);

    const params = parameters?.buildParameters();

    return this.http.get<ReadonlyArray<OverviewGroupDto>>(`${API_ENDPOINT}/groups/overview`, {params});
  }

  getGroups(): Observable<ReadonlyArray<SimpleGroupDto>> {
    this.log.info("Get groups");

    return this.http.get<ReadonlyArray<SimpleGroupDto>>(`${API_ENDPOINT}/groups`);
  }

  getGroup(groupName: string): Observable<SimpleGroupDto> {
    this.log.info("Get group: name =", groupName);

    return this.http.get<SimpleGroupDto>(`${API_ENDPOINT}/group/${groupName}`);
  }

  importGroups(file: File): Observable<void> {
    this.log.info("Import groups with file:", file.name);

    const formData = new FormData();
    formData.append("group-input", file);

    return this.http.post(`${API_ENDPOINT}/groups/import`, formData, {responseType: "text"})
      .pipe(mapTo(undefined));
  }
}

export class OverviewGroupsParameters implements ApiParameters {
  constructor(
    readonly statusType: GroupStatusType,
  ) {
  }

  buildParameters(): HttpParams {
    return new HttpParams()
      .set("status_type", this.statusType);
  }
}
