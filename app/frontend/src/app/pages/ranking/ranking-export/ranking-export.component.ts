import { Component, OnInit } from "@angular/core";
import { faDownload } from "@fortawesome/free-solid-svg-icons/faDownload";
import { Store } from "@ngrx/store";

import { flatMapping } from "../../../@core/lib/collections";
import { ALL_DISCIPLINES } from "../../../dto/athletics";
import { GenderDto, genderDtoOfValue } from "../../../dto/participation";
import { DisciplineRanking } from "../../../dto/ranking";
import { FEMALE, MALE } from "../../../modules/participant/gender/gender-constants";
import { LABEL_ALL, TreeCheckNodeModel } from "../../../modules/tree/tree-model";
import {
  downloadDisciplineRankingAction,
  downloadTotalRankingAction,
  downloadTriathlonRankingAction,
  downloadUbsCupRankingAction,
  loadRankingData,
} from "../../../store/ranking/ranking.action";
import {
  selectIsDisciplineRankingDownloading,
  selectIsParticipationOpen,
  selectIsTotalRankingDownloading,
  selectIsTriathlonRankingDownloading,
  selectIsUbsCupRankingDownloading,
} from "../../../store/ranking/ranking.selector";

const GENDER_TREE_BUILDER = TreeCheckNodeModel.newBuilder()
  .setLabel(LABEL_ALL)
  .setCollapsedEnabled(false)
  .addLeafNode(MALE, GenderDto.MALE)
  .addLeafNode(FEMALE, GenderDto.FEMALE);

@Component({
             selector: "app-ranking-export",
             templateUrl: "./ranking-export.component.html",
             styleUrls: ["./ranking-export.component.scss"],
           })
export class RankingExportComponent implements OnInit {
  readonly faDownload = faDownload;

  readonly isParticipationOpen$ = this.store.select(selectIsParticipationOpen);

  readonly totalTree = GENDER_TREE_BUILDER.build();
  readonly isTotalRankingDownloading = this.store.select(selectIsTotalRankingDownloading);

  readonly triathlonTree = GENDER_TREE_BUILDER.build();
  readonly isTriathlonRankingDownloading = this.store.select(selectIsTriathlonRankingDownloading);

  readonly ubsCupTree = GENDER_TREE_BUILDER.build();
  readonly isUbsCupRankingDownloading = this.store.select(selectIsUbsCupRankingDownloading);

  readonly disciplinesTree = TreeCheckNodeModel.newBuilder()
    .setLabel(LABEL_ALL)
    .setCollapsedEnabled(false)
    // tslint:disable-next-line:no-magic-numbers
    .setSplitting(4)
    .addNodes(ALL_DISCIPLINES.map(discipline =>
                                    TreeCheckNodeModel.newBuilder()
                                      .setLabel(discipline)
                                      .setCollapsed(true)
                                      .addLeafNode(MALE, GenderDto.MALE)
                                      .addLeafNode(FEMALE, GenderDto.FEMALE)))
    .build();
  readonly isDisciplineRankingDownloading = this.store.select(selectIsDisciplineRankingDownloading);

  constructor(
    private readonly store: Store,
  ) {
  }

  ngOnInit(): void {
    this.store.dispatch(loadRankingData());
  }

  downloadTotalRanking(): void {
    const genders = createGenderListOfTree(this.totalTree);
    this.store.dispatch(downloadTotalRankingAction({genders}));
  }

  downloadTriathlonRanking(): void {
    const genders = createGenderListOfTree(this.triathlonTree);
    this.store.dispatch(downloadTriathlonRankingAction({genders}));
  }

  downloadUbsCupRanking(): void {
    const genders = createGenderListOfTree(this.ubsCupTree);
    this.store.dispatch(downloadUbsCupRankingAction({genders}));
  }

  downloadDisciplineRanking(): void {
    const disciplines = this.disciplinesTree.checkedNodes
      .map(disciplineNode =>
             disciplineNode.checkedNodes.map<DisciplineRanking>(genderNode => (
               {
                 discipline: disciplineNode.value,
                 gender: genderDtoOfValue(genderNode.value),
               }
             )))
      .reduce(flatMapping(), []);

    this.store.dispatch(downloadDisciplineRankingAction({disciplines}));
  }
}

function createGenderListOfTree(tree: TreeCheckNodeModel): ReadonlyArray<GenderDto> {
  return tree.checkedNodes.map(node => genderDtoOfValue(node.value));
}
