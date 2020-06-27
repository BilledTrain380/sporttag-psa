import { Component, forwardRef, OnDestroy, OnInit } from "@angular/core";
import { ControlValueAccessor, FormBuilder, FormGroup, NG_VALUE_ACCESSOR } from "@angular/forms";
import { faCalendar } from "@fortawesome/free-solid-svg-icons";
import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import { FormControlsObject } from "../../../@core/forms/form-util";
import { Consumer } from "../../../@core/lib/function";
import { isNullOrUndefined } from "../../../@core/lib/lib";
import { now } from "../../../@core/lib/time";

@Component({
             selector: "app-date-input",
             templateUrl: "./date-input.component.html",
             providers: [
               {
                 provide: NG_VALUE_ACCESSOR,
                 useExisting: forwardRef(() => DateInputComponent),
                 multi: true,
               },
             ],
           })
export class DateInputComponent implements OnInit, OnDestroy, ControlValueAccessor {
  readonly faCalendar = faCalendar;

  readonly formControls: FormControlsObject = {
    date: "date",
  };
  form?: FormGroup;

  set value(val: NgbDateStruct) {
    if (!isNullOrUndefined(val)) {
      this.val = val;
      this.onChange?.call(this, this.val);
      this.onTouched?.call(this, this.val);
    }
  }

  private onChange?: Consumer<NgbDateStruct>;
  private onTouched?: Consumer<NgbDateStruct>;
  private val: NgbDateStruct = now();

  private readonly destroy$ = new Subject();

  constructor(
    private readonly formBuilder: FormBuilder,
  ) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
                                         [this.formControls.date]: this.val,
                                       });

    this.form.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(values => {
        this.value = values[this.formControls.date];
      });
  }

  ngOnDestroy(): void {
    this.destroy$.complete();
  }

  registerOnChange(fn: Consumer<NgbDateStruct>): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: Consumer<NgbDateStruct>): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    const control = this.form?.controls[this.formControls.date];

    if (isDisabled) {
      control?.disable();
    } else {
      control?.enable();
    }
  }

  writeValue(obj: NgbDateStruct): void {
    this.form?.controls[this.formControls.date]?.setValue(obj);
  }
}
