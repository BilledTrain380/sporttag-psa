import { Component, OnInit } from "@angular/core";
import { faUpload } from "@fortawesome/free-solid-svg-icons";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

import { OverviewGroupDto } from "../../../dto/group";
import { Alert } from "../../../modules/alert/alert";
import { AppState } from "../../../store/app";
import { loadGroupsOverviewAction } from "../../../store/group/group.action";
import { selectGroups } from "../../../store/group/group.selector";

import { ImportGroupsComponent } from "./import-groups/import-groups.component";

@Component({
             selector: "app-group-overview",
             templateUrl: "./group-overview.component.html",
             styleUrls: ["./group-overview.component.scss"],
           })
export class GroupOverviewComponent implements OnInit {
  readonly groups$: Observable<ReadonlyArray<OverviewGroupDto>> = this.store.select(selectGroups);

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
    this.store.dispatch(loadGroupsOverviewAction({}));
  }
}
