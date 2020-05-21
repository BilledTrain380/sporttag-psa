import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbAlertModule, NgbPopoverModule } from "@ng-bootstrap/ng-bootstrap";

import { MenuItemComponent } from "./menu/menu-item/menu-item.component";

@NgModule({
            declarations: [
              MenuItemComponent,
            ],
            imports: [
              CommonModule,
              RouterModule,
              NgbPopoverModule,
              NgbAlertModule,
              FontAwesomeModule,
            ],
            exports: [
              MenuItemComponent,
            ],
          })
export class CoreModule {
}
