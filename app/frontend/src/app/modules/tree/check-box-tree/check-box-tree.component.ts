import { Component, Input, OnDestroy } from "@angular/core";

import { TreeCheckNodeModel } from "../tree-model";

@Component({
             selector: "app-check-box-tree",
             templateUrl: "./check-box-tree.component.html",
           })
export class CheckBoxTreeComponent implements OnDestroy {

  @Input()
  tree?: TreeCheckNodeModel;

  ngOnDestroy(): void {
    this.tree?.close();
  }
}
