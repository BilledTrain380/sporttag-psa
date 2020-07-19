import { Component, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { filter } from "rxjs/operators";

import { FormControlsObject } from "../../../../../@core/forms/form-util";
import { requireNonNullOrUndefined } from "../../../../../@core/lib/lib";
import { BirthdayDto, BirthdayDtoImpl, ParticipantDto, ParticipantElement } from "../../../../../dto/participation";
import { Alert } from "../../../../../modules/alert/alert";
import { AbstractSubmitModalComponent } from "../../../../../modules/modal/submit-modal/abstract-submit-modal.component";
import { clearActiveParticipantAction, updateParticipantAction } from "../../../../../store/group/group.action";
import { selectActiveParticipant, selectParticipantAlert } from "../../../../../store/group/group.selector";
import { VOID_PROPS } from "../../../../../store/standard-props";

@Component({
             selector: "app-edit-participant-modal",
             templateUrl: "./edit-participant-modal.component.html",
           })
export class EditParticipantModalComponent extends AbstractSubmitModalComponent implements OnInit, OnDestroy {
  readonly participant$: Observable<ParticipantDto | undefined> = this.store.select(selectActiveParticipant);

  readonly formControls: FormControlsObject = {
    id: "id",
    prename: "prename",
    surname: "surname",
    address: "address",
    gender: "gender",
    birthday: "birthday",
  };

  form?: FormGroup;

  protected readonly alert$: Observable<Alert | undefined> = this.store.select(selectParticipantAlert);

  constructor(
    activeModal: NgbActiveModal,
    private readonly store: Store,
    private readonly formBuilder: FormBuilder,
  ) {
    super(activeModal);
  }

  ngOnInit(): void {
    super.ngOnInit();

    this.participant$
      .pipe(filter(participant => participant !== undefined))
      .subscribe((participant: ParticipantDto) => {
        // tslint:disable: no-magic-numbers max-line-length
        this.form = this.formBuilder.group({
                                             [this.formControls.id]: participant.id,
                                             [this.formControls.prename]: [participant.prename, [Validators.required, Validators.maxLength(30)]],
                                             [this.formControls.surname]: [participant.surname, [Validators.required, Validators.maxLength(30)]],
                                             [this.formControls.address]: [participant.address, [Validators.required, Validators.maxLength(80)]],
                                             [this.formControls.gender]: [participant.gender, Validators.required],
                                             [this.formControls.birthday]: [participant.birthday.date, Validators.required],
                                           });
        // tslint:enable
      });
  }

  ngOnDestroy(): void {
    this.store.dispatch(clearActiveParticipantAction(VOID_PROPS));
    super.ngOnDestroy();
  }

  submit(): void {
    let birthday: BirthdayDto | undefined;
    const date = this.form?.value[this.formControls.birthday];

    if (date) {
      birthday = BirthdayDtoImpl.of(date);
    }

    const participant: ParticipantElement = {
      id: requireNonNullOrUndefined(this.form?.value[this.formControls.id]),
      prename: this.form?.value[this.formControls.prename],
      surname: this.form?.value[this.formControls.surname],
      gender: this.form?.value[this.formControls.gender],
      birthday,
      address: this.form?.value[this.formControls.address],
    };

    this.store.dispatch(updateParticipantAction({participant}));
  }
}
