import { Component } from "@angular/core";
import { faMugHot } from "@fortawesome/free-solid-svg-icons/faMugHot";

@Component({
             selector: "app-athletics-not-started",
             templateUrl: "./athletics-not-started.component.html",
             styleUrls: ["./athletics-not-started.component.scss"],
           })
export class AthleticsNotStartedComponent {
  readonly faMugHot = faMugHot;
}
