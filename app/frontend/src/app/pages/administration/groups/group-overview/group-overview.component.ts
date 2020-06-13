import { Component, OnInit } from "@angular/core";
import { faUpload } from "@fortawesome/free-solid-svg-icons";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

import { OverviewGroupDto } from "../../../../dto/group";
import { Alert } from "../../../../modules/alert/alert";
import { AppState } from "../../../../store/app";
import { loadOverviewGroupsAction } from "../../../../store/group/group.action";
import { selectOverviewGroups } from "../../../../store/group/group.selector";
import { VOID_PROPS } from "../../../../store/standard-props";
import { GROUP_DETAIL_PREFIX_PATH } from "../groups-paths";

import { ImportGroupsComponent } from "./import-groups/import-groups.component";

@Component({
             selector: "app-group-overview",
             templateUrl: "./group-overview.component.html",
           })
export class GroupOverviewComponent implements OnInit {
  readonly groupDetailPrefixPath = GROUP_DETAIL_PREFIX_PATH;

  readonly groups$: Observable<ReadonlyArray<OverviewGroupDto>> = this.store.select(selectOverviewGroups);

  readonly faUpload = faUpload;

  alert?: Alert;

  constructor(
    private readonly store: Store<AppState>,
    private readonly modalService: NgbModal,
  ) {
  }

  ngOnInit(): void {
    this.loadGroups();
  }

  openImportModal(): void {
    this.modalService
      .open(ImportGroupsComponent, {size: "lg"})
      .result
      .then((alert?: Alert) => {
        if (alert?.isSuccess()) {
          this.alert = alert;
          this.loadGroups();
        }
      });
  }

  private loadGroups(): void {
    this.store.dispatch(loadOverviewGroupsAction(VOID_PROPS));
  }
}
