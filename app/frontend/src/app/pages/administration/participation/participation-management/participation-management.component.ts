import { Component, OnInit } from "@angular/core";
import { faEraser } from "@fortawesome/free-solid-svg-icons/faEraser";
import { faLock } from "@fortawesome/free-solid-svg-icons/faLock";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

import { ParticipationCommand, ParticipationDto } from "../../../../dto/participation";
import { confirmModalOptions, ConfirmType } from "../../../../modules/modal/confirm-modal/confirm-modal-util";
import { ConfirmModalComponent } from "../../../../modules/modal/confirm-modal/confirm-modal.component";
import { loadParticipationStatusAction, updateParticipationStatusAction } from "../../../../store/participation/participation.action";
import { selectParticipationStatus } from "../../../../store/participation/participation.selector";
import { VOID_PROPS } from "../../../../store/standard-props";

@Component({
             selector: "app-participation-management",
             templateUrl: "./participation-management.component.html",
             styleUrls: ["./participation-management.component.scss"],
           })
export class ParticipationManagementComponent implements OnInit {
  readonly faLock = faLock;
  readonly faEraser = faEraser;

  participation$: Observable<ParticipationDto> = this.store.select(selectParticipationStatus);

  constructor(
    private readonly store: Store,
    private readonly modalService: NgbModal,
  ) {
  }

  ngOnInit(): void {
    this.store.dispatch(loadParticipationStatusAction(VOID_PROPS));
  }

  closeParticipation(): void {
    const modalRef = this.modalService.open(ConfirmModalComponent, confirmModalOptions);

    modalRef.componentInstance.buildText(
      $localize`Do you really want to close the participation?`,
    );

    modalRef.result
      .then((type: ConfirmType) => {
        if (type === ConfirmType.CONFIRM) {
          this.store.dispatch(updateParticipationStatusAction({command: ParticipationCommand.CLOSE}));
        }
      });
  }

  resetParticipation(): void {
    const modalRef = this.modalService.open(ConfirmModalComponent, confirmModalOptions);

    modalRef.componentInstance.buildText(
      $localize`Do you really want to reset the participation?`,
    );

    modalRef.result
      .then((type: ConfirmType) => {
        if (type === ConfirmType.CONFIRM) {
          this.store.dispatch(updateParticipationStatusAction({command: ParticipationCommand.RESET}));
        }
      });
  }
}
