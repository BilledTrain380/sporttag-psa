import { Component } from "@angular/core";
import { faCat } from "@fortawesome/free-solid-svg-icons/faCat";

@Component({
             selector: "app-not-found-page",
             templateUrl: "./not-found-page.component.html",
             styleUrls: ["./not-found-page.component.scss"],
           })
export class NotFoundPageComponent {
  readonly faCat = faCat;
}
