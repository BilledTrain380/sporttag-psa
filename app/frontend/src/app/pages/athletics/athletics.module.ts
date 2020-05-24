import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { AthleticsRoutingModule } from "./athletics-routing.module";
import { AthleticsComponent } from "./athletics.component";

@NgModule({
  declarations: [AthleticsComponent],
  imports: [
    CommonModule,
    AthleticsRoutingModule,
  ],
})
export class AthleticsModule { }
