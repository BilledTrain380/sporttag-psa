import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

import { CompetitorDto, ResultDto, ResultElement } from "../../../dto/athletics";
import { GenderDto } from "../../../dto/participation";
import { ifNotNullOrUndefined } from "../../lib/lib";
import { getLogger, Logger } from "../../logging";

import { API_ENDPOINT, ApiParameters } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class CompetitorApi {
  private readonly log: Logger = getLogger("CompetitorApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  getCompetitors(parameters?: CompetitorParameters): Observable<ReadonlyArray<CompetitorDto>> {
    this.log.info("Get competitors: parameters =", parameters);

    const params = parameters?.buildParameters();

    return this.http.get<ReadonlyArray<CompetitorDto>>(`${API_ENDPOINT}/competitors`, {params})
      .pipe(map(competitors => competitors.map(enhanceResultObject)));
  }

  getCompetitor(id: number): Observable<CompetitorDto> {
    this.log.info("Get competitor: id", id);

    return this.http.get<CompetitorDto>(`${API_ENDPOINT}/competitor/${id}`);
  }

  updateCompetitorResult(competitorId: number, result: ResultElement): Observable<ResultDto> {
    this.log.info("Update competitor result: competitorId =", competitorId, "result =", result);

    return this.http.put<ResultDto>(`${API_ENDPOINT}/competitor/${competitorId}`, result);
  }
}

export class CompetitorParameters implements ApiParameters {
  constructor(
    private readonly group?: string,
    private readonly gender?: GenderDto,
    private readonly absent?: boolean,
  ) {
    if (!group && !gender && !absent) {
      throw new Error("All parameters are undefined. Declare at least one parameter");
    }
  }

  buildParameters(): HttpParams {
    const params = new HttpParams();

    ifNotNullOrUndefined(this.group, group => params.set("group", group));
    ifNotNullOrUndefined(this.gender, gender => params.set("gender", gender));
    ifNotNullOrUndefined(this.absent, absent => params.set("absent", `${absent}`));

    return params;
  }
}

/**
 * Since the json for the results is just a plain javascript object,
 * this functions converts it to a actual `Map`.
 *
 * @param dto the dto to enhance
 */
// tslint:disable-next-line:no-any
function enhanceResultObject(dto: any): CompetitorDto {
  dto.results = new Map(Object.entries(dto.results));

  return dto;
}
