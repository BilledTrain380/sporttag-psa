import { AfterViewInit, Component, ElementRef, Input, ViewChild } from "@angular/core";
import { faAngleDown } from "@fortawesome/free-solid-svg-icons/faAngleDown";
import { faAngleRight } from "@fortawesome/free-solid-svg-icons/faAngleRight";

import { TreeCheckNodeModel } from "../../tree-model";

@Component({
             // tslint:disable-next-line:component-selector
             selector: "[app-check-box-tree-node]",
             templateUrl: "./check-box-tree-node.component.html",
           })
export class CheckBoxTreeNodeComponent implements AfterViewInit {
  readonly faAngleRight = faAngleRight;
  readonly faAngleDown = faAngleDown;

  @Input()
  node?: TreeCheckNodeModel;

  @ViewChild("checkBoxInput", {static: false})
  checkBoxElement?: ElementRef;

  ngAfterViewInit(): void {
    this.node?.isChecked$
      .subscribe(value => {
        this.checkBoxElement!.nativeElement.indeterminate = value === undefined;
      });
  }
}
