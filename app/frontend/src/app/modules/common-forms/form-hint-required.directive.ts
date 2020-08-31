import { DOCUMENT } from "@angular/common";
import { Directive, ElementRef, Inject, OnInit, Renderer2 } from "@angular/core";

@Directive({
             selector: "[appFormHintRequired]",
           })
export class FormHintRequiredDirective implements OnInit {
  constructor(
    private readonly elementRef: ElementRef,
    private readonly renderer: Renderer2,
    @Inject(DOCUMENT)
    private readonly document: Document,
  ) {
  }

  ngOnInit(): void {
    const span = this.document.createElement("span");
    span.style.color = "red";
    span.innerText = " *";
    this.renderer.appendChild(this.elementRef.nativeElement, span);
  }
}
