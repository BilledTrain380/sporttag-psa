import { Component } from "@angular/core";

@Component({
             selector: "app-pages",
             styleUrls: ["./pages.component.scss"],
             template: `
               <div class="pages-container">
                 <div class="container-fluid">
                   <router-outlet></router-outlet>
                 </div>
                 <app-footer class="footer"></app-footer>
               </div>
             `,
           })
export class PagesComponent {
}
