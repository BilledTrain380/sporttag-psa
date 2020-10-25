import { Directive, OnDestroy, OnInit } from "@angular/core";
import { ControlValueAccessor, FormControl } from "@angular/forms";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import { Consumer } from "../../@core/lib/function";
import { isNullOrUndefined } from "../../@core/lib/lib";

@Directive()
// tslint:disable-next-line:directive-class-suffix
export abstract class AbstractFormInput<T> implements OnInit, OnDestroy, ControlValueAccessor {
  readonly formControl = new FormControl();

  set value(val: T) {
    if (!isNullOrUndefined(val)) {
      this.val = val;

      if (this.onChange) {
        this.onChange(this.val);
      }

      if (this.onTouched) {
        this.onTouched(this.val);
      }
    }
  }

  private onChange?: Consumer<T>;
  private onTouched?: Consumer<T>;
  protected abstract val: T;

  private readonly destroy$ = new Subject();

  ngOnInit(): void {
    this.formControl.setValue(this.val, {emitEvent: false});

    this.formControl.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(value => {
        this.value = value;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.complete();
  }

  registerOnChange(fn: Consumer<T>): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: Consumer<T>): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    if (isDisabled) {
      this.formControl.disable();
    } else {
      this.formControl.enable();
    }
  }

  writeValue(obj: T): void {
    this.formControl.setValue(obj);
  }
}
