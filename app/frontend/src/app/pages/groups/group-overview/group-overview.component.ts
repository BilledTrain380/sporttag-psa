import { Component, OnInit } from "@angular/core";
import { faUpload } from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: "app-group-overview",
  templateUrl: "./group-overview.component.html",
  styleUrls: ["./group-overview.component.scss"],
})
export class GroupOverviewComponent implements OnInit {

  faUpload = faUpload;

  ngOnInit(): void {
  }
}
