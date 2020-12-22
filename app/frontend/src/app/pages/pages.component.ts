import { Component } from "@angular/core";
import { Store } from "@ngrx/store";
import { map } from "rxjs/operators";

import { isVersionUpToDate } from "../dto/about";
import { selectBuildInfo } from "../store/metadata/metadata.selector";

@Component({
             selector: "app-pages",
             styleUrls: ["./pages.component.scss"],
             template: `
               <div class="pages-container">
                 <div *ngIf="isUpdateAvailable$ | async" class="bg-warning text-center pt-1 pb-1" i18n>
                   <span>A newer version of PSA is available. </span>
                   <a href="https://billedtrain380.github.io/sporttag-psa/pages/downloads" target="_blank">Download now!</a>
                 </div>

                 <div class="container-fluid">
                   <router-outlet></router-outlet>
                 </div>
                 <app-footer class="footer"></app-footer>
               </div>
             `,
           })
export class PagesComponent {
  readonly isUpdateAvailable$ = this.store.select(selectBuildInfo)
    .pipe(map(buildInfo => buildInfo ? !isVersionUpToDate(buildInfo) : false));

  constructor(
    private readonly store: Store,
  ) {
  }
}
