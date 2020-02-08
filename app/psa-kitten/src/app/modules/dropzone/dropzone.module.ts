import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DropzoneComponent} from './dropzone.component';
import {ThemeModule} from '../../@theme/theme.module';

@NgModule({
    imports: [
        CommonModule,
        ThemeModule,
    ],
    exports: [
        DropzoneComponent,
    ],
    declarations: [DropzoneComponent],
})
export class DropzoneModule {
}
