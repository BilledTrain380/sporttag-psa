import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";

import { FileDropComponent } from "./file-drop/file-drop.component";

const COMPONENTS = [
  FileDropComponent,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
              ReactiveFormsModule,
              FontAwesomeModule,
            ],
            exports: [...COMPONENTS],
          })
export class DragAndDropModule {
}
