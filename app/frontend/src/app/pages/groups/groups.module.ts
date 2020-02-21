import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";

import { ThemeModule } from "../../@theme/theme.module";

import { GroupOverviewComponent } from "./group-overview/group-overview.component";

@NgModule({
  declarations: [GroupOverviewComponent],
  imports: [
    CommonModule,
    ThemeModule,
    FontAwesomeModule,
  ],
})
export class GroupsModule {
}
