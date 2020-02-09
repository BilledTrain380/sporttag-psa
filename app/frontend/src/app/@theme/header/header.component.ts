import { Component, OnInit } from "@angular/core";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { AppState } from "../../store/app";
import { selectUsername } from "../../store/user/user.selector";
import { faUser } from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"],
})
export class HeaderComponent implements OnInit {

  collapsed = true;

  username$?: Observable<string>;

  faUser = faUser;

  constructor(private readonly store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.username$ = this.store.select(selectUsername);
  }
}
