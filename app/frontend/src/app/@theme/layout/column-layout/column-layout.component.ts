import { Component, HostBinding, Input } from "@angular/core";

@Component({
  selector: "app-column-layout",
  templateUrl: "./column-layout.component.html",
})
export class ColumnLayoutComponent {

  @Input()
  title?: string;

  @Input()
  size = 12;

  @HostBinding("class")
  get class(): string {
    return `col-lg-${this.size}`;
  }
}
