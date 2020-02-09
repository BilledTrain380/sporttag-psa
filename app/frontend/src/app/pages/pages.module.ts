import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { ThemeModule } from "../@theme/theme.module";

import { GroupsModule } from "./groups/groups.module";
import { PagesRoutingModule } from "./pages-routing.module";
import { PagesComponent } from "./pages.component";

@NgModule({
  declarations: [PagesComponent],
  imports: [
    CommonModule,
    ThemeModule,
    PagesRoutingModule,
    GroupsModule,
  ],
})
export class PagesModule {
}
