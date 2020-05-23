import { Component } from "@angular/core";

import { MENU_ITEMS } from "./pages-menu";

@Component({
             selector: "app-pages",
             styleUrls: ["./pages.component.scss"],
             template: `
<!--    <app-sidebar [items]="menu"></app-sidebar>-->
    <div class="pages-container">
        <div class="container-fluid">
            <router-outlet></router-outlet>
        </div>
        <app-footer class="footer"></app-footer>
    </div>
  `,
           })
export class PagesComponent {
  readonly menu = MENU_ITEMS;
}
