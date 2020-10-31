import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbCollapseModule } from "@ng-bootstrap/ng-bootstrap";

import { CheckBoxTreeNodeComponent } from "./check-box-tree/check-box-tree-node/check-box-tree-node.component";
import { CheckBoxTreeComponent } from "./check-box-tree/check-box-tree.component";

const COMPONENTS = [
  CheckBoxTreeComponent,
];

@NgModule({
            declarations: [...COMPONENTS, CheckBoxTreeNodeComponent],
            imports: [
              CommonModule,
              FormsModule,
              NgbCollapseModule,
              FontAwesomeModule,
            ],
            exports: [...COMPONENTS],
          })
export class TreeModule {
}
