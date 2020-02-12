import { Component, OnInit } from "@angular/core";
import { NGXLogger } from "ngx-logger";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
})
export class AppComponent implements OnInit {

  constructor(private readonly log: NGXLogger) {
  }

  ngOnInit(): void {
    this.log.info("Started PSA frontend Angular application");
    this.log.info("Browser version: ", navigator.userAgent);
  }
}
