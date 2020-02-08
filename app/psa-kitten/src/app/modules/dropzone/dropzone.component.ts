import {AfterViewInit, Component, EventEmitter, Input, Output} from '@angular/core';
import * as uuidv4 from 'uuid/v4';

@Component({
    selector: 'ngx-dropzone',
    templateUrl: './dropzone.component.html',
    styleUrls: ['./dropzone.component.scss'],
})
export class DropzoneComponent implements AfterViewInit {

    readonly dropzoneContainerID: string;

    @Input()
    label: string = 'Drop file here...';

    @Output()
    readonly onFileDrop: EventEmitter<File> = new EventEmitter();

    constructor() {
        this.dropzoneContainerID = uuidv4();
    }

    ngAfterViewInit(): void {
        const dropzoneContainer: HTMLDivElement = document.getElementById(this.dropzoneContainerID) as HTMLDivElement;

        dropzoneContainer.addEventListener('dragover', (ev: Event) => {
            ev.preventDefault();
            dropzoneContainer.classList.add('drag-enter');
        });
        dropzoneContainer.addEventListener('dragleave', (ev: Event) => {
            ev.preventDefault();
            dropzoneContainer.classList.remove('drag-enter');
        });

        dropzoneContainer.addEventListener('drop', (ev: DragEvent) => {
            ev.preventDefault();
            dropzoneContainer.classList.remove('drag-enter');
            if (ev.dataTransfer.files.length > 0) {
                const file: File = ev.dataTransfer.files.item(0);
                this.onFileDrop.emit(file);
                this.label = file.name;
                dropzoneContainer.classList.add('drag-success');
            }
        });
    }
}
