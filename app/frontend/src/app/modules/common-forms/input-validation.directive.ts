import { Directive, ElementRef, OnDestroy, OnInit, Renderer2, Self } from "@angular/core";
import { NgControl } from "@angular/forms";
import { Subject } from "rxjs";
import { filter, takeUntil } from "rxjs/operators";

const IS_VALID_CSS = "is-valid";
const IS_INVALID_CSS = "is-invalid";

@Directive({
             selector: "[appInputValidation]",
           })
export class InputValidationDirective implements OnInit, OnDestroy {
  private readonly destroy$ = new Subject();

  constructor(
    @Self()
    private readonly control: NgControl,
    private readonly renderer: Renderer2,
    private readonly hostElement: ElementRef,
  ) {
  }

  ngOnInit(): void {
    this.control.valueChanges
      ?.pipe(takeUntil(this.destroy$))
      ?.pipe(filter(() => !!this.control.dirty))
      ?.subscribe(() => {
        if (this.control.valid) {
          this.renderer.addClass(this.hostElement.nativeElement, IS_VALID_CSS);
          this.renderer.removeClass(this.hostElement.nativeElement, IS_INVALID_CSS);
        } else {
          this.renderer.addClass(this.hostElement.nativeElement, IS_INVALID_CSS);
          this.renderer.removeClass(this.hostElement.nativeElement, IS_VALID_CSS);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.complete();
  }
}
