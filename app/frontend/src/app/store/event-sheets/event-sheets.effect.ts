import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY, forkJoin } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";

import { getLogger } from "../../@core/logging";
import { GroupApi, OverviewGroupsParameters } from "../../@core/service/api/group-api";
import { ParticipationApi } from "../../@core/service/api/participation-api";
import { DownloadService } from "../../@core/service/download-service";
import { GroupStatusType } from "../../dto/group";
import { ParticipationStatusType } from "../../dto/participation";

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
    .pipe(switchMap(() => {
                      const parameters = new OverviewGroupsParameters(GroupStatusType.GROUP_TYPE_COMPETITIVE);

                      return forkJoin([this.groupApi.getOverviewGroups(parameters), this.participationApi.getParticipationStatus()])
                        .pipe(map(groupsAndStatus => {
                          const overviewGroups = groupsAndStatus[0];
                          const isParticipationOpen = groupsAndStatus[1].type === ParticipationStatusType.OPEN;

                          this.log.info("Successfully loaded groups, size =", overviewGroups.length);

                          const groups = overviewGroups.map(overviewGroup => overviewGroup.group);

                          return setEventSheetsDataAction({groups, isParticipationOpen});
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not load groups", err);

                          return EMPTY;
                        }));
                    },
    )));

  readonly downloadParticipantList = createEffect(() => this.actions
    .pipe(ofType(downloadParticipantListAction.type))
    .pipe(switchMap((action: DownloadParticipantListProps) =>
                      this.participationApi.createParticipantList(action.data)
                        .pipe(map(binary => {
                          this.downloadService.downloadBinary(binary, "participant-list.zip");

                          return finishParticipantFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download participant lint", err);

                          return EMPTY;
                        })),
    )));

  readonly downloadEventSheets = createEffect(() => this.actions
    .pipe(ofType(downloadEventSheetsAction.type))
    .pipe(switchMap((action: DownloadEventSheetsProps) =>
                      this.participationApi.createEventSheets(action.data)
                        .pipe(map(binary => {
                          this.downloadService.downloadBinary(binary, "event-sheets.zip");

                          return finishEventSheetsFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download event sheets", err);

                          return EMPTY;
                        })),
    )));

  readonly downloadStartlist = createEffect(() => this.actions
    .pipe(ofType(downloadStartlistAction.type))
    .pipe(switchMap(() =>
                      this.participationApi.createStartlist()
                        .pipe(map(binary => {
                          this.downloadService.downloadBinary(binary, "startlist.pdf");

                          return finishStartlistFileAction();
                        }))
                        .pipe(catchError(err => {
                          this.log.warn("Could not download startlist", err);

                          return EMPTY;
                        })),
    )));

  private readonly log = getLogger("EventSheetsEffects");

  constructor(
    private readonly actions: Actions,
    private readonly groupApi: GroupApi,
    private readonly participationApi: ParticipationApi,
    private readonly downloadService: DownloadService,
  ) {
  }
}
