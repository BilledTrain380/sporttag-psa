import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";

import { CardComponent } from "./component/card/card.component";
import { MenuItemComponent } from "./menu/menu-item/menu-item.component";

@NgModule({
  declarations: [
    MenuItemComponent,
    CardComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    FontAwesomeModule,
  ],
  exports: [
    MenuItemComponent,
    CardComponent,
  ],
})
export class CoreModule {
}
