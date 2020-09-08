import { Component, forwardRef, Input, OnDestroy, OnInit } from "@angular/core";
import { ControlValueAccessor, FormBuilder, FormGroup, NG_VALUE_ACCESSOR } from "@angular/forms";
import { faChevronLeft } from "@fortawesome/free-solid-svg-icons/faChevronLeft";
import { faChevronRight } from "@fortawesome/free-solid-svg-icons/faChevronRight";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import { FormControlsObject } from "../../../@core/forms/form-util";
import { Consumer } from "../../../@core/lib/function";
import { isNullOrUndefined } from "../../../@core/lib/lib";

@Component({
             selector: "app-pre-next-select",
             templateUrl: "./pre-next-select.component.html",
             providers: [
               {
                 provide: NG_VALUE_ACCESSOR,
                 useExisting: forwardRef(() => PreNextSelectComponent),
                 multi: true,
               },
             ],
           })
export class PreNextSelectComponent implements OnInit, OnDestroy, ControlValueAccessor {

  @Input()
  values: ReadonlyArray<string> = [];

  @Input()
  prependText = "";

  readonly formControls: FormControlsObject = {
    value: "value",
  };
  form?: FormGroup;

  readonly faChevronLeft = faChevronLeft;
  readonly faChevronRight = faChevronRight;

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
                                         [this.formControls.value]: this.val,
                                       });

    this.form.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(values => {
        this.value = values[this.formControls.value];
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

  writeValue(obj: string): void {
    this.form?.controls[this.formControls.value]?.setValue(obj);
  }

  get hasPreviousValue(): boolean {
    return this.hasPreviousIndex(this.values.indexOf(this.val));
  }

  previousValue(): void {
    const currentValueIndex = this.values.indexOf(this.val);

    if (this.hasPreviousIndex(currentValueIndex)) {
      this.writeValue(this.values[currentValueIndex - 1]);
    }
  }

  get hasNextValue(): boolean {
    return this.hasNextIndex(this.values.indexOf(this.val));
  }

  nextValue(): void {
    const currentValueIndex = this.values.indexOf(this.val);

    if (this.hasNextIndex(currentValueIndex)) {
      this.writeValue(this.values[currentValueIndex + 1]);
    }
  }

  private hasPreviousIndex(index: number): boolean {
    return index > 0 && this.values.length > 0;
  }

  private hasNextIndex(index: number): boolean {
    return index >= 0 && index < this.values.length - 1;
  }
}
