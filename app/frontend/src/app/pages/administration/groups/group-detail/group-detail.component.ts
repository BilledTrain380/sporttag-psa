import { Component, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { faPlusCircle } from "@fortawesome/free-solid-svg-icons";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable, Subject } from "rxjs";
import { map, takeUntil } from "rxjs/operators";

import { Timer } from "../../../../@core/lib/timer";
import { ParticipantElement } from "../../../../dto/participation";
import { Alert } from "../../../../modules/alert/alert";
import { ConfirmModalComponent } from "../../../../modules/modal/confirm-modal/confirm-modal.component";
import { ConfirmType } from "../../../../modules/modal/confirm-modal/confirm-type";
import {
  clearActiveGroupAction,
  deleteParticipantAction,
  loadGroupAction,
  updateParticipantAction
} from "../../../../store/group/group.action";
import { selectActiveGroup, selectActiveGroupAlert } from "../../../../store/group/group.selector";
import { VOID_PROPS } from "../../../../store/standard-props";
import { GROUP_NAME_PATH_VARIABLE, ROOT_PATH } from "../groups-paths";

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

  readonly alert$: Observable<Alert | undefined> = this.store.select(selectActiveGroupAlert);

  private readonly updateParticipantAbsentTimer = Timer.ofHalfSecond<ParticipantElement>();

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
  }

  ngOnDestroy(): void {
    this.store.dispatch(clearActiveGroupAction(VOID_PROPS));
    this.destroy$.complete();
  }

  toggleAbsent(participant: ParticipantModel): void {
    const participantElement: ParticipantElement = {
      id: participant.id,
      isAbsent: participant.isAbsent,
    };

    this.updateParticipantAbsentTimer.trigger(participantElement);
  }

  deleteParticipant(participant: ParticipantModel): void {
    const modalRef = this.modalService.open(ConfirmModalComponent, {size: "md"});

    modalRef.componentInstance.buildText(
      $localize`Are you really want to delete the participant `,
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
}
