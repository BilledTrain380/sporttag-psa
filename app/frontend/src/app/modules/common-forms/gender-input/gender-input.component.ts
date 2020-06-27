import { Component, forwardRef, OnInit } from "@angular/core";
import { ControlValueAccessor, FormBuilder, FormGroup, NG_VALUE_ACCESSOR } from "@angular/forms";

import { FormControlsObject } from "../../../@core/forms/form-util";
import { Consumer } from "../../../@core/lib/function";
import { isNullOrUndefined } from "../../../@core/lib/lib";
import { GenderDto } from "../../../dto/participation";

@Component({
             selector: "app-gender-input",
             templateUrl: "./gender-input.component.html",
             providers: [
               {
                 provide: NG_VALUE_ACCESSOR,
                 useExisting: forwardRef(() => GenderInputComponent),
                 multi: true,
               },
             ],
           })
export class GenderInputComponent implements OnInit, ControlValueAccessor {
  readonly GenderDto = GenderDto;

  form?: FormGroup;
  readonly formControl: FormControlsObject = {
    gender: "gender",
  };

  set value(val: GenderDto) {
    if (!isNullOrUndefined(val)) {
      this.val = val;
      this.onChange?.call(this, this.val);
      this.onTouched?.call(this, this.val);

      this.form?.controls[this.formControl.gender]?.setValue(this.val);
    }
  }

  private val: GenderDto = GenderDto.MALE;
  private onChange?: Consumer<GenderDto>;
  private onTouched?: Consumer<GenderDto>;

  constructor(
    private readonly formBuilder: FormBuilder,
  ) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
                                         [this.formControl.gender]: this.val,
                                       });
  }

  registerOnChange(fn: Consumer<GenderDto>): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: Consumer<GenderDto>): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    const control = this.form?.controls[this.formControl.gender];

    if (isDisabled) {
      control?.disable();
    } else {
      control?.enable();
    }
  }

  writeValue(obj: GenderDto): void {
    this.value = obj;
  }
}
