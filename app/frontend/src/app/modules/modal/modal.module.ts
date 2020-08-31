import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { AlertModule } from "../alert/alert.module";

import { ConfirmModalComponent } from "./confirm-modal/confirm-modal.component";
import { SubmitModalComponent } from "./submit-modal/submit-modal.component";

const COMPONENTS = [
  SubmitModalComponent,
  ConfirmModalComponent,
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
