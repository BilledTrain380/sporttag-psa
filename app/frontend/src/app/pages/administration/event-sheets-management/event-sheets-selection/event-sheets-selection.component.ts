import { Component, OnInit } from "@angular/core";
import { faDownload } from "@fortawesome/free-solid-svg-icons/faDownload";
import { Store } from "@ngrx/store";
import { map } from "rxjs/operators";

import { ALL_DISCIPLINES } from "../../../../dto/athletics";
import { BRENNBALL, ParticipationStatusType, SCHATZSUCHE, SportDto, VELO_ROLLERBLADES } from "../../../../dto/participation";
import { FEMALE, MALE } from "../../../../modules/participant/gender/gender-constants";
import { LABEL_ALL, TreeCheckNodeModel } from "../../../../modules/tree/tree-model";
import { selectParticipationStatus } from "../../../../store/athletics/athletics.selector";
import {
  downloadEventSheetsAction,
  downloadParticipantListAction,
  downloadStartlistAction,
  loadEventSheetDataAction
} from "../../../../store/event-sheets/event-sheets.action";
import { selectEventSheetsGroups, selectIsParticipantListDownloading } from "../../../../store/event-sheets/event-sheets.selector";

@Component({
             selector: "app-event-sheets-selection",
             templateUrl: "./event-sheets-selection.component.html",
             styleUrls: ["./event-sheets-selection.component.scss"],
           })
export class EventSheetsSelectionComponent implements OnInit {
  readonly faDownload = faDownload;

  readonly isParticipationClosed$ = this.store.select(selectParticipationStatus)
    .pipe(map(status => status.type === ParticipationStatusType.CLOSED));
  readonly isParticipantListDownloading$ = this.store.select(selectIsParticipantListDownloading);

  readonly eventSheetsTree$ = this.store.select(selectEventSheetsGroups)
    .pipe(map(groups => {

      const rootTree = TreeCheckNodeModel.newBuilder()
        .setLabel(LABEL_ALL)
        .setCollapsedEnabled(false)
        .setSplitter(3);

      ALL_DISCIPLINES.forEach(discipline => {
        const disciplineBuilder = TreeCheckNodeModel.newBuilder()
          .setLabel(discipline)
          .setSplitter(2);

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
