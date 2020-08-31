import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { ParticipationManagementComponent } from "./participation-management/participation-management.component";

const routes: Routes = [
  {
    path: "",
    component: ParticipationManagementComponent,
  },
];

@NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule],
          })
export class ParticipationRoutingModule {
}
