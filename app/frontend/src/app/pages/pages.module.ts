import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";

import { ThemeModule } from "../@theme/theme.module";

import { NotFoundPageComponent } from "./miscellaneous/not-found-page/not-found-page.component";
import { PagesRoutingModule } from "./pages-routing.module";
import { PagesComponent } from "./pages.component";

@NgModule({
            declarations: [
              PagesComponent,
              NotFoundPageComponent,
            ],
            imports: [
              CommonModule,
              ThemeModule,
              PagesRoutingModule,
              FontAwesomeModule,
            ],
          })
export class PagesModule {
}
