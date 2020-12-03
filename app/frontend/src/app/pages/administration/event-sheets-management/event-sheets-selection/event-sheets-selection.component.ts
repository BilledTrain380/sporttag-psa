import { Component, OnDestroy, OnInit } from "@angular/core";
import { faDownload } from "@fortawesome/free-solid-svg-icons/faDownload";
import { Store } from "@ngrx/store";
import { Subject } from "rxjs";
import { map, takeUntil } from "rxjs/operators";

import { ALL_DISCIPLINES } from "../../../../dto/athletics";
import { EventSheetExport } from "../../../../dto/event-sheets";
import { BRENNBALL, GenderDto, genderDtoOfValue, SCHATZSUCHE, SportDto, VELO_ROLLERBLADES } from "../../../../dto/participation";
import { FEMALE, MALE } from "../../../../modules/participant/gender/gender-constants";
import { LABEL_ALL, TreeCheckNodeModel } from "../../../../modules/tree/tree-model";
import {
  downloadEventSheetsAction,
  downloadParticipantListAction,
  downloadStartlistAction,
  loadEventSheetDataAction,
} from "../../../../store/event-sheets/event-sheets.action";
import {
  selectEventSheetsGroups,
  selectIsEventSheetsDownloading,
  selectIsParticipantListDownloading,
  selectIsParticipationOpen,
  selectIsStartlistDownloading,
} from "../../../../store/event-sheets/event-sheets.selector";

@Component({
             selector: "app-event-sheets-selection",
             templateUrl: "./event-sheets-selection.component.html",
             styleUrls: ["./event-sheets-selection.component.scss"],
           })
export class EventSheetsSelectionComponent implements OnInit, OnDestroy {
  readonly faDownload = faDownload;

  readonly isParticipationOpen$ = this.store.select(selectIsParticipationOpen);
  readonly isParticipantListDownloading$ = this.store.select(selectIsParticipantListDownloading);
  readonly isEventSheetsDownloading$ = this.store.select(selectIsEventSheetsDownloading);
  readonly isStartlistDownloading$ = this.store.select(selectIsStartlistDownloading);

  eventSheetsTree?: TreeCheckNodeModel;

  private readonly eventSheetsTree$ = this.store.select(selectEventSheetsGroups)
    .pipe(map(groupNames => {
      const rootTree = TreeCheckNodeModel.newBuilder()
        .setLabel(LABEL_ALL)
        .setCollapsedEnabled(false)
        // tslint:disable-next-line:no-magic-numbers
        .setSplitting(3);

      ALL_DISCIPLINES.forEach(discipline => {
        const disciplineBuilder = TreeCheckNodeModel.newBuilder()
          .setLabel(discipline)
          .splitHalf();

        groupNames.forEach(groupName => {
          const groupBuilder = TreeCheckNodeModel.newBuilder()
            .setLabel(groupName)
            .setCollapsed(true)
            .addLeafNode(MALE, GenderDto.MALE)
            .addLeafNode(FEMALE, GenderDto.FEMALE);

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

  private readonly destroy$ = new Subject();

  constructor(
    private readonly store: Store,
  ) {
  }

  ngOnInit(): void {
    this.eventSheetsTree$
      .pipe(takeUntil(this.destroy$))
      .subscribe(tree => this.eventSheetsTree = tree);

    this.store.dispatch(loadEventSheetDataAction());
  }

  ngOnDestroy(): void {
    this.destroy$.complete();
  }

  downloadParticipantList(): void {
    const data: ReadonlyArray<SportDto> = this.participantListTree.checkedNodes
      .map(node => ({name: node.label}));

    this.store.dispatch(downloadParticipantListAction({data}));
  }

  downloadEventSheets(): void {
    const data: Array<EventSheetExport> = [];

    this.eventSheetsTree?.checkedNodes
      .forEach(disciplineNode => {
        disciplineNode.checkedNodes.forEach(groupNode => {
          groupNode.checkedNodes.forEach(genderNode => {
            data.push({
                        discipline: disciplineNode.value,
                        group: groupNode.value,
                        gender: genderDtoOfValue(genderNode.value),
                      });
          });
        });
      });

    this.store.dispatch(downloadEventSheetsAction({data}));
  }

  downloadStartlist(): void {
    this.store.dispatch(downloadStartlistAction());
  }
}
