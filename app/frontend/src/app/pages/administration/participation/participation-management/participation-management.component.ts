import { Component, OnInit } from "@angular/core";
import { faEraser } from "@fortawesome/free-solid-svg-icons/faEraser";
import { faLock } from "@fortawesome/free-solid-svg-icons/faLock";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

import { ParticipationCommand, ParticipationDto } from "../../../../dto/participation";
import { StatusSeverity } from "../../../../dto/status";
import { confirmModalOptions, ConfirmType } from "../../../../modules/modal/confirm-modal/confirm-modal-util";
import { ConfirmModalComponent } from "../../../../modules/modal/confirm-modal/confirm-modal.component";
import { loadParticipationStatusAction, updateParticipationStatusAction } from "../../../../store/participation/participation.action";
import { selectParticipationStatus } from "../../../../store/participation/participation.selector";
import { VOID_PROPS } from "../../../../store/standard-props";

@Component({
             selector: "app-participation-management",
             templateUrl: "./participation-management.component.html",
           })
export class ParticipationManagementComponent implements OnInit {
  readonly faLock = faLock;
  readonly faEraser = faEraser;

  readonly participation$: Observable<ParticipationDto> = this.store.select(selectParticipationStatus);
  readonly isCloseParticipationDisabled$: Observable<boolean> = this.participation$
    .pipe(map(dto => dto.status.severity === StatusSeverity.INFO));

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
      .then((type: ConfirmType) => this.handleParticipationConfirmModal(type, ParticipationCommand.CLOSE));
  }

  resetParticipation(): void {
    const modalRef = this.modalService.open(ConfirmModalComponent, confirmModalOptions);

    modalRef.componentInstance.buildText(
      $localize`Do you really want to reset the participation?`,
    );

    modalRef.result
      .then((type: ConfirmType) => this.handleParticipationConfirmModal(type, ParticipationCommand.RESET));
  }

  private handleParticipationConfirmModal(type: ConfirmType, command: ParticipationCommand): void {
    if (type === ConfirmType.CONFIRM) {
      this.store.dispatch(updateParticipationStatusAction({command}));
    }
  }
}
