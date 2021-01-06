import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY, of } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger } from "../../@core/logging";
import { EventSheetApi } from "../../@core/service/api/event-sheet-api";
import { DownloadService } from "../../@core/service/download-service";

import {
  downloadEventSheetsAction,
  DownloadEventSheetsProps,
  downloadParticipantListAction,
  DownloadParticipantListProps,
  downloadStartlistAction,
  finishEventSheetsFileAction,
  finishParticipantFileAction,
  finishStartlistFileAction,
  loadEventSheetDataAction,
  setEventSheetsDataAction,
} from "./event-sheets.action";

@Injectable()
export class EventSheetsEffects {
  readonly loadEventSheetsData = createEffect(() => this.actions
    .pipe(ofType(loadEventSheetDataAction.type))
    .pipe(switchMap(() =>
                      this.eventSheetApi.getData()
                        .pipe(map(eventSheetData => {
                          this.log.info("Successfully event sheet data, groupSize =", eventSheetData.groups.length);

                          return setEventSheetsDataAction({
                                                            groupNames: eventSheetData.groups,
                                                            isParticipationOpen: eventSheetData.participationOpen,
                                                          });
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not load groups", err);

                          return EMPTY;
                        })),
    )));

  readonly downloadParticipantList = createEffect(() => this.actions
    .pipe(ofType(downloadParticipantListAction.type))
    .pipe(switchMap((action: DownloadParticipantListProps) =>
                      this.eventSheetApi.createParticipantList(action.data)
                        .pipe(map(file => {
                          this.downloadService.downloadBinary(file.binary, file.name);

                          return finishParticipantFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download participant lint", err);

                          return of(finishParticipantFileAction());
                        })),
    )));

  readonly downloadEventSheets = createEffect(() => this.actions
    .pipe(ofType(downloadEventSheetsAction.type))
    .pipe(switchMap((action: DownloadEventSheetsProps) =>
                      this.eventSheetApi.createEventSheets(action.data)
                        .pipe(map(file => {
                          this.downloadService.downloadBinary(file.binary, file.name);

                          return finishEventSheetsFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download event sheets", err);

                          return of(finishEventSheetsFileAction());
                        })),
    )));

  readonly downloadStartlist = createEffect(() => this.actions
    .pipe(ofType(downloadStartlistAction.type))
    .pipe(switchMap(() =>
                      this.eventSheetApi.createStartlist()
                        .pipe(map(file => {
                          this.downloadService.downloadBinary(file.binary, file.name);

                          return finishStartlistFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download startlist", err);

                          return of(finishStartlistFileAction());
                        })),
    )));

  private readonly log = getLogger("EventSheetsEffects");

  constructor(
    private readonly actions: Actions,
    private readonly eventSheetApi: EventSheetApi,
    private readonly downloadService: DownloadService,
  ) {
  }
}
