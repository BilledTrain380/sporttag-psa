import { Component } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

import { ConfirmType } from "./confirm-modal-util";

@Component({
             selector: "app-confirm-modal",
             templateUrl: "./confirm-modal.component.html",
           })
export class ConfirmModalComponent {
  text = "";

  constructor(
    private readonly activeModal: NgbActiveModal,
  ) {
  }

  confirm(): void {
    this.activeModal.close(ConfirmType.CONFIRM);
  }

  cancel(): void {
    this.activeModal.close(ConfirmType.CANCEL);
  }

  buildText(...parts: ReadonlyArray<string>): void {
    this.text = parts.join("");
  }
}
