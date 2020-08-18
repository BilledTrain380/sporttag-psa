import { Component, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { faPlusCircle } from "@fortawesome/free-solid-svg-icons";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable, Subject } from "rxjs";
import { map, takeUntil } from "rxjs/operators";

import { Timer } from "../../../../@core/lib/timer";
import { ParticipantElement, ParticipantRelation } from "../../../../dto/participation";
import { Alert } from "../../../../modules/alert/alert";
import { confirmModalOptions, ConfirmType } from "../../../../modules/modal/confirm-modal/confirm-modal-util";
import { ConfirmModalComponent } from "../../../../modules/modal/confirm-modal/confirm-modal.component";
import {
  clearActiveGroupAction,
  clearParticipantAlertAction,
  deleteParticipantAction,
  loadActiveParticipantAction,
  loadGroupAction,
  updateParticipantAction,
  updateParticipantRelationAction,
} from "../../../../store/group/group.action";
import { selectActiveGroup, selectParticipantAlert, selectParticipants } from "../../../../store/group/group.selector";
import { VOID_PROPS } from "../../../../store/standard-props";
import { GROUP_NAME_PATH_VARIABLE, ROOT_PATH } from "../groups-paths";

import { AddParticipantComponent } from "./add-participant/add-participant.component";
import { EditParticipantModalComponent } from "./edit-participant-modal/edit-participant-modal.component";
import { GroupViewModel, ParticipantModel } from "./view-model";

@Component({
             selector: "app-group-detail",
             templateUrl: "./group-detail.component.html",
           })
export class GroupDetailComponent implements OnInit, OnDestroy {
  readonly faPlusCircle = faPlusCircle;

  readonly groupManagementLink = ROOT_PATH;

  readonly group$: Observable<GroupViewModel> = this.store.select(selectActiveGroup)
    .pipe(map(group => {
      if (group) {
        return GroupViewModel.fromState(group);
      }

      return GroupViewModel.empty();
    }));

  readonly participants$: Observable<ReadonlyArray<ParticipantModel>> = this.store.select(selectParticipants)
    .pipe(map(participants => participants
      .map(participant => ParticipantModel.fromDto(participant))
      .sort((a, b) => a.compareTo(b))));

  readonly alert$: Observable<Alert | undefined> = this.store.select(selectParticipantAlert);

  private readonly updateParticipantAbsentTimer = Timer.ofHalfSecond<ParticipantElement>();
  private readonly updateParticipantSportTypeTimer = Timer.ofHalfSecond<ParticipantRelation>();

  private readonly destroy$ = new Subject();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly store: Store,
    private readonly modalService: NgbModal,
  ) {
  }

  ngOnInit(): void {
    this.route.params
      .pipe(takeUntil(this.destroy$))
      .pipe(map(params => params[GROUP_NAME_PATH_VARIABLE]))
      .subscribe(name => this.store.dispatch(loadGroupAction({name})));

    this.updateParticipantAbsentTimer.run$
      .pipe(takeUntil(this.destroy$))
      .subscribe(participant => this.store.dispatch(updateParticipantAction({participant})));

    this.updateParticipantSportTypeTimer.run$
      .pipe(takeUntil(this.destroy$))
      .subscribe(participant => this.store.dispatch(updateParticipantRelationAction({participant})));
  }

  ngOnDestroy(): void {
    this.store.dispatch(clearActiveGroupAction(VOID_PROPS));
    this.destroy$.complete();
  }

  toggleAbsent(participant: ParticipantModel): void {
    this.clearAlert();
    const participantElement: ParticipantElement = {
      id: participant.id,
      isAbsent: participant.isAbsent,
    };

    this.updateParticipantAbsentTimer.trigger(participantElement);
  }

  toggleSportType(participant: ParticipantModel): void {
    this.clearAlert();
    const participantRelation: ParticipantRelation = {
      id: participant.id,
      sportType: participant.sportType,
    };

    this.updateParticipantSportTypeTimer.trigger(participantRelation);
  }

  openAddParticipantModal(): void {
    this.clearAlert();
    this.modalService.open(AddParticipantComponent, {size: "lg"});
  }

  openEditParticipantModal(participant: ParticipantModel): void {
    this.clearAlert();
    this.store.dispatch(loadActiveParticipantAction({participantId: participant.id}));
    this.modalService.open(EditParticipantModalComponent, {size: "lg"});
  }

  deleteParticipant(participant: ParticipantModel): void {
    this.clearAlert();
    const modalRef = this.modalService.open(ConfirmModalComponent, confirmModalOptions);

    modalRef.componentInstance.buildText(
      $localize`Do you really want to delete the participant `,
      "\"",
      participant.fullName,
      "\"?",
    );

    modalRef.result
      .then((type: ConfirmType) => {
        if (type === ConfirmType.CONFIRM) {
          this.store.dispatch(deleteParticipantAction({participant_id: participant.id}));
        }
      });
  }

  private clearAlert(): void {
    this.store.dispatch(clearParticipantAlertAction(VOID_PROPS));
  }
}
