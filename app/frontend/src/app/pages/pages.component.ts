import { Component } from "@angular/core";

import { MENU_ITEMS } from "./pages-menu";

@Component({
  selector: "app-pages",
  styleUrls: ["./pages.component.scss"],
  template: `
    <app-sidebar class="sidebar" [items]="menu"></app-sidebar>
    <div class="container-fluid p-4">
        <router-outlet></router-outlet>
    </div>
  `,
})
export class PagesComponent {
  menu = MENU_ITEMS;
}
