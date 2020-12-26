import { Component, forwardRef, Input, OnDestroy, OnInit } from "@angular/core";
import { ControlValueAccessor, FormBuilder, FormGroup, NG_VALUE_ACCESSOR } from "@angular/forms";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import { FormControlsObject } from "../../../@core/forms/form-util";
import { Consumer } from "../../../@core/lib/function";
import { isNullOrUndefined } from "../../../@core/lib/lib";
import { ATHLETICS, BRENNBALL, SCHATZSUCHE, VELO_ROLLERBLADES } from "../../../dto/participation";

@Component({
             selector: "app-sport-type-input",
             templateUrl: "./sport-type-input.component.html",
             providers: [
               {
                 provide: NG_VALUE_ACCESSOR,
                 useExisting: forwardRef(() => SportTypeInputComponent),
                 multi: true,
               },
             ],
           })
export class SportTypeInputComponent implements OnInit, OnDestroy, ControlValueAccessor {
  @Input()
  size: "" | "sm" = "";

  readonly sportTypes: ReadonlyArray<SportTypeModel> = [
    new SportTypeModel(ATHLETICS, $localize`Athletics`),
    new SportTypeModel(SCHATZSUCHE),
    new SportTypeModel(BRENNBALL),
    new SportTypeModel(VELO_ROLLERBLADES),
  ];

  readonly formControls: FormControlsObject = {
    sportType: "sportType",
  };
  form?: FormGroup;

  set value(val: string) {
    if (!isNullOrUndefined(val)) {
      this.val = val;
      this.onChange?.call(this, this.val);
      this.onTouched?.call(this, this.val);
    }
  }

  private onChange?: Consumer<string>;
  private onTouched?: Consumer<string>;
  private val = "";

  private readonly destroy$ = new Subject();

  constructor(
    private readonly formBuilder: FormBuilder,
  ) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
                                         [this.formControls.sportType]: this.val,
                                       });

    this.form.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(values => {
        this.value = values[this.formControls.sportType];
      });
  }

  ngOnDestroy(): void {
    this.destroy$.complete();
  }

  registerOnChange(fn: Consumer<string>): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: Consumer<string>): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    const control = this.form?.controls[this.formControls.sportType];

    if (isDisabled) {
      control?.disable();
    } else {
      control?.enable();
    }
  }

  writeValue(obj: string): void {
    this.form?.controls[this.formControls.sportType]?.setValue(obj);
  }
}

class SportTypeModel {
  constructor(
    readonly value: string,
    readonly displayValue: string = value,
  ) {
  }
}
