import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";

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
            ],
            exports: [...COMPONENTS],
          })
export class TreeModule {
}
