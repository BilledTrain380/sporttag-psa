import { Component, EventEmitter, HostListener, Input, Output } from "@angular/core";

import { Alert } from "../../alert/alert";

@Component({
             selector: "app-submit-modal",
             templateUrl: "./submit-modal.component.html",
           })
export class SubmitModalComponent {

  @Input()
  readonly title?: string;

  @Input()
  readonly isSubmitEnabled = false;

  @Input()
  readonly alert?: Alert;

  @Output()
  private readonly cancel = new EventEmitter<void>();

  @Output()
  private readonly submitData = new EventEmitter<void>();

  @HostListener("document:keydown.enter", ["$event"])
  onKeydownHandler(event: KeyboardEvent): void {
    event.preventDefault();

    if (this.isSubmitEnabled) {
      this.emitSubmit();
    }
  }

  emitCancel(): void {
    this.cancel.emit();
  }

  emitSubmit(): void {
    this.submitData.emit();
  }
}
