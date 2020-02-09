import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";

import { MenuItemComponent } from "./menu/menu-item/menu-item.component";

@NgModule({
  declarations: [MenuItemComponent],
  imports: [
    CommonModule,
    RouterModule,
    FontAwesomeModule,
  ],
  exports: [MenuItemComponent],
})
export class CoreModule {
}
