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
  private onTouch?: Runnable;

  registerOnChange(fn: Consumer<File | undefined>): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: Runnable): void {
    this.onTouch = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }

  writeValue(obj: File): void {
    this.file = obj;
  }

  onInputChange(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const file = inputElement.files?.item(0);
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
    const file = event.dataTransfer?.files?.item(0);
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
    this.setFileAndNotify();
  }

  private setFileAndNotify(file?: File | null): void {
    this.file = file === null ? undefined : file;

    if (this.onTouch) {
      this.onTouch();
    }

    if (this.onChange) {
      this.onChange(this.file);
    }
  }
}
