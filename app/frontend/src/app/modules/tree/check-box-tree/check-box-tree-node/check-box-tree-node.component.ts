import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from "@angular/core";

import { TreeCheckNodeModel } from "../../tree-model";

@Component({
             // tslint:disable-next-line:component-selector
             selector: "[app-check-box-tree-node]",
             templateUrl: "./check-box-tree-node.component.html",
             styleUrls: ["./check-box-tree-node.component.scss"],
           })
export class CheckBoxTreeNodeComponent implements OnInit, AfterViewInit {

  @Input()
  node?: TreeCheckNodeModel;

  @ViewChild("checkBoxInput", {static: false})
  checkBoxElement?: ElementRef;

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.node?.isChecked$
      .subscribe(value => {
        this.checkBoxElement!.nativeElement.indeterminate = value === undefined;
      });
  }
}
