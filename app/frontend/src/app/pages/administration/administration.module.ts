import { NgModule } from "@angular/core";

import { ThemeModule } from "../../@theme/theme.module";

import { AdministrationRoutingModule } from "./administration-routing.module";
import { AdministrationComponent } from "./administration.component";
import { GroupsModule } from "./groups/groups.module";
import { OverviewComponent } from "./overview/overview.component";

@NgModule({
            declarations: [
              AdministrationComponent,
              OverviewComponent,
            ],
            imports: [
              ThemeModule,
              AdministrationRoutingModule,
              GroupsModule,
            ],
          })
export class AdministrationModule {
}
