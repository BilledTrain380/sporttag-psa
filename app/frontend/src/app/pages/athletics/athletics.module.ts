import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { NgbDropdownModule } from "@ng-bootstrap/ng-bootstrap";
import { EffectsModule } from "@ngrx/effects";

import { ThemeModule } from "../../@theme/theme.module";
import { CommonFormsModule } from "../../modules/common-forms/common-forms.module";
import { ParticipantModule } from "../../modules/participant/participant.module";
import { TableModule } from "../../modules/table/table.module";
import { AthleticsEffects } from "../../store/athletics/athletics.effect";
import { ParticipationModule } from "../administration/participation/participation.module";

import { AthleticsNotStartedComponent } from "./athletics-not-started/athletics-not-started.component";
import { AthleticsRoutingModule } from "./athletics-routing.module";
import { AthleticsComponent } from "./athletics.component";

@NgModule({
            declarations: [
              AthleticsComponent,
              AthleticsNotStartedComponent,
            ],
            imports: [
              CommonModule,
              AthleticsRoutingModule,
              ThemeModule,
              CommonFormsModule,
              FormsModule,
              ReactiveFormsModule,
              ParticipationModule,
              ParticipantModule,
              NgbDropdownModule,
              TableModule,
              EffectsModule.forFeature([AthleticsEffects]),
            ],
          })
export class AthleticsModule {
}
