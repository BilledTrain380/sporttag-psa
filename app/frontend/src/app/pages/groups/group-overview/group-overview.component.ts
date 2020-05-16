import { Component, OnInit } from "@angular/core";
import { faUpload } from "@fortawesome/free-solid-svg-icons";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

import { OverviewGroupDto } from "../../../dto/group";
import { AppState } from "../../../store/app";
import { loadGroupsOverviewAction } from "../../../store/group/group.action";
import { selectGroups } from "../../../store/group/group.selector";

@Component({
  selector: "app-group-overview",
  templateUrl: "./group-overview.component.html",
  styleUrls: ["./group-overview.component.scss"],
})
export class GroupOverviewComponent implements OnInit {
  readonly displayedColumns: ReadonlyArray<string> = ["name", "coach", "status"];

  readonly groups$: Observable<ReadonlyArray<OverviewGroupDto>> = this.store.select(selectGroups);

  readonly faUpload = faUpload;

  constructor(
    private readonly store: Store<AppState>,
  ) {
  }

  ngOnInit(): void {
    this.store.dispatch(loadGroupsOverviewAction({}));
  }
}
