import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SmartSelectComponent} from './smart-select.component';
import {FormsModule} from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
    ],
    exports: [SmartSelectComponent],
    declarations: [SmartSelectComponent],
    entryComponents: [SmartSelectComponent],
})
export class SmartSelectModule {
}
