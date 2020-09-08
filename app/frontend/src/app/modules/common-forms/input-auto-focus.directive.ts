import { Directive, ElementRef, Input } from "@angular/core";

@Directive({
             selector: "[appInputAutoFocus]",
           })
export class InputAutoFocusDirective {

  @Input()
  set appInputAutoFocus(value: boolean) {
    if (value) {
      this.elementRef.nativeElement.focus();
    }
  }

  constructor(
    private readonly elementRef: ElementRef,
  ) {
  }
}
