import { Component, ElementRef, Input, ViewChild } from "@angular/core";
import { faAngleDown } from "@fortawesome/free-solid-svg-icons/faAngleDown";
import { faAngleRight } from "@fortawesome/free-solid-svg-icons/faAngleRight";

import { TreeCheckNodeModel } from "../../tree-model";

@Component({
             // tslint:disable-next-line:component-selector
             selector: "[app-check-box-tree-node]",
             templateUrl: "./check-box-tree-node.component.html",
           })
export class CheckBoxTreeNodeComponent {
  readonly faAngleRight = faAngleRight;
  readonly faAngleDown = faAngleDown;

  @Input()
  set node(node: TreeCheckNodeModel | undefined) {
    this._node = node;
    this._node?.isChecked$
      .subscribe(value => {
        if (this.checkBoxElement) {
          this.checkBoxElement!.nativeElement.indeterminate = value === undefined;
        }
      });
  }

  get node(): TreeCheckNodeModel | undefined {
    return this._node;
  }

  private _node?: TreeCheckNodeModel;

  @ViewChild("checkBoxInput", {static: false})
  private checkBoxElement?: ElementRef;
}
