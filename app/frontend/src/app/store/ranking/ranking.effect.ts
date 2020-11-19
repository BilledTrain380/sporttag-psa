import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY, of } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger } from "../../@core/logging";
import { ParticipationApi } from "../../@core/service/api/participation-api";
import { RankingApi } from "../../@core/service/api/ranking-api";
import { DownloadService } from "../../@core/service/download-service";
import { ParticipationStatusType } from "../../dto/participation";

import {
  downloadDisciplineRankingAction,
  DownloadDisciplineRankingProps,
  downloadTotalRankingAction,
  DownloadTotalRankingProps,
  downloadTriathlonRankingAction,
  DownloadTriathlonRankingProps,
  downloadUbsCupRankingAction,
  DownloadUbsCupRankingProps,
  finishDisciplineRankingFileAction,
  finishTotalRankingFileAction,
  finishTriathlonRankingFileAction,
  finishUbsCupRankingFileAction,
  loadRankingData,
  setRankingDataAction,
} from "./ranking.action";

@Injectable({
              providedIn: "root",
            })
export class RankingEffects {
  readonly loadRankingData$ = createEffect(() => this.actions
    .pipe(ofType(loadRankingData.type))
    .pipe(switchMap(() =>
                      this.participationApi.getParticipationStatus()
                        .pipe(map(status => {
                          this.log.info("Successfully loaded participation status:", status.type);

                          const isParticipationOpen = status.type === ParticipationStatusType.OPEN;

                          return setRankingDataAction({isParticipationOpen});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not load participation status", err);

                          return EMPTY;
                        })),
    )));

  readonly downloadTotalRanking = createEffect(() => this.actions
    .pipe(ofType(downloadTotalRankingAction.type))
    .pipe(switchMap((action: DownloadTotalRankingProps) =>
                      this.rankingApi.createTotalRanking(action.genders)
                        .pipe(map(binary => {
                          this.downloadService.downloadBinary(binary, "total-ranking.zip");

                          return finishTotalRankingFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download total ranking", err);

                          return of(finishTotalRankingFileAction());
                        })),
    )));

  readonly downloadTriathlonRanking = createEffect(() => this.actions
    .pipe(ofType(downloadTriathlonRankingAction.type))
    .pipe(switchMap((action: DownloadTriathlonRankingProps) =>
                      this.rankingApi.createTriathlonRanking(action.genders)
                        .pipe(map(binary => {
                          this.downloadService.downloadBinary(binary, "triathlon-ranking.zip");

                          return finishTriathlonRankingFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download triathlon ranking", err);

                          return of(finishTriathlonRankingFileAction());
                        })),
    )));

  readonly downloadUbsCupRanking = createEffect(() => this.actions
    .pipe(ofType(downloadUbsCupRankingAction.type))
    .pipe(switchMap((action: DownloadUbsCupRankingProps) =>
                      this.rankingApi.createUbsCupRanking(action.genders)
                        .pipe(map(binary => {
                          this.downloadService.downloadBinary(binary, "ubs-cup-ranking.zip");

                          return finishUbsCupRankingFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download UBS-Cup ranking", err);

                          return of(finishUbsCupRankingFileAction());
                        })),
    )));

  readonly downloadDisciplineRanking = createEffect(() => this.actions
    .pipe(ofType(downloadDisciplineRankingAction.type))
    .pipe(switchMap((action: DownloadDisciplineRankingProps) =>
                      this.rankingApi.createDisciplineRanking(action.disciplines)
                        .pipe(map(binary => {
                          this.downloadService.downloadBinary(binary, "discipline-ranking.zip");

                          return finishDisciplineRankingFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download discipline ranking", err);

                          return of(finishDisciplineRankingFileAction());
                        })),
    )));

  private readonly log = getLogger("RankingEffects");

  constructor(
    private readonly actions: Actions,
    private readonly participationApi: ParticipationApi,
    private readonly rankingApi: RankingApi,
    private readonly downloadService: DownloadService,
  ) {
  }
}
