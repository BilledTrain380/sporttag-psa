import { Component, OnInit } from "@angular/core";
import { faUpload } from "@fortawesome/free-solid-svg-icons";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

import { GroupDto } from "../../../@core/service/api/group-api";
import { AppState } from "../../../store/app";
import { loadGroups } from "../../../store/group/group.action";
import { selectGroups } from "../../../store/group/group.selector";

@Component({
  selector: "app-group-overview",
  templateUrl: "./group-overview.component.html",
  styleUrls: ["./group-overview.component.scss"],
})
export class GroupOverviewComponent implements OnInit {

  readonly groups$: Observable<ReadonlyArray<GroupDto>> = this.store.select(selectGroups);

  readonly faUpload = faUpload;

  constructor(
    private readonly store: Store<AppState>,
  ) {
  }

  ngOnInit(): void {
    this.store.dispatch(loadGroups({}));
  }
}
