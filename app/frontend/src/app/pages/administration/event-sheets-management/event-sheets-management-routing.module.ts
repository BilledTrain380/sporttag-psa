import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { EventSheetsSelectionComponent } from "./event-sheets-selection/event-sheets-selection.component";

const routes: Routes = [
  {
    path: "",
    component: EventSheetsSelectionComponent,
  },
];

@NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule],
          })
export class EventSheetsManagementRoutingModule {
}
