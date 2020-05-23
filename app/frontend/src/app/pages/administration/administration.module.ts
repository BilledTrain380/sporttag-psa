import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { ThemeModule } from "../../@theme/theme.module";

import { AdministrationRoutingModule } from "./administration-routing.module";
import { AdministrationComponent } from "./administration.component";
import { GroupsModule } from "./groups/groups.module";

@NgModule({
  declarations: [AdministrationComponent],
  imports: [
    CommonModule,
    ThemeModule,
    AdministrationRoutingModule,
    GroupsModule,
  ],
})
export class AdministrationModule { }
