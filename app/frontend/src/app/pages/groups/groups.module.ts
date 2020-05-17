import { NgModule } from "@angular/core";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../@theme/theme.module";
import { GroupEffects } from "../../store/group/group.effect";

import { GroupOverviewComponent } from "./group-overview/group-overview.component";

@NgModule({
            declarations: [GroupOverviewComponent],
            imports: [
              ThemeModule,
              EffectsModule.forFeature([GroupEffects]),
            ],
          })
export class GroupsModule {
}
