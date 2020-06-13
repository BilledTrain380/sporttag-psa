import { Component, Input } from "@angular/core";

@Component({
             selector: "app-button-group-item",
             template: "<button ngbDropdownItem>{{text}}</button>",
           })
export class ButtonGroupItemComponent {

  @Input()
  text = "";
}
