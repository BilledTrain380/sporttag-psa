import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { ThemeModule } from "../@theme/theme.module";
import { PagesRoutingModule } from "./pages-routing.module";
import { PagesComponent } from "./pages.component";

@NgModule({
            declarations: [PagesComponent],
            imports: [
              CommonModule,
              ThemeModule,
              PagesRoutingModule,
            ],
          })
export class PagesModule {
}
