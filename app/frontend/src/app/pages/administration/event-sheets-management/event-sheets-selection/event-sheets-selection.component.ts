import { Component, OnInit } from "@angular/core";
import { faDownload } from "@fortawesome/free-solid-svg-icons/faDownload";
import { Store } from "@ngrx/store";
import { map } from "rxjs/operators";

import { ALL_DISCIPLINES } from "../../../../dto/athletics";
import { BRENNBALL, SCHATZSUCHE, SportDto, VELO_ROLLERBLADES } from "../../../../dto/participation";
import { FEMALE, MALE } from "../../../../modules/participant/gender/gender-constants";
import { LABEL_ALL, TreeCheckNodeModel } from "../../../../modules/tree/tree-model";
import {
  downloadEventSheetsAction,
  downloadParticipantListAction,
  downloadStartlistAction,
  loadEventSheetDataAction
} from "../../../../store/event-sheets/event-sheets.action";
import {
  selectEventSheetsGroups,
  selectIsEventSheetsDowloading,
  selectIsParticipantListDownloading,
  selectIsParticipationOpen,
  selectIsStartlistDownloading,
} from "../../../../store/event-sheets/event-sheets.selector";

@Component({
             selector: "app-event-sheets-selection",
             templateUrl: "./event-sheets-selection.component.html",
             styleUrls: ["./event-sheets-selection.component.scss"],
           })
export class EventSheetsSelectionComponent implements OnInit {
  readonly faDownload = faDownload;

  readonly isParticipationOpen$ = this.store.select(selectIsParticipationOpen);
  readonly isParticipantListDownloading$ = this.store.select(selectIsParticipantListDownloading);
  readonly isEventSheetsDownloading$ = this.store.select(selectIsEventSheetsDowloading);
  readonly isStartlistDownloading$ = this.store.select(selectIsStartlistDownloading);

  readonly eventSheetsTree$ = this.store.select(selectEventSheetsGroups)
    .pipe(map(groups => {

      const rootTree = TreeCheckNodeModel.newBuilder()
        .setLabel(LABEL_ALL)
        .setCollapsedEnabled(false)
        // tslint:disable-next-line:no-magic-numbers
        .setSplitting(3);

      ALL_DISCIPLINES.forEach(discipline => {
        const disciplineBuilder = TreeCheckNodeModel.newBuilder()
          .setLabel(discipline)
          .splitHalf();

        groups.forEach(group => {
          const groupBuilder = TreeCheckNodeModel.newBuilder()
            .setLabel(group.name)
            .setCollapsed(true)
            .addLeafNode(MALE)
            .addLeafNode(FEMALE);

          disciplineBuilder.addNode(groupBuilder);
        });

        rootTree.addNode(disciplineBuilder);
      });

      return rootTree.build();
    }));

  readonly participantListTree: TreeCheckNodeModel = TreeCheckNodeModel.newBuilder()
    .setLabel(LABEL_ALL)
    .setCollapsedEnabled(false)
    .addLeafNode(VELO_ROLLERBLADES)
    .addLeafNode(SCHATZSUCHE)
    .addLeafNode(BRENNBALL)
    .build();

  constructor(
    private readonly store: Store,
  ) {
  }

  ngOnInit(): void {
    this.store.dispatch(loadEventSheetDataAction());
  }

  downloadParticipantList(): void {
    const data: ReadonlyArray<SportDto> = this.participantListTree.flatNodes
      .map(node => ({name: node.label}));

    this.store.dispatch(downloadParticipantListAction({data}));
  }

  downloadEventSheets(): void {
    this.store.dispatch(downloadEventSheetsAction({data: []}));
  }

  downloadStartlist(): void {
    this.store.dispatch(downloadStartlistAction());
  }
}
