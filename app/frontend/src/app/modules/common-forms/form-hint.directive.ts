import { DOCUMENT } from "@angular/common";
import { Directive, ElementRef, Inject, OnInit, Renderer2 } from "@angular/core";

@Directive({
             selector: "[appFormHint]",
           })
export class FormHintDirective implements OnInit {
  constructor(
    private readonly elementRef: ElementRef,
    private readonly renderer: Renderer2,
    @Inject(DOCUMENT)
    private readonly document: Document,
  ) {
  }

  ngOnInit(): void {
    const rootDiv = this.document.createElement("div");
    rootDiv.classList.add("form-row");
    rootDiv.setAttribute("data-test-selector", "form-hint");

    const hintDiv = this.document.createElement("div");
    hintDiv.classList.add("form-group", "col-md-12");

    const preText = $localize`Fields with`;
    const appText = $localize`are required`;
    hintDiv.innerHTML = `<i>${preText} <span style="color: red">*</span> ${appText}</i>`;

    rootDiv.appendChild(hintDiv);

    const firstChild = this.elementRef.nativeElement.firstChild;
    this.renderer.insertBefore(this.elementRef.nativeElement, rootDiv, firstChild);
  }
}
