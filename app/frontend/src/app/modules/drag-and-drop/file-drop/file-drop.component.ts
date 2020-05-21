import { Component, ElementRef, HostListener, ViewChild } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { faTrash } from "@fortawesome/free-solid-svg-icons";

import { Consumer, Runnable } from "../../../@core/lib/function";

@Component({
             selector: "app-file-drop",
             templateUrl: "./file-drop.component.html",
             styleUrls: ["./file-drop.component.scss"],
             providers: [
               {
                 provide: NG_VALUE_ACCESSOR,
                 useExisting: FileDropComponent,
                 multi: true,
               },
             ],
           })
export class FileDropComponent implements ControlValueAccessor {

  @ViewChild("fileInput", {static: false})
  fileInput?: ElementRef;

  file?: File;

  isDisabled = false;
  isDragOver = false;

  readonly faTrash = faTrash;

  private onChange?: Consumer<File | undefined>;

  registerOnChange(fn: Consumer<File | undefined>): void {
    this.onChange = fn;
  }

  registerOnTouched(_: Runnable): void {
    // Intentionally left blank
  }

  setDisabledState(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }

  writeValue(obj: File): void {
    this.file = obj;
  }

  onInputChange(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const file = inputElement.files?.item(0) || undefined;
    this.setFileAndNotify(file);
  }

  @HostListener("dragover", ["$event"])
  onDragOver(event: Event): void {
    event.stopPropagation();
    event.preventDefault();
    this.isDragOver = true;
  }

  @HostListener("drop", ["$event"])
  onDrop(event: DragEvent): void {
    event.stopPropagation();
    event.preventDefault();
    this.isDragOver = false;
    const file = event.dataTransfer?.files?.item(0) || undefined;
    this.setFileAndNotify(file);
  }

  @HostListener("dragleave", ["$event"])
  onDragLeave(event: DragEvent): void {
    event.stopPropagation();
    event.preventDefault();
    this.isDragOver = false;
  }

  onClick(): void {
    this.fileInput?.nativeElement.click();
  }

  clearFile(): void {
    this.setFileAndNotify(undefined);
  }

  private setFileAndNotify(file?: File): void {
    this.file = file;

    if (this.onChange) {
      this.onChange(this.file);
    }
  }
}
