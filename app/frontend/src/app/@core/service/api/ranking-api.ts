import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { GenderDto } from "../../../dto/participation";
import { DisciplineRanking, RankingData } from "../../../dto/ranking";
import { getLogger } from "../../logging";

import { API_ENDPOINT } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class RankingApi {
  private readonly log = getLogger("RankingApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  createTotalRanking(data: ReadonlyArray<GenderDto>): Observable<Blob> {
    this.log.info("Create total ranking:", data);

    const rankingData: RankingData = {
      discipline: [],
      total: data,
      triathlon: [],
      ubsCup: [],
    };

    return this.http.post(`${API_ENDPOINT}/ranking/download`, rankingData, {responseType: "blob"});
  }

  createTriathlonRanking(data: ReadonlyArray<GenderDto>): Observable<Blob> {
    this.log.info("Create triathlon ranking:", data);

    const rankingData: RankingData = {
      discipline: [],
      total: [],
      triathlon: data,
      ubsCup: [],
    };

    return this.http.post(`${API_ENDPOINT}/ranking/download`, rankingData, {responseType: "blob"});
  }

  createUbsCupRanking(data: ReadonlyArray<GenderDto>): Observable<Blob> {
    this.log.info("Create UBS-Cup ranking:", data);

    const rankingData: RankingData = {
      discipline: [],
      total: [],
      triathlon: [],
      ubsCup: data,
    };

    return this.http.post(`${API_ENDPOINT}/ranking/download`, rankingData, {responseType: "blob"});
  }

  createDisciplineRanking(data: ReadonlyArray<DisciplineRanking>): Observable<Blob> {
    this.log.info("Create discipline ranking: ", data);

    const rankingData: RankingData = {
      discipline: data,
      total: [],
      triathlon: [],
      ubsCup: [],
    };

    return this.http.post(`${API_ENDPOINT}/ranking/download`, rankingData, {responseType: "blob"});
  }
}
