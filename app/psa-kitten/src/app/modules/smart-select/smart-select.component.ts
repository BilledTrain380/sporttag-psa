import {Component, EventEmitter, Input, OnChanges, Output} from '@angular/core';
import {SmartSelectSettings} from './smart-select-settings';

@Component({
    selector: 'ngx-smart-select',
    templateUrl: './smart-select.component.html',
    styleUrls: ['./smart-select.component.scss'],
})
export class SmartSelectComponent implements OnChanges {

    @Input('label')
    readonly label?: string;

    @Input('data')
    readonly data: ReadonlyArray<any> = [];

    @Input('settings')
    readonly settings: SmartSelectSettings<any> = {
        onDisplay: value => String(value),
    };

    @Output()
    readonly onChange: EventEmitter<any> = new EventEmitter();

    selectedValue?: any;
    previousIndex: number = -1;
    nextIndex: number = -1;

    constructor() {
    }

    ngOnChanges(): void {
        if (this.selectedValue || !this.data) return;
        this.selectedValue = this.data[0];
        this.change();
    }

    selectPrevious(): void {
        if (this.previousIndex < 0) return;
        this.selectedValue = this.data[this.previousIndex];
        this.change();
    }

    selectNext(): void {
        if (this.nextIndex < 0) return;
        this.selectedValue = this.data[this.nextIndex];
        this.change();
    }

    change(): void {
        this.previousIndex = this.data.indexOf(this.selectedValue) - 1;
        this.nextIndex = (this.data.indexOf(this.selectedValue) + 1 < this.data.length) ?
            this.data.indexOf(this.selectedValue) + 1 : -1;
        this.onChange.emit(this.selectedValue);
    }
}
