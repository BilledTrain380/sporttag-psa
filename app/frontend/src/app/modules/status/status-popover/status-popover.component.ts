import { Component, Input } from "@angular/core";

import { StatusDto } from "../../../dto/status";
import { StatusModel } from "../status-model";

@Component({
             selector: "app-popover-status",
             templateUrl: "./status-popover.component.html",
             styleUrls: [
               "../status.scss",
               "./status-popover.component.scss",
             ],
           })
export class StatusPopoverComponent {

  @Input()
  set status(value: StatusDto) {
    this.statusModel = StatusModel.fromDto(value);
  }

  statusModel: StatusModel = StatusModel.unknown();
}
