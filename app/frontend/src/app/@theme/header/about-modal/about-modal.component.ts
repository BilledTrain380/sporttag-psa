import { Component } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Observable } from "rxjs";

import { AboutApi } from "../../../@core/service/api/about-api";
import { BuildInfoDto } from "../../../dto/about";

@Component({
             selector: "app-about-modal",
             templateUrl: "./about-modal.component.html",
           })
export class AboutModalComponent {
  readonly buildInfo$: Observable<BuildInfoDto> = this.aboutApi.getBuildInfo();

  constructor(
    private readonly modal: NgbActiveModal,
    private readonly aboutApi: AboutApi,
  ) {
  }

  close(): void {
    this.modal.close();
  }
}
