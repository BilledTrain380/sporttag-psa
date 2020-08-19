import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";

import { GenderComponent } from "./gender/gender.component";

@NgModule({
            declarations: [GenderComponent],
            imports: [
              CommonModule,
              FontAwesomeModule,
            ],
            exports: [
              GenderComponent,
            ],
          })
export class ParticipantModule {
}
