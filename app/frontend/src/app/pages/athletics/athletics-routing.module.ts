import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { AthleticsComponent } from "./athletics.component";

const routes: Routes = [{
  path: "",
  component: AthleticsComponent,
}];

@NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule],
          })
export class AthleticsRoutingModule {
}
