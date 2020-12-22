import { Component } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

import { BuildInfoDto } from "../../../dto/about";
import { selectBuildInfo } from "../../../store/metadata/metadata.selector";

@Component({
             selector: "app-about-modal",
             templateUrl: "./about-modal.component.html",
           })
export class AboutModalComponent {
  readonly buildInfo$: Observable<BuildInfoDto> = this.store.select(selectBuildInfo);

  constructor(
    private readonly modal: NgbActiveModal,
    private readonly store: Store,
  ) {
  }

  close(): void {
    this.modal.close();
  }
}
