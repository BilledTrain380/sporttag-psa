import { Component } from "@angular/core";
import { faGithub } from "@fortawesome/free-brands-svg-icons";

@Component({
             selector: "app-footer",
             templateUrl: "./footer.component.html",
           })
export class FooterComponent {
  faGithub = faGithub;
}
