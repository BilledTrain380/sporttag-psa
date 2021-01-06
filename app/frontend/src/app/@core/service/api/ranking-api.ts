import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

import { GenderDto } from "../../../dto/participation";
import { DisciplineRanking, RankingData } from "../../../dto/ranking";
import { getLogger } from "../../logging";

import { API_ENDPOINT, SimpleFile } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class RankingApi {
  private readonly log = getLogger("RankingApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  createTotalRanking(data: ReadonlyArray<GenderDto>): Observable<SimpleFile> {
    this.log.info("Create total ranking:", data);

    const rankingData: RankingData = {
      disciplines: [],
      total: data,
      triathlon: [],
      ubsCup: [],
    };

    return this.http.post(`${API_ENDPOINT}/ranking/download`, rankingData, {responseType: "blob", observe: "response"})
      .pipe(map(response => SimpleFile.fromResponse(response)));
  }

  createTriathlonRanking(data: ReadonlyArray<GenderDto>): Observable<SimpleFile> {
    this.log.info("Create triathlon ranking:", data);

    const rankingData: RankingData = {
      disciplines: [],
      total: [],
      triathlon: data,
      ubsCup: [],
    };

    return this.http.post(`${API_ENDPOINT}/ranking/download`, rankingData, {responseType: "blob", observe: "response"})
      .pipe(map(response => SimpleFile.fromResponse(response)));
  }

  createUbsCupRanking(data: ReadonlyArray<GenderDto>): Observable<SimpleFile> {
    this.log.info("Create UBS-Cup ranking:", data);

    const rankingData: RankingData = {
      disciplines: [],
      total: [],
      triathlon: [],
      ubsCup: data,
    };

    return this.http.post(`${API_ENDPOINT}/ranking/download`, rankingData, {responseType: "blob", observe: "response"})
      .pipe(map(response => SimpleFile.fromResponse(response)));
  }

  createDisciplineRanking(data: ReadonlyArray<DisciplineRanking>): Observable<SimpleFile> {
    this.log.info("Create discipline ranking: ", data);

    const rankingData: RankingData = {
      disciplines: data,
      total: [],
      triathlon: [],
      ubsCup: [],
    };

    return this.http.post(`${API_ENDPOINT}/ranking/download`, rankingData, {responseType: "blob", observe: "response"})
      .pipe(map(response => SimpleFile.fromResponse(response)));
  }
}
