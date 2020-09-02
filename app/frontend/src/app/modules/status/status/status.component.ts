import { Component, Input } from "@angular/core";

import { StatusDto } from "../../../dto/status";
import { StatusModel } from "../status-model";

@Component({
             selector: "app-status",
             templateUrl: "./status.component.html",
             styleUrls: ["../status.scss"],
           })
export class StatusComponent {

  @Input()
  set status(value: StatusDto) {
    this.statusModel = StatusModel.fromDto(value);
  }

  statusModel: StatusModel = StatusModel.unknown();
}
