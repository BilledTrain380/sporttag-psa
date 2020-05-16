import { Component, Input } from "@angular/core";

import { StatusEntryModel } from "./status-entry-model";

@Component({
  selector: "app-status-detail",
  templateUrl: "./status-detail.component.html",
})
export class StatusDetailComponent {

  @Input()
  entries: ReadonlyArray<StatusEntryModel> = [];
}
