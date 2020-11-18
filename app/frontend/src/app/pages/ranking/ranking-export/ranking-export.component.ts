import { Component, OnInit } from "@angular/core";
import { faDownload } from "@fortawesome/free-solid-svg-icons/faDownload";

import { ALL_DISCIPLINES } from "../../../dto/athletics";
import { GenderDto } from "../../../dto/participation";
import { FEMALE, MALE } from "../../../modules/participant/gender/gender-constants";
import { LABEL_ALL, TreeCheckNodeModel } from "../../../modules/tree/tree-model";

const GENDER_TREE_BUILDER = TreeCheckNodeModel.newBuilder()
  .setLabel(LABEL_ALL)
  .addLeafNode(MALE, GenderDto.MALE)
  .addLeafNode(FEMALE, GenderDto.FEMALE);

@Component({
             selector: "app-ranking-export",
             templateUrl: "./ranking-export.component.html",
             styleUrls: ["./ranking-export.component.scss"],
           })
export class RankingExportComponent implements OnInit {
  readonly faDownload = faDownload;

  readonly totalTree = GENDER_TREE_BUILDER.build();
  readonly triathlonTree = GENDER_TREE_BUILDER.build();
  readonly ubsCupTree = GENDER_TREE_BUILDER.build();

  readonly disciplinesTree = TreeCheckNodeModel.newBuilder()
    .setLabel(LABEL_ALL)
    // tslint:disable-next-line:no-magic-numbers
    .setSplitting(4)
    .addNodes(ALL_DISCIPLINES.map(discipline =>
                                    TreeCheckNodeModel.newBuilder()
                                      .setLabel(discipline)
                                      .addLeafNode(MALE, GenderDto.MALE)
                                      .addLeafNode(FEMALE, GenderDto.FEMALE)))
    .build();

  ngOnInit(): void {
  }
}
