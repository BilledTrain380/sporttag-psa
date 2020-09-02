import { DOCUMENT } from "@angular/common";
import { Directive, ElementRef, Inject, Input, OnChanges, Renderer2, SimpleChanges } from "@angular/core";

@Directive({
             selector: "[appNoEntries]",
           })
export class NoEntriesDirective implements OnChanges {

  @Input()
  rowCount = 0;

  @Input()
  columnCount = 1;

  private noEntriesRoot?: HTMLTableRowElement;

  constructor(
    private readonly elementRef: ElementRef,
    private readonly renderer: Renderer2,
    @Inject(DOCUMENT)
    private readonly document: Document,
  ) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.rowCount.currentValue === 0) {
      this.insertNoEntries();
    } else {
      this.clearNoEntries();
    }
  }

  private insertNoEntries(): void {
    this.noEntriesRoot = this.document.createElement("tr");

    const tableHeader = this.document.createElement("td");
    tableHeader.setAttribute("colspan", this.columnCount.toString());

    const noEntryText = this.document.createElement("p");
    noEntryText.classList.add("text-secondary", "text-center");
    noEntryText.innerText = $localize`No entries`;

    tableHeader.appendChild(noEntryText);
    this.noEntriesRoot.appendChild(tableHeader);

    const tBody = this.getTableBody();

    if (tBody) {
      this.renderer.appendChild(tBody, this.noEntriesRoot);
    } else {
      throw new Error("Expected table to contain tbody");
    }
  }

  private clearNoEntries(): void {
    const tBody = this.getTableBody();

    if (tBody && this.noEntriesRoot) {
      this.renderer.removeChild(tBody, this.noEntriesRoot);
    }
  }

  private getTableBody(): ChildNode | undefined {
    return Array.from<ChildNode>(this.elementRef.nativeElement.childNodes)
      .find(child => child.nodeName.toLowerCase() === "tbody");
  }
}
