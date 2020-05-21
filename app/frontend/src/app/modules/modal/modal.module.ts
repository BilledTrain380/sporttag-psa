import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { AlertModule } from "../alert/alert.module";

import { SubmitModalComponent } from "./submit-modal/submit-modal.component";

const COMPONENTS = [
  SubmitModalComponent,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
              AlertModule,
            ],
            exports: [...COMPONENTS],
          })
export class ModalModule {
}
