import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { NgbDropdownModule } from "@ng-bootstrap/ng-bootstrap";

import { ButtonGroupItemComponent } from "./button-group/button-group-item.component";
import { ButtonGroupComponent } from "./button-group/button-group.component";
import { SingleButtonComponent } from "./single-button/single-button.component";

const COMPONENTS = [
  ButtonGroupComponent,
  ButtonGroupItemComponent,
  SingleButtonComponent,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
              NgbDropdownModule,
            ],
            exports: [...COMPONENTS],
          })
export class ActionButtonsModule {
}
