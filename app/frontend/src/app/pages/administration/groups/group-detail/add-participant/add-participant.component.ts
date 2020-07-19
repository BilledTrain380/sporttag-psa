import { Component, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { FormControlsObject } from "../../../../../@core/forms/form-util";
import { requireNonNullOrUndefined } from "../../../../../@core/lib/lib";
import { ATHLETICS, BirthdayDto, BirthdayDtoImpl, GenderDto, ParticipantDto } from "../../../../../dto/participation";
import { Alert } from "../../../../../modules/alert/alert";

import { AbstractSubmitModalComponent } from "../../../../../modules/modal/submit-modal/abstract-submit-modal.component";
import { addParticipantAction, clearParticipantAlertAction } from "../../../../../store/group/group.action";
import { selectActiveGroup, selectParticipantAlert } from "../../../../../store/group/group.selector";
import { VOID_PROPS } from "../../../../../store/standard-props";

@Component({
             selector: "app-add-participant",
             templateUrl: "./add-participant.component.html",
           })
export class AddParticipantComponent extends AbstractSubmitModalComponent implements OnInit, OnDestroy {
  readonly formControls: FormControlsObject = {
    prename: "prename",
    surname: "surname",
    address: "address",
    gender: "gender",
    birthday: "birthday",
    zip: "zip",
    town: "town",
    group: "group",
    sportType: "sportType",
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
    this.store.select(selectActiveGroup)
      .pipe(takeUntil(this.destroy$))
      .subscribe(group => {
        // tslint:disable: no-magic-numbers
        this.form = this.formBuilder.group({
                                             [this.formControls.prename]: ["", [Validators.required, Validators.maxLength(30)]],
                                             [this.formControls.surname]: ["", [Validators.required, Validators.maxLength(30)]],
                                             [this.formControls.address]: ["", [Validators.required, Validators.maxLength(80)]],
                                             [this.formControls.gender]: [GenderDto.MALE, Validators.required],
                                             [this.formControls.birthday]: ["", Validators.required],
                                             [this.formControls.zip]: ["", [Validators.required, Validators.maxLength(4)]],
                                             [this.formControls.town]: ["", [Validators.required, Validators.maxLength(50)]],
                                             [this.formControls.group]: [group, Validators.required],
                                             [this.formControls.sportType]: [ATHLETICS, Validators.required],
                                           });
        // tslint:enable
      });
  }

  ngOnDestroy(): void {
    this.store.dispatch(clearParticipantAlertAction(VOID_PROPS));
    super.ngOnDestroy();
  }

  submit(): void {
    let birthday: BirthdayDto | undefined;
    const date = this.form?.value[this.formControls.birthday];

    if (date) {
      birthday = BirthdayDtoImpl.of(date);
    }

    const participant: ParticipantDto = {
      id: 0,
      prename: requireNonNullOrUndefined(this.form?.value[this.formControls.prename]),
      surname: requireNonNullOrUndefined(this.form?.value[this.formControls.surname]),
      gender: requireNonNullOrUndefined(this.form?.value[this.formControls.gender]),
      birthday: requireNonNullOrUndefined(birthday),
      address: requireNonNullOrUndefined(this.form?.value[this.formControls.address]),
      absent: false,
      town: {
        zip: requireNonNullOrUndefined(this.form?.value[this.formControls.zip]),
        name: requireNonNullOrUndefined(this.form?.value[this.formControls.town]),
      },
      group: requireNonNullOrUndefined(this.form?.value[this.formControls.group]),
      sportType: requireNonNullOrUndefined(this.form?.value[this.formControls.sportType]),
    };

    this.store.dispatch(addParticipantAction({participant}));
  }
}
